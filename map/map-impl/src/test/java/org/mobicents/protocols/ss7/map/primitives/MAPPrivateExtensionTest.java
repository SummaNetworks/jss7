/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.map.primitives;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.MAPParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.MAPPrivateExtension;
import org.mobicents.protocols.ss7.map.api.primitives.nokia.ExtensionType;
import org.mobicents.protocols.ss7.map.api.primitives.nokia.ExtensionTypeImpl;
import org.mobicents.protocols.ss7.map.api.primitives.nokia.PrivateFeatureCodeImpl;
import org.mobicents.protocols.ss7.map.api.primitives.nokia.PrivateFeatureUlArgDataImpl;
import org.mobicents.protocols.ss7.map.api.primitives.nokia.UlArgData;
import org.mobicents.protocols.ss7.map.api.primitives.nokia.UlArgDataImpl;
import org.testng.annotations.Test;

import static org.mobicents.protocols.ss7.map.primitives.MAPPrivateExtensionImpl.NOKIA_OID;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author sergey vetyutnev
 *
 */
public class MAPPrivateExtensionTest {
    MAPParameterFactory mapServiceFactory = new MAPParameterFactoryImpl();

    public static MAPExtensionContainer GetTestExtensionContainer() {
        MAPParameterFactory mapServiceFactory = new MAPParameterFactoryImpl();

        ArrayList<MAPPrivateExtension> al = new ArrayList<>();
        al.add(mapServiceFactory.createMAPPrivateExtension(new long[] { 1, 2, 3, 4 }, new byte[] { 11, 12, 13, 14, 15 }));
        al.add(mapServiceFactory.createMAPPrivateExtension(new long[] { 1, 2, 3, 6 }, null));
        al.add(mapServiceFactory.createMAPPrivateExtension(new long[] { 1, 2, 3, 5 }, new byte[] { 21, 22, 23, 24, 25, 26 }));

        MAPExtensionContainer cnt = mapServiceFactory.createMAPExtensionContainer(al, new byte[] { 31, 32, 33 });

        return cnt;
    }


    public static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    public static MAPExtensionContainer GetTestExtensionContainerMine() {
        MAPParameterFactory mapServiceFactory = new MAPParameterFactoryImpl();

        ArrayList<MAPPrivateExtension> al = new ArrayList<>();
        al.add(mapServiceFactory.createMAPPrivateExtension(NOKIA_OID,
                hexStringToByteArray("a70f300d81010f83085335835026728187")));

        return mapServiceFactory.createMAPExtensionContainer(al, null);
    }

    public static Boolean CheckTestExtensionContainer(MAPExtensionContainer extContainer) {
        if (extContainer == null || extContainer.getPrivateExtensionList().size() != 3)
            return false;

        for (int i = 0; i < 3; i++) {
            MAPPrivateExtension pe = extContainer.getPrivateExtensionList().get(i);
            long[] lx = null;
            byte[] bx = null;

            switch (i) {
                case 0:
                    lx = new long[] { 1, 2, 3, 4 };
                    bx = new byte[] { 11, 12, 13, 14, 15 };
                    break;
                case 1:
                    lx = new long[] { 1, 2, 3, 6 };
                    bx = null;
                    break;
                case 2:
                    lx = new long[] { 1, 2, 3, 5 };
                    bx = new byte[] { 21, 22, 23, 24, 25, 26 };
                    break;
            }

            if (pe.getOId() == null || !Arrays.equals(pe.getOId(), lx))
                return false;
            if (bx == null) {
                if (pe.getData() != null)
                    return false;
            } else {
                if (pe.getData() == null || !Arrays.equals(pe.getData(), bx))
                    return false;
            }
        }

        byte[] by = new byte[] { 31, 32, 33 };
        if (extContainer.getPcsExtensions() == null || !Arrays.equals(extContainer.getPcsExtensions(), by))
            return false;

        return true;
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        //byte[] data = hexStringToByteArray("3020a01e301c06092a863a0089613a0100a70f300d81010f83085335835026728187");
        byte[] data = hexStringToByteArray("3027a025302306092a863a0089613a0100a716300581010e8200300d81010f83085386355032713011");
        AsnInputStream ais = new AsnInputStream(data);
        int tag = ais.readTag();
        MAPExtensionContainerImpl extCont = new MAPExtensionContainerImpl();
        extCont.decodeAll(ais);
        System.out.println(extCont);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
        byte[] data = hexStringToByteArray("3027a025302306092a863a0089613a0100a716300581010e8200300d81010f83085386355032713011");
        AsnInputStream ais = new AsnInputStream(data);
        ais.readTag();
        MAPExtensionContainerImpl extCont = new MAPExtensionContainerImpl();
        extCont.decodeAll(ais);

        AsnOutputStream asnOS = new AsnOutputStream();
        extCont.encodeAll(asnOS);
        byte[] res = asnOS.toByteArray();

        AsnInputStream ais2 = new AsnInputStream(res);
        ais2.readTag();
        MAPExtensionContainerImpl extContRes = new MAPExtensionContainerImpl();
        extContRes.decodeAll(ais2);
        //assertEquals(data, res);
        System.out.println(extContRes);
        System.out.println(extCont);

    }

