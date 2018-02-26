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

package org.mobicents.protocols.ss7.map.service.pdpContextActivation;

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.MAPDialogImpl;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.pdpContextActivation.FailureReportRequestImpl;
import org.mobicents.protocols.ss7.map.api.service.pdpContextActivation.FailureReportResponseImpl;
import org.mobicents.protocols.ss7.map.api.service.pdpContextActivation.MAPDialogPdpContextActivation;
import org.mobicents.protocols.ss7.map.api.service.pdpContextActivation.MAPServicePdpContextActivation;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MAPDialogPdpContextActivationImpl extends MAPDialogImpl implements MAPDialogPdpContextActivation {

    protected MAPDialogPdpContextActivationImpl(MAPApplicationContext appCntx, Dialog tcapDialog,
            MAPProviderImpl mapProviderImpl, MAPServicePdpContextActivation mapService, AddressString origReference,
            AddressString destReference) {
        super(appCntx, tcapDialog, mapProviderImpl, mapService, origReference, destReference);
    }


    @Override
    public Long addSendRoutingInfoForGprsRequest(IMSI imsi, GSNAddress ggsnAddress, ISDNAddressString ggsnNumber, MAPExtensionContainer extensionContainer)
            throws MAPException {
        return addSendRoutingInfoForGprsRequest(_Timer_Default, imsi, ggsnAddress, ggsnNumber, extensionContainer);
    }

    @Override
    public Long addSendRoutingInfoForGprsRequest(int customInvokeTimeout, IMSI imsi, GSNAddress ggsnAddress, ISDNAddressString ggsnNumber,
            MAPExtensionContainer extensionContainer) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.gprsLocationInfoRetrievalContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3 && this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version4))
            throw new MAPException("Bad application context name for addSendRoutingInfoForGprsRequest: must be gprsLocationInfoRetrievalContext_V3 or V4");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_m);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.sendRoutingInfoForGprs);
        invoke.setOperationCode(oc);

        SendRoutingInfoForGprsRequestImpl req = new SendRoutingInfoForGprsRequestImpl(imsi, ggsnAddress, ggsnNumber, extensionContainer);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
        p.setTagClass(req.getTagClass());
        p.setPrimitive(req.getIsPrimitive());
        p.setTag(req.getTag());
        p.setData(aos.toByteArray());
        invoke.setParameter(p);

        Long invokeId;
        try {
            invokeId = this.tcapDialog.getNewInvokeId();
            invoke.setInvokeId(invokeId);
        } catch (TCAPException e) {
            throw new MAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public void addSendRoutingInfoForGprsResponse(long invokeId, GSNAddress sgsnAddress, GSNAddress ggsnAddress, Integer mobileNotReachableReason,
            MAPExtensionContainer extensionContainer) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.gprsLocationInfoRetrievalContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3 &&
                this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version4))
            throw new MAPException("Bad application context name for addSendRoutingInfoForGprsResponse: must be gprsLocationInfoRetrievalContext_V3 or V4");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.sendRoutingInfoForGprs);
        resultLast.setOperationCode(oc);

        SendRoutingInfoForGprsResponseImpl resp = new SendRoutingInfoForGprsResponseImpl(sgsnAddress, ggsnAddress, mobileNotReachableReason, extensionContainer);
        AsnOutputStream aos = new AsnOutputStream();
        resp.encodeData(aos);

        Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
        p.setTagClass(resp.getTagClass());
        p.setPrimitive(resp.getIsPrimitive());
        p.setTag(resp.getTag());
        p.setData(aos.toByteArray());
        resultLast.setParameter(p);

        this.sendReturnResultLastComponent(resultLast);
    }

    @Override
    public Long addFailureReportRequest(IMSI imsi, ISDNAddressString ggsnNumber, GSNAddress ggsnAddress, MAPExtensionContainer extensionContainer)
            throws MAPException {
        return addFailureReportRequest(_Timer_Default, imsi, ggsnNumber, ggsnAddress, extensionContainer);
    }

    @Override
    public Long addFailureReportRequest(int customInvokeTimeout, IMSI imsi, ISDNAddressString ggsnNumber, GSNAddress ggsnAddress,
                                                 MAPExtensionContainer extensionContainer) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.failureReportContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3 &&
                this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version4))
            throw new MAPException("Bad application context name for addFailureReportRequest: must be failureReportContext_V3 or V4");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_m);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.failureReport);
        invoke.setOperationCode(oc);

        FailureReportRequestImpl req = new FailureReportRequestImpl(imsi, ggsnNumber, ggsnAddress, extensionContainer);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
        p.setTagClass(req.getTagClass());
        p.setPrimitive(req.getIsPrimitive());
        p.setTag(req.getTag());
        p.setData(aos.toByteArray());
        invoke.setParameter(p);

        Long invokeId;
        try {
            invokeId = this.tcapDialog.getNewInvokeId();
            invoke.setInvokeId(invokeId);
        } catch (TCAPException e) {
            throw new MAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }



    @Override
    public void addFailureReportResponse(long invokeId, GSNAddress ggsnAddress, MAPExtensionContainer extensionContainer) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.failureReportContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3 &&
                this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version4))
            throw new MAPException("Bad application context name for addFailureReportResponse: must be failureReportContext_V3 or V4");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.failureReport);
        resultLast.setOperationCode(oc);

        FailureReportResponseImpl resp = new FailureReportResponseImpl(ggsnAddress, extensionContainer);
        AsnOutputStream aos = new AsnOutputStream();
        resp.encodeData(aos);

        Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
        p.setTagClass(resp.getTagClass());
        p.setPrimitive(resp.getIsPrimitive());
        p.setTag(resp.getTag());
        p.setData(aos.toByteArray());
        resultLast.setParameter(p);

        this.sendReturnResultLastComponent(resultLast);
    }

    @Override
    public Long addNoteMsPresentForGprsRequest(IMSI imsi, GSNAddress sgsnAddress,
                                               GSNAddress ggsnAddress, MAPExtensionContainer extensionContainer) throws MAPException {
        return addNoteMsPresentForGprsRequest(_Timer_Default, imsi, sgsnAddress, ggsnAddress, extensionContainer);
    }

        @Override
    public Long addNoteMsPresentForGprsRequest(int customInvokeTimeout, IMSI imsi, GSNAddress sgsnAddress,
                                               GSNAddress ggsnAddress, MAPExtensionContainer extensionContainer) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.gprsNotifyContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
            throw new MAPException("Bad application context name for addNoteMsPresentForGprsRequest: must be gprsNotifyContext_V3 ");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_m);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.noteMsPresentForGprs);
        invoke.setOperationCode(oc);

        NoteMsPresentForGprsRequestImpl req = new NoteMsPresentForGprsRequestImpl(imsi, sgsnAddress, ggsnAddress, extensionContainer);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
        p.setTagClass(req.getTagClass());
        p.setPrimitive(req.getIsPrimitive());
        p.setTag(req.getTag());
        p.setData(aos.toByteArray());
        invoke.setParameter(p);

        Long invokeId;
        try {
            invokeId = this.tcapDialog.getNewInvokeId();
            invoke.setInvokeId(invokeId);
        } catch (TCAPException e) {
            throw new MAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }



    @Override
    public void addNoteMsPresentForGprsResponse(long invokeId, MAPExtensionContainer extensionContainer) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.gprsNotifyContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
            throw new MAPException("Bad application context name for addNoteMsPresentForGprsResponse: must be gprsNotifyContext_V3 ");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.noteMsPresentForGprs);
        resultLast.setOperationCode(oc);

        NoteMsPresentForGprsResponseImpl resp = new NoteMsPresentForGprsResponseImpl(extensionContainer);
        AsnOutputStream aos = new AsnOutputStream();
        resp.encodeData(aos);

        Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
        p.setTagClass(resp.getTagClass());
        p.setPrimitive(resp.getIsPrimitive());
        p.setTag(resp.getTag());
        p.setData(aos.toByteArray());
        resultLast.setParameter(p);

        this.sendReturnResultLastComponent(resultLast);
    }
}
