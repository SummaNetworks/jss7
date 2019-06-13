package org.mobicents.protocols.ss7.map.api.primitives.nokia;

import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.primitives.IMEI;


/**
 * <code>
 * PrivateFeatureUlArgData ::= CHOICE {
 *          adc [3] IMEI
 * }
 * </code>
 *
 * User: eva
 * Date: 7/02/19 12:13
 */

public interface PrivateFeatureUlArgData extends Serializable {

    IMEI getAdc();
}
