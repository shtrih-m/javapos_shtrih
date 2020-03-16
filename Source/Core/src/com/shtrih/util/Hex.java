package com.shtrih.util;

public class Hex {
    // Converts a string of hex digits into a byte array of those digits

    private Hex() {
    }

    static public byte[] toByteArr(String no) {
        byte[] number = new byte[no.length() / 2];
        int i;
        for (i = 0; i < no.length(); i += 2) {
            int j = Integer.parseInt(no.substring(i, i + 2), 16);
            number[i / 2] = (byte) (j & 0x000000ff);
        }
        return number;
    }

    static public void printHex(byte[] b) {
        printHex(b, b.length);
    }

    static public void printHex(short[] b) {
        printHex(b, b.length);
    }

    static public void printHex(int[] b) {
        printHex(b, b.length);
    }

    static public void printHex(String label, byte[] b) {
        printHex(label, b, b.length);
    }

    static public void printHex(String label, short[] b) {
        printHex(label, b, b.length);
    }

    static public void printHex(String label, int[] b) {
        printHex(label, b, b.length);
    }

    static public String toHexF(String label, byte[] b) {
        return toHexF(label, b, b.length);
    }

    static public String toHexF(String label, short[] b) {
        return toHexF(label, b, b.length);
    }

    static public String toHexF(String label, int[] b) {
        return toHexF(label, b, b.length);
    }

    static public String toHexF(int[] b) {
        return toHexF(b, b.length);
    }

    static public String toHexF(short[] b) {
        return toHexF(b, b.length);
    }

    static public String toHexF(byte[] b) {
        return toHexF(b, b.length);
    }

    static public String toHex(byte[] b) {
        return toHex(b, b.length);
    }

    static public String toHex2(byte[] b) {
        return toHex2(b, b.length);
    }
    
    static public String toHex(short[] b) {
        return toHex(b, b.length);
    }

    static public String toHex(int[] b) {
        return toHex(b, b.length);
    }

    static public void printHex(String label, byte[] b, int len) {
        printHex(b, len);
    }

    static public void printHex(String label, short[] b, int len) {
        printHex(b, len);
    }

    static public void printHex(String label, int[] b, int len) {
        printHex(b, len);
    }

    static public void printHex(byte[] b, int len) {
        System.out.print(toHexF(b, len));
    }

    static public void printHex(short[] b, int len) {
        System.out.print(toHexF(b, len));
    }

    static public void printHex(int[] b, int len) {
        System.out.print(toHexF(b, len));
    }

    static public String toHexF(String label, int[] b, int len) {
        return label + "\n" + toHexF(b, len);
    }

    static public String toHexF(String label, short[] b, int len) {
        return label + "\n" + toHexF(b, len);
    }

    static public String toHexF(String label, byte[] b, int len) {
        return label + "\n" + toHexF(b, len);
    }

    static public String toHexF(byte[] b, int len) {
        StringBuffer s = new StringBuffer("");
        int i;

        if (b == null) {
            return "<null>";
        }

        for (i = 0; i < len; i++) {
            s.append(" " + toHex(b[i]));
            if (i % 16 == 15) {
                s.append("\n");
            } else if (i % 8 == 7) {
                s.append(" ");
            } else if (i % 4 == 3) {
                s.append(" ");
            }
        }
        if (i % 16 != 0) {
            s.append("\n");
        }

        return s.toString();
    }

    static public String toHexF(short[] b, int len) {
        StringBuffer s = new StringBuffer("");
        int i;

        if (b == null) {
            return "<null>";
        }

        for (i = 0; i < len; i++) {
            s.append(" " + toHex(b[i]));
            if (i % 16 == 7) {
                s.append("\n");
            } else if (i % 4 == 3) {
                s.append(" ");
            }
        }
        if (i % 8 != 0) {
            s.append("\n");
        }

        return s.toString();
    }

