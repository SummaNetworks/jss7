package org.mobicents.protocols.ss7.map.service.lsm;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.mobicents.protocols.ss7.map.api.service.lsm.NetworkNodeDiameterAddress;
import org.mobicents.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 * <code>
 *  NetworkNodeDiameterAddress::= SEQUENCE {
 *          diameter-Name [0] DiameterIdentity,
 *          diameter-Realm [1] DiameterIdentity
 *  }
 *  </code>
 *
 * User: Eva Ogallar
 * Date: 23/6/20 13:51
 */
public class NetworkNodeDiameterAddressImpl extends SequenceBase implements NetworkNodeDiameterAddress {


    private static final int _TAG_diameterName = 0;
    private static final int _TAG_diameterRealm = 1;
    public static final String PRIMITIVE_NAME = "NetworkNodeDiameterAddress";

    private DiameterIdentity diameterName;
    private DiameterIdentity diameterRealm;


    public NetworkNodeDiameterAddressImpl() {
        super(PRIMITIVE_NAME);
    }

    public NetworkNodeDiameterAddressImpl(DiameterIdentity diameterName, DiameterIdentity diameterRealm) {
        super(PRIMITIVE_NAME);
        this.diameterName = diameterName;
        this.diameterRealm = diameterRealm;
    }/*

    @Override
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    public int getTag() throws MAPException {
        return Tag.CLASS_UNIVERSAL;
    }

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }
*/

    @Override
    public DiameterIdentity getDiameterName() {
        return diameterName;
    }

    @Override
    public DiameterIdentity getDiameterRealm() {
        return diameterRealm;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.diameterName = null;
        this.diameterRealm = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        int tag = ais.readTag();
        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive() || tag != _TAG_diameterName) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parameter 0 [diameterName [0] DiameterName] bad tag class, tag or not primitive",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
        this.diameterName = new DiameterIdentityImpl();
        ((DiameterIdentityImpl) this.diameterName).decodeAll(ais);

        tag = ais.readTag();
        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive() || tag != _TAG_diameterRealm) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parameter 1 [diameterRealm] bad tag class, tag or not primitive",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
        this.diameterRealm = new DiameterIdentityImpl();
        ((DiameterIdentityImpl) this.diameterRealm).decodeAll(ais);

        while (true) {
            if (ais.available() == 0)
                break;
            switch (ais.readTag()) {
                default:
                    ais.advanceElement();
                    break;
            }
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.diameterName == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter[diameterName [0] DiameterIdentity] is not defined");
        }

        if (this.diameterRealm == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter[diameterRealm [1] DiameterIdentity] is not defined");
        }

        ((DiameterIdentityImpl) this.diameterName).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_diameterName);
        ((DiameterIdentityImpl) this.diameterRealm).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_diameterRealm);

    }
}
