package com.summanetworks.topic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpDataMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.message.ParseException;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * @author ajimenez, created on 19/3/20.
 */
public class TopicSccpMessage extends TopicMessage {

    private static final Logger logger = Logger.getLogger(TopicMessage.class);

    private static ParameterFactoryImpl sccpParameterFactory;

    static {
        sccpParameterFactory = new ParameterFactoryImpl();
    }

    protected long id;
    protected byte [] data;

    //Estos serian los mensajes necesarios para procesar el continue, quizas haya que usar el SccpDataMessage completo...
    protected SccpAddress localAddress;
    protected SccpAddress remoteAddress;


    public TopicSccpMessage(){}

    public TopicSccpMessage (long dialogId, SccpDataMessageImpl impl){
        this.id = dialogId;
        this.localAddress = impl.getCallingPartyAddress();
        this.remoteAddress = impl.getCalledPartyAddress();
        this.data = impl.getData();
    }

    public boolean isInitialMessage(){
        boolean result = false;
        if(data == null)
            result = true;
        return result;
    }

    /**
     * Parse message received and build a TopicMessage
     * @param data
     */
    public static TopicSccpMessage fromByte(byte [] data){
        TopicSccpMessage tm = null;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);
            tm = new TopicSccpMessage();
            tm.id = ois.readLong();
            tm.localAddress = readSccpAddressEncBytes(ois);
            tm.remoteAddress = readSccpAddressEncBytes(ois);
            int size = ois.readInt();
            tm.data = new byte [size];
            readBytes(ois,size, tm.data);
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
    public byte [] toByte(){
        ByteArrayOutputStream baos;
        try {
            baos = new ByteArrayOutputStream();
            ObjectOutputStream oos;
            oos = new ObjectOutputStream(baos);
            oos.writeLong(this.id);
            writeSccpAddressEncBytes(this.localAddress, oos);
            writeSccpAddressEncBytes(this.remoteAddress, oos);
            //Message data.
            oos.writeShort(data.length);
            oos.write(data);

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
