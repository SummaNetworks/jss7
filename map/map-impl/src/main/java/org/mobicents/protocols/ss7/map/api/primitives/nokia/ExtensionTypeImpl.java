package org.mobicents.protocols.ss7.map.api.primitives.nokia;

import java.io.IOException;
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AbstractMAPAsnPrimitive;

/**
 * User: Eva Ogallar
 * Date: 7/02/19 12:13
 */
public class ExtensionTypeImpl extends AbstractMAPAsnPrimitive implements ExtensionType {


    public static final int _TAG_ulArgTypeList = 7;

    private static final String DEFAULT_STRING_VALUE = null;

    public static final String _PrimitiveName = "ExtensionTypeImpl";

    private ArrayList<UlArgData> ulArgTypeList;

    public ExtensionTypeImpl() {
    }

    public ExtensionTypeImpl(ArrayList<UlArgData> ulArgTypeList) {
        this.ulArgTypeList = ulArgTypeList;
    }

    public ArrayList<UlArgData> getUlArgTypeList() {
        return ulArgTypeList;
    }

    public int getTag() throws MAPException {
        if (this.ulArgTypeList != null) {
            return _TAG_ulArgTypeList;
        } else {
            return 0;
        }
    }

    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    public boolean getIsPrimitive() {
        return false;
    }


    public void _decode(AsnInputStream ais, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.ulArgTypeList = null;

        int tag = ais.getTag();

        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
            switch (tag) {
                case _TAG_ulArgTypeList:
                    if (ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".ulArgTypeList: Parameter is primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);

                    //AsnInputStream ais2 = ais.readSequenceStream();
                    this.ulArgTypeList = new ArrayList<>();
                    while (true) {
                        if (ais.available() == 0)
                            break;

                        //ais2.readTag();

                        UlArgData ulArgData = new UlArgDataImpl();
                        ((UlArgDataImpl) ulArgData).decodeAll(ais);
                        this.ulArgTypeList.add(ulArgData);
                    }
                    if (this.ulArgTypeList.size() < 1 || this.ulArgTypeList.size() > 50) {
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ": Parameter ulArgTypeList size must be from 1 to 70, found: " + this.ulArgTypeList.size(),
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    }
                    break;
                default:
                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tag",
                            MAPParsingComponentExceptionReason.MistypedParameter);
            }
        } else {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tagClass",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        }

    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.ulArgTypeList != null) {
            try {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_ulArgTypeList);
                int pos = asnOs.StartContentDefiniteLength();
                for (UlArgData item : this.ulArgTypeList) {
                    ((UlArgDataImpl) item).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            } catch (AsnException e) {
                throw new MAPException("AsnException when encoding " + _PrimitiveName + ".ulArgTypeList: " + e.getMessage(), e);
            }
        }


    }

    @Override
    protected String getPrimitiveName() {
        return _PrimitiveName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExtensionTypeImpl [");

        if (this.ulArgTypeList != null) {
            sb.append("ulArgTypeList=");
            sb.append(this.ulArgTypeList);
        }

        sb.append("]");

        return sb.toString();
    }


}