    @Test
    public void testEncodeAndDecode() throws Exception {
      ArrayList<UlArgData> ulArgTypeList = new ArrayList<>();
        ulArgTypeList.add(new UlArgDataImpl(new PrivateFeatureUlArgDataImpl(new IMEIImpl("3553380562271878")), new PrivateFeatureCodeImpl()));
        ExtensionType extensionType = new ExtensionTypeImpl(ulArgTypeList);
        AsnOutputStream asnOSExtensionType = new AsnOutputStream();
        ((ExtensionTypeImpl) extensionType).encodeAll(asnOSExtensionType);
        byte[] extensionTypeArray = asnOSExtensionType.toByteArray();

        ArrayList<MAPPrivateExtension> privateExtensionList = new ArrayList<>();
        MAPPrivateExtensionImpl privateExtension = new MAPPrivateExtensionImpl(NOKIA_OID,
                extensionTypeArray);
        privateExtensionList.add(privateExtension);

        MAPExtensionContainerImpl extCont = new MAPExtensionContainerImpl(privateExtensionList, null);
        AsnOutputStream asnOS = new AsnOutputStream();
        extCont.encodeAll(asnOS);
        byte[] res = asnOS.toByteArray();

        MAPExtensionContainerImpl extContResp = new MAPExtensionContainerImpl();
        AsnInputStream ais = new AsnInputStream(getTestT());
        extContResp.decodeAll(ais);
        System.out.println(extContResp);

/*
        byte[] data = this.getTestT();
        AsnInputStream ais = new AsnInputStream(data);
        int tag = ais.readTag();
        MAPExtensionContainerImpl extCont = new MAPExtensionContainerImpl();
        extCont.decodeAll(ais);
*/


    }

    @Test(groups = { "functional.equality", "primitives" })
    public void testEquality() throws Exception {
        MAPExtensionContainerImpl original = (MAPExtensionContainerImpl) GetTestExtensionContainer();
        MAPExtensionContainerImpl copy = (MAPExtensionContainerImpl) GetTestExtensionContainer();
        assertEquals(copy, original);
    }

    @Test(groups = { "functional.serialize", "primitives" })
    public void testSerialization() throws Exception {
        MAPExtensionContainerImpl original = (MAPExtensionContainerImpl) GetTestExtensionContainer();
        // serialize
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(original);
        oos.close();

        // deserialize
        byte[] pickled = out.toByteArray();
        InputStream in = new ByteArrayInputStream(pickled);
        ObjectInputStream ois = new ObjectInputStream(in);
        Object o = ois.readObject();
        MAPExtensionContainerImpl copy = (MAPExtensionContainerImpl) o;

        // test result
        assertTrue(Arrays.equals(copy.getPcsExtensions(), original.getPcsExtensions()));
        assertEquals(copy.getPrivateExtensionList().size(), original.getPrivateExtensionList().size());

        ArrayList<MAPPrivateExtension> copyPriExt = copy.getPrivateExtensionList();
        ArrayList<MAPPrivateExtension> originalPriExt = original.getPrivateExtensionList();

        for (int i = 0; i < copyPriExt.size(); i++) {
            assertEquals(copyPriExt.get(i), originalPriExt.get(i));
        }

    }