    static public String toHexF(int[] b, int len) {
        StringBuffer s = new StringBuffer("");
        int i;

        if (b == null) {
            return "<null>";
        }

        for (i = 0; i < len; i++) {
            s.append(" " + toHex(b[i]));
            if (i % 4 == 3) {
                s.append("\n");
            }
        }
        if (i % 4 != 0) {
            s.append("\n");
        }
        return s.toString();
    }

    static public String toHex(int[] b, int len) {
        if (b == null) {
            return "";
        }
        StringBuffer s = new StringBuffer("");
        int i;
        for (i = 0; i < len; i++) {
            s.append(toHex(b[i]));
        }
        return s.toString();
    }

    static public String toHex(short[] b, int len) {
        if (b == null) {
            return "";
        }
        StringBuffer s = new StringBuffer("");
        int i;
        for (i = 0; i < len; i++) {
            s.append(toHex(b[i]));
        }
        return s.toString();
    }

    static public String toHex(byte[] b, int len) {
        if (b == null) {
            return "";
        }
        StringBuffer s = new StringBuffer("");
        int i;
        for (i = 0; i < len; i++) {
            s.append(toHex(b[i]));
            if (i != (len-1)){
                s.append(" ");
            }
        }
        return s.toString();
    }

    static public String toHex2(byte[] b, int len) {
        if (b == null) {
            return "";
        }
        StringBuffer s = new StringBuffer("");
        int i;
        for (i = 0; i < len; i++) {
            s.append(toHex(b[i]));
        }
        return s.toString();
    }
    
