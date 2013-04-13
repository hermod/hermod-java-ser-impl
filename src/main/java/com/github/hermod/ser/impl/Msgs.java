package com.github.hermod.ser.impl;

import com.github.hermod.ser.EPrecision;
import com.github.hermod.ser.EType;
import com.github.hermod.ser.IMsg;
import com.github.hermod.ser.Types;

/**
 * IntMapConstants.
 * 
 * @author anavarro - Dec 15, 2011
 * 
 */
public final class Msgs
{
    
    // Might be set in another interface
    
//    // Null key
//    public static final byte TYPE_NULL = EType.NULL.getId();
//    
//    // Msg
//    public static final byte TYPE_MSG = EType.MSG.getId();
//    
//    // Integer (long / int / short / byte / boolean)
//    public static final byte TYPE_INTEGER = EType.INTEGER.getId();
    
    
    // byte / boolean
    public static final byte BYTE_TYPE = (byte) (Types.INTEGER_TYPE | 1);
    
    // short
    public static final byte SHORT_TYPE = (byte) (Types.INTEGER_TYPE | 2);
    
    // int
    public static final byte INT_TYPE = (byte) (Types.INTEGER_TYPE | 4);
    
    // long
    public static final byte LONG_TYPE = (byte) (Types.INTEGER_TYPE | 8);
    
    
    // float / double or double encoded on 5 bits
    public static final byte TYPE_DECIMAL = EType.DECIMAL.getId();
    
    // float
    public static final byte FLOAT_TYPE = (byte) (Types.DECIMAL_TYPE | 4);
    
    // double
    public static final byte DOUBLE_TYPE = (byte) (Types.DECIMAL_TYPE | 8);
    
    // double (encoded on 3 bits)
    public static final byte THREE_BITS_DECIMAL_TYPE = (byte) (Types.DECIMAL_TYPE | 3);
    
    // double (encoded on 5 bits)
    public static final byte FIVE_BITS_DECIMAL_TYPE = (byte) (Types.DECIMAL_TYPE | 5);
    
    
//    // String (Extended ASCII) or byte[]
//    public static final byte TYPE_STRING_ISO_8859_1 = EType.STRING_ISO_8859_1.getId();
//    
//    // TODO not implement
//    // String (UTF16) or char[]
//    public static final byte TYPE_STRING_UTF16 = EType.STRING_UTF16.getId();
//    
//    // TODO not implemented
//    // Fixed Value Length Array
//    public static final byte TYPE_FIXED_VALUE_LENGTH_ARRAY = EType.FIXED_VALUE_LENGTH_ARRAY.getId();
//    
//    // TODO not implemented
//    // Variable Value Length Array
//    public static final byte TYPE_VARIABLE_VALUE_LENGTH_ARRAY = EType.VARIABLE_VALUE_LENGTH_ARRAY.getId();
    
    // Length is implemented as
    // 1-29, the length is in the last 5 bits of the type
    // 30, the length is on next bytes (as litte endian byte)
    // 31, the length is on next 4 bytes (as litte endian int)
    
    // Size = 0 on the last 5 bites of type, null value
    // Size = 0 on the next byte (Empty value like "" for String)
    
    /**
     * 
     */
    //public static final byte TYPE_MASK = (byte) 0b1110_0000;
    
    /**
     * 
     */
    public static final byte LENGTH_MASK = (byte) 0b0001_1111;
    
    /**
     * 
     */
    public static final int LENGTH_ENCODED_IN_A_BIT = LENGTH_MASK - 1;
    
    /**
     * 
     */
    public static final int LENSTH_ENCODED_IN_AN_INT = LENGTH_MASK;
    
    /**
     * DEFAULT_MSG_SIZE
     */
    public static final int DEFAULT_MSG_LENGTH = 64;
    
    /**
     * DEFAULT_MAX_KEY
     */
    public static final int DEFAULT_MAX_KEY = DEFAULT_MSG_LENGTH / 4;
    
    /**
     * FIRST_KEY
     */
    public static final int FIRST_AUTHORIZED_KEY = 0;
    
    /**
     * DEFAULT_INT_VALUE
     */
    public static final int DEFAULT_INT_VALUE = 0;
    
    /**
     * DEFAULT_SHORT_VALUE
     */
    public static final short DEFAULT_SHORT_VALUE = (short) DEFAULT_INT_VALUE;
    
    /**
     * DEFAULT_BYTE_VALUE
     */
    public static final byte DEFAULT_BYTE_VALUE = (byte) DEFAULT_INT_VALUE;
    
    /**
     * DEFAULT_BOOLEAN_VALUE
     */
    public static final boolean DEFAULT_BOOLEAN_VALUE = false;
    
    /**
     * DEFAULT_LONG_VALUE
     */
    public static final long DEFAULT_LONG_VALUE = DEFAULT_INT_VALUE;
    
    /**
     * DEFAULT_DOUBLE_VALUE
     */
    public static final double DEFAULT_DOUBLE_VALUE = Double.NaN;
    
    /**
     * DEFAULT_FLOAT_VALUE
     */
    public static final float DEFAULT_FLOAT_VALUE = (float) DEFAULT_DOUBLE_VALUE;
    
    
    /**
     * DEFAULT_STRING_VALUE
     */
    public static final String DEFAULT_STRING_VALUE = "";
    
    
    /**
     * DEFAULT_BYTES_VALUE
     */
    public static final byte[] DEFAULT_BYTES_VALUE = new byte[0];
    
    
    /**
     * DEFAULT_MSG_VALUE
     */
    public static final IMsg DEFAULT_MSG_VALUE = KeyObjectMsgs.newMsg();
    
    /**
     * DEFAULT_ARRAY_VALUE
     */
    public static final Object[] DEFAULT_ARRAY_VALUE = new Object[0];

    
    /**
     * DOZENS
     */
    
    public static double[] DOZENS =
    { 1.0, 10.0, 100.0, 1000.0, 10000.0, 100000.0, 1000000.0, 10000000.0, 100000000.0 };
    
    public static final double[] DOZENS2 = {EPrecision.UNITS.getPrecision()};
    
    
    /**
     * FORCE_ENCODING_ZERO_ON_2BITS
     * 
     * We encode variable size on 2 bits (1 byte in addition of type) instead on 1 byte (in type) to distinct null and Object with 0 size. 
     */
    public static boolean FORCE_ENCODING_ZERO_ON_2BITS = true;
    
    
    
  

}
