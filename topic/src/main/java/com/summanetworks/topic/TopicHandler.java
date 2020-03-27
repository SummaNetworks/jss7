package com.summanetworks.topic;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.apache.log4j.Logger;

/**
 * @author ajimenez, created on 16/3/20.
 */
public class TopicHandler extends ChannelDuplexHandler implements WritableConnection{

    private static final Logger logger = Logger.getLogger(TopicHandler.class);

    private TopicController controller;
    private String remoteAddress;
    private boolean registered = false;
    private ChannelHandlerContext ctx;
    private boolean asClient = false;
    private int remotePeerId;

    private int heartBeatLost = 0;

    public String getRemoteAddress(){
        return remoteAddress;
    }

    public int getRemotePeerId() {
        return remotePeerId;
    }

    public TopicHandler(TopicController controller){
        this(controller, false);
    }
    public TopicHandler(TopicController controller, boolean asClient){
        this.asClient = asClient;
        this.controller = controller;
    }

    private void write(ByteBuffer message) {
        //TODO: Review if reuse a buffer to optimize memory.
        ctx.writeAndFlush(Unpooled.wrappedBuffer(message));
    }



    /**
     * Calls {@link ChannelHandlerContext#fireChannelActive()} to forward
     * to the next {@link io.netty.channel.ChannelInboundHandler} in the {@link io.netty.channel.ChannelPipeline}.
     *
     * Sub-classes may override this method to change behavior.
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception  {
        InetSocketAddress socket = (InetSocketAddress) ctx.channel().remoteAddress();
        remoteAddress = socket.getAddress().getHostAddress();
        this.ctx = ctx;

        //Check that other handler doesn't start the connection.
        if(controller.connected(this) != null){
            //Previous connection found
            logger.info("Previous handler found for remote host "+remoteAddress);
            ctx.close();
            return;
        }

        if(asClient){
            TopicSccpMessage hello =TopicSccpMessage.createRegisterMessage(controller.getTopicConfig().getLocalPeerId());
            logger.debug("channelActive(): As client, sending hello. Local Id: "+hello.id+".");
            this.sendMessage(hello);
        }else{
            logger.debug("channelActive(): As server, waiting for remote hello.");
        }
        ctx.fireChannelActive();
    }

    public void close () {
        logger.info("Channel closed with peer "+ remotePeerId);
        ctx.close();
        controller.unregisterHandler(this);
        controller.disconnected(this);
        registered = false;
        heartBeatLost = 0;
    }

    /**
     * Calls {@link ChannelHandlerContext#fireChannelInactive()} to forward
     * to the next {@link io.netty.channel.ChannelInboundHandler} in the {@link io.netty.channel.ChannelPipeline}.
     * <p>
     * Sub-classes may override this method to change behavior.
     *
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info("Channel inactive with peer "+ remotePeerId +".");
        ctx.fireChannelInactive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            ByteBuf byteBuf = (ByteBuf) msg;

//            //Remove to no alter
//            while (bb.isReadable()) { // (1)
//                System.out.print((char) bb.readByte());
//                System.out.flush();
//            }

            //Mandar el mensaje al diameterHandler vinculado a esta conexión.
            ByteBuffer bb = null;
            bb = byteBuf.nioBuffer();

            // Ensure bb has an internal array.
            if (!bb.hasArray()) {
                byte[] bytes = new byte[bb.remaining()];
                bb.get(bytes);
                bb = ByteBuffer.wrap(bytes);
            }
            parseMessage(bb);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void parseMessage(ByteBuffer bb){
            byte[] message = bb.array();
            TopicSccpMessage tsm = TopicSccpMessage.fromBytes(message);
            if(!registered){
                if(tsm.messageType == TopicSccpMessage.TYPE_REGISTER){
                    processRegistration(tsm);
                } else if( tsm.messageType == TopicSccpMessage.TYPE_HEARTBEAT ) {
                    logger.debug("Sending heartbeat ack.");
                    this.sendMessage(TopicSccpMessage.createHeartbeatAck());
                } else if( tsm.messageType == TopicSccpMessage.TYPE_HEARTBEAT_ACK ) {
                    logger.debug("Heartbeat ack received.");
                    heartBeatLost = 0;
                } else {
                    // TODO: 24/3/20 by Ajimenez - Could an alternative to send a registration request message.
                    logger.warn("Invalid message. Host not registered yet. Closing.");
                    controller.channelInvalid(this);
                    this.close();
                }
            } else {
                if( tsm.messageType == TopicSccpMessage.TYPE_DATA ) {
                    if (logger.isTraceEnabled()) {
                        logger.trace(String.format("parseMessage(): Message from peerId %d to handle dialog %d.", remotePeerId, tsm.id));
                    }
                    controller.onMessage(this.remotePeerId, tsm);
                } else if( tsm.messageType == TopicSccpMessage.TYPE_HEARTBEAT ) {
                    logger.debug("Received heartbeat, sending ack.");
                    this.sendMessage(TopicSccpMessage.createHeartbeatAck());
                } else if( tsm.messageType == TopicSccpMessage.TYPE_HEARTBEAT_ACK ) {
                    logger.debug("Heartbeat ack received.");
                    heartBeatLost = 0;
                }
            }
    }

    private void processRegistration(TopicSccpMessage tsm) {
        logger.info("Register message from remote ID "+tsm.id+".");
        this.remotePeerId = (int) tsm.id;

        if(controller.getHandlerRegistered(this.remotePeerId) != null){
            logger.debug("processRegistration(): Peer %d already registered with different handler. Ignoring registration and closing.");
            ctx.close();
            controller.disconnected(this);
            return;
        }
        controller.registerHandler(this.remotePeerId, this);
        registered = true;
        if(!asClient){
            TopicSccpMessage response = TopicSccpMessage.createRegisterMessage(controller.getTopicConfig().getLocalPeerId());
            logger.info("processRegistration(): [ServerMode] Sending register response. Local ID: "+response.id+".");
            this.sendMessage(response);
        }
    }

    public void sendMessage(TopicSccpMessage tsm){
        ByteBuffer bb = ByteBuffer.wrap(tsm.toBytes());
        this.write(bb);
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                logger.debug("Reader-Idle detected.");
                if(registered) {
                    if (heartBeatLost++ < 2) {
                        logger.debug("Sending heart beat to peer " + remotePeerId);
                        this.sendMessage(TopicSccpMessage.createHeartbeat());
                    } else {
                        logger.warn("Several heartbeat lost with peer " + remotePeerId + " CLOSING CONNECTION!");
                        this.close();
                    }
                } else {
                    logger.warn("Not registered yet, sending hello again.");
                    TopicSccpMessage hello =TopicSccpMessage.createRegisterMessage(controller.getTopicConfig().getLocalPeerId());
                    this.sendMessage(hello);
                }
            } else if (e.state() == IdleState.WRITER_IDLE) {
                logger.debug("Write-Idle detected.");
                //Nothing to do.
                //this.sendMessage(TopicSccpMessage.createHeartbeat());
            }
        }
    }
}
