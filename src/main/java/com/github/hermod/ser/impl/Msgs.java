package com.github.hermod.ser.impl;

import static com.github.hermod.ser.Types.ARRAY_FIXED_VALUE_TYPE;
import static com.github.hermod.ser.Types.ARRAY_VARIABLE_VALUE_TYPE;

import com.github.hermod.ser.Msg;
import com.github.hermod.ser.Types;

/**
 * <p>Msgs.</p>
 * 
 * @author anavarro - Dec 15, 2011
 * 
 */
public final class Msgs {
    /**
     * Constructor.
     * 
     */
    private Msgs() {
    }

    /**
     * ZERO
     */
    public static final int      ZERO                                  = 0;

    /**
     * ONE
     */
    public static final int      ONE                                   = 1;

    /**
     * TWO
     */
    public static final int      TWO                                   = 2;

    /**
     * THREE
     */
    public static final int      THREE                                 = 3;

    /**
     * FOUR
     */
    public static final int      FOUR                                  = 4;

    /**
     * FIVE
     */
    public static final int      FIVE                                  = 5;

    /**
     * SIX
     */
    public static final int      SIX                                   = 6;

    /**
     * SEVEN
     */
    public static final int      SEVEN                                 = 7;

    /**
     * EIGHT
     */
    public static final int      EIGHT                                 = 8;

    /**
     * NINE
     */
    public static final int      NINE                                  = 9;

    /**
     * SIXTEEN
     */
    public static final int      SIXTEEN                               = 16;

    /**
     * TWENTY_FOUR
     */
    public static final int      TWENTY_FOUR                           = 24;

    /**
     * THIRTY_TWO
     */
    public static final int      THIRTY_TWO                            = 32;

    /**
     * FORTY
     */
    public static final int      FORTY                                 = 40;

    /**
     * FORTY_EIGHT
     */
    public static final int      FORTY_EIGHT                           = 48;

    /**
     * FIFTY_SIX
     */
    public static final int      FIFTY_SIX                             = 56;

    /**
     * SIXTY_FOUR
     */
    public static final int      SIXTY_FOUR                            = 64;

    /**
     * XFF
     */
    public static final int      XFF                                   = 0xFF;

    /**
     * MAX_VALUE_FOR_UNSIGNED_BYTE
     */
    public static final int      MAX_VALUE_FOR_UNSIGNED_BYTE           = 255;

    /**
     * DEFAULT_MSG_SIZE
     */
    static final int             DEFAULT_MSG_LENGTH                    = SIXTY_FOUR;

    /**
     * DEFAULT_MAX_KEY
     */
    public static final int      DEFAULT_MAX_KEY                       = DEFAULT_MSG_LENGTH / FOUR;

    /**
     * DEFAULT_VALUE
     */
    public static final int      DEFAULT_VALUE                         = ZERO;

    // static final double[] TENTHS_DOZENS;
    /**
     * FORCE_ENCODING_ZERO_ON_2BITS
     * 
     * We encode variable size on 2 bits (1 byte in addition of type) instead on 1 byte (in type) to distinct null and Object with 0 size.
     */
    // TODO to remove
    // TODO move in hermod-java-ser-hermod
    // static final boolean FORCE_ENCODING_ZERO_ON_2BITS = true;

    /**
     * ERROR_WHEN_KEY_NOT_PRESENT.
     */
    public static final String   ERROR_WHEN_KEY_NOT_PRESENT            = "The key=%s is not present for type asked.";

    /**
     * ERROR_WHEN_YOU_SET_NULL_WITH_LENGTH_0.
     */
    public static final String   ERROR_WHEN_YOU_SET_NULL_WITH_LENGTH_0 = "You must set a Null with type Null with a length > 0, use (Integer) null or Null.INTEGER_NULL or Null.valueOf(Type.INTEGER) any Types.* except Null.NULL or Null.valueOf(0) if you want to have length = 0.";

    /**
     * HALF.
     */
    static final double          HALF                                  = 0.5;

    /**
     * POW_2.
     */
    private static final int[]   POW_2                                 = new int[Integer.SIZE];

    /**
     * DOZENS
     */

    // TODO replace
    // static final double[] DOZENS; // = { 1.0, 10.0, 100.0, 1000.0, 10000.0, 100000.0, 1000000.0, 10000000.0, 100000000.0 };

    // static final double[] TENTHS;
    /*
     * = { 1.0 / Scale.UNITS.getDecimal(), 1.0 / Scale.TENS.getDecimal(), 1.0 / Scale.HUNDREDS.getDecimal(), 1.0 / Scale.THOUSANDS.getDecimal(), 1.0 /
     * Scale.TEN_THOUSANDS.getDecimal(), 1.0 / Scale.HUNDRED_THOUSANDS.getDecimal(), 1.0 / Scale.MILLIONS.getDecimal(), 1.0 /
     * Scale.TEN_MILLIONS.getDecimal(), 1.0 / Scale.HUNDRED_MILLIONS.getDecimal() };
     */

