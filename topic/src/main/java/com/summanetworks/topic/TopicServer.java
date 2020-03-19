package com.summanetworks.topic;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.log4j.Logger;

/**
 * @author ajimenez, created on 15/3/20.
 */
public class TopicServer {

    private static final Logger logger = Logger.getLogger(TopicServer.class);

    // BossGroup: Receive and register incoming connection. (DRAs connections... so will be called 1-4 times... )
    private EventLoopGroup bossGroup = null;
    // WorkerGroup: Handle traffic from channels.
    private EventLoopGroup workerGroup = null;



    public void start(final TopicController controller) throws Exception {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup(50);

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))

                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    public void initChannel(NioSocketChannel ch) throws Exception {
                        if(logger.isInfoEnabled()) {
                            if (ch.remoteAddress() != null && ch.remoteAddress().getAddress() != null) {
                                logger.info("[TCPListener] initChannel() New connection attempt from IP " + ch.remoteAddress().getAddress().toString());
                            } else if (ch.remoteAddress() != null) {
                                logger.info("[TCPListener] initChannel() New connection attempt from " + ch.remoteAddress().toString());
                            } else {
                                logger.info("[TCPListener] initChannel() New connection attempt from chanel " + ch);
                            }
                        }
                        ch.pipeline()
                                .addLast(
                                        //FIXME: Revisar valores de los lengths
                                        //Only propagate full messages
                                        new LengthFieldBasedFrameDecoder(TopicConfig.getInstance().getMaxTCPFrameSize(), 1, 3, -4, 0),
                                        new TopicHandler(controller)
                                );
                    }
                })
                //Number of connection queued.
                .option(ChannelOption.SO_BACKLOG, 128)
        //FIXME: Ver si hace falta.
        //.childOption(ChannelOption.SO_KEEPALIVE, true)
        ;


    }


}
