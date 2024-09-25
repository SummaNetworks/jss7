/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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
package org.mobicents.protocols.ss7.m3ua.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.As;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.RouteAs;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.message.ssnm.DestinationAvailableImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.ssnm.DestinationAvailable;
import org.mobicents.protocols.ss7.m3ua.message.ssnm.DestinationRestricted;
import org.mobicents.protocols.ss7.m3ua.message.ssnm.DestinationStateAudit;
import org.mobicents.protocols.ss7.m3ua.message.ssnm.DestinationUPUnavailable;
import org.mobicents.protocols.ss7.m3ua.message.ssnm.DestinationUnavailable;
import org.mobicents.protocols.ss7.m3ua.message.ssnm.SignallingCongestion;
import org.mobicents.protocols.ss7.m3ua.parameter.AffectedPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.CongestedIndication;
import org.mobicents.protocols.ss7.m3ua.parameter.CongestedIndication.CongestionLevel;
import org.mobicents.protocols.ss7.m3ua.parameter.ErrorCode;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.UserCause;
import org.mobicents.protocols.ss7.mtp.Mtp3PausePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3ResumePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusCause;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusPrimitive;

/**
 *
 * @author amit bhayani
 *
 */
public class SignalingNetworkManagementHandler extends MessageHandler {

    private static final Logger logger = LogManager.getLogger(SignalingNetworkManagementHandler.class);

    public SignalingNetworkManagementHandler(AspFactoryImpl aspFactoryImpl) {
        super(aspFactoryImpl);
    }

