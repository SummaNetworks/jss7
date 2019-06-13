package org.mobicents.protocols.ss7.map.api.primitives.nokia;

import java.io.Serializable;

/**
 * <code>
 *      PrivateFeatureCode ::= OCTET STRING (SIZE (1))
 * </code>
 *
 * User: Eva Ogallar
 * Date: 7/02/19 12:13
 */

public interface PrivateFeatureCode extends Serializable {

    int getData();

}
