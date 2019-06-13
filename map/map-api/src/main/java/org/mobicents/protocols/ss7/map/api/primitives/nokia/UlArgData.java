package org.mobicents.protocols.ss7.map.api.primitives.nokia;

import java.io.Serializable;

/**
 * <code>
 UlArgData ::= SEQUENCE {
     privateFeatureCode  [1] PrivateFeatureCode OPTIONAL,
     privateFeatureUlArgData PrivateFeatureUlArgData OPTIONAL,
 ... }
 * </code>
 *
 * User: Eva Ogallar
 * Date: 7/02/19 12:13
 */

public interface UlArgData extends Serializable {

    PrivateFeatureUlArgData getPrivateFeatureUlArgData();

    PrivateFeatureCode getPrivateFeatureCode();
}