    public void handleDestinationUnavailable(DestinationUnavailable duna) {

        RoutingContext rcObj = duna.getRoutingContexts();

        if(duna.getAffectedPointCodes() != null) {
            logger.warn(String.format("DUNA received for pointCodes %s and context %s",
                    Arrays.toString(duna.getAffectedPointCodes().getPointCodes()),
                    rcObj != null ? Arrays.toString(rcObj.getRoutingContexts()) : null
                    ));
        }

        // FIXME: 24/11/20 by Ajimenez - Por qué no se tenia en cuenta en IPSP?
        if (aspFactoryImpl.getFunctionality() == Functionality.AS || aspFactoryImpl.getFunctionality() == Functionality.IPSP) {
            if (rcObj == null) {
                AspImpl aspImpl = this.getAspForNullRc();
                if (aspImpl == null) {
                    ErrorCode errorCodeObj = this.aspFactoryImpl.parameterFactory
                            .createErrorCode(ErrorCode.Invalid_Routing_Context);
                    sendError(rcObj, errorCodeObj);
                    logger.error(String
                            .format("Rx : DUNA=%s with null RC for Aspfactory=%s. But no ASP configured for null RC. Sending back Error",
                                    duna, this.aspFactoryImpl.getName()));
                    return;
                }

                FSM fsm = aspImpl.getLocalFSM();

                if (fsm == null) {
                    logger.error(String.format("Rx : DUNA=%s for ASP=%s. But Local FSM is null.", duna,
                            this.aspFactoryImpl.getName()));
                    return;
                }

                AspState aspState = AspState.getState(fsm.getState().getName());

                if (aspState == AspState.ACTIVE) {

                    aspImpl.processDuna(duna);

                    AffectedPointCode affectedPcObjs = duna.getAffectedPointCodes();
                    int[] affectedPcs = affectedPcObjs.getPointCodes();

                    for (int i = 0; i < affectedPcs.length; i++) {
                        Mtp3PausePrimitive mtpPausePrimi = new Mtp3PausePrimitive(affectedPcs[i]);
                        ((AsImpl) aspImpl.getAs()).getM3UAManagement().sendPauseMessageToLocalUser(mtpPausePrimi);
                    }
                } else {
                    logger.error(String.format("Rx : DUNA for null RoutingContext. But ASP State=%s. Message=%s", aspState,
                            duna));
                }

            } else {
                long[] rcs = rcObj.getRoutingContexts();
                for (int count = 0; count < rcs.length; count++) {
                    AspImpl aspImpl = this.aspFactoryImpl.getAsp(rcs[count]);
                    if (aspImpl == null) {
                        // this is error. Send back error
                        RoutingContext rcObjTemp = this.aspFactoryImpl.parameterFactory
                                .createRoutingContext(new long[] { rcs[count] });
                        ErrorCode errorCodeObj = this.aspFactoryImpl.parameterFactory
                                .createErrorCode(ErrorCode.Invalid_Routing_Context);
                        sendError(rcObjTemp, errorCodeObj);
                        logger.error(String
                                .format("Rx : DUNA=%s with RC=%d for Aspfactory=%s. But no ASP configured for this RC. Sending back Error",
                                        duna, rcs[count], this.aspFactoryImpl.getName()));
                        continue;
                    }// if (asp == null)

                    FSM fsm = aspImpl.getLocalFSM();

                    if (fsm == null) {
                        logger.error(String.format("Rx : DUNA=%s for ASP=%s. But Local FSM is null.", duna,
                                this.aspFactoryImpl.getName()));
                        return;
                    }

                    AspState aspState = AspState.getState(fsm.getState().getName());

                    if (aspState == AspState.ACTIVE) {

                        aspImpl.processDuna(duna);

                        /* XXX: Disable the PAUSE or prohibited message for all the PC received in all the systems.
                        AffectedPointCode affectedPcObjs = duna.getAffectedPointCodes();
                        int[] affectedPcs = affectedPcObjs.getPointCodes();

                        for (int i = 0; i < affectedPcs.length; i++) {
                            Mtp3PausePrimitive mtpPausePrimi = new Mtp3PausePrimitive(affectedPcs[i]);
                            ((AsImpl) aspImpl.getAs()).getM3UAManagement().sendPauseMessageToLocalUser(mtpPausePrimi);
                        }*/
                    } else {
                        logger.error(String.format("Rx : DUNA for RoutingContext=%d. But ASP State=%s. Message=%s", rcs[count],
                                aspState, duna));
                    }
                }// for loop
            }

        } else {
            // TODO : Should we silently drop DUNA?

            logger.error(String.format(
                    "Rx : DUNA =%s But AppServer Functionality is not As. Sending back ErrorCode.Unexpected_Message", duna));

            ErrorCode errorCodeObj = this.aspFactoryImpl.parameterFactory.createErrorCode(ErrorCode.Unexpected_Message);
            sendError(rcObj, errorCodeObj);
        }
    }

