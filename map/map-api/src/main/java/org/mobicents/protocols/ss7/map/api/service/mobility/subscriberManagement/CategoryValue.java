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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

/**
*
<code>
The following codes are used in the calling party's category parameter field.
0 0 0 0 0 0 0 0 calling party's category unknown at this time (national use)
0 0 0 0 0 0 0 1 operator, language French
0 0 0 0 0 0 1 0 operator, language English
0 0 0 0 0 0 1 1 operator, language German
0 0 0 0 0 1 0 0 operator, language Russian
0 0 0 0 0 1 0 1 operator, language Spanish
0 0 0 0 0 1 1 0
0 0 0 0 0 1 1 1
0 0 0 0 1 0 0 0
- (available to Administrations for selection a particular language by mutual agreement)
0 0 0 0 1 0 0 1 reserved (see ITU-T Recommendation Q.104) (Note) (national use)
0 0 0 0 1 0 1 0 ordinary calling subscriber
0 0 0 0 1 0 1 1 calling subscriber with priority
0 0 0 0 1 1 0 0 data call (voice band data)
0 0 0 0 1 1 0 1 test call
0 0 0 0 1 1 1 0 spare
0 0 0 0 1 1 1 1 payphone

0 0 0 1 0 0 0 0
to
1 1 0 1 1 1 1 1
- spare

1 1 1 0 0 0 0 0
to
1 1 1 1 1 1 1 0
- reserved for national use

1 1 1 1 1 1 1 1 - spare

NOTE  In national networks, code 00001001 may be used to indicate that the calling party is a national
operator.
</code>
*
*
*
* @author sergey vetyutnev
*
*/
public enum CategoryValue {

    categoryUnknownAtThisTime_NationalUse(0), operator_languageFrench(1), operator_languageEnglish(2), operator_languageGerman(3), operator_languageRussian(4), operator_languageSpanish(
            5), operator_languageAdmSelection_6(6), operator_languageAdmSelection_7(7), operator_languageAdmSelection_8(8), reserved_9(9), ordinaryCallingSubscriber(
            10), callingSubscriberWithPriority(11), dataCall_VoiceBandData(12), testCall(13), spare_14(14), payphone(15),

    spare_16(16), spare_17(17), spare_18(18), spare_19(19), spare_20(20), spare_21(21), spare_22(22), spare_23(23), spare_24(24), spare_25(25),
    spare_26(26), spare_27(27), spare_28(28), spare_29(29), spare_30(30), spare_31(31), spare_32(32), spare_33(33), spare_34(34), spare_35(35),
    spare_36(36), spare_37(37), spare_38(38), spare_39(39), spare_40(40), spare_41(41), spare_42(42), spare_43(43), spare_44(44), spare_45(45),
    spare_46(46), spare_47(47), spare_48(48), spare_49(49), spare_50(50), spare_51(51), spare_52(52), spare_53(53), spare_54(54), spare_55(55),
    spare_56(56), spare_57(57), spare_58(58), spare_59(59), spare_60(60), spare_61(61), spare_62(62), spare_63(63), spare_64(64), spare_65(65),
    spare_66(66), spare_67(67), spare_68(68), spare_69(69), spare_70(70), spare_71(71), spare_72(72), spare_73(73), spare_74(74), spare_75(75),
    spare_76(76), spare_77(77), spare_78(78), spare_79(79), spare_80(80), spare_81(81), spare_82(82), spare_83(83), spare_84(84), spare_85(85),
    spare_86(86), spare_87(87), spare_88(88), spare_89(89), spare_90(90), spare_91(91), spare_92(92), spare_93(93), spare_94(94), spare_95(95),
    spare_96(96), spare_97(97), spare_98(98), spare_99(99), spare_100(100), spare_101(101), spare_102(102), spare_103(103), spare_104(104), spare_105(105),
    spare_106(106), spare_107(107), spare_108(108), spare_109(109), spare_110(110), spare_111(111), spare_112(112), spare_113(113), spare_114(114), spare_115(115),
    spare_116(116), spare_117(117), spare_118(118), spare_119(119), spare_120(120), spare_121(121), spare_122(122), spare_123(123), spare_124(124), spare_125(125),
    spare_126(126), spare_127(127), spare_128(128), spare_129(129), spare_130(130), spare_131(131), spare_132(132), spare_133(133), spare_134(134), spare_135(135),
    spare_136(136), spare_137(137), spare_138(138), spare_139(139), spare_140(140), spare_141(141), spare_142(142), spare_143(143), spare_144(144), spare_145(145),
    spare_146(146), spare_147(147), spare_148(148), spare_149(149), spare_150(150), spare_151(151), spare_152(152), spare_153(153), spare_154(154), spare_155(155),
    spare_156(156), spare_157(157), spare_158(158), spare_159(159), spare_160(160), spare_161(161), spare_162(162), spare_163(163), spare_164(164), spare_165(165),
    spare_166(166), spare_167(167), spare_168(168), spare_169(169), spare_170(170), spare_171(171), spare_172(172), spare_173(173), spare_174(174), spare_175(175),
    spare_176(176), spare_177(177), spare_178(178), spare_179(179), spare_180(180), spare_181(181), spare_182(182), spare_183(183), spare_184(184), spare_185(185),
    spare_186(186), spare_187(187), spare_188(188), spare_189(189), spare_190(190), spare_191(191), spare_192(192), spare_193(193), spare_194(194), spare_195(195),
    spare_196(196), spare_197(197), spare_198(198), spare_199(199), spare_200(200), spare_201(201), spare_202(202), spare_203(203), spare_204(204), spare_205(205),
    spare_206(206), spare_207(207), spare_208(208), spare_209(209), spare_210(210), spare_211(211), spare_212(212), spare_213(213), spare_214(214), spare_215(215),
    spare_216(216), spare_217(217), spare_218(218), spare_219(219), spare_220(220), spare_221(221), spare_222(222), spare_223(223),

