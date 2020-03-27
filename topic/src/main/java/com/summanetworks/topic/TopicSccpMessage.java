package com.summanetworks.topic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.message.ParseException;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * @author ajimenez, created on 19/3/20.
 */
public class TopicSccpMessage extends TopicMessage {

    private static final Logger logger = Logger.getLogger(TopicMessage.class);

    public static final short TYPE_DATA = 0;
    public static final short TYPE_REGISTER = 1;
    public static final short TYPE_HEARTBEAT = 2;
    public static final short TYPE_HEARTBEAT_ACK = 3;

    private static ParameterFactoryImpl sccpParameterFactory;

    static {
        sccpParameterFactory = new ParameterFactoryImpl();
    }

    protected short messageType;
    protected long id;
    protected int ssn;
    protected byte [] data;

    //Estos serian los mensajes necesarios para procesar el continue, quizas haya que usar el SccpDataMessage completo...
    protected SccpAddress localAddress;
    protected SccpAddress remoteAddress;

    public TopicSccpMessage(){}

    public TopicSccpMessage (long dialogId, SccpDataMessage impl){
        this.messageType = TYPE_DATA;
        this.id = dialogId;
        this.localAddress = impl.getCallingPartyAddress();
        this.remoteAddress = impl.getCalledPartyAddress();
        //this.ssn = impl.getOriginLocalSsn(); //<-- Qué es esto?
        this.ssn = this.remoteAddress.getSubsystemNumber();
        this.data = impl.getData();
    }

    public static TopicSccpMessage createRegisterMessage(int peerId){
        TopicSccpMessage tsm = new TopicSccpMessage();
        tsm.messageType = TYPE_REGISTER;
        tsm.id = peerId;
        return tsm;
    }

    public static TopicSccpMessage createHeartbeat(){
        TopicSccpMessage tsm = new TopicSccpMessage();
        tsm.messageType = TYPE_HEARTBEAT;
        return tsm;
    }

    public static TopicSccpMessage createHeartbeatAck(){
        TopicSccpMessage tsm = new TopicSccpMessage();
        tsm.messageType = TYPE_HEARTBEAT_ACK;
        return tsm;
    }

    /**
     * Parse message received and build a TopicMessage
     * @param data
     */
    public static TopicSccpMessage fromBytes(byte [] data){
        TopicSccpMessage tm = null;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);
            tm = new TopicSccpMessage();
            tm.messageType = ois.readShort();
            if(tm.messageType == TYPE_DATA) {
                tm.id = ois.readLong();
                tm.ssn = ois.readInt();

                tm.localAddress = readSccpAddressEncBytes(ois);
                tm.remoteAddress = readSccpAddressEncBytes(ois);

                int size = ois.readShort();
                if (size > 0) {
                    tm.data = new byte[size];
                    readBytes(ois, size, tm.data);
                }
            } else if(tm.messageType == TYPE_REGISTER) {
                tm.id = ois.readLong();
            }
        }catch(Exception e){
            logger.error("Unexpected error", e);
            throw new RuntimeException(e);
        }
        return tm;
    }


    /**
     * Convert current message to byte buffer.
     * @return
     */
    public byte [] toBytes(){
        ByteArrayOutputStream baos;
        try {
            baos = new ByteArrayOutputStream();
            ObjectOutputStream oos;
            oos = new ObjectOutputStream(baos);

            oos.writeShort(this.messageType);

            if(this.messageType == TYPE_DATA) {
                oos.writeLong(this.id);
                oos.writeInt(this.ssn);
                writeSccpAddressEncBytes(this.localAddress, oos);
                writeSccpAddressEncBytes(this.remoteAddress, oos);
                //Message data.
                if (data != null) {
                    oos.writeShort(data.length);
                    oos.write(data);
                } else {
                    oos.writeShort(0);
                }
            }else if(this.messageType == TYPE_REGISTER) {
                oos.writeLong(this.id);
            }
            oos.close();
            return baos.toByteArray();
        }catch(Exception e){
            logger.error("Unexpected error", e);
            throw new RuntimeException(e);
        }
    }


    private static void writeSccpAddressEncBytes(SccpAddress address, ObjectOutputStream oos) throws IOException, ParseException {
        //Importa como codificarlo para recuperarlo? Implementar test con las dos formas.

        if(address != null) {
            byte[] data =((SccpAddressImpl) address).encode(false, SccpProtocolVersion.ITU);
            oos.writeShort(data.length);
            oos.write(data);
        }else{
            oos.writeShort(-1);
        }

    }
    private static SccpAddress readSccpAddressEncBytes(ObjectInputStream ois) throws IOException, ParseException {
        //Importa como codificarlo para recuperarlo? Implementar test con las dos formas.
        int length = ois.readShort();
        if(length != -1) {
            byte[] dsBytes = new byte[length];
            readBytes(ois, length, dsBytes);
            SccpAddressImpl result = new SccpAddressImpl();
            result.decode(dsBytes, sccpParameterFactory, SccpProtocolVersion.ITU);
            return result;
        }else {
            return null;
        }
    }

    private static void readBytes(ObjectInputStream ois, int length, byte[] dsBytes) throws IOException {
        int read = 0;
        do{
            read += ois.read(dsBytes, read, length - read);
        }while(read < length);
    }
}