    public void handleDestinationAvailable(DestinationAvailable dava) {
        RoutingContext rcObj = dava.getRoutingContexts();

        if(dava.getAffectedPointCodes() != null) {
            logger.warn(String.format("DAVA received for pointCodes %s", Arrays.toString(dava.getAffectedPointCodes().getPointCodes())));
        }

        // FIXME: 24/11/20 by Ajimenez - Por qué no se tenia en cuenta en IPSP?
        if (aspFactoryImpl.getFunctionality() == Functionality.AS || aspFactoryImpl.getFunctionality() == Functionality.IPSP) {

            if (rcObj == null) {
                AspImpl aspImpl = this.getAspForNullRc();
                if (aspImpl == null) {
                    ErrorCode errorCodeObj = this.aspFactoryImpl.parameterFactory
                            .createErrorCode(ErrorCode.Invalid_Routing_Context);
                    sendError(rcObj, errorCodeObj);
                    logger.error(String
                            .format("Rx : DAVA=%s with null RC for Aspfactory=%s. But no ASP configured for null RC. Sending back Error",
                                    dava, this.aspFactoryImpl.getName()));
                    return;
                }

                FSM fsm = aspImpl.getLocalFSM();

                if (fsm == null) {
                    logger.error(String.format("Rx : DAVA=%s for ASP=%s. But Local FSM is null.", dava,
                            this.aspFactoryImpl.getName()));
                    return;
                }

                AspState aspState = AspState.getState(fsm.getState().getName());

                if (aspState == AspState.ACTIVE) {

                    aspImpl.processDava(dava);

                    AffectedPointCode affectedPcObjs = dava.getAffectedPointCodes();
                    int[] affectedPcs = affectedPcObjs.getPointCodes();

                    for (int i = 0; i < affectedPcs.length; i++) {
                        Mtp3ResumePrimitive mtpResumePrimi = new Mtp3ResumePrimitive(affectedPcs[i]);
                        ((AsImpl) aspImpl.getAs()).getM3UAManagement().sendResumeMessageToLocalUser(mtpResumePrimi);
                    }
                } else {
                    logger.error(String.format("Rx : DAVA for null RoutingContext. But ASP State=%s. Message=%s", aspState,
                            dava));
                }

            } else {
                long[] rcs = rcObj.getRoutingContexts();
                for (int count = 0; count < rcs.length; count++) {
                    AspImpl aspImpl = this.aspFactoryImpl.getAsp(rcs[count]);
                    if (aspImpl == null) {
                        // this is error. Send back error
                        RoutingContext rcObjTemp = this.aspFactoryImpl.parameterFactory
                                .createRoutingContext(new long[] { rcs[count] });
                        ErrorCode errorCodeObj = this.aspFactoryImpl.parameterFactory
                                .createErrorCode(ErrorCode.Invalid_Routing_Context);
                        sendError(rcObjTemp, errorCodeObj);
                        logger.error(String
                                .format("Rx : DAVA=%s with RC=%d for Aspfactory=%s. But no ASP configured for this RC. Sending back Error",
                                        dava, rcs[count], this.aspFactoryImpl.getName()));
                        continue;
                    }// if (asp == null)

                    FSM fsm = aspImpl.getLocalFSM();

                    if (fsm == null) {
                        logger.error(String.format("Rx : DAVA=%s for ASP=%s. But Local FSM is null", dava,
                                this.aspFactoryImpl.getName()));
                        return;
                    }

                    AspState aspState = AspState.getState(fsm.getState().getName());

                    if (aspState == AspState.ACTIVE) {

                        aspImpl.processDava(dava);

                        AffectedPointCode affectedPcObjs = dava.getAffectedPointCodes();
                        int[] affectedPcs = affectedPcObjs.getPointCodes();

                        for (int i = 0; i < affectedPcs.length; i++) {
                            Mtp3ResumePrimitive mtpResumePrimi = new Mtp3ResumePrimitive(affectedPcs[i]);
                            ((AsImpl) aspImpl.getAs()).getM3UAManagement().sendResumeMessageToLocalUser(mtpResumePrimi);
                        }
                    } else {
                        logger.error(String.format("Rx : DAVA for RoutingContext=%d. But ASP State=%s. Message=%s", rcs[count],
                                aspState, dava));
                    }
                }// for loop
            }

        } else {
            // TODO : Should we silently drop DUNA?

            logger.error(String.format(
                    "Rx : DAVA =%s But AppServer Functionality is not As. Sending back ErrorCode.Unexpected_Message", dava));

            ErrorCode errorCodeObj = this.aspFactoryImpl.parameterFactory.createErrorCode(ErrorCode.Unexpected_Message);
            sendError(rcObj, errorCodeObj);
        }
    }

