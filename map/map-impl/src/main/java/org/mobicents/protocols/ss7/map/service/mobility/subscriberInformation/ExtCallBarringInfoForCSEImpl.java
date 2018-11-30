/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AbstractMAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ExtCallBarringInfoForCSE;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtCallBarringFeature;
import org.mobicents.protocols.ss7.map.api.service.supplementary.Password;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.datacoding.IntegerEncoderFacility;
import org.mobicents.protocols.ss7.map.datacoding.NullEncoderFacility;
import org.mobicents.protocols.ss7.map.datacoding.ObjectEncoderFacility;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtCallBarringFeatureImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.PasswordImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;

/**
 *
 <code>
 Ext-CallBarringInfoFor-CSE ::= SEQUENCE {
     ss-Code                       [0] SS-Code,
     callBarringFeatureList        [1] Ext-CallBarFeatureList,
     password                      [2] Password OPTIONAL,
     wrongPasswordAttemptsCounter  [3] WrongPasswordAttemptsCounter OPTIONAL,
     notificationToCSE             [4] NULL OPTIONAL,
     extensionContainer            [5] ExtensionContainer OPTIONAL,
     ...
 }

 Ext-CallBarFeatureList ::= SEQUENCE SIZE (1..32) OF Ext-CallBarringFeature

 WrongPasswordAttemptsCounter ::= INTEGER (0..4)
 </code>
 * @author eva ogallar
 *
 */
public class ExtCallBarringInfoForCSEImpl extends AbstractMAPAsnPrimitive implements ExtCallBarringInfoForCSE {

    public static final String _PrimitiveName = "ExtCallBarringInfoForCSE";

    private SSCode ssCode;
    private ArrayList<ExtCallBarringFeature> callBarringFeatureList;
    private Password password;
    private Integer wrongPasswordAttemptsCounter;
    private boolean notificationToCSE;
    private MAPExtensionContainer extensionContainer;


    private static final int TAG_SS_CODE = 0;
    private static final int TAG_CALL_BARRING_FEATURE_LIST = 1;
    private static final int TAG_PASSWORD = 2;
    private static final int TAG_WRONG_PASSWORD_ATTEMPTS_COUNTER = 3;
    private static final int TAG_NOTIFICATION_TO_CSE = 4;
    private static final int TAG_EXTENSION_CONTAINER = 5;

    public ExtCallBarringInfoForCSEImpl() {
    }

    public ExtCallBarringInfoForCSEImpl(SSCode ssCode, ArrayList<ExtCallBarringFeature> callBarringFeatureList,
                                        Password password, Integer wrongPasswordAttemptsCounter, boolean notificationToCSE,
                                        MAPExtensionContainer extensionContainer) {
        this.ssCode = ssCode;
        this.callBarringFeatureList = callBarringFeatureList;
        this.password = password;
        this.wrongPasswordAttemptsCounter = wrongPasswordAttemptsCounter;
        this.notificationToCSE = notificationToCSE;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public SSCode getSsCode() {
        return ssCode;
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

        ssCode = null;
        callBarringFeatureList = new ArrayList<ExtCallBarringFeature>();
        password = null;
        wrongPasswordAttemptsCounter = null;
        notificationToCSE = false;
        extensionContainer = null;


        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag(); //
            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case TAG_SS_CODE:
                        ssCode = (SSCode) ObjectEncoderFacility.decodePrimitiveObject(ais, new SSCodeImpl(), "ssCode", getPrimitiveName());
                        break;
                    case TAG_CALL_BARRING_FEATURE_LIST:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".callBarringFeatureList: is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        AsnInputStream ais2 = ais.readSequenceStream();
                        this.callBarringFeatureList = new ArrayList<ExtCallBarringFeature>();
                        while (true) {
                            if (ais2.available() == 0)
                                break;

                            int tag2 = ais2.readTag();
                            if (tag2 != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": bad callBarringFeatureList tag or tagClass or is primitive ",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            ExtCallBarringFeature elem = new ExtCallBarringFeatureImpl();
                            ((ExtCallBarringFeatureImpl) elem).decodeAll(ais2);
                            this.callBarringFeatureList.add(elem);
                        }
                        if (this.callBarringFeatureList.size() < 1 || this.callBarringFeatureList.size() > 32) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter callBarringFeatureList size must be from 1 to 32, found: "
                                    + this.callBarringFeatureList.size(), MAPParsingComponentExceptionReason.MistypedParameter);
                        }

                        break;
                    case TAG_PASSWORD:
                        password = (Password) ObjectEncoderFacility.decodePrimitiveObject(ais, new PasswordImpl(), "password", getPrimitiveName());
                        break;
                    case TAG_WRONG_PASSWORD_ATTEMPTS_COUNTER:
                        wrongPasswordAttemptsCounter = IntegerEncoderFacility.decode(ais, "wrongPasswordAttemptsCounter", getPrimitiveName());
                        break;
                    case TAG_NOTIFICATION_TO_CSE:
                        this.notificationToCSE = NullEncoderFacility.decode(ais, "notificationToCSE", _PrimitiveName);
                        break;
                    case TAG_EXTENSION_CONTAINER:
                        extensionContainer = (MAPExtensionContainer) ObjectEncoderFacility.
                                decodePrimitiveObject(ais, new MAPExtensionContainerImpl(), "extensionContainer", getPrimitiveName());
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }
        validate();
    }

    private void validate() throws MAPParsingComponentException {
        if (this.ssCode == null) {
            throw new MAPParsingComponentException(
                    "Error while decoding " + getPrimitiveName() + ": ssCode is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
        if (this.callBarringFeatureList == null ||
                this.callBarringFeatureList.size() > 32 || this.callBarringFeatureList.size() < 1 ){
            throw new MAPParsingComponentException(
                    "Error while decoding " + getPrimitiveName() + ": callBarringFeatureList size must be between 1 and 32 ",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        }
    }


    public void encodeData(AsnOutputStream asnOs) throws MAPException {


        try {
            validate();
        } catch (MAPParsingComponentException e) {
            throw new MAPException(e.getMessage());
        }

        ((SSCodeImpl) this.ssCode).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_SS_CODE);

        try {
            asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, TAG_CALL_BARRING_FEATURE_LIST);
            int pos = asnOs.StartContentDefiniteLength();
            for (ExtCallBarringFeature extCallBarringFeature : this.callBarringFeatureList) {
                if (extCallBarringFeature != null) {
                    ((ExtCallBarringFeatureImpl) extCallBarringFeature).encodeAll(asnOs);
                }
            }
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }

        if (this.password != null) {
            ((PasswordImpl) this.password).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_PASSWORD);
        }

        if (this.wrongPasswordAttemptsCounter != null) {
            IntegerEncoderFacility.encode(asnOs, wrongPasswordAttemptsCounter, Tag.CLASS_CONTEXT_SPECIFIC,
                    TAG_WRONG_PASSWORD_ATTEMPTS_COUNTER,"wrongPasswordAttemptsCounter");
        }

        NullEncoderFacility.encode(asnOs, this.notificationToCSE, Tag.CLASS_CONTEXT_SPECIFIC, TAG_NOTIFICATION_TO_CSE, "longFtnSupported");

        if (this.extensionContainer != null) {
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_EXTENSION_CONTAINER);
        }

    }

    @Override
    protected String getPrimitiveName() {
        return _PrimitiveName;
    }

}
