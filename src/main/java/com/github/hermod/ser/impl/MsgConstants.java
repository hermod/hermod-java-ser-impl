package com.github.hermod.ser.impl;

import com.github.hermod.ser.Msg;

/**
 * IntMapConstants.
 * 
 * @author anavarro - Dec 15, 2011
 * 
 */
public final class MsgConstants
{
    
    // Might be set in another interface
    
    // null key
    public static final byte TYPE_NULL_KEY = 0x0; //0b0000_0000; 
    
    // TODO not implemented
    // date / timestamp
    public static final byte TYPE_DATE_VALUE = 0x20; //0b0010_0000;
    
    // long / int / short / byte
    public static final byte TYPE_INTEGER = 0x40; //0b0100_0000;
    
    // byte
    public static final byte TYPE_BYTE = TYPE_INTEGER | 1;
    
    // short
    public static final byte TYPE_SHORT = TYPE_INTEGER | 2;
    
    // int
    public static final byte TYPE_INT = TYPE_INTEGER | 4;
    
    // long
    public static final byte TYPE_LONG = TYPE_INTEGER | 8;
    
    // float / double or double encoded on 5 bits
    public static final byte TYPE_DECIMAL = 0x60; //0b0110_0000;
    
    // float
    public static final byte TYPE_FLOAT = TYPE_DECIMAL | 4;
    
    // double
    public static final byte TYPE_DOUBLE = TYPE_DECIMAL | 8;
    
    // double (encoded on 3 bits)
    public static final byte TYPE_3BITS_DECIMAL = TYPE_DECIMAL | 3;
    
    // double (encoded on 5 bits)
    public static final byte TYPE_5BITS_DECIMAL = TYPE_DECIMAL | 5;
    
    // String (Extended ASCII) or byte[]
    public static final byte TYPE_STRING_ISO_8859_1 = (byte) 0x80;//0b1000_0000;
    
    // TODO not implement
    // String (UTF16) or char[]
    public static final byte TYPE_STRING_UTF16 = (byte) 0xA0;//0b1010_0000;
    
    // TODO not implemented
    // Array
    public static final byte TYPE_ARRAY = (byte) 0xC0; //0b1100_0000;
    
    // Msg
    public static final byte TYPE_MSG = (byte) 0xE0; //0b1110_0000;
    
    // Length is implemented as
    // 1-29, the length is in the last 5 bits of the type
    // 30, the length is on next bytes (as litte endian byte)
    // 31, the length is on next 4 bytes (as litte endian int)
    
    // Size = 0 on the last 5 bites of type, null value
    // Size = 0 on the next byte (Empty value like "" for String)
    
    /**
     * 
     */
    public static final byte TYPE_MASK = (byte) 0xE0; //0b1110_0000;
    
    /**
     * 
     */
    public static final byte SIZE_MASK = 0x1F; //0b0001_1111;
    
    /**
     * 
     */
    public static final int SIZE_ENCODED_IN_A_BIT = SIZE_MASK - 1;
    
    /**
     * 
     */
    public static final int SIZE_ENCODED_IN_AN_INT = SIZE_MASK;
    
    /**
     * DEFAULT_MSG_SIZE
     */
    public static final int DEFAULT_MSG_SIZE = 64;
    
    /**
     * DEFAULT_MAX_KEY
     */
    public static final int DEFAULT_MAX_KEY = DEFAULT_MSG_SIZE / 4;
    
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
     * DEFAULT_LONG_VALUE
     */
    public static final long DEFAULT_LONG_VALUE = DEFAULT_INT_VALUE;
    
    /**
     * DEFAULT_FLOAT_VALUE
     */
    public static final float DEFAULT_FLOAT_VALUE = DEFAULT_INT_VALUE;
    
    /**
     * DEFAULT_DOUBLE_VALUE
     */
    public static final double DEFAULT_DOUBLE_VALUE = DEFAULT_INT_VALUE;
    
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
    public static final Msg DEFAULT_MSG_VALUE = (new KeyObjectMsgFactory()).create(0);
    
    /**
     * DEFAULT_ARRAY_VALUE
     */
    public static final Object[] DEFAULT_ARRAY_VALUE = new Object[0];

    
    /**
     * DOZENS
     */
    public static double[] DOZENS =
    { 1.0, 10.0, 100.0, 1000.0, 10000.0, 100000.0, 1000000.0, 10000000.0, 100000000.0 };
    
    
  
}
