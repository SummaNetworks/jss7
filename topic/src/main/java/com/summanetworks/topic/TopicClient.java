package com.summanetworks.topic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author ajimenez, created on 15/3/20.
 */
public class TopicClient {

    private static final Logger logger = LogManager.getLogger(TopicClient.class);


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
        if(host != null && !"".equals(host.trim())) {
            new Thread("TC-" + host) {
                @Override
                public void run() {
                    logger.info("Starting client for remote host [" + host+"]");
                    EventLoopGroup workerGroup = new NioEventLoopGroup(0,new DefaultThreadFactory("Topic-Client"));
                    workers.add(workerGroup);
                    try {
                        //First at all check that connection to peer is not established by peer yet.
                        Random random = new Random();
                        Bootstrap b;
                        b = buildBootstrap(workerGroup, controller);
                        do {
                            if (!workerGroup.isShuttingDown() && !workerGroup.isShutdown()) {
                                try {
                                    if (logger.isDebugEnabled())
                                        logger.debug("Trying to connect to host [" + host + "]...");
                                    if (!controller.isConnected(host)) {
                                        ChannelFuture f = b.connect(host, port).sync();
                                        beConnected = true;
                                        f.channel().closeFuture().sync();
                                        //Channel closed. Rebuild before try again.
                                        b = buildBootstrap(workerGroup, controller);
                                    } else {
                                        logger.info("Host [" + host + "] already registered.");
                                        break;
                                    }
                                } catch (Exception e) {
                                    logger.info(String.format("Can not connect to %s with port %d. Cause: %s", host, port, e.getMessage()));
                                    if (logger.isTraceEnabled()) {
                                        logger.trace(String.format("Can not connect to %s with port %d", host, port), e);
                                    }
                                }
                                try {
                                    controller.onUnableToConnect(host);
                                    logger.trace("Waiting before try again...");
                                    Thread.sleep(3000 + random.nextInt(2000));
                                } catch (InterruptedException ignored) {
                                }
                            } else {
                                logger.trace("Shutting down. Stopping client.");
                                beConnected = false;
                            }
                        } while (beConnected);
                    } finally {
                        if (!workerGroup.isShuttingDown())
                            workerGroup.shutdownGracefully();
                    }
                }
            }.start();
        }
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
                        new IdleStateHandler(controller.getTopicConfig().getHartBeatInterval(), 0, 0),
                        new TopicHandler(controller, true));
            }
        });
        return b;
    }

    public void stop() {
        logger.info("Stop called. Stopping...");
        beConnected = false;
        for(EventLoopGroup elg : this.workers){
            if(!elg.isShuttingDown()){
                elg.shutdownGracefully();
            }
        }
    }


}
