package org.mobicents.protocols.ss7.m3ua.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.message.ssnm.DestinationAvailable;
import org.mobicents.protocols.ss7.m3ua.message.ssnm.DestinationUnavailable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author ajimenez, created on 12/5/23.
 * STOPSHIP: Change name to DestinationAvailabilityStatusHandler ???
 **/
public class SsnmStateHandler {

    private static final Logger logger = LogManager.getLogger(SsnmStateHandler.class);

    private String aspName;

    private Set<SsnmItem> activeDunas = new HashSet<>();

    public SsnmStateHandler(String aspName) {
        this.aspName = aspName;
    }

    public int activeDunasCount() {
        return activeDunas.size();
    }

    public void processDuna(DestinationUnavailable dunaMessage) {
        List<SsnmItem> dunaItems = SsnmItem.buildSsnmItemsList(dunaMessage);
        logger.debug("SsnmItems generated: {}", dunaItems);

        this.activeDunas.addAll(dunaItems); //If equals will be replaced.
        logger.info("Active DUNAs in ASP {}: {}",this.aspName, this.activeDunas);

    }

    public void processDava(DestinationAvailable davaMessage) {
        List<SsnmItem> davaItems = SsnmItem.buildSsnmItemsList(davaMessage);
        logger.debug("SsnmItems generated: {}", davaItems);
        activeDunas.removeAll(davaItems);
        logger.info("Active DUNAs in ASP {}: {}",this.aspName, this.activeDunas);
    }

    public boolean isProhibited(int pointCode) {
        for (SsnmItem item : activeDunas) {
            if (item.isProhibited(pointCode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * SS7 Signalling Network Management messages split by theirs affected point codes.
     */
    private static class SsnmItem {
        private long[] routingContext; //optional. FIXME: Este valor es para procesar el DUNA de entrada o se tiene en cuenta luego para filtrar mensajes de salida?
        private String info; //optional
        //private AffectedPointCode affectedPointCodes; //masks and point-codes.

        private short mask;
        private int pointCode;

        private static List<SsnmItem> buildSsnmItemsList(DestinationUnavailable dunaMessage) {
            List<SsnmItem> result = new ArrayList<>(dunaMessage.getAffectedPointCodes().getMasks().length);
            for (int i = 0; i < dunaMessage.getAffectedPointCodes().getMasks().length; i++) {
                result.addAll(buildNoMaskedItems(new SsnmItem(
                        dunaMessage.getAffectedPointCodes().getPointCodes()[i],
                        dunaMessage.getAffectedPointCodes().getMasks()[i],
                        //Next values are just informative... could be ignored.
                        dunaMessage.getRoutingContexts() != null ? dunaMessage.getRoutingContexts().getRoutingContexts() : null,
                        dunaMessage.getInfoString() != null ? dunaMessage.getInfoString().getString() : null)));
            }
            return result;
            //this(dunaMessage.getAffectedPointCodes(), dunaMessage.getRoutingContexts().getRoutingContexts(), dunaMessage.getInfoString().getString());
        }

        private static List<SsnmItem> buildSsnmItemsList(DestinationAvailable davaMessage) {
            List<SsnmItem> result = new ArrayList<>(davaMessage.getAffectedPointCodes().getMasks().length);
            for (int i = 0; i < davaMessage.getAffectedPointCodes().getMasks().length; i++) {
                result.addAll(buildNoMaskedItems(new SsnmItem(
                        davaMessage.getAffectedPointCodes().getPointCodes()[i],
                        davaMessage.getAffectedPointCodes().getMasks()[i],
                        davaMessage.getRoutingContexts() != null ? davaMessage.getRoutingContexts().getRoutingContexts() : null,
                        davaMessage.getInfoString() != null ? davaMessage.getInfoString().getString() : null)));
            }
            return result;
            //this(dunaMessage.getAffectedPointCodes(), dunaMessage.getRoutingContexts().getRoutingContexts(), dunaMessage.getInfoString().getString());
        }

        private SsnmItem(int pointCode, short mask, long[] routingContext, String info) {
            this.pointCode = pointCode;
            this.mask = mask;
            this.routingContext = routingContext;
            this.info = info;
        }

        private static List<SsnmItem> buildNoMaskedItems(SsnmItem ssnmItem) {
            List<SsnmItem> result = new ArrayList<>();
            if (ssnmItem.mask == 0) {
                result.add(ssnmItem);
            } else {
                int maskedCount = 0;
                for (int i = 0; i < ssnmItem.mask; i++) {
                    maskedCount = maskedCount << 1;
                    maskedCount += 1;
                }
                for (int i = 0; i <= maskedCount; i++) {
                    int pcMasked = ssnmItem.pointCode & ~maskedCount;
                    SsnmItem si = new SsnmItem(pcMasked + i, (short) 0, ssnmItem.routingContext, ssnmItem.info);
                    result.add(si);
                }
            }
            return result;
        }

        private boolean isProhibited(int pointCode) {
            if (mask == 0) {
                if (this.pointCode == pointCode)
                    return true;
            } else {
                //The mask indicate how many bits are masked. (see https://www.rfc-editor.org/rfc/rfc4666.html#page-39)
                int pcMasked = this.pointCode >>> mask;
                int pointCodeMasked = pointCode >>> mask;
                if (pcMasked == pointCodeMasked) return true;
            }
            return false;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SsnmItem)) return false;
            SsnmItem dunaItem = (SsnmItem) o;
            return mask == dunaItem.mask && pointCode == dunaItem.pointCode;
        }

        @Override
        public int hashCode() {
            return Objects.hash(mask, pointCode);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("SsnmItem{");
            sb.append(" pointCode=").append(pointCode);
            sb.append(", mask=").append(mask);
            sb.append(", routingContext=").append(Arrays.toString(routingContext));
            sb.append(", info='").append(info).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}
