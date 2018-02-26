package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ServingNode;
import org.mobicents.protocols.ss7.map.primitives.BitStringBase;

/**
 * <code>
 ServingNode ::= BIT STRING {
 mme (0),
 sgsn (1)
 } (SIZE (2..8))
 -- Other bits than listed above shall be discarded.
 </code>
 *
 * @author eva ogallar
 */
public class ServingNodeImpl extends BitStringBase implements ServingNode {


    public static final String PRIMITIVE_NAME = "ServingNode";

    private static final int INDEX_MME = 0;
    private static final int INDEX_SGSN = 1;


    public ServingNodeImpl() {
        super(1, 8, 2, PRIMITIVE_NAME);
    }

    public ServingNodeImpl(boolean mme, boolean sgsn) {
        super(2, 8, 2, PRIMITIVE_NAME);
        if (mme) {
            this.bitString.set(INDEX_MME);
        }
        if (sgsn) {
            this.bitString.set(INDEX_SGSN);
        }
    }

    public boolean getMme() {
        return bitString.get(INDEX_MME);
    }

    public boolean getSgsn() {
        return bitString.get(INDEX_SGSN);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");
        if (this.getMme())
            sb.append("Mme, ");
        if (this.getSgsn())
            sb.append("Sgsn, ");
        sb.append("]");
        return sb.toString();
    }
}
