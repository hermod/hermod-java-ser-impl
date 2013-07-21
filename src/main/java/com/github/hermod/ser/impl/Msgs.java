package com.github.hermod.ser.impl;

import com.github.hermod.ser.Precision;
import com.github.hermod.ser.Type;
import com.github.hermod.ser.Types;

/**
 * IntMapConstants.
 * 
 * @author anavarro - Dec 15, 2011
 * 
 */
public final class Msgs
{
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
    static final byte LENGTH_MASK = (byte) 0b0001_1111;
    
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
    //TODO replace
    static final double[] DOZENS =
    { 1.0, 10.0, 100.0, 1000.0, 10000.0, 100000.0, 1000000.0, 10000000.0, 100000000.0 };
    
    
    static final double[] DOZENS2 = {1.0 / Precision.UNITS.getPrecision(), 1.0 / Precision.TENS.getPrecision(), 1.0 / Precision.HUNDREDS.getPrecision(), 1.0 / Precision.THOUSANDS.getPrecision(), 1.0 / Precision.TEN_THOUSANDS.getPrecision(), 1.0 / Precision.HUNDRED_THOUSANDS.getPrecision(), 1.0 / Precision.MILLIONS.getPrecision(), 1.0 / Precision.TEN_MILLIONS.getPrecision(), 1.0 / Precision.HUNDRED_MILLIONS.getPrecision()};
    
    /**
     * FORCE_ENCODING_ZERO_ON_2BITS
     * 
     * We encode variable size on 2 bits (1 byte in addition of type) instead on 1 byte (in type) to distinct null and Object with 0 size. 
     */
    //TODO to remove
    static final boolean FORCE_ENCODING_ZERO_ON_2BITS = true;
    

    
}