    reservedForNationalUse_224(224), reservedForNationalUse_225(225), reservedForNationalUse_226(226), reservedForNationalUse_227(227), reservedForNationalUse_228(228),
    reservedForNationalUse_229(229), reservedForNationalUse_230(230), reservedForNationalUse_231(231), reservedForNationalUse_232(232), reservedForNationalUse_233(233),
    reservedForNationalUse_234(234), reservedForNationalUse_235(235), reservedForNationalUse_236(236), reservedForNationalUse_237(237), reservedForNationalUse_238(238),
    reservedForNationalUse_239(239), reservedForNationalUse_240(240), reservedForNationalUse_241(241), reservedForNationalUse_242(242), reservedForNationalUse_243(243),
    reservedForNationalUse_244(244), reservedForNationalUse_245(245), reservedForNationalUse_246(246), reservedForNationalUse_247(247), reservedForNationalUse_248(248),
    reservedForNationalUse_249(249), reservedForNationalUse_250(250), reservedForNationalUse_251(251), reservedForNationalUse_252(252), reservedForNationalUse_253(253),
    reservedForNationalUse_254(254),

    spare_255(255);

    private int code;

    private CategoryValue(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static CategoryValue getInstance(int code) {
        switch (code) {
            case 0:
                return CategoryValue.categoryUnknownAtThisTime_NationalUse;
            case 1:
                return CategoryValue.operator_languageFrench;
            case 2:
                return CategoryValue.operator_languageEnglish;
            case 3:
                return CategoryValue.operator_languageGerman;
            case 4:
                return CategoryValue.operator_languageRussian;
            case 5:
                return CategoryValue.operator_languageSpanish;
            case 6:
                return CategoryValue.operator_languageAdmSelection_6;
            case 7:
                return CategoryValue.operator_languageAdmSelection_7;
            case 8:
                return CategoryValue.operator_languageAdmSelection_8;
            case 9:
                return CategoryValue.reserved_9;
            case 10:
                return CategoryValue.ordinaryCallingSubscriber;
            case 11:
                return CategoryValue.callingSubscriberWithPriority;
            case 12:
                return CategoryValue.dataCall_VoiceBandData;
            case 13:
                return CategoryValue.testCall;
            case 14:
                return CategoryValue.spare_14;
            case 15:
                return CategoryValue.payphone;
            case 16:
                return CategoryValue.spare_16;
            case 17:
                return CategoryValue.spare_17;
            case 18:
                return CategoryValue.spare_18;
            case 19:
                return CategoryValue.spare_19;
            case 20:
                return CategoryValue.spare_20;
            case 21:
                return CategoryValue.spare_21;
            case 22:
                return CategoryValue.spare_22;
            case 23:
                return CategoryValue.spare_23;
            case 24:
                return CategoryValue.spare_24;
            case 25:
                return CategoryValue.spare_25;
            case 26:
                return CategoryValue.spare_26;
            case 27:
                return CategoryValue.spare_27;
            case 28:
                return CategoryValue.spare_28;
            case 29:
                return CategoryValue.spare_29;
            case 30:
                return CategoryValue.spare_30;
            case 31:
                return CategoryValue.spare_31;
            case 32:
                return CategoryValue.spare_32;
            case 33:
                return CategoryValue.spare_33;
            case 34:
                return CategoryValue.spare_34;
            case 35:
                return CategoryValue.spare_35;
            case 36:
                return CategoryValue.spare_36;
            case 37:
                return CategoryValue.spare_37;
            case 38:
                return CategoryValue.spare_38;
            case 39:
                return CategoryValue.spare_39;
            case 40:
                return CategoryValue.spare_40;
            case 41:
                return CategoryValue.spare_41;
            case 42:
                return CategoryValue.spare_42;
            case 43:
                return CategoryValue.spare_43;
            case 44:
                return CategoryValue.spare_44;
            case 45:
                return CategoryValue.spare_45;
            case 46:
                return CategoryValue.spare_46;
            case 47:
                return CategoryValue.spare_47;
            case 48:
                return CategoryValue.spare_48;
            case 49:
                return CategoryValue.spare_49;
            case 50:
                return CategoryValue.spare_50;
            case 51:
                return CategoryValue.spare_51;
            case 52:
                return CategoryValue.spare_52;
            case 53:
                return CategoryValue.spare_53;
            case 54:
                return CategoryValue.spare_54;
            case 55:
                return CategoryValue.spare_55;
            case 56:
                return CategoryValue.spare_56;
            case 57:
                return CategoryValue.spare_57;
            case 58:
                return CategoryValue.spare_58;
            case 59:
                return CategoryValue.spare_59;
            case 60:
                return CategoryValue.spare_60;
            case 61:
                return CategoryValue.spare_61;
            case 62:
                return CategoryValue.spare_62;
            case 63:
                return CategoryValue.spare_63;
            case 64:
                return CategoryValue.spare_64;
            case 65:
                return CategoryValue.spare_65;
            case 66:
                return CategoryValue.spare_66;
            case 67:
                return CategoryValue.spare_67;
            case 68:
                return CategoryValue.spare_68;
            case 69:
                return CategoryValue.spare_69;
            case 70:
                return CategoryValue.spare_70;
            case 71:
                return CategoryValue.spare_71;
            case 72:
                return CategoryValue.spare_72;
            case 73:
                return CategoryValue.spare_73;
            case 74:
                return CategoryValue.spare_74;
            case 75:
                return CategoryValue.spare_75;
            case 76:
                return CategoryValue.spare_76;
            case 77:
                return CategoryValue.spare_77;
            case 78:
                return CategoryValue.spare_78;
            case 79:
                return CategoryValue.spare_79;
            case 80:
                return CategoryValue.spare_80;
            case 81:
                return CategoryValue.spare_81;
            case 82:
                return CategoryValue.spare_82;
            case 83:
                return CategoryValue.spare_83;
            case 84:
                return CategoryValue.spare_84;
            case 85:
                return CategoryValue.spare_85;
            case 86:
                return CategoryValue.spare_86;
            case 87:
                return CategoryValue.spare_87;
            case 88:
                return CategoryValue.spare_88;
            case 89:
                return CategoryValue.spare_89;
            case 90:
                return CategoryValue.spare_90;
            case 91:
                return CategoryValue.spare_91;
            case 92:
                return CategoryValue.spare_92;
            case 93:
                return CategoryValue.spare_93;
            case 94:
                return CategoryValue.spare_94;
            case 95:
                return CategoryValue.spare_95;
            case 96:
                return CategoryValue.spare_96;
            case 97:
                return CategoryValue.spare_97;
            case 98:
                return CategoryValue.spare_98;
            case 99:
                return CategoryValue.spare_99;
            case 100:
                return CategoryValue.spare_100;
            case 101:
                return CategoryValue.spare_101;
            case 102:
                return CategoryValue.spare_102;
            case 103:
                return CategoryValue.spare_103;
            case 104:
                return CategoryValue.spare_104;
            case 105:
                return CategoryValue.spare_105;
            case 106:
                return CategoryValue.spare_106;
            case 107:
                return CategoryValue.spare_107;
            case 108:
                return CategoryValue.spare_108;
            case 109:
                return CategoryValue.spare_109;
            case 110:
                return CategoryValue.spare_110;
            case 111:
                return CategoryValue.spare_111;
            case 112:
                return CategoryValue.spare_112;
            case 113:
                return CategoryValue.spare_113;
            case 114:
                return CategoryValue.spare_114;
            case 115:
                return CategoryValue.spare_115;
            case 116:
                return CategoryValue.spare_116;
            case 117:
                return CategoryValue.spare_117;
            case 118:
                return CategoryValue.spare_118;
            case 119:
                return CategoryValue.spare_119;
            case 120:
                return CategoryValue.spare_120;
            case 121:
                return CategoryValue.spare_121;
            case 122:
                return CategoryValue.spare_122;
            case 123:
                return CategoryValue.spare_123;
            case 124:
                return CategoryValue.spare_124;
            case 125:
                return CategoryValue.spare_125;
            case 126:
                return CategoryValue.spare_126;
            case 127:
                return CategoryValue.spare_127;
            case 128:
                return CategoryValue.spare_128;
            case 129:
                return CategoryValue.spare_129;
            case 130:
                return CategoryValue.spare_130;
            case 131:
                return CategoryValue.spare_131;
            case 132:
                return CategoryValue.spare_132;
            case 133:
                return CategoryValue.spare_133;
            case 134:
                return CategoryValue.spare_134;
            case 135:
                return CategoryValue.spare_135;
            case 136:
                return CategoryValue.spare_136;
            case 137:
                return CategoryValue.spare_137;
            case 138:
                return CategoryValue.spare_138;
            case 139:
                return CategoryValue.spare_139;
            case 140:
                return CategoryValue.spare_140;
            case 141:
                return CategoryValue.spare_141;
            case 142:
                return CategoryValue.spare_142;
            case 143:
                return CategoryValue.spare_143;
            case 144:
                return CategoryValue.spare_144;
            case 145:
                return CategoryValue.spare_145;
            case 146:
                return CategoryValue.spare_146;
            case 147:
                return CategoryValue.spare_147;
            case 148:
                return CategoryValue.spare_148;
            case 149:
                return CategoryValue.spare_149;
            case 150:
                return CategoryValue.spare_150;
            case 151:
                return CategoryValue.spare_151;
            case 152:
                return CategoryValue.spare_152;
            case 153:
                return CategoryValue.spare_153;
            case 154:
                return CategoryValue.spare_154;
            case 155:
                return CategoryValue.spare_155;
            case 156:
                return CategoryValue.spare_156;
            case 157:
                return CategoryValue.spare_157;
            case 158:
                return CategoryValue.spare_158;
            case 159:
                return CategoryValue.spare_159;
            case 160:
                return CategoryValue.spare_160;
            case 161:
                return CategoryValue.spare_161;
            case 162:
                return CategoryValue.spare_162;
            case 163:
                return CategoryValue.spare_163;
            case 164:
                return CategoryValue.spare_164;
            case 165:
                return CategoryValue.spare_165;
            case 166:
                return CategoryValue.spare_166;
            case 167:
                return CategoryValue.spare_167;
            case 168:
                return CategoryValue.spare_168;
            case 169:
                return CategoryValue.spare_169;
            case 170:
                return CategoryValue.spare_170;
            case 171:
                return CategoryValue.spare_171;
            case 172:
                return CategoryValue.spare_172;
            case 173:
                return CategoryValue.spare_173;
            case 174:
                return CategoryValue.spare_174;
            case 175:
                return CategoryValue.spare_175;
            case 176:
                return CategoryValue.spare_176;
            case 177:
                return CategoryValue.spare_177;
            case 178:
                return CategoryValue.spare_178;
            case 179:
                return CategoryValue.spare_179;
            case 180:
                return CategoryValue.spare_180;
            case 181:
                return CategoryValue.spare_181;
            case 182:
                return CategoryValue.spare_182;
            case 183:
                return CategoryValue.spare_183;
            case 184:
                return CategoryValue.spare_184;
            case 185:
                return CategoryValue.spare_185;
            case 186:
                return CategoryValue.spare_186;
            case 187:
                return CategoryValue.spare_187;
            case 188:
                return CategoryValue.spare_188;
            case 189:
                return CategoryValue.spare_189;
            case 190:
                return CategoryValue.spare_190;
            case 191:
                return CategoryValue.spare_191;
            case 192:
                return CategoryValue.spare_192;
            case 193:
                return CategoryValue.spare_193;
            case 194:
                return CategoryValue.spare_194;
            case 195:
                return CategoryValue.spare_195;
            case 196:
                return CategoryValue.spare_196;
            case 197:
                return CategoryValue.spare_197;
            case 198:
                return CategoryValue.spare_198;
            case 199:
                return CategoryValue.spare_199;
            case 200:
                return CategoryValue.spare_200;
            case 201:
                return CategoryValue.spare_201;
            case 202:
                return CategoryValue.spare_202;
            case 203:
                return CategoryValue.spare_203;
            case 204:
                return CategoryValue.spare_204;
            case 205:
                return CategoryValue.spare_205;
            case 206:
                return CategoryValue.spare_206;
            case 207:
                return CategoryValue.spare_207;
            case 208:
                return CategoryValue.spare_208;
            case 209:
                return CategoryValue.spare_209;
            case 210:
                return CategoryValue.spare_210;
            case 211:
                return CategoryValue.spare_211;
            case 212:
                return CategoryValue.spare_212;
            case 213:
                return CategoryValue.spare_213;
            case 214:
                return CategoryValue.spare_214;
            case 215:
                return CategoryValue.spare_215;
            case 216:
                return CategoryValue.spare_216;
            case 217:
                return CategoryValue.spare_217;
            case 218:
                return CategoryValue.spare_218;
            case 219:
                return CategoryValue.spare_219;
            case 220:
                return CategoryValue.spare_220;
            case 221:
                return CategoryValue.spare_221;
            case 222:
                return CategoryValue.spare_222;
            case 223:
                return CategoryValue.spare_223;
            case 224:
                return CategoryValue.reservedForNationalUse_224;
            case 225:
                return CategoryValue.reservedForNationalUse_225;
            case 226:
                return CategoryValue.reservedForNationalUse_226;
            case 227:
                return CategoryValue.reservedForNationalUse_227;
            case 228:
                return CategoryValue.reservedForNationalUse_228;
            case 229:
                return CategoryValue.reservedForNationalUse_229;
            case 230:
                return CategoryValue.reservedForNationalUse_230;
            case 231:
                return CategoryValue.reservedForNationalUse_231;
            case 232:
                return CategoryValue.reservedForNationalUse_232;
            case 233:
                return CategoryValue.reservedForNationalUse_233;
            case 234:
                return CategoryValue.reservedForNationalUse_234;
            case 235:
                return CategoryValue.reservedForNationalUse_235;
            case 236:
                return CategoryValue.reservedForNationalUse_236;
            case 237:
                return CategoryValue.reservedForNationalUse_237;
            case 238:
                return CategoryValue.reservedForNationalUse_238;
            case 239:
                return CategoryValue.reservedForNationalUse_239;
            case 240:
                return CategoryValue.reservedForNationalUse_240;
            case 241:
                return CategoryValue.reservedForNationalUse_241;
            case 242:
                return CategoryValue.reservedForNationalUse_242;
            case 243:
                return CategoryValue.reservedForNationalUse_243;
            case 244:
                return CategoryValue.reservedForNationalUse_244;
            case 245:
                return CategoryValue.reservedForNationalUse_245;
            case 246:
                return CategoryValue.reservedForNationalUse_246;
            case 247:
                return CategoryValue.reservedForNationalUse_247;
            case 248:
                return CategoryValue.reservedForNationalUse_248;
            case 249:
                return CategoryValue.reservedForNationalUse_249;
            case 250:
                return CategoryValue.reservedForNationalUse_250;
            case 251:
                return CategoryValue.reservedForNationalUse_251;
            case 252:
                return CategoryValue.reservedForNationalUse_252;
            case 253:
                return CategoryValue.reservedForNationalUse_253;
            case 254:
                return CategoryValue.reservedForNationalUse_254;
            case 255:
                return CategoryValue.spare_255;

        default:
            return null;
        }
    }
}