    private byte[] getEncodedData() {
        return new byte[] { 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11,
                6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecodeA() throws Exception {

        byte[] data = this.getTestA();
        AsnInputStream ais = new AsnInputStream(data);
        MAPExtensionContainerImpl extCont = new MAPExtensionContainerImpl();
        extCont.decodeAll(ais);

        assertEquals(extCont.getPrivateExtensionList().size(), 1);
        assertTrue(Arrays
                .equals(extCont.getPrivateExtensionList().get(0).getOId(), new long[] { 1, 2, 826, 0, 1249, 58, 1, 0 }));
    }

    private byte[] getTestA() {
        return new byte[] { (byte) 0xAD, 0x3F, (byte) 0xa0, 0x3d, 0x30, 0x3b, 0x06, 0x09, 0x2a, (byte) 0x86, 0x3a, 0x00,
                (byte) 0x89, 0x61, 0x3a, 0x01, 0x00, (byte) 0xa4, 0x2e, 0x30, 0x03, (byte) 0x81, 0x01, 0x11, 0x30, 0x03,
                (byte) 0x81, 0x01, 0x06, 0x30, 0x03, (byte) 0x81, 0x01, 0x07, 0x30, 0x18, (byte) 0x81, 0x01, 0x09, (byte) 0xa2,
                0x13, 0x02, 0x01, 0x01, 0x04, 0x0e, 0x09, 0x01, 0x0a, 0x0a, 0x09, (byte) 0x84, 0x13, 0x32, (byte) 0x84, 0x30,
                0x03, (byte) 0x92, 0x11, 0x07, 0x30, 0x03, (byte) 0x81, 0x01, 0x0a };
    }

    private byte[] getTestT() {
        return new byte[] {
                0x30, 0x20, (byte) 0xa0, 0x1e, 0x30, 0x1c, 0x06, 0x09, 0x2a, (byte) 0x86, 0x3a, 0x00, (byte) 0x89, 0x61, 0x3a, 0x01,
            0x00,(byte)  0xa7, 0x0f, 0x30, 0x0d, (byte) 0x81, 0x01, 0x0f, (byte) 0x83, 0x08, 0x53, 0x35, (byte) 0x83, 0x50, 0x26, 0x72,
                (byte) 0x81,(byte)  0x87
        };
    }


    @Test(groups = { "functional.xml.serialize", "primitives" })
    public void testXMLSerialize() throws Exception {

        MAPExtensionContainerImpl original = (MAPExtensionContainerImpl) MAPPrivateExtensionTest.GetTestExtensionContainer();

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "mapExtensionContainer", MAPExtensionContainerImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        MAPExtensionContainerImpl copy = reader.read("mapExtensionContainer", MAPExtensionContainerImpl.class);

        MAPPrivateExtensionTest.CheckTestExtensionContainer(copy);

    }

    @Test(groups = { "functional.xml.serialize", "primitives" })
    public void testXMLSerialize2() throws Exception {


        ArrayList<UlArgData> ulArgTypeList = new ArrayList<>();
        ulArgTypeList.add(new UlArgDataImpl(new PrivateFeatureUlArgDataImpl(new IMEIImpl("3553380562271878")), new PrivateFeatureCodeImpl()));
        ExtensionType extensionType = new ExtensionTypeImpl(ulArgTypeList);
        AsnOutputStream asnOSExtensionType = new AsnOutputStream();
        ((ExtensionTypeImpl) extensionType).encodeAll(asnOSExtensionType);
        byte[] extensionTypeArray = asnOSExtensionType.toByteArray();

        ArrayList<MAPPrivateExtension> privateExtensionList = new ArrayList<>();
        MAPPrivateExtensionImpl privateExtension = new MAPPrivateExtensionImpl(NOKIA_OID,
                extensionTypeArray);
        privateExtensionList.add(privateExtension);

        MAPExtensionContainerImpl original = new MAPExtensionContainerImpl(privateExtensionList, null);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "mapExtensionContainer", MAPExtensionContainerImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        MAPExtensionContainerImpl copy = reader.read("mapExtensionContainer", MAPExtensionContainerImpl.class);

        MAPPrivateExtensionTest.CheckTestExtensionContainer(copy);

    }

    @Test(groups = { "functional.xml.serialize", "primitives" })
    public void testXMLSerialize3() throws Exception {

        byte[] data = this.getTestT();
        AsnInputStream ais = new AsnInputStream(data);
        int tag = ais.readTag();
        MAPExtensionContainerImpl extCont = new MAPExtensionContainerImpl();
        extCont.decodeAll(ais);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(extCont, "mapExtensionContainer", MAPExtensionContainerImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        MAPExtensionContainerImpl copy = reader.read("mapExtensionContainer", MAPExtensionContainerImpl.class);

        MAPPrivateExtensionTest.CheckTestExtensionContainer(copy);

    }
}
