package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedServingNode;
import org.mobicents.protocols.ss7.map.primitives.BitStringBase;

/**
 * @author eva ogallar
 */
public class RequestedServingNodeImpl extends BitStringBase implements RequestedServingNode {


    public static final String PRIMITIVE_NAME = "RequestedServingNode";

    private static final int INDEX_MME_AND_SGSN = 0;

    private boolean mmeAndSgsn;

    public RequestedServingNodeImpl() {
        super(1, 8, 1, PRIMITIVE_NAME);
    }

    public RequestedServingNodeImpl(boolean mmeAndSgsn) {
        super(1, 8, 1, PRIMITIVE_NAME);
        if (mmeAndSgsn)
            this.bitString.set(INDEX_MME_AND_SGSN);
    }

    public boolean getMmeAndSgsn() {
        return bitString.get(INDEX_MME_AND_SGSN);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");
        if (this.getMmeAndSgsn())
            sb.append("MmeAndSgsn, ");
        sb.append("]");
        return sb.toString();
    }
}
