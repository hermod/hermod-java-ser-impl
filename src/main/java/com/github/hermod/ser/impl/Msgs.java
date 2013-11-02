package com.github.hermod.ser.impl;

import static com.github.hermod.ser.Types.ARRAY_FIXED_VALUE_TYPE;
import static com.github.hermod.ser.Types.ARRAY_VARIABLE_VALUE_TYPE;
import static com.github.hermod.ser.Types.NULL_TYPE;

import com.github.hermod.ser.Msg;
import com.github.hermod.ser.Scale;
import com.github.hermod.ser.Type;
import com.github.hermod.ser.Types;

/**
 * IntMapConstants.
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
        super();
    }

    /**
     * ZERO
     */
    static final int ZERO = 0;

    /**
     * ONE
     */
    static final int ONE = 1;

    /**
     * TWO
     */
    static final int TWO = 2;

    /**
     * THREE
     */
    static final int THREE = 3;

    /**
     * FOUR
     */
    static final int FOUR = 4;

    /**
     * FIVE
     */
    static final int FIVE = 5;

    /**
     * SIX
     */
    static final int SIX = 6;

    /**
     * SEVEN
     */
    static final int SEVEN = 7;

    /**
     * EIGHT
     */
    static final int EIGHT = 8;

    /**
     * SIXTEEN
     */
    static final int SIXTEEN = 16;

    /**
     * TWENTY_FOUR
     */
    static final int TWENTY_FOUR = 24;

    /**
     * THIRTY_TWO
     */
    static final int THIRTY_TWO = 32;

    /**
     * FORTY
     */
    static final int FORTY = 40;

    /**
     * FORTY_EIGHT
     */
    static final int FORTY_EIGHT = 48;

    /**
     * FIFTY_SIX
     */
    static final int FIFTY_SIX = 56;

    /**
     * SIXTY_FOUR
     */
    static final int SIXTY_FOUR = 64;

    // byte / boolean
    static final byte BYTE_TYPE = (byte) (Types.INTEGER_TYPE | ONE);

    // short
    static final byte SHORT_TYPE = (byte) (Types.INTEGER_TYPE | TWO);

    // int
    static final byte INT_TYPE = (byte) (Types.INTEGER_TYPE | FOUR);

    // long
    static final byte LONG_TYPE = (byte) (Types.INTEGER_TYPE | EIGHT);

    // float / double or double encoded on 5 bits
    static final byte TYPE_DECIMAL = Type.DECIMAL.getId();

    // float
    static final byte FLOAT_TYPE = (byte) (Types.DECIMAL_TYPE | FOUR);

    // double
    static final byte DOUBLE_TYPE = (byte) (Types.DECIMAL_TYPE | EIGHT);

    // double (encoded on 3 bits)
    static final byte THREE_BITS_DECIMAL_TYPE = (byte) (Types.DECIMAL_TYPE | THREE);

    // double (encoded on 5 bits)
    static final byte FIVE_BITS_DECIMAL_TYPE = (byte) (Types.DECIMAL_TYPE | FIVE);

    // Length is implemented as
    // 1-29, the length is in the last 5 bits of the type
    // 30, the length is on next bytes (as litte endian byte)
    // 31, the length is on next 4 bytes (as litte endian int)

    // Size = 0 on the last 5 bites of type, null value
    // Size = 0 on the next byte (Empty value like "" for String)

    /**
     * XFF
     */
    static final int XFF = 0xFF;

    /**
     * 
     */
    static final byte LENGTH_MASK = (byte) 0x1F;//(byte) 0b0001_1111;

    /**
     * 
     */
    static final int LENGTH_ENCODED_IN_A_BIT = LENGTH_MASK - ONE;

    /**
     * 
     */
    static final int LENGTH_ENCODED_IN_AN_INT = LENGTH_MASK;

    /**
     * DEFAULT_MSG_SIZE
     */
    static final int DEFAULT_MSG_LENGTH = SIXTY_FOUR;

    /**
     * DEFAULT_MAX_KEY
     */
    static final int DEFAULT_MAX_KEY = DEFAULT_MSG_LENGTH / FOUR;

    /**
     * FIRST_KEY
     */
    static final int FIRST_AUTHORIZED_KEY = ZERO;

    /**
     * DEFAULT_VALUE
     */
    static final int DEFAULT_VALUE = ZERO;

    /**
     * DOZENS
     */
    // TODO replace
    static final double[] DOZENS = { 1.0, 10.0, 100.0, 1000.0, 10000.0, 100000.0, 1000000.0, 10000000.0, 100000000.0 };

    static final double[] TENTHS = { 1.0 / Scale.UNITS.getDecimal(), 1.0 / Scale.TENS.getDecimal(),
            1.0 / Scale.HUNDREDS.getDecimal(), 1.0 / Scale.THOUSANDS.getDecimal(), 1.0 / Scale.TEN_THOUSANDS.getDecimal(),
            1.0 / Scale.HUNDRED_THOUSANDS.getDecimal(), 1.0 / Scale.MILLIONS.getDecimal(), 1.0 / Scale.TEN_MILLIONS.getDecimal(),
            1.0 / Scale.HUNDRED_MILLIONS.getDecimal() };

    /**
     * FORCE_ENCODING_ZERO_ON_2BITS
     * 
     * We encode variable size on 2 bits (1 byte in addition of type) instead on 1 byte (in type) to distinct null and Object with 0 size.
     */
    // TODO to remove
    static final boolean FORCE_ENCODING_ZERO_ON_2BITS = true;
    
    /**
     * ERROR_WHEN_KEY_NOT_PRESENT
     */
    static final String ERROR_WHEN_KEY_NOT_PRESENT = "The key=%s is not present for type asked.";


    private static final int[] POW_2 = new int[Integer.SIZE];

    static {
        for (int i = 0; i < POW_2.length - 1; i++) {
            POW_2[i] = 1 << i;
        }
        POW_2[POW_2.length - 1] = Integer.MAX_VALUE;
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
            if (typeAsByte != NULL_TYPE) {
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
                } else if (typeAsByte == Types.INTEGER_TYPE) {
                    sb.append(aMsg.getAsNullableLong(key));
                } else if (typeAsByte == Types.DECIMAL_TYPE) {
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
                } else {
                    // should not occur
                }
                sb.append(",");
            }
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
