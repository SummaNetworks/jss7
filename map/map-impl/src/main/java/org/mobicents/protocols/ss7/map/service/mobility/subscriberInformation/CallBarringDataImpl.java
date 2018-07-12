package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import java.io.IOException;
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CallBarringData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtCallBarringFeature;
import org.mobicents.protocols.ss7.map.api.service.supplementary.Password;
import org.mobicents.protocols.ss7.map.datacoding.IntegerEncoderFacility;
import org.mobicents.protocols.ss7.map.datacoding.NullEncoderFacility;
import org.mobicents.protocols.ss7.map.datacoding.ObjectEncoderFacility;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtCallBarringFeatureImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.PasswordImpl;

/**
 *
 <code>
 CallBarringData ::= SEQUENCE {
     callBarringFeatureList        Ext-CallBarFeatureList,
     password                      Password OPTIONAL,
     wrongPasswordAttemptsCounter  WrongPasswordAttemptsCounter OPTIONAL,
     notificationToCSE             NULL OPTIONAL,
     extensionContainer            ExtensionContainer OPTIONAL,
     ...
 }

 Ext-CallBarFeatureList ::= SEQUENCE SIZE (1..32) OF Ext-CallBarringFeature

 WrongPasswordAttemptsCounter ::= INTEGER (0..4)
 </code>
 *
 * @author eva ogallar
 */
public class CallBarringDataImpl extends SequenceBase implements CallBarringData, MAPAsnPrimitive {

    public static final String _PrimitiveName = "CallBarringData";

    private ArrayList<ExtCallBarringFeature> callBarringFeatureList;
    private Password password;
    private Integer wrongPasswordAttemptsCounter;
    private boolean notificationToCSE;
    private MAPExtensionContainer extensionContainer;

    public CallBarringDataImpl() {
        super(_PrimitiveName);
    }

    public CallBarringDataImpl(ArrayList<ExtCallBarringFeature> callBarringFeatureList, Password password,
                               Integer wrongPasswordAttemptsCounter, boolean notificationToCSE, MAPExtensionContainer extensionContainer) {
        super(_PrimitiveName);
        this.callBarringFeatureList = callBarringFeatureList;
        this.password = password;
        this.wrongPasswordAttemptsCounter = wrongPasswordAttemptsCounter;
        this.notificationToCSE = notificationToCSE;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public ArrayList<ExtCallBarringFeature> getCallBarringFeatureList() {
        return callBarringFeatureList;
    }

    @Override
    public Password getPassword() {
        return password;
    }

    @Override
    public Integer getWrongPasswordAttemptsCounter() {
        return wrongPasswordAttemptsCounter;
    }

    @Override
    public boolean getNotificationToCSE() {
        return notificationToCSE;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public int getTag() throws MAPException {
        return Tag.SEQUENCE;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    @Override
    public boolean getIsPrimitive() {
        return false;
    }
    protected void _decode(AsnInputStream ansIS, int length) throws AsnException, IOException, MAPParsingComponentException {

        password = null;
        wrongPasswordAttemptsCounter = null;
        callBarringFeatureList = null;
        notificationToCSE = false;
        extensionContainer = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                switch (tag) {
                    case Tag.SEQUENCE:
                        if (num==0) {

                            // decode callBarringFeatureList
                            AsnInputStream ais2 = ais.readSequenceStream();
                            while (true) {
                                if (ais2.available() == 0)
                                    break;

                                tag = ais2.readTag();
                                if (tag != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".extCallBarringFeature: Parameter extCallBarringFeature is primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                this.callBarringFeatureList.add((ExtCallBarringFeature) ObjectEncoderFacility.
                                        decodeObject(ais, new ExtCallBarringFeatureImpl(), "callBarringFeatureList", getPrimitiveName()));

                            }

                            if (this.callBarringFeatureList.size() < 1 || this.callBarringFeatureList.size() > 32) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter callBarringFeatureList size must be from 1 to 32, found: "
                                        + this.callBarringFeatureList.size(), MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                        } else {
                            extensionContainer = (MAPExtensionContainer) ObjectEncoderFacility.
                                    decodeObject(ais, new MAPExtensionContainerImpl(), "extensionContainer", getPrimitiveName());
                        }
                        break;
                    case Tag.INTEGER:
                        this.wrongPasswordAttemptsCounter = new IntegerEncoderFacility(_PrimitiveName).decode(ais, "wrongPasswordAttemptsCounter");
                        break;
                    case Tag.STRING_NUMERIC:
                        this.password = (Password) ObjectEncoderFacility.
                                decodePrimitiveObject(ais, new PasswordImpl(), "password", getPrimitiveName());
                        break;
                    case Tag.NULL:
                        this.notificationToCSE = NullEncoderFacility.decode(ais, "notificationToCSE", getPrimitiveName());
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            }
            num++;
        }
   }

    private String getPrimitiveName() {
        return _PrimitiveName;
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.callBarringFeatureList == null || this.callBarringFeatureList.size() < 1
                || this.callBarringFeatureList.size() > 32) {
            throw new MAPException("CallBarringFeatureList list must contains from 1 to 32 elements");
        }
        try {
            asnOs.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int pos = asnOs.StartContentDefiniteLength();
            for (ExtCallBarringFeature extCallBarringFeature : this.callBarringFeatureList) {
                ((ExtCallBarringFeatureImpl) extCallBarringFeature).encodeAll(asnOs);
            }
            asnOs.FinalizeContent(pos);

            if (this.password != null) {
                ((PasswordImpl) this.password).encodeAll(asnOs);
            }

            if (this.wrongPasswordAttemptsCounter != null) {
                if (this.wrongPasswordAttemptsCounter < 0 || this.wrongPasswordAttemptsCounter > 4) {
                    throw new MAPException("Error while encoding " + _PrimitiveName
                            + " parameter wrongPasswordAttemptsCounter is out of range (0..4). Actual value: " + this.wrongPasswordAttemptsCounter);
                }

                IntegerEncoderFacility.encode(asnOs, wrongPasswordAttemptsCounter, "wrongPasswordAttemptsCounter");
            }

            NullEncoderFacility.encode(asnOs, this.notificationToCSE, "notificationToCSE");

            if (this.extensionContainer != null) {
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);
            }
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.callBarringFeatureList != null) {
            sb.append("callBarringFeatureList=[");
            boolean firstItem = true;
            for (ExtCallBarringFeature extCallBarringFeature: callBarringFeatureList) {
                if (firstItem) {
                    firstItem = false;
                } else {
                    sb.append(", ");
                }
                sb.append(extCallBarringFeature);
            }
            sb.append("]");
        }
        if (this.password != null) {
            sb.append(", password=");
            sb.append(this.password);
        }
        if (this.wrongPasswordAttemptsCounter != null) {
            sb.append(", wrongPasswordAttemptsCounter=");
            sb.append(this.wrongPasswordAttemptsCounter);
        }
        if (this.notificationToCSE) {
            sb.append(", notificationToCSE");
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
    }

        sb.append("]");
        return sb.toString();
    }
}
