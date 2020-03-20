package com.summanetworks.topic;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
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
                            ch.pipeline().addLast(new TopicHandler(controller, true));
                        }
                    });

                    do {
                        try {
                            // Start the client.
                            ChannelFuture f = b.connect(host, port).sync();
                            beConnected = true;
                            // Wait until the connection is closed.
                            //f.channel().closeFuture().sync();
                            f.channel().closeFuture().sync();
                        } catch (Exception e) {
                            logger.info(String.format("Can not connect to %s with port %d", host, port));
                        }
                        try {
                            Thread.sleep(3000);
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
