package com.summanetworks.topic;

import java.util.ArrayList;
import java.util.List;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.log4j.Logger;

/**
 * @author ajimenez, created on 15/3/20.
 */
public class TopicClient {

    private static final Logger logger = Logger.getLogger(TopicClient.class);


    private boolean beConnected = true;
    private List<EventLoopGroup> workers = new ArrayList<>();

    /**
     * Init connection with the peer.
     * If established try to maintain if is lost.
     * If is not established does not try.
     * @param host
     * @param port
     * @param controller
     */
    public void initConnection(final String host, final int port, final TopicController controller) {
        new Thread("TC-" + host) {
            @Override
            public void run() {
                logger.info("Starting client for remote host "+host);
                EventLoopGroup workerGroup = new NioEventLoopGroup();
                workers.add(workerGroup);
                try {
                    //First at all check that connection to peer is not established by peer yet.
                    Bootstrap b;
                    b = buildBootstrap(workerGroup, controller);
                    do {
                        try {
                            if(logger.isDebugEnabled())
                                logger.debug("Trying to connect to host "+host+"...");
                            if(!controller.isConnected(host)) {
                                ChannelFuture f = b.connect(host, port).sync();
                                beConnected = true;
                                f.channel().closeFuture().sync();
                                //Channel closed. Rebuild before try again.
                                b = buildBootstrap(workerGroup, controller);
                            }else{
                                logger.info("Host "+host+" already registered.");
                                // FIXME: 22/3/20 by Ajimenez - Al romper el bucle tampoco intentará reconectarse luego, aunque el último que llega tiene que conectarse...
                                break;
                            }
                        } catch (Exception e) {
                            if(logger.isDebugEnabled())
                                logger.debug(String.format("Can not connect to %s with port %d", host, port));
                        }
                        try {
                            logger.debug("Waiting before try again..."); // FIXME: 22/3/20 by Ajimenez - Limite de reintentos.
                            Thread.sleep(5000);
                        } catch (InterruptedException ignored) {
                        }
                    } while (beConnected);
                } finally {
                    if(!workerGroup.isShuttingDown())
                        workerGroup.shutdownGracefully();
                }

            }
        }.start();
    }

    private Bootstrap buildBootstrap(EventLoopGroup workerGroup, final TopicController controller) {
        Bootstrap b;
        b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(
                        //Only propagate full messages
                        new LengthFieldBasedFrameDecoder(controller.getTopicConfig().getMaxTCPFrameSize(),
                                0, 2, 0, 2),
                        new LengthFieldPrepender(2),
                        new IdleStateHandler(10, 5, 0),
                        new TopicHandler(controller, true));
            }
        });
        return b;
    }

    public void stop() {
        beConnected = false;
        for(EventLoopGroup elg : this.workers){
            if(!elg.isShuttingDown()){
                elg.shutdownGracefully();
            }
        }
    }


}
