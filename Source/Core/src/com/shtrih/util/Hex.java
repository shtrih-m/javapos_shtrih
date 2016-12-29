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
            s.append(" ");
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
        String result = "";
        Integer I = new Integer(((b) << 24) >>> 24);
        int i = I.intValue();

        if (i < (byte) 16) {
            result = "0" + Integer.toString(i, 16);
        } else {
            result = Integer.toString(i, 16);
        }
        return result.toUpperCase();
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