    public void handleDestinationStateAudit(DestinationStateAudit daud) {
        logger.info("DAUD: Received. Local functionality: "+ aspFactoryImpl.getFunctionality());
        try {
            //Validar el mensaje.
            if (daud.getAffectedPointCodes() == null) {
                logger.info("DAUD received without pointCodes! " +
                        (daud.getInfoString() != null ? "Info: " + daud.getInfoString() : ""));
                ErrorCode errorCodeObj = this.aspFactoryImpl.parameterFactory.createErrorCode(ErrorCode.Missing_Parameter);
                sendError(daud.getRoutingContexts(), errorCodeObj);
                return;
            } else {
                logger.info(String.format("DAUD received for pointCodes %s", Arrays.toString(daud.getAffectedPointCodes().getPointCodes())));
            }


            RoutingContext rcObj = daud.getRoutingContexts();
            if (rcObj == null) {
                logger.info("DAUD: No Routing context received.");
                AspImpl aspImpl = this.getAspForNullRc();
                // FIXME: 24/11/20 by Ajimenez - Terminar este caso.
            } else {
                logger.info("DAUD: Routing Context received.");
                // FIXME: 25/11/20 by Ajimenez - Asegurarse que efectivamente solo puede tener uno (un ASP solo puede estar en un AS)
                boolean destinationFound = false;
                logger.info("DAUD: Validating ONLY again the Association related with current ASP.");
                As as = this.aspFactoryImpl.getAspList().get(0).getAs();
                String asName = as.getName();
                logger.info("DAUD: Current Association: " + asName);

                //MATCH Routing Contexts given
                List<Long> rcMatched = new ArrayList<>();
                for(long rc : rcObj.getRoutingContexts()){
                    for(long arc : as.getRoutingContext().getRoutingContexts()){
                        if(rc == arc){
                            rcMatched.add(rc);
                        }
                    }
                }
                if(rcMatched.size() == 0){
                    logger.info("DAUD received but but no AS found for given Routing Contexts");
                    DestinationUnavailable msg = (DestinationUnavailable) this.aspFactoryImpl.messageFactory.createMessage(
                            MessageClass.SIGNALING_NETWORK_MANAGEMENT, MessageType.DESTINATION_UNAVAILABLE);
                    msg.setAffectedPointCodes(daud.getAffectedPointCodes());
                    msg.setRoutingContexts(daud.getRoutingContexts());
                    this.aspFactoryImpl.write(msg);
                }
                //Validate PCs.
                AffectedPointCode affectedPcObjs = daud.getAffectedPointCodes();
                logger.info("DAUD: Searching AS for those point codes.");
                int[] affectedPcs = affectedPcObjs.getPointCodes();
                for (int i = 0; i < affectedPcs.length; i++) {
                    logger.info("DAUD: Going over routes.");
                    for (Map.Entry<String, RouteAs> es : this.aspFactoryImpl.getM3UAManagement().getRoute().entrySet()) {
                        //Busco el AS (dnd está el ASP) en los routes.
                        logger.info("DAUD: Route rule: " + es.getKey());
                        for (As asRouted : es.getValue().getAsArray()) {
                            //Check routes for this AS.
                            logger.info("DAUD: Checking AS and its PC: " + asRouted.getName());
                            if (asRouted.getName().equals(asName)) {
                                //Check if point code is declared in route rules.
                                String[] pcs = es.getKey().split(":");
                                if ((Integer.valueOf(pcs[0]).intValue() == affectedPcs[i] || Integer.valueOf(pcs[1]).intValue() == affectedPcs[i])) {
                                    //Point code asked is configured in this AS.
                                    logger.info("DAUD: Point code found. Checking state.");
                                    if (asRouted.isUp() && this.aspFactoryImpl.getAspList().get(0).isUp()) {
                                        logger.info("DAUD: Active association with name " + asName + " found for the point code indicated by the DAUD.");
                                        DestinationAvailableImpl msg = (DestinationAvailableImpl) this.aspFactoryImpl.messageFactory.createMessage(
                                                MessageClass.SIGNALING_NETWORK_MANAGEMENT, MessageType.DESTINATION_AVAILABLE);
                                        AffectedPointCode apc = this.aspFactoryImpl.parameterFactory.createAffectedPointCode(new int[]{affectedPcs[i]}, new short []{0});
                                        msg.setAffectedPointCodes(apc);
                                        //Routing Context matched.
                                        long [] rcArray = new long [rcMatched.size()];
                                        for(int j = 0; j < rcArray.length; j++){
                                            rcArray[0] = rcMatched.get(j).longValue();
                                        }
                                        RoutingContext rc = this.aspFactoryImpl.parameterFactory.createRoutingContext(rcArray);
                                        msg.setRoutingContexts(rc);
                                        this.aspFactoryImpl.write(msg);
                                        destinationFound = true;
                                        break;
                                    } else {
                                        logger.info("DAUD: State no valid. AS " + asRouted.getState() + ", ASP status " + this.aspFactoryImpl.getAspList().get(0).getState());
                                        logger.info("DAUD: Trying with next route.");
                                    }
                                }
                            }
                        }
                        if (destinationFound)
                            break;
                    }
                    if (!destinationFound) {
                        logger.info("DAUD received but ASP/AS active for point code indicated not found.");
                        DestinationUnavailable msg = (DestinationUnavailable) this.aspFactoryImpl.messageFactory.createMessage(
                                MessageClass.SIGNALING_NETWORK_MANAGEMENT, MessageType.DESTINATION_UNAVAILABLE);
                        AffectedPointCode apc = this.aspFactoryImpl.parameterFactory.createAffectedPointCode(new int[]{affectedPcs[i]}, new short []{0});
                        msg.setAffectedPointCodes(apc);
                        this.aspFactoryImpl.write(msg);
                    }
                }
            }

        } catch (Exception e){
            logger.error("Unexpected error handling DAUD.", e);

        }
//        RoutingContext rcObj = daud.getRoutingContexts();
//        if (aspFactoryImpl.getFunctionality() == Functionality.SGW) {
//            logger.warn(String.format("Received DAUD=%s. Handling of DAUD message is not yet implemented", daud));
//        } else {
//            // TODO : Should we silently drop DUNA?
//
//            logger.error(String.format(
//                    "Rx : DAUD =%s But AppServer Functionality is not SGW. Sending back ErrorCode.Unexpected_Message", daud));
//
//            // ASPACTIVE_ACK is unexpected in this state
//            ErrorCode errorCodeObj = this.aspFactoryImpl.parameterFactory.createErrorCode(ErrorCode.Unexpected_Message);
//            sendError(rcObj, errorCodeObj);
//        }
    }

