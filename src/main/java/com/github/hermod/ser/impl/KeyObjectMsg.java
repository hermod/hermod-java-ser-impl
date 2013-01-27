package com.github.hermod.ser.impl;

import static com.github.hermod.ser.impl.MsgConstants.DEFAULT_BYTE_VALUE;
import static com.github.hermod.ser.impl.MsgConstants.DEFAULT_DOUBLE_VALUE;
import static com.github.hermod.ser.impl.MsgConstants.DEFAULT_FLOAT_VALUE;
import static com.github.hermod.ser.impl.MsgConstants.DEFAULT_INT_VALUE;
import static com.github.hermod.ser.impl.MsgConstants.DEFAULT_LONG_VALUE;
import static com.github.hermod.ser.impl.MsgConstants.DEFAULT_SHORT_VALUE;
import static com.github.hermod.ser.impl.MsgConstants.DOZENS;
import static com.github.hermod.ser.impl.MsgConstants.SIZE_ENCODED_IN_A_BIT;
import static com.github.hermod.ser.impl.MsgConstants.SIZE_ENCODED_IN_AN_INT;

import org.junit.internal.ArrayComparisonFailure;

import static com.github.hermod.ser.impl.MsgConstants.SIZE_MASK;
import static com.github.hermod.ser.impl.MsgConstants.TYPE_5BITS_DECIMAL;
import static com.github.hermod.ser.impl.MsgConstants.TYPE_BYTE;
import static com.github.hermod.ser.impl.MsgConstants.TYPE_DOUBLE;
import static com.github.hermod.ser.impl.MsgConstants.TYPE_FLOAT;
import static com.github.hermod.ser.impl.MsgConstants.TYPE_INT;
import static com.github.hermod.ser.impl.MsgConstants.TYPE_LONG;
import static com.github.hermod.ser.impl.MsgConstants.TYPE_MASK;
import static com.github.hermod.ser.impl.MsgConstants.TYPE_NULL_KEY;
import static com.github.hermod.ser.impl.MsgConstants.TYPE_SHORT;
import static com.github.hermod.ser.impl.MsgConstants.DEFAULT_MAX_KEY;
import static com.github.hermod.ser.impl.MsgConstants.TYPE_INTEGER;
import static com.github.hermod.ser.impl.MsgConstants.*;

import com.github.hermod.ser.Msg;

/**
 * <p>KeyObjectMsg. </p>
 * 
 * @author anavarro - Jan 21, 2013
 * 
 */
public class KeyObjectMsg implements Msg {

    private byte[] types;
    private long[] primitiveValues;
    private Object[] objectValues;

    /**
     * Constructor.
     * 
     * @param aDescriptor
     */
    public KeyObjectMsg(final int aKeyMax) {
        this.types = new byte[aKeyMax + 1];
        this.primitiveValues = new long[aKeyMax + 1];
        this.objectValues = new Object[aKeyMax + 1];
    }

