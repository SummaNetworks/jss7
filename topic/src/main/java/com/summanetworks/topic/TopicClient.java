package com.summanetworks.topic;

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
import org.apache.log4j.Logger;

/**
 * @author ajimenez, created on 15/3/20.
 */
public class TopicClient {

    private static final Logger logger = Logger.getLogger(TopicController.class);


    private boolean beConnected = true;

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
                logger.info("Starting client to connect with host "+host);
                EventLoopGroup workerGroup = new NioEventLoopGroup();
                try {
                    //First at all check that connection to peer is not established by peer yet.
                    Bootstrap b = new Bootstrap();
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
                                    new TopicHandler(controller, true));
                        }
                    });

                    do {
                        try {
                            logger.info("Trying to connect to host "+host+"...");
                            if(!controller.hostHandlerMap.containsKey(host)) {
                                // Start the client.
                                ChannelFuture f = b.connect(host, port).sync();
                                beConnected = true;
                                // Wait until the connection is closed.
                                //f.channel().closeFuture().sync();
                                f.channel().closeFuture().sync();
                            }else{
                                logger.info("Host "+host+" already registered ");
                                // FIXME: 22/3/20 by Ajimenez - Al romper el bucle tampoco intentará reconectarse luego, aunque el último que llega tiene que conectarse...
                                break;
                            }
                        } catch (Exception e) {
                            logger.info(String.format("Can not connect to %s with port %d", host, port));
                        }
                        try {
                            logger.debug("Waiting before try again..."); // FIXME: 22/3/20 by Ajimenez - Limite de reintentos.
                            Thread.sleep(5000);
                        } catch (InterruptedException ignored) {
                        }
                    } while (beConnected);
                } finally {
                    workerGroup.shutdownGracefully();
                }

            }
        }.start();
    }

    public void stop() {
        beConnected = false;
    }


}