    public void handleSignallingCongestion(SignallingCongestion scon) {
        RoutingContext rcObj = scon.getRoutingContexts();
        if (aspFactoryImpl.getFunctionality() == Functionality.AS || aspFactoryImpl.getFunctionality() == Functionality.IPSP) {
            if (rcObj == null) {
                AspImpl aspImpl = this.getAspForNullRc();
                if (aspImpl == null) {
                    ErrorCode errorCodeObj = this.aspFactoryImpl.parameterFactory
                            .createErrorCode(ErrorCode.Invalid_Routing_Context);
                    sendError(rcObj, errorCodeObj);
                    logger.error(String
                            .format("Rx : SCON=%s with null RC for Aspfactory=%s. But no ASP configured for null RC. Sending back Error",
                                    scon, this.aspFactoryImpl.getName()));
                    return;
                }

                FSM fsm = aspImpl.getLocalFSM();

                if (fsm == null) {
                    logger.error(String.format("Rx : SCON=%s for ASP=%s. But Local FSM is null.", scon,
                            this.aspFactoryImpl.getName()));
                    return;
                }

                AspState aspState = AspState.getState(fsm.getState().getName());

                if (aspState == AspState.ACTIVE) {
                    AffectedPointCode affectedPcObjs = scon.getAffectedPointCodes();
                    int[] affectedPcs = affectedPcObjs.getPointCodes();

                    int cong = 0;
                    for (int i = 0; i < affectedPcs.length; i++) {
                        CongestedIndication congeInd = scon.getCongestedIndication();
                        if (congeInd != null) {
                            CongestionLevel congLevel = congeInd.getCongestionLevel();
                            if (congLevel != null) {
                                cong = congLevel.getLevel();
                            }
                        }

                        Mtp3StatusPrimitive mtpPausePrimi = new Mtp3StatusPrimitive(affectedPcs[i],
                                Mtp3StatusCause.SignallingNetworkCongested, cong);
                        ((AsImpl) aspImpl.getAs()).getM3UAManagement().sendStatusMessageToLocalUser(mtpPausePrimi);
                    }
                } else {
                    logger.error(String.format("Rx : SCON for null RoutingContext. But ASP State=%s. Message=%s", aspState,
                            scon));
                }
            } else {
                long[] rcs = rcObj.getRoutingContexts();
                for (int count = 0; count < rcs.length; count++) {
                    AspImpl aspImpl = this.aspFactoryImpl.getAsp(rcs[count]);
                    if (aspImpl == null) {
                        // this is error. Send back error
                        RoutingContext rcObjTemp = this.aspFactoryImpl.parameterFactory
                                .createRoutingContext(new long[] { rcs[count] });
                        ErrorCode errorCodeObj = this.aspFactoryImpl.parameterFactory
                                .createErrorCode(ErrorCode.Invalid_Routing_Context);
                        sendError(rcObjTemp, errorCodeObj);
                        logger.error(String
                                .format("Rx : SCON=%s with RC=%d for Aspfactory=%s. But no ASP configured for this RC. Sending back Error",
                                        scon, rcs[count], this.aspFactoryImpl.getName()));
                        continue;
                    }// if (asp == null)

                    FSM fsm = aspImpl.getLocalFSM();

                    if (fsm == null) {
                        logger.error(String.format("Rx : SCON=%s for ASP=%s. But Local FSM is null", scon,
                                this.aspFactoryImpl.getName()));
                        return;
                    }

                    AspState aspState = AspState.getState(fsm.getState().getName());

                    if (aspState == AspState.ACTIVE) {
                        AffectedPointCode affectedPcObjs = scon.getAffectedPointCodes();
                        int[] affectedPcs = affectedPcObjs.getPointCodes();

                        int cong = 0;
                        for (int i = 0; i < affectedPcs.length; i++) {
                            CongestedIndication congeInd = scon.getCongestedIndication();
                            if (congeInd != null) {
                                CongestionLevel congLevel = congeInd.getCongestionLevel();
                                if (congLevel != null) {
                                    cong = congLevel.getLevel();
                                }
                            }

                            Mtp3StatusPrimitive mtpPausePrimi = new Mtp3StatusPrimitive(affectedPcs[i],
                                    Mtp3StatusCause.SignallingNetworkCongested, cong);
                            ((AsImpl) aspImpl.getAs()).getM3UAManagement().sendStatusMessageToLocalUser(mtpPausePrimi);
                        }
                    } else {
                        logger.error(String.format("Rx : DAVA for RoutingContext=%d. But ASP State=%s. Message=%s", rcs[count],
                                aspState, scon));
                    }
                }// for loop
            }

        } else {
            logger.error(String.format(
                    "Rx : SCON =%s But AppServer Functionality is not AS or IPSP. Sending back ErrorCode.Unexpected_Message",
                    scon));

            // SCON is unexpected in this state
            ErrorCode errorCodeObj = this.aspFactoryImpl.parameterFactory.createErrorCode(ErrorCode.Unexpected_Message);
            sendError(rcObj, errorCodeObj);
        }
    }