    public static final double[] DOZENS_TENTHS;

    static {

        // PWO_2 init
        for (int i = 0; i < POW_2.length - 1; i++) {
            POW_2[i] = 1 << i;
        }
        POW_2[POW_2.length - 1] = Integer.MAX_VALUE;

        // //DOZENS
        // DOZENS = new double[Byte.MAX_VALUE];
        // for (int i = 0; i < DOZENS.length; i++) {
        // DOZENS[i] = Math.pow(10, i);
        // }
        //
        //
        // // TENTHS
        // TENTHS = new double[- Byte.MIN_VALUE];
        // for (int i = 0; i < TENTHS.length; i++) {
        // TENTHS[i] = 1 / Math.pow(10, i);
        // }

        // TENTHS_DOZENS
        DOZENS_TENTHS = new double[MAX_VALUE_FOR_UNSIGNED_BYTE];
        for (int i = 0; i < DOZENS_TENTHS.length; i++) {
            if (i == (byte) i) {
                DOZENS_TENTHS[i] = Math.pow(10, i);
            } else {
                DOZENS_TENTHS[i] = 1 / Math.pow(10, i);
            }
        }

    }

    /**
     * nextPowerOf2.
     * 
     * @param aNumber
     * @return
     */
    public static final int calculateNextPowerOf2(final int aNumber) {
        int i = 0;
        while (POW_2[++i] < aNumber) {
            // Do nothing
        }
        return POW_2[i];
    }

    /**
     * isAsciiString.
     * 
     * @param aString
     * @return
     */
    public static final boolean isAsciiString(final String aString) {
        if (aString != null) {
            for (int i = 0; i < aString.length(); i++) {
                final char c = aString.charAt(i);
                if (c != ((byte) c)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * serializeToJsonString.
     * 
     * @param aSrcMsg
     * @return
     */
    public static final String serializeToJson(final Msg aMsg) {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        final int[] keys = aMsg.getKeysArray();
        for (final int key : keys) {
            final byte typeAsByte = aMsg.getTypeAsByte(key);
            sb.append("\"");
            sb.append(key);
            sb.append("\"");
            sb.append(":");
            if (typeAsByte == ARRAY_FIXED_VALUE_TYPE || typeAsByte == ARRAY_VARIABLE_VALUE_TYPE) {
                final Object[] objects = aMsg.getAsObjects(key);
                if (objects != null) {
                    sb.append("[");
                    for (Object object : objects) {
                        sb.append(object);
                        sb.append(",");
                    }
                    if (objects.length != 0) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    sb.append("]");
                } else {
                    sb.append("null");
                }
            } else if (typeAsByte == Types.BYTE_TYPE) {
                sb.append(aMsg.getAsNullableByte(key));
            } else if (typeAsByte == Types.SHORT_TYPE) {
                sb.append(aMsg.getAsNullableShort(key));
            } else if (typeAsByte == Types.INT_TYPE) {
                sb.append(aMsg.getAsNullableInteger(key));
            } else if (typeAsByte == Types.INTEGER_TYPE || typeAsByte == Types.LONG_TYPE) {
                sb.append(aMsg.getAsNullableLong(key));
            } else if (typeAsByte == Types.FLOAT_TYPE) {
                sb.append(aMsg.getAsNullableFloat(key));
            } else if (typeAsByte == Types.DECIMAL_TYPE || typeAsByte == Types.DOUBLE_TYPE || typeAsByte == Types.FIVE_BITS_DECIMAL_TYPE) {
                sb.append(aMsg.getAsNullableDouble(key));
            } else if (typeAsByte == Types.STRING_UTF_8_TYPE) {
                final String s = aMsg.getAsString(key);
                if (s != null) {
                    sb.append("\"");
                    sb.append(s);
                    sb.append("\"");
                } else {
                    sb.append("null");
                }
            } else if (typeAsByte == Types.MSG_TYPE) {
                Msg msg = aMsg.getAsMsg(key);
                if (msg != null) {
                    sb.append(msg);
                } else {
                    sb.append("null");
                }
            } else if (typeAsByte == Types.NULL_TYPE) {
                sb.append("null");
            }
            sb.append(",");

        }
        if (keys.length > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("}");
        return sb.toString();

    }

    /**
     * hashCode.
     * 
     * @param aMsg
     * @return
     */
    public static final int hashCode(final Msg aMsg) {
        int hashcode = 0;
        // TODO to optimize
        final int[] keys = aMsg.getKeysArray();
        for (final int key : keys) {
            hashcode += key ^ aMsg.get(key).hashCode();
        }
        return hashcode;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public static final boolean equals(final Object aObj, final Msg aMsg) {
        // TODO to optimize
        if (aObj != null && aObj instanceof Msg) {
            final Msg msg = (Msg) aObj;
            final int[] keys = aMsg.getKeysArray();
            if (keys.length != msg.getKeysLength()) {
                return false;
            }
            for (final int key : keys) {
                if (!aMsg.get(key).equals(msg.get(key))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
