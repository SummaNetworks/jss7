package com.summanetworks.topic;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.apache.log4j.Logger;

/**
 * @author ajimenez, created on 16/3/20.
 */
public class TopicHandler extends ChannelInboundHandlerAdapter implements WritableConnection{

    private static final Logger logger = Logger.getLogger(TopicHandler.class);

    private TopicController controller;
    private String remoteAddress;
    private boolean registered = false;
    private ChannelHandlerContext ctx;
    private boolean asClient = false;
    private long peerId;

    public String getRemoteAddress(){
        return remoteAddress;
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

    public void close() {
        ctx.close();
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
        controller.connected(this);
        if(asClient){
            TopicSccpMessage hello = new TopicSccpMessage();
            hello.id = controller.getTopicConfig().getLocalPeerId();
            logger.debug("channelActive(): As client, sending hello. Local ID: "+hello.id);
            this.sendMessage(hello);
        }else{
            logger.debug("channelActive(): As server, waiting for remote hello.");
        }
        ctx.fireChannelActive();
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
        controller.disconnected(this);
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
                if(tsm.isInitialMessage()){
                    logger.info("Register message from remote ID "+tsm.id);
                    this.peerId = tsm.id;
                    controller.registerHandler(tsm.id, this);
                    registered = true;
                    if(!asClient){
                        TopicSccpMessage response = new TopicSccpMessage();
                        response.id = controller.getTopicConfig().getLocalPeerId();
                        logger.info("[ServerMode] Sending register response. Local ID: "+response.id);
                        this.sendMessage(response);
                    }
                }else{
                    // TODO: 24/3/20 by Ajimenez - Could an alternative to send a registration request message.
                    logger.warn("Invalid message. Host not registered yet. Closing.");
                    controller.channelInvalid(this);
                    this.close();
                }
            } else {
                if(logger.isTraceEnabled()) {
                    logger.trace(String.format("Message from peerId %d to handle dialog %d", peerId, tsm.id));
                }
                controller.onMessage(this.peerId, tsm);
            }
    }

    public void sendMessage(TopicSccpMessage tsm){
        ByteBuffer bb = ByteBuffer.wrap(prepareBytes(tsm));
        this.write(bb);
    }

    private byte[] prepareBytes(TopicSccpMessage cm) {
        byte [] data = cm.toBytes();
        /* Used LengthFieldPrepender
        byte [] dataToSend = new byte [data.length + 2];
        short length = (short) data.length;
        dataToSend[1] = (byte) (length & 0xff);
        dataToSend[0] = (byte) ((length >> 8) & 0xff);
        for(int i= 0; i < data.length; i++){
            dataToSend[i+2] = data[i];
        }
        return dataToSend;
         */
        return data;
    }

}
