package org.mobicents.protocols.ss7.map.api.primitives.nokia;

import org.mobicents.protocols.ss7.map.primitives.OctetStringLength1Base;

/**
 * <code>
 *      PrivateFeatureCode ::= OCTET STRING (SIZE (1))
 * </code>
 *
 * User: Eva Ogallar
 * Date: 7/02/19 12:13
 */
public class PrivateFeatureCodeImpl extends OctetStringLength1Base implements PrivateFeatureCode {

    public PrivateFeatureCodeImpl() {
        super("PrivateFeatureCode");
    }

    public PrivateFeatureCodeImpl(int data) {
        super("PrivateFeatureCode", data);
    }

    public int getData() {
        return data;
    }
}
