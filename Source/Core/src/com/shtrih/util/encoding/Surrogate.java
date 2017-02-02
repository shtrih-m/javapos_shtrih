//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shtrih.util.encoding;

import java.nio.CharBuffer;
import java.nio.charset.CoderResult;

public class Surrogate {
    public static final char MIN_HIGH = '\ud800';
    public static final char MAX_HIGH = '\udbff';
    public static final char MIN_LOW = '\udc00';
    public static final char MAX_LOW = '\udfff';
    public static final char MIN = '\ud800';
    public static final char MAX = '\udfff';
    public static final int UCS4_MIN = 65536;
    public static final int UCS4_MAX = 1114111;

    private Surrogate() {
    }

    public static boolean isHigh(int var0) {
        return '\ud800' <= var0 && var0 <= '\udbff';
    }

    public static boolean isLow(int var0) {
        return '\udc00' <= var0 && var0 <= '\udfff';
    }

    public static boolean is(int var0) {
        return '\ud800' <= var0 && var0 <= '\udfff';
    }

    public static boolean neededFor(int var0) {
        return var0 >= 65536 && var0 <= 1114111;
    }

    public static char high(int var0) {
        assert neededFor(var0);

        return (char)('\ud800' | var0 - 65536 >> 10 & 1023);
    }

    public static char low(int var0) {
        assert neededFor(var0);

        return (char)('\udc00' | var0 - 65536 & 1023);
    }

    public static int toUCS4(char var0, char var1) {
        assert isHigh(var0) && isLow(var1);

        return ((var0 & 1023) << 10 | var1 & 1023) + 65536;
    }

    public static class Generator {
        private CoderResult error;

        public Generator() {
            this.error = CoderResult.OVERFLOW;
        }

        public CoderResult error() {
            assert this.error != null;

            return this.error;
        }

        public int generate(int var1, int var2, CharBuffer var3) {
            if(var1 <= '\uffff') {
                if(Surrogate.is(var1)) {
                    this.error = CoderResult.malformedForLength(var2);
                    return -1;
                } else if(var3.remaining() < 1) {
                    this.error = CoderResult.OVERFLOW;
                    return -1;
                } else {
                    var3.put((char)var1);
                    this.error = null;
                    return 1;
                }
            } else if(var1 < 65536) {
                this.error = CoderResult.malformedForLength(var2);
                return -1;
            } else if(var1 <= 1114111) {
                if(var3.remaining() < 2) {
                    this.error = CoderResult.OVERFLOW;
                    return -1;
                } else {
                    var3.put(Surrogate.high(var1));
                    var3.put(Surrogate.low(var1));
                    this.error = null;
                    return 2;
                }
            } else {
                this.error = CoderResult.unmappableForLength(var2);
                return -1;
            }
        }

        public int generate(int var1, int var2, char[] var3, int var4, int var5) {
            if(var1 <= '\uffff') {
                if(Surrogate.is(var1)) {
                    this.error = CoderResult.malformedForLength(var2);
                    return -1;
                } else if(var5 - var4 < 1) {
                    this.error = CoderResult.OVERFLOW;
                    return -1;
                } else {
                    var3[var4] = (char)var1;
                    this.error = null;
                    return 1;
                }
            } else if(var1 < 65536) {
                this.error = CoderResult.malformedForLength(var2);
                return -1;
            } else if(var1 <= 1114111) {
                if(var5 - var4 < 2) {
                    this.error = CoderResult.OVERFLOW;
                    return -1;
                } else {
                    var3[var4] = Surrogate.high(var1);
                    var3[var4 + 1] = Surrogate.low(var1);
                    this.error = null;
                    return 2;
                }
            } else {
                this.error = CoderResult.unmappableForLength(var2);
                return -1;
            }
        }
    }

    public static class Parser {
        private int character;
        private CoderResult error;
        private boolean isPair;

        public Parser() {
            this.error = CoderResult.UNDERFLOW;
        }

        public int character() {
            assert this.error == null;

            return this.character;
        }

        public boolean isPair() {
            assert this.error == null;

            return this.isPair;
        }

        public int increment() {
            assert this.error == null;

            return this.isPair?2:1;
        }

        public CoderResult error() {
            assert this.error != null;

            return this.error;
        }

        public CoderResult unmappableResult() {
            assert this.error == null;

            return CoderResult.unmappableForLength(this.isPair?2:1);
        }

        public int parse(char var1, CharBuffer var2) {
            if(Surrogate.isHigh(var1)) {
                if(!var2.hasRemaining()) {
                    this.error = CoderResult.UNDERFLOW;
                    return -1;
                } else {
                    char var3 = var2.get();
                    if(Surrogate.isLow(var3)) {
                        this.character = Surrogate.toUCS4(var1, var3);
                        this.isPair = true;
                        this.error = null;
                        return this.character;
                    } else {
                        this.error = CoderResult.malformedForLength(1);
                        return -1;
                    }
                }
            } else if(Surrogate.isLow(var1)) {
                this.error = CoderResult.malformedForLength(1);
                return -1;
            } else {
                this.character = var1;
                this.isPair = false;
                this.error = null;
                return this.character;
            }
        }

        public int parse(char var1, char[] var2, int var3, int var4) {
            assert var2[var3] == var1;

            if(Surrogate.isHigh(var1)) {
                if(var4 - var3 < 2) {
                    this.error = CoderResult.UNDERFLOW;
                    return -1;
                } else {
                    char var5 = var2[var3 + 1];
                    if(Surrogate.isLow(var5)) {
                        this.character = Surrogate.toUCS4(var1, var5);
                        this.isPair = true;
                        this.error = null;
                        return this.character;
                    } else {
                        this.error = CoderResult.malformedForLength(1);
                        return -1;
                    }
                }
            } else if(Surrogate.isLow(var1)) {
                this.error = CoderResult.malformedForLength(1);
                return -1;
            } else {
                this.character = var1;
                this.isPair = false;
                this.error = null;
                return this.character;
            }
        }
    }
}