    /**
     * @param aKeyMax
     */
    private final void clear(final int aKeyMax) {
        if (aKeyMax > this.primitiveValues.length) {
            this.types = new byte[aKeyMax + 1];
            this.primitiveValues = new long[aKeyMax + 1];
            this.objectValues = new Object[aKeyMax + 1];
        } else {
            for (int i = 0; i < this.primitiveValues.length; i++) {
                this.types[i] = 0;
                this.primitiveValues[i] = 0;
                this.objectValues[i] = null;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.WriteIntMap#clear()
     */
    @Override
    public final void clear() {
        for (int i = 0; i < this.primitiveValues.length; i++) {
            this.types[i] = 0;
            this.primitiveValues[i] = 0;
            this.objectValues[i] = null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.ReadIntMap#contains(int)
     */
    @Override
    public final boolean contains(final int aKey) {
        try {
            return (this.types[aKey] != 0) ? true : false;
        } catch (final ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.ReadIntMap#get(int)
     */
    @Override
    public final Object get(final int aKey) {
        if (contains(aKey)) {
            // TODO
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.ReadIntMap#getAsByte(int)
     */
    @Override
    public final byte getAsByte(final int aKey) {
        try {
            return (this.types[aKey] == TYPE_BYTE) ? (byte) this.primitiveValues[aKey] : DEFAULT_BYTE_VALUE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            return DEFAULT_BYTE_VALUE;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.ReadIntMap#getAsShort(int)
     */
    @Override
    public final short getAsShort(final int aKey) {
        try {
            return (this.types[aKey] == TYPE_BYTE || this.types[aKey] == TYPE_SHORT) ? (short) this.primitiveValues[aKey] : DEFAULT_SHORT_VALUE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            return DEFAULT_SHORT_VALUE;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.ReadIntMap#getAsInt(int)
     */
    @Override
    public final int getAsInt(final int aKey) {
        try {
            return (this.types[aKey] == TYPE_BYTE || this.types[aKey] == TYPE_SHORT || this.types[aKey] == TYPE_INT) ? (int) this.primitiveValues[aKey]
                    : DEFAULT_INT_VALUE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            return DEFAULT_INT_VALUE;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.ReadIntMap#getAsLong(int)
     */
    @Override
    public final long getAsLong(final int aKey) {
        try {
            return ((this.types[aKey] & TYPE_MASK) == TYPE_INTEGER) ? (long) this.primitiveValues[aKey] : DEFAULT_LONG_VALUE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            return DEFAULT_LONG_VALUE;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.ReadIntMap#getAsFloat(int)
     */
    @Override
    public final float getAsFloat(final int aKey) {
        try {
            // TODO do some stuff if the type TYPE_5BITS_DECIMAL
            return (this.types[aKey] == TYPE_FLOAT) ? Float.intBitsToFloat((int) this.primitiveValues[aKey]) : DEFAULT_FLOAT_VALUE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            return DEFAULT_FLOAT_VALUE;
        }
    }

    /**
     * getAsDecimal
     * 
     * @param aKey
     * @return
     */
    // TODO set as private
    @Override
    public final double getAsDouble(final int aKey) {
        try {
            switch (this.types[aKey]) {
            // case TYPE_3BITS_DECIMAL:
            // return (this.primitiveValues[aKey] >> 8) / DOZENS[(int) (0xFF &
            // this.primitiveValues[aKey])];
            case TYPE_5BITS_DECIMAL:
                return getAs5BitsDecimal(aKey);
                // return (this.primitiveValues[aKey] >> 8) / DOZENS[(int) (0xFF & this.primitiveValues[aKey])];
            case TYPE_DOUBLE:
                return Double.longBitsToDouble(this.primitiveValues[aKey]);
            case TYPE_FLOAT:
                return Float.intBitsToFloat((int) this.primitiveValues[aKey]);
            default:
                return DEFAULT_DOUBLE_VALUE;
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            return DEFAULT_DOUBLE_VALUE;
        }
    }

    /**
     * getAs5BitsDecimal.
     * 
     * @param aKey
     * @return
     */
    private final double getAs5BitsDecimal(final int aKey) {
        final int digit = (int) (0xFF & this.primitiveValues[aKey]);
        final double allDigit = (this.primitiveValues[aKey] >> 8);
        switch (digit) {
        case 0:
            return allDigit;
        case 1:
            return allDigit * 0.1;
        case 2:
            return allDigit * 0.01;
        case 3:
            return allDigit * 0.001;
        case 4:
            return allDigit * 0.0001;
        case 5:
            return allDigit * 0.00001;
        case 6:
            return allDigit * 0.000001;
        case 7:
            return allDigit * 0.0000001;
        case 8:
            return allDigit * 0.00000001;
        default:
            return DEFAULT_DOUBLE_VALUE;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.ReadIntMap#getAsString(int)
     */
    @Override
    public final String getAsString(final int aKey) {
        try {
            return ((this.types[aKey] & TYPE_MASK) == TYPE_STRING_ISO_8859_1) ? (String) this.objectValues[aKey] : DEFAULT_STRING_VALUE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            return DEFAULT_STRING_VALUE;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser3.intmap.ReadIntMap#getAsMap(int)
     */
    @Override
    public final Msg getAsMsg(int aKey) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser3.intmap.ReadIntMap#getKeys()
     */
    @Override
    public final int[] getKeys() {
        int size = 0;
        for (int i = 0; i < this.types.length; i++) {
            if (this.types[i] != TYPE_NULL_KEY) {
                size++;
            }
        }
        final int[] keys = new int[size];
        int index = 0;
        for (int i = 0; i < this.types.length; i++) {
            if (this.types[i] != TYPE_NULL_KEY) {
                keys[index++] = i;
            }
        }
        return keys;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser3.intmap.ReadIntMap#getIterator()
     */
    // @Override
    // public final IntMapIterator<ReadIntMapMsg> getIterator()
    // {
    // // TODO Auto-generated method stub
    // return null;
    // }

    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.WriteIntMap#set(int, byte)
     */
    @Override
    public final void set(final int aKey, final byte aByte) {
        try {
            this.primitiveValues[aKey] = aByte;
            this.types[aKey] = TYPE_BYTE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            if (aKey < 0) {
                throw new IllegalArgumentException("The key=" + aKey + " must be positive.");
            } else {
                increaseKeyMax(aKey);
                set(aKey, aByte);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.WriteIntMap#set(int, short)
     */
    @Override
    public final void set(final int aKey, final short aShort) {
        try {
            // TODO do optimization if the value can be encoded as byte
            this.primitiveValues[aKey] = aShort;
            this.types[aKey] = TYPE_SHORT;
        } catch (final ArrayIndexOutOfBoundsException e) {
            if (aKey < 0) {
                throw new IllegalArgumentException("The key=" + aKey + " must be positive.");
            } else {
                increaseKeyMax(aKey);
                set(aKey, aShort);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.WriteIntMap#set(int, int)
     */
    @Override
    public final void set(final int aKey, final int aInt) {
        try {
            // TODO do optimization if the value can be encoded as byte or short
            this.primitiveValues[aKey] = aInt;
            this.types[aKey] = TYPE_INT;
        } catch (final ArrayIndexOutOfBoundsException e) {
            if (aKey < 0) {
                throw new IllegalArgumentException("The key=" + aKey + " must be positive.");
            } else {
                increaseKeyMax(aKey);
                set(aKey, aInt);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.WriteIntMap#set(int, long)
     */
    @Override
    public final void set(final int aKey, final long aLong) {
        try {
            // TODO do optimization if the value can be encoded as byte, short or int
            this.primitiveValues[aKey] = aLong;
            this.types[aKey] = TYPE_LONG;
        } catch (final ArrayIndexOutOfBoundsException e) {
            if (aKey < 0) {
                throw new IllegalArgumentException("The key=" + aKey + " must be positive.");
            } else {
                increaseKeyMax(aKey);
                set(aKey, aLong);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.WriteIntMap#set(int, float)
     */
    @Override
    public final void set(final int aKey, final float aFloat) {
        try {
            this.primitiveValues[aKey] = Float.floatToIntBits(aFloat);
            this.types[aKey] = TYPE_FLOAT;
        } catch (final ArrayIndexOutOfBoundsException e) {
            if (aKey < 0) {
                throw new IllegalArgumentException("The key=" + aKey + " must be positive.");
            } else {
                increaseKeyMax(aKey);
                set(aKey, aFloat);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.WriteIntMap#set(int, double)
     */
    @Override
    public final void set(final int aKey, final double aDouble) {
        try {
            this.primitiveValues[aKey] = Double.doubleToLongBits(aDouble);
            this.types[aKey] = TYPE_DOUBLE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            if (aKey < 0) {
                throw new IllegalArgumentException("The key=" + aKey + " must be positive.");
            } else {
                increaseKeyMax(aKey);
                set(aKey, aDouble);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.WriteIntMap#set(int, double, int)
     */
    // TODO check nbDigit via enum or the
    @Override
    public final void set(final int aKey, final double aDouble, final int nbDigit) {
        try {
            final double d = (aDouble * DOZENS[nbDigit]) + 0.5;
            // if (d >= Short.MIN_VALUE && d <= Short.MAX_VALUE)
            // {
            // this.primitiveValues[aKey] = (nbDigit) | (((short) (d)) << 8);
            // this.types[aKey] = TYPE_3BITS_DECIMAL;
            // }
            // else
            {
                if (d >= Integer.MIN_VALUE && d <= Integer.MAX_VALUE) {
                    this.primitiveValues[aKey] = (nbDigit) | (((int) (d)) << 8);
                    this.types[aKey] = TYPE_5BITS_DECIMAL;
                } else {
                    this.set(aKey, aDouble);
                }
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            if (aKey < 0) {
                throw new IllegalArgumentException("The key=" + aKey + " must be positive.");
            } else {
                increaseKeyMax(aKey);
                set(aKey, aDouble, nbDigit);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.WriteIntMap#set(int, java.lang.String)
     */
    @Override
    public final void set(final int aKey, final String aString) {
        try {
            this.objectValues[aKey] = aString;
            this.types[aKey] = TYPE_STRING_ISO_8859_1;
        } catch (final ArrayIndexOutOfBoundsException e) {
            if (aKey < 0) {
                throw new IllegalArgumentException("The key=" + aKey + " must be positive.");
            } else {
                increaseKeyMax(aKey);
                set(aKey, aString);
            }
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, com.github.hermod.ser.Msg)
     */
    @Override
    public final void set(final int aKey, final Msg aMsg) {
        // TODO
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.WriteIntMap#setAll(org.fame.z.intmap.ReadIntMap)
     */
    @Override
    public final void setAll(final Msg aMsg) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.WriteIntMap#remove(int)
     */
    @Override
    public final void remove(final int aKey) {
        try {
            this.types[aKey] = TYPE_NULL_KEY;
        } catch (ArrayIndexOutOfBoundsException e) {
            // TODO
            throw new IllegalArgumentException("");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.ReadMsg#readFrom(byte[], int, int)
     */
    @Override
    public final void readFrom(final byte[] bytes, final int offset, final int length) {
        int pos = offset;
        int key = 0;

        // find max key
        // not need for feed
        // Calculate max key value
        // TODO to optimize with a try catch, lastPos and relaunch readFrom
        while (pos < offset + length) {
            if ((bytes[pos] & TYPE_MASK) == TYPE_NULL_KEY) {
                final int sizeMask = bytes[pos++] & SIZE_MASK;
                key += (sizeMask < SIZE_ENCODED_IN_A_BIT) ? sizeMask : (sizeMask == SIZE_ENCODED_IN_A_BIT) ? bytes[pos++] : (bytes[pos++] & 0xFF)
                        | ((bytes[pos++] & 0xFF) << 8) | ((bytes[pos++] & 0xFF) << 16) | ((bytes[pos] & 0xFF) << 24);
            } else {
                final int sizeMask = bytes[pos++] & SIZE_MASK;
                pos += (sizeMask != SIZE_ENCODED_IN_A_BIT) ? sizeMask : (bytes[pos++] & 0xFF) | ((bytes[pos++] & 0xFF) << 8)
                        | ((bytes[pos++] & 0xFF) << 16) | ((bytes[pos] & 0xFF) << 24);
                key++;
            }
        }
        clear(key);

        // Decoding
        key = 0;
        pos = offset;
        while (pos < offset + length) {
            final byte type = bytes[pos++];

            // Skip null key
            if ((type & TYPE_MASK) == TYPE_NULL_KEY) {
                final int sizeMask = type & SIZE_MASK;
                key += (sizeMask < SIZE_ENCODED_IN_A_BIT) ? sizeMask : (sizeMask == SIZE_ENCODED_IN_A_BIT) ? bytes[pos++] : (bytes[pos++] & 0xFF)
                        | ((bytes[pos++] & 0xFF) << 8) | ((bytes[pos++] & 0xFF) << 16) | ((bytes[pos] & 0xFF) << 24);
            }
            // Decode values
            else {
                this.types[key] = type;

                switch (type) {
                //  All fixed type
                case TYPE_BYTE:
                    this.primitiveValues[key] = (byte) ((bytes[pos++] & 0xFF));
                    break;

                case TYPE_SHORT:
                    this.primitiveValues[key] = (((short) bytes[pos++] & 0xFF)) | (((short) bytes[pos++] & 0xFF) << 8);
                    break;

                case TYPE_INT:
                    this.primitiveValues[key] = (((int) bytes[pos++] & 0xFF)) | (((int) bytes[pos++] & 0xFF) << 8)
                            | (((int) bytes[pos++] & 0xFF) << 16) | (((int) bytes[pos++] & 0xFF) << 24);
                    break;

                case TYPE_DOUBLE:
                    this.primitiveValues[key] = (((long) bytes[pos++] & 0xFF) | (((long) bytes[pos++] & 0xFF) << 8)
                            | (((long) bytes[pos++] & 0xFF) << 16) | (((long) bytes[pos++] & 0xFF) << 24) | (((long) bytes[pos++] & 0xFF) << 32)
                            | (((long) bytes[pos++] & 0xFF) << 40) | (((long) bytes[pos++] & 0xFF) << 48) | (((long) bytes[pos++] & 0xFF) << 56));
                    break;

                case TYPE_5BITS_DECIMAL:
                    this.primitiveValues[key] = (((int) bytes[pos++] & 0xFF) << 8) | (((int) bytes[pos++] & 0xFF) << 16)
                            | (((int) bytes[pos++] & 0xFF) << 24) | (((int) bytes[pos++] & 0xFF) << 32) | ((byte) bytes[pos++] & 0xFF);
                    break;

                // case TYPE_3BITS_DECIMAL:
                // this.primitiveValues[key] = this.primitiveValues[key] =
                // ((bytes[pos++] & 0xFF) << 8)
                // | ((bytes[pos++] & 0xFF) << 16) | (bytes[pos++] & 0xFF);
                // break;

                case TYPE_FLOAT:
                    this.primitiveValues[key] = (((int) bytes[pos++] & 0xFF)) | (((int) bytes[pos++] & 0xFF) << 8)
                            | (((int) bytes[pos++] & 0xFF) << 16) | (((int) bytes[pos++] & 0xFF) << 24);
                    break;

                case TYPE_LONG:
                    this.primitiveValues[key] = (((long) bytes[pos++] & 0xFF)) | (((long) bytes[pos++] & 0xFF) << 8)
                            | (((long) bytes[pos++] & 0xFF) << 16) | (((long) bytes[pos++] & 0xFF) << 24) | (((long) bytes[pos++] & 0xFF) << 32)
                            | (((long) bytes[pos++] & 0xFF) << 40) | (((long) bytes[pos++] & 0xFF) << 48) | (((long) bytes[pos++] & 0xFF) << 56);
                    break;

                // All non fixed type
                default:
                    final byte typeMask = (byte) (type & TYPE_MASK);
                    final int sizeMask = (SIZE_MASK & type);
                    final int size = (sizeMask < SIZE_ENCODED_IN_A_BIT) ? sizeMask : (sizeMask == SIZE_ENCODED_IN_A_BIT) ? bytes[pos++]
                            : (bytes[pos++] & 0xFF) | ((bytes[pos++] & 0xFF) << 8) | ((bytes[pos++] & 0xFF) << 16) | ((bytes[pos++] & 0xFF) << 24);

                    switch (typeMask) {
                    case TYPE_STRING_ISO_8859_1:
                        final char[] chars = new char[size];
                        for (int i = 0; i < size; i++) {
                            chars[i] = (char) bytes[pos++];
                        }
                        this.objectValues[key] = new String(chars);
                        break;

                    default:
                        break;
                    }

                    break;
                }
                key++;
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.WriteMsg#writeTo()
     */
    @Override
    public final byte[] writeTo() {
        final byte[] bytes = new byte[getBytesSize()];
        this.writeTo(bytes, 0);
        return bytes;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.WriteMsg#writeTo(byte[], int)
     */
    @Override
    public final int writeTo(final byte[] bytes, final int offset) {

        // Calculate size
        // TODO to optimize with a try catch
        final int size = getBytesSize();
        if (bytes.length - offset < size) {
            throw new IllegalArgumentException("Bytes array too small from the offset");
        }

        int pos = offset;
        int consecutiveNullKey = 0;

        // Write Type / Values
        for (int i = 0; i < this.types.length; i++) {
            if (this.types[i] != TYPE_NULL_KEY) {
                if (consecutiveNullKey != 0) {
                    pos = writeVariableSize(bytes, pos, consecutiveNullKey);
                    consecutiveNullKey = 0;
                }

                final byte type = this.types[i];
                bytes[pos++] = type;
                switch (type) {
                //  all fixed type
                case TYPE_BYTE:
                    bytes[pos++] = (byte) this.primitiveValues[i];
                    break;

                case TYPE_SHORT:
                    final int aShort = (short) this.primitiveValues[i];
                    bytes[pos++] = (byte) (aShort);
                    bytes[pos++] = (byte) (aShort >> 8);
                    break;

                case TYPE_INT:
                    final int aInt = (int) this.primitiveValues[i];
                    bytes[pos++] = (byte) (aInt);
                    bytes[pos++] = (byte) (aInt >> 8);
                    bytes[pos++] = (byte) (aInt >> 16);
                    bytes[pos++] = (byte) (aInt >> 24);
                    break;

                case TYPE_DOUBLE:
                    final long longBits = this.primitiveValues[i];
                    bytes[pos++] = (byte) (longBits);
                    bytes[pos++] = (byte) (longBits >> 8);
                    bytes[pos++] = (byte) (longBits >> 16);
                    bytes[pos++] = (byte) (longBits >> 24);
                    bytes[pos++] = (byte) (longBits >> 32);
                    bytes[pos++] = (byte) (longBits >> 40);
                    bytes[pos++] = (byte) (longBits >> 48);
                    bytes[pos++] = (byte) (longBits >> 56);
                    break;

                case TYPE_5BITS_DECIMAL:
                    final int anInt = (int) (this.primitiveValues[i] >> 8);
                    bytes[pos++] = (byte) (anInt);
                    bytes[pos++] = (byte) (anInt >> 8);
                    bytes[pos++] = (byte) (anInt >> 16);
                    bytes[pos++] = (byte) (anInt >> 24);
                    bytes[pos++] = (byte) (this.primitiveValues[i]);
                    break;

                // case TYPE_3BITS_DECIMAL:
                // final short aShort1 = (short) (this.primitiveValues[i] >> 8);
                // bytes[pos++] = (byte) (aShort1);
                // bytes[pos++] = (byte) (aShort1 >> 8);
                // bytes[pos++] = (byte) (this.primitiveValues[i]);
                //
                // break;

                case TYPE_FLOAT:
                    final int intBits = (int) (this.primitiveValues[i]);
                    bytes[pos++] = (byte) (intBits);
                    bytes[pos++] = (byte) (intBits >> 8);
                    bytes[pos++] = (byte) (intBits >> 16);
                    bytes[pos++] = (byte) (intBits >> 24);
                    break;

                case TYPE_LONG:
                    final long aLong = this.primitiveValues[i];
                    bytes[pos++] = (byte) (aLong);
                    bytes[pos++] = (byte) (aLong >> 8);
                    bytes[pos++] = (byte) (aLong >> 16);
                    bytes[pos++] = (byte) (aLong >> 24);
                    bytes[pos++] = (byte) (aLong >> 32);
                    bytes[pos++] = (byte) (aLong >> 40);
                    bytes[pos++] = (byte) (aLong >> 48);
                    bytes[pos++] = (byte) (aLong >> 56);
                    break;

                case TYPE_STRING_ISO_8859_1:
                    final String aString = (String) this.objectValues[i];
                    if (aString != null) {
                        final int length = aString.length();
                        pos = writeVariableSize(bytes, pos - 1, length);
                        for (int j = 0; j < length; j++) {
                            bytes[pos++] = (byte) aString.charAt(j);
                        }
                    }
                    break;

                default:
                    break;
                }
            } else {
                consecutiveNullKey++;
            }
        }
        return pos;

    }

    /**
     * writeVariableSize.
     * 
     * @param bytes
     * @param pos
     * @param length
     * @return
     */
    private final int writeVariableSize(final byte[] bytes, int pos, final int length) {
        if (length < SIZE_ENCODED_IN_A_BIT) {
            bytes[pos++] |= (byte) length;
        } else {
            final boolean isEncodedInAnInt = (length > Byte.MAX_VALUE);
            bytes[pos++] |= (byte) ((isEncodedInAnInt) ? SIZE_ENCODED_IN_AN_INT : SIZE_ENCODED_IN_A_BIT);
            bytes[pos++] = (byte) (length);
            if (isEncodedInAnInt) {
                bytes[pos++] = (byte) (length >> 8);
                bytes[pos++] = (byte) (length >> 16);
                bytes[pos++] = (byte) (length >> 24);
            }
        }
        return pos;
    }

    /**
     * getBytesSize.
     * 
     * @return size
     */
    // TODO to optimize
    private final int getBytesSize() {
        int size = 0;
        int consecutiveNullKey = 0;
        for (int i = 0; i < this.types.length; i++) {
            if (this.types[i] != TYPE_NULL_KEY) {
                // TODO manage all size
                size += getValueSize(i);
                // (this.types[i] & SIZE_MASK) + 1;
                if (consecutiveNullKey != 0) {
                    size += getVariableSize(consecutiveNullKey);
                    consecutiveNullKey = 0;
                }
            } else {
                consecutiveNullKey++;
            }
        }
        return size;
    }

    /**
     * getVariableSize.
     * 
     * @param size
     * @return
     */
    private final int getVariableSize(final int size) {
        return (size < SIZE_ENCODED_IN_A_BIT) ? 1 : (size <= Byte.MAX_VALUE) ? 2 : 5;
    }

    /**
     * getValueSize.
     * 
     * @param key
     * @return
     */
    // TODO to optimize
    private final int getValueSize(final int key) {
        final int sizeMask = (this.types[key] & SIZE_MASK);
        // Fixed value
        if (sizeMask != 0) {
            return sizeMask + 1;
            //  Non Fixed value
        } else {
            switch (this.types[key]) {
            case TYPE_STRING_ISO_8859_1:
                final int length = ((String) this.objectValues[key]).length();
                return getVariableSize(length) + length;

            default:
                return 0;
            }
        }
    }

    /**
     * increaseKeyMax.
     * 
     * @param keyMax
     */
    private final void increaseKeyMax(final int keyMax) {
        final byte[] types = new byte[keyMax + 1];
        final long[] primitiveValues = new long[keyMax + 1];
        final Object[] objectValues = new Object[keyMax + 1];
        System.arraycopy(this.types, 0, types, 0, this.types.length);
        System.arraycopy(this.primitiveValues, 0, primitiveValues, 0, this.primitiveValues.length);
        System.arraycopy(this.objectValues, 0, objectValues, 0, this.objectValues.length);
    }

}