    public void handleDestinationUPUnavailable(DestinationUPUnavailable dupu) {
        RoutingContext rcObj = dupu.getRoutingContext();

        if (aspFactoryImpl.getFunctionality() == Functionality.AS) {

            if (rcObj == null) {
                AspImpl aspImpl = this.getAspForNullRc();
                if (aspImpl == null) {
                    ErrorCode errorCodeObj = this.aspFactoryImpl.parameterFactory
                            .createErrorCode(ErrorCode.Invalid_Routing_Context);
                    sendError(rcObj, errorCodeObj);
                    logger.error(String
                            .format("Rx : DUPU=%s with null RC for Aspfactory=%s. But no ASP configured for null RC. Sending back Error",
                                    dupu, this.aspFactoryImpl.getName()));
                    return;
                }

                FSM fsm = aspImpl.getLocalFSM();

                if (fsm == null) {
                    logger.error(String.format("Rx : DUPU=%s for ASP=%s. But Local FSM is null.", dupu,
                            this.aspFactoryImpl.getName()));
                    return;
                }

                AspState aspState = AspState.getState(fsm.getState().getName());

                if (aspState == AspState.ACTIVE) {
                    AffectedPointCode affectedPcObjs = dupu.getAffectedPointCode();
                    int[] affectedPcs = affectedPcObjs.getPointCodes();

                    int cause = 0;
                    for (int i = 0; i < affectedPcs.length; i++) {

                        UserCause userCause = dupu.getUserCause();
                        cause = userCause.getCause();
                        Mtp3StatusPrimitive mtpPausePrimi = new Mtp3StatusPrimitive(affectedPcs[i],
                                Mtp3StatusCause.getMtp3StatusCause(cause), 0);
                        ((AsImpl) aspImpl.getAs()).getM3UAManagement().sendStatusMessageToLocalUser(mtpPausePrimi);
                    }
                } else {
                    logger.error(String.format("Rx : DUPU for null RoutingContext. But ASP State=%s. Message=%s", aspState,
                            dupu));
                }

            } else {
                long[] rcs = rcObj.getRoutingContexts();
                for (int count = 0; count < rcs.length; count++) {
                    AspImpl aspImpl = this.aspFactoryImpl.getAsp(rcs[count]);
                    if (aspImpl == null) {
                        // this is error. Send back error
                        RoutingContext rcObjTemp = this.aspFactoryImpl.parameterFactory
                                .createRoutingContext(new long[] { rcs[count] });
                        ErrorCode errorCodeObj = this.aspFactoryImpl.parameterFactory
                                .createErrorCode(ErrorCode.Invalid_Routing_Context);
                        sendError(rcObjTemp, errorCodeObj);
                        logger.error(String
                                .format("Rx : DUPU=%s with RC=%d for Aspfactory=%s. But no ASP configured for this RC. Sending back Error",
                                        dupu, rcs[count], this.aspFactoryImpl.getName()));
                        continue;
                    }// if (asp == null)

                    FSM fsm = aspImpl.getLocalFSM();

                    if (fsm == null) {
                        logger.error(String.format("Rx : DUPU=%s for ASP=%s. But Local FSM is null", dupu,
                                this.aspFactoryImpl.getName()));
                        return;
                    }

                    AspState aspState = AspState.getState(fsm.getState().getName());

                    if (aspState == AspState.ACTIVE) {
                        AffectedPointCode affectedPcObjs = dupu.getAffectedPointCode();
                        int[] affectedPcs = affectedPcObjs.getPointCodes();
                        int cause = 0;
                        for (int i = 0; i < affectedPcs.length; i++) {

                            UserCause userCause = dupu.getUserCause();
                            cause = userCause.getCause();
                            Mtp3StatusPrimitive mtpPausePrimi = new Mtp3StatusPrimitive(affectedPcs[i],
                                    Mtp3StatusCause.getMtp3StatusCause(cause), 0);
                            ((AsImpl) aspImpl.getAs()).getM3UAManagement().sendStatusMessageToLocalUser(mtpPausePrimi);
                        }
                    } else {
                        logger.error(String.format("Rx : DUPU for RoutingContext=%d. But ASP State=%s. Message=%s", rcs[count],
                                aspState, dupu));
                    }
                }// for loop
            }

        } else {
            // TODO : Should we silently drop DUNA?

            logger.error(String.format(
                    "Rx : DUPU =%s But AppServer Functionality is not AS. Sending back ErrorCode.Unexpected_Message", dupu));

            ErrorCode errorCodeObj = this.aspFactoryImpl.parameterFactory.createErrorCode(ErrorCode.Unexpected_Message);
            sendError(rcObj, errorCodeObj);
        }
    }

    public void handleDestinationRestricted(DestinationRestricted drst) {
        if (aspFactoryImpl.getFunctionality() == Functionality.AS) {
            if (logger.isWarnEnabled()) {
                logger.warn(String.format("Received DRST message for AS side. Not implemented yet", drst));
            }
        } else {
            // TODP log error
        }
    }

}