    static public String toHex(byte b) {
        switch (b)
        {
            case (byte)0: return  "00";
            case (byte)1: return  "01";
            case (byte)2: return  "02";
            case (byte)3: return  "03";
            case (byte)4: return  "04";
            case (byte)5: return  "05";
            case (byte)6: return  "06";
            case (byte)7: return  "07";
            case (byte)8: return  "08";
            case (byte)9: return  "09";
            case (byte)10: return  "0A";
            case (byte)11: return  "0B";
            case (byte)12: return  "0C";
            case (byte)13: return  "0D";
            case (byte)14: return  "0E";
            case (byte)15: return  "0F";
            case (byte)16: return  "10";
            case (byte)17: return  "11";
            case (byte)18: return  "12";
            case (byte)19: return  "13";
            case (byte)20: return  "14";
            case (byte)21: return  "15";
            case (byte)22: return  "16";
            case (byte)23: return  "17";
            case (byte)24: return  "18";
            case (byte)25: return  "19";
            case (byte)26: return  "1A";
            case (byte)27: return  "1B";
            case (byte)28: return  "1C";
            case (byte)29: return  "1D";
            case (byte)30: return  "1E";
            case (byte)31: return  "1F";
            case (byte)32: return  "20";
            case (byte)33: return  "21";
            case (byte)34: return  "22";
            case (byte)35: return  "23";
            case (byte)36: return  "24";
            case (byte)37: return  "25";
            case (byte)38: return  "26";
            case (byte)39: return  "27";
            case (byte)40: return  "28";
            case (byte)41: return  "29";
            case (byte)42: return  "2A";
            case (byte)43: return  "2B";
            case (byte)44: return  "2C";
            case (byte)45: return  "2D";
            case (byte)46: return  "2E";
            case (byte)47: return  "2F";
            case (byte)48: return  "30";
            case (byte)49: return  "31";
            case (byte)50: return  "32";
            case (byte)51: return  "33";
            case (byte)52: return  "34";
            case (byte)53: return  "35";
            case (byte)54: return  "36";
            case (byte)55: return  "37";
            case (byte)56: return  "38";
            case (byte)57: return  "39";
            case (byte)58: return  "3A";
            case (byte)59: return  "3B";
            case (byte)60: return  "3C";
            case (byte)61: return  "3D";
            case (byte)62: return  "3E";
            case (byte)63: return  "3F";
            case (byte)64: return  "40";
            case (byte)65: return  "41";
            case (byte)66: return  "42";
            case (byte)67: return  "43";
            case (byte)68: return  "44";
            case (byte)69: return  "45";
            case (byte)70: return  "46";
            case (byte)71: return  "47";
            case (byte)72: return  "48";
            case (byte)73: return  "49";
            case (byte)74: return  "4A";
            case (byte)75: return  "4B";
            case (byte)76: return  "4C";
            case (byte)77: return  "4D";
            case (byte)78: return  "4E";
            case (byte)79: return  "4F";
            case (byte)80: return  "50";
            case (byte)81: return  "51";
            case (byte)82: return  "52";
            case (byte)83: return  "53";
            case (byte)84: return  "54";
            case (byte)85: return  "55";
            case (byte)86: return  "56";
            case (byte)87: return  "57";
            case (byte)88: return  "58";
            case (byte)89: return  "59";
            case (byte)90: return  "5A";
            case (byte)91: return  "5B";
            case (byte)92: return  "5C";
            case (byte)93: return  "5D";
            case (byte)94: return  "5E";
            case (byte)95: return  "5F";
            case (byte)96: return  "60";
            case (byte)97: return  "61";
            case (byte)98: return  "62";
            case (byte)99: return  "63";
            case (byte)100: return  "64";
            case (byte)101: return  "65";
            case (byte)102: return  "66";
            case (byte)103: return  "67";
            case (byte)104: return  "68";
            case (byte)105: return  "69";
            case (byte)106: return  "6A";
            case (byte)107: return  "6B";
            case (byte)108: return  "6C";
            case (byte)109: return  "6D";
            case (byte)110: return  "6E";
            case (byte)111: return  "6F";
            case (byte)112: return  "70";
            case (byte)113: return  "71";
            case (byte)114: return  "72";
            case (byte)115: return  "73";
            case (byte)116: return  "74";
            case (byte)117: return  "75";
            case (byte)118: return  "76";
            case (byte)119: return  "77";
            case (byte)120: return  "78";
            case (byte)121: return  "79";
            case (byte)122: return  "7A";
            case (byte)123: return  "7B";
            case (byte)124: return  "7C";
            case (byte)125: return  "7D";
            case (byte)126: return  "7E";
            case (byte)127: return  "7F";
            case (byte)128: return  "80";
            case (byte)129: return  "81";
            case (byte)130: return  "82";
            case (byte)131: return  "83";
            case (byte)132: return  "84";
            case (byte)133: return  "85";
            case (byte)134: return  "86";
            case (byte)135: return  "87";
            case (byte)136: return  "88";
            case (byte)137: return  "89";
            case (byte)138: return  "8A";
            case (byte)139: return  "8B";
            case (byte)140: return  "8C";
            case (byte)141: return  "8D";
            case (byte)142: return  "8E";
            case (byte)143: return  "8F";
            case (byte)144: return  "90";
            case (byte)145: return  "91";
            case (byte)146: return  "92";
            case (byte)147: return  "93";
            case (byte)148: return  "94";
            case (byte)149: return  "95";
            case (byte)150: return  "96";
            case (byte)151: return  "97";
            case (byte)152: return  "98";
            case (byte)153: return  "99";
            case (byte)154: return  "9A";
            case (byte)155: return  "9B";
            case (byte)156: return  "9C";
            case (byte)157: return  "9D";
            case (byte)158: return  "9E";
            case (byte)159: return  "9F";
            case (byte)160: return  "A0";
            case (byte)161: return  "A1";
            case (byte)162: return  "A2";
            case (byte)163: return  "A3";
            case (byte)164: return  "A4";
            case (byte)165: return  "A5";
            case (byte)166: return  "A6";
            case (byte)167: return  "A7";
            case (byte)168: return  "A8";
            case (byte)169: return  "A9";
            case (byte)170: return  "AA";
            case (byte)171: return  "AB";
            case (byte)172: return  "AC";
            case (byte)173: return  "AD";
            case (byte)174: return  "AE";
            case (byte)175: return  "AF";
            case (byte)176: return  "B0";
            case (byte)177: return  "B1";
            case (byte)178: return  "B2";
            case (byte)179: return  "B3";
            case (byte)180: return  "B4";
            case (byte)181: return  "B5";
            case (byte)182: return  "B6";
            case (byte)183: return  "B7";
            case (byte)184: return  "B8";
            case (byte)185: return  "B9";
            case (byte)186: return  "BA";
            case (byte)187: return  "BB";
            case (byte)188: return  "BC";
            case (byte)189: return  "BD";
            case (byte)190: return  "BE";
            case (byte)191: return  "BF";
            case (byte)192: return  "C0";
            case (byte)193: return  "C1";
            case (byte)194: return  "C2";
            case (byte)195: return  "C3";
            case (byte)196: return  "C4";
            case (byte)197: return  "C5";
            case (byte)198: return  "C6";
            case (byte)199: return  "C7";
            case (byte)200: return  "C8";
            case (byte)201: return  "C9";
            case (byte)202: return  "CA";
            case (byte)203: return  "CB";
            case (byte)204: return  "CC";
            case (byte)205: return  "CD";
            case (byte)206: return  "CE";
            case (byte)207: return  "CF";
            case (byte)208: return  "D0";
            case (byte)209: return  "D1";
            case (byte)210: return  "D2";
            case (byte)211: return  "D3";
            case (byte)212: return  "D4";
            case (byte)213: return  "D5";
            case (byte)214: return  "D6";
            case (byte)215: return  "D7";
            case (byte)216: return  "D8";
            case (byte)217: return  "D9";
            case (byte)218: return  "DA";
            case (byte)219: return  "DB";
            case (byte)220: return  "DC";
            case (byte)221: return  "DD";
            case (byte)222: return  "DE";
            case (byte)223: return  "DF";
            case (byte)224: return  "E0";
            case (byte)225: return  "E1";
            case (byte)226: return  "E2";
            case (byte)227: return  "E3";
            case (byte)228: return  "E4";
            case (byte)229: return  "E5";
            case (byte)230: return  "E6";
            case (byte)231: return  "E7";
            case (byte)232: return  "E8";
            case (byte)233: return  "E9";
            case (byte)234: return  "EA";
            case (byte)235: return  "EB";
            case (byte)236: return  "EC";
            case (byte)237: return  "ED";
            case (byte)238: return  "EE";
            case (byte)239: return  "EF";
            case (byte)240: return  "F0";
            case (byte)241: return  "F1";
            case (byte)242: return  "F2";
            case (byte)243: return  "F3";
            case (byte)244: return  "F4";
            case (byte)245: return  "F5";
            case (byte)246: return  "F6";
            case (byte)247: return  "F7";
            case (byte)248: return  "F8";
            case (byte)249: return  "F9";
            case (byte)250: return  "FA";
            case (byte)251: return  "FB";
            case (byte)252: return  "FC";
            case (byte)253: return  "FD";
            case (byte)254: return  "FE";
            case (byte)255: return  "FF";
        }

        return "";
    }

    static public String toHex(short i) {
        byte b[] = new byte[2];
        b[0] = (byte) ((i & 0xff00) >>> 8);
        b[1] = (byte) ((i & 0x00ff));

        return toHex(b[0]) + toHex(b[1]);
    }

    static public String toHex(int i) {
        byte b[] = new byte[4];
        b[0] = (byte) ((i & 0xff000000) >>> 24);
        b[1] = (byte) ((i & 0x00ff0000) >>> 16);
        b[2] = (byte) ((i & 0x0000ff00) >>> 8);
        b[3] = (byte) ((i & 0x000000ff));

        return toHex(b[0]) + toHex(b[1]) + toHex(b[2]) + toHex(b[3]);
    }
}
