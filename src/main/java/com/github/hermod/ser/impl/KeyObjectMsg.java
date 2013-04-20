package com.github.hermod.ser.impl;

import static com.github.hermod.ser.Types.ARRAY_FIXED_VALUE_TYPE;
import static com.github.hermod.ser.Types.ARRAY_VARIABLE_VALUE_TYPE;
import static com.github.hermod.ser.Types.DECIMAL_TYPE;
import static com.github.hermod.ser.Types.INTEGER_TYPE;
import static com.github.hermod.ser.Types.MSG_TYPE;
import static com.github.hermod.ser.Types.NULL_TYPE;
import static com.github.hermod.ser.Types.STRING_ISO_8859_1_TYPE;
import static com.github.hermod.ser.Types.STRING_UTF_16_TYPE;
import static com.github.hermod.ser.Types.TYPE_MASK;
import static com.github.hermod.ser.Types.UTF_16_CHARSET;
import static com.github.hermod.ser.impl.Msgs.BYTE_TYPE;
import static com.github.hermod.ser.impl.Msgs.DEFAULT_MAX_KEY;
import static com.github.hermod.ser.impl.Msgs.DEFAULT_VALUE;
import static com.github.hermod.ser.impl.Msgs.DOUBLE_TYPE;
import static com.github.hermod.ser.impl.Msgs.DOZENS;
import static com.github.hermod.ser.impl.Msgs.EIGHT;
import static com.github.hermod.ser.impl.Msgs.FIFTY_SIX;
import static com.github.hermod.ser.impl.Msgs.FIVE;
import static com.github.hermod.ser.impl.Msgs.FIVE_BITS_DECIMAL_TYPE;
import static com.github.hermod.ser.impl.Msgs.FLOAT_TYPE;
import static com.github.hermod.ser.impl.Msgs.FORCE_ENCODING_ZERO_ON_2BITS;
import static com.github.hermod.ser.impl.Msgs.FORTY;
import static com.github.hermod.ser.impl.Msgs.FORTY_EIGHT;
import static com.github.hermod.ser.impl.Msgs.FOUR;
import static com.github.hermod.ser.impl.Msgs.INT_TYPE;
import static com.github.hermod.ser.impl.Msgs.LENGTH_ENCODED_IN_AN_INT;
import static com.github.hermod.ser.impl.Msgs.LENGTH_ENCODED_IN_A_BIT;
import static com.github.hermod.ser.impl.Msgs.LENGTH_MASK;
import static com.github.hermod.ser.impl.Msgs.LONG_TYPE;
import static com.github.hermod.ser.impl.Msgs.ZERO;
import static com.github.hermod.ser.impl.Msgs.ONE;
import static com.github.hermod.ser.impl.Msgs.SEVEN;
import static com.github.hermod.ser.impl.Msgs.SHORT_TYPE;
import static com.github.hermod.ser.impl.Msgs.SIX;
import static com.github.hermod.ser.impl.Msgs.SIXTEEN;
import static com.github.hermod.ser.impl.Msgs.THIRTY_TWO;
import static com.github.hermod.ser.impl.Msgs.THREE;
import static com.github.hermod.ser.impl.Msgs.TWENTY_FOUR;
import static com.github.hermod.ser.impl.Msgs.TWO;
import static com.github.hermod.ser.impl.Msgs.XFF;
import static com.github.hermod.ser.EPrecision.HALF;


import java.nio.ByteBuffer;

import com.github.hermod.ser.EPrecision;
import com.github.hermod.ser.EType;
import com.github.hermod.ser.IByteBufferSerializable;
import com.github.hermod.ser.IBytesSerializable;
import com.github.hermod.ser.IMsg;
import com.github.hermod.ser.Types;

/**
 * <p>KeyObjectMsg. </p>
 * 
 * @author anavarro - Jan 21, 2013
 * 
 */
public class KeyObjectMsg implements IMsg, IBytesSerializable, IByteBufferSerializable {

    private byte[] types;
    private long[] primitiveValues;
    private Object[] objectValues;

    
    /**
     * ERROR_WHEN_KEY_NOT_PRESENT
     */
    private static final String ERROR_WHEN_KEY_NOT_PRESENT = "The key=%s is not present for type asked."; 
    /**
     * Constructor.
     * 
     */
    public KeyObjectMsg() {
        this(DEFAULT_MAX_KEY);
    }

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
     * Constructor.
     *
     * @param aMsg
     */
    public KeyObjectMsg(final IMsg aMsg) {
        if (aMsg instanceof KeyObjectMsg) {
            final KeyObjectMsg keyObjectMsg = (KeyObjectMsg) aMsg;
            final int length = keyObjectMsg.types.length;
            this.types = new byte[length];
            this.primitiveValues = new long[length];
            this.objectValues = new Object[length];
            System.arraycopy(keyObjectMsg.types, 0, this.types, 0, length);
            System.arraycopy(keyObjectMsg.primitiveValues, 0, this.primitiveValues, 0, length);
            System.arraycopy(keyObjectMsg.objectValues, 0, this.objectValues, 0, length);
        } else {
            // TODO to optimize with getType
            if (aMsg != null) {
                final int[] keys = aMsg.getKeys();
                for (int i = 0; i < keys.length; i++) {
                    set(keys[i], aMsg.getAsObject(keys[i]));
                }
            }
        }
    }
    
    
    /**
     * clear.
     * 
     * @param aKeyMax
     */
    private void removeAll(final int aKeyMax) {
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

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#clear()
     */
    @Override
    public final void removeAll() {
        for (int i = 0; i < this.primitiveValues.length; i++) {
            this.types[i] = 0;
            this.primitiveValues[i] = 0;
            this.objectValues[i] = null;
        }
    }

    /**
     * increaseKeyMax.
     * 
     * @param keyMax
     */
    private void increaseKeyMax(final int keyMax) {
        if (keyMax < 0) {
            throw new IllegalArgumentException("The maxKey=" + keyMax + " must be positive.");
        } else {
            final byte[] destTypes = new byte[keyMax + 1];
            final long[] destPrimitiveValues = new long[keyMax + 1];
            final Object[] destObjectValues = new Object[keyMax + 1];
            System.arraycopy(this.types, 0, destTypes, 0, this.types.length);
            System.arraycopy(this.primitiveValues, 0, destPrimitiveValues, 0, this.primitiveValues.length);
            System.arraycopy(this.objectValues, 0, destObjectValues, 0, this.objectValues.length);
            this.types = destTypes;
            this.primitiveValues = destPrimitiveValues;
            this.objectValues = destObjectValues;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#contains(int)
     */
    @Override
    public final boolean contains(final int aKey) {
        try {
            return (this.types[aKey] != 0) ? true : false;
        } catch (final ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#getAsByte(int)
     */
    @Override
    public final boolean getAsBoolean(final int aKey) {
        try {
            if (this.types[aKey] == BYTE_TYPE) {
                return (this.primitiveValues[aKey] == 0) ? false : true;
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
        }
        throw new IllegalArgumentException(String.format(ERROR_WHEN_KEY_NOT_PRESENT, aKey));
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#getAsByte(int)
     */
    @Override
    public final byte getAsByte(final int aKey) {
        try {
            if (this.types[aKey] == BYTE_TYPE) {
                return (byte) this.primitiveValues[aKey];
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
        }
        throw new IllegalArgumentException(String.format(ERROR_WHEN_KEY_NOT_PRESENT, aKey));
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#getAsShort(int)
     */
    @Override
    public final short getAsShort(final int aKey) {
        try {
            if (this.types[aKey] == BYTE_TYPE || this.types[aKey] == SHORT_TYPE) {
                return (short) this.primitiveValues[aKey];
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
        }
        throw new IllegalArgumentException(String.format(ERROR_WHEN_KEY_NOT_PRESENT, aKey));
    }
    
    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#getAsInt(int)
     */
    @Override
    public final int getAsInt(final int aKey) {
        try {
            if (this.types[aKey] == BYTE_TYPE || this.types[aKey] == SHORT_TYPE || this.types[aKey] == INT_TYPE) {
                return (int) this.primitiveValues[aKey];
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
        }
        throw new IllegalArgumentException(String.format(ERROR_WHEN_KEY_NOT_PRESENT, aKey));
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#getAsLong(int)
     */
    @Override
    public final long getAsLong(final int aKey) {
        try {
            if ((this.types[aKey] & TYPE_MASK) == INTEGER_TYPE) {
                return (long) this.primitiveValues[aKey];
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
        }
        throw new IllegalArgumentException(String.format(ERROR_WHEN_KEY_NOT_PRESENT, aKey));
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#getAsFloat(int)
     */
    @Override
    public final float getAsFloat(final int aKey) {
        try {
            // TODO do some stuff if the type TYPE_5BITS_DECIMAL
            if (this.types[aKey] == FLOAT_TYPE) {
                return Float.intBitsToFloat((int) this.primitiveValues[aKey]);
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
        }
        throw new IllegalArgumentException(String.format(ERROR_WHEN_KEY_NOT_PRESENT, aKey));
    }

    /**
     * getAsDouble
     * 
     * @param aKey
     * @return
     */
    @Override
    public final double getAsDouble(final int aKey) {
        try {
            switch (this.types[aKey]) {
            // case TYPE_3BITS_DECIMAL:
            // return (this.primitiveValues[aKey] >> 8) / DOZENS[(int) (XFF &
            // this.primitiveValues[aKey])];
            case FIVE_BITS_DECIMAL_TYPE:
                // return Precisions.fromNbDigit((int) (XFF & this.primitiveValues[aKey])).calculateDouble((this.primitiveValues[aKey] >> 8));
                return getAs5BitsDecimal(aKey);
                // return (this.primitiveValues[aKey] >> 8) / DOZENS[(int) (XFF & this.primitiveValues[aKey])];
            case DOUBLE_TYPE:
                return Double.longBitsToDouble(this.primitiveValues[aKey]);
            case FLOAT_TYPE:
                return Float.intBitsToFloat((int) this.primitiveValues[aKey]);
            default:
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
        }
        throw new IllegalArgumentException(String.format(ERROR_WHEN_KEY_NOT_PRESENT, aKey));
    }

    /**
     * getAs5BitsDecimal.
     * 
     * @param aKey
     * @return
     */
    private double getAs5BitsDecimal(final int aKey) {
        final int digit = (int) (XFF & this.primitiveValues[aKey]);
        final double allDigit = (this.primitiveValues[aKey] >> EIGHT);
        // return Precisions.fromNbDigit((int) (XFF & this.primitiveValues[aKey])).calculateDouble((this.primitiveValues[aKey] >> EIGHT));
        switch (digit) {
        case ZERO:
            return allDigit;
        case ONE:
            return allDigit * EPrecision.TENTHS.getPrecision();
        case TWO:
            return allDigit * EPrecision.HUNDREDTHS.getPrecision();
        case THREE:
            return allDigit * EPrecision.THOUSANDTHS.getPrecision();
        case FOUR:
            return allDigit * EPrecision.TEN_THOUSANDTHS.getPrecision();
        case FIVE:
            return allDigit * EPrecision.HUNDRED_THOUSANDTHS.getPrecision();
        case SIX:
            return allDigit * EPrecision.MILLIONTHS.getPrecision();
        case SEVEN:
            return allDigit * EPrecision.TEN_MILLIONTHS.getPrecision();
        case EIGHT:
            return allDigit * EPrecision.HUNDRED_MILLIONTHS.getPrecision();
        default:
            return EPrecision.valueOf(digit).calculateIntegerMantissa(allDigit);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#getAsString(int)
     */
    @Override
    public final String getAsString(final int aKey) {
        try {
            return ((this.types[aKey] & TYPE_MASK) == STRING_ISO_8859_1_TYPE) ? (String) this.objectValues[aKey] : null;
        } catch (final ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser3.intmap.ReadIntMap#getAsMap(int)
     */
    @Override
    public final IMsg getAsMsg(final int aKey) {
        try {
            
            return ((this.types[aKey] & TYPE_MASK) == MSG_TYPE && this.objectValues[aKey] != null) ? new KeyObjectMsg((IMsg) this.objectValues[aKey]) : null;
        } catch (final ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#getAsMsgInto(int, com.github.hermod.ser.IMsg)
     */
    @Override
    public final void getAsMsg(final int aKey, final IMsg destValue) {
        try {
            if ((this.types[aKey] & TYPE_MASK) == MSG_TYPE) {
                destValue.setAll((IMsg) this.objectValues[aKey]);
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#getAsObject(int)
     */
    @Override
    public final Object getAsObject(final int aKey) {
        return getAsObject(aKey, Object.class);    
        }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#getAsObject(int, java.lang.Class)
     */
    @Override
    public final <T> T getAsObject(final int aKey, final Class<T> aClazz) {
        try {
            final byte type = this.types[aKey];
            switch (type) {
            case BYTE_TYPE:
                return aClazz.cast(getAsByte(aKey));

            case SHORT_TYPE:
                return aClazz.cast(getAsShort(aKey));

            case INT_TYPE:
                return aClazz.cast(getAsInt(aKey));

            case LONG_TYPE:
                return aClazz.cast(getAsLong(aKey));

            case INTEGER_TYPE:
                return aClazz.cast((Integer) null);

            case FLOAT_TYPE:
                return aClazz.cast(getAsFloat(aKey));

            case DOUBLE_TYPE:
                return aClazz.cast(getAsDouble(aKey));

            case FIVE_BITS_DECIMAL_TYPE:
                return aClazz.cast(getAsDouble(aKey));

            case DECIMAL_TYPE:
                return aClazz.cast((Double) null);

            case STRING_ISO_8859_1_TYPE:
                return aClazz.cast(getAsString(aKey));

            case STRING_UTF_16_TYPE:
                return aClazz.cast(getAsString(aKey));

            case MSG_TYPE:
                return aClazz.cast(getAsMsg(aKey));

            case ARRAY_FIXED_VALUE_TYPE:
                return aClazz.cast(getAsObject(aKey));
                
            case ARRAY_VARIABLE_VALUE_TYPE:
                return aClazz.cast(getAsObject(aKey));

            default:
                return null;
            }

        } catch (final ArrayIndexOutOfBoundsException e) {
            return null;
        }
    
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#getAsBooleans(int)
     */
    @Override
    public final boolean[] getAsBooleans(final int aKey) {
        try {
            if ((this.types[aKey] & TYPE_MASK) == ARRAY_FIXED_VALUE_TYPE && (this.objectValues[aKey] instanceof boolean[])) {
                final boolean[] booleans =  (boolean[]) this.objectValues[aKey];
                final boolean[] results = new boolean[booleans.length];
                System.arraycopy(booleans, 0, results, 0, booleans.length);
                return results;
            } else {
                return null;
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#getAsBytes(int)
     */
    @Override
    public final byte[] getAsBytes(final int aKey) {
        try {
            if ((this.types[aKey] & TYPE_MASK) == ARRAY_FIXED_VALUE_TYPE && (this.objectValues[aKey] instanceof byte[])) {
                final byte[] bytes = (byte[]) this.objectValues[aKey];
                final byte[] results = new byte[bytes.length];
                System.arraycopy(bytes, 0, results, 0, bytes.length);
                return results;
            } else {
                return null;
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#getAsShorts(int)
     */
    @Override
    public final short[] getAsShorts(final int aKey) {
        try {
            if ((this.types[aKey] & TYPE_MASK) == ARRAY_FIXED_VALUE_TYPE && (this.objectValues[aKey] instanceof short[])) {
                final short[] shorts = (short[]) this.objectValues[aKey];
                final short[] results = new short[shorts.length];
                System.arraycopy(shorts, 0, results, 0, shorts.length);
                return results;
            } else {
                return null;
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#getAsInts(int)
     */
    @Override
    public final int[] getAsInts(final int aKey) {
        try {
            if (((this.types[aKey] & TYPE_MASK) == ARRAY_FIXED_VALUE_TYPE && (this.objectValues[aKey] instanceof int[]))) {
                final int[] ints = (int[]) this.objectValues[aKey];
                final int[] results = new int[ints.length];
                System.arraycopy(ints, 0, results, 0, ints.length);
                return results;
            } else {
                return null;
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#getAsLongs(int)
     */
    @Override
    public final long[] getAsLongs(final int aKey) {
        try {
            if ((this.types[aKey] & TYPE_MASK) == ARRAY_FIXED_VALUE_TYPE && (this.objectValues[aKey] instanceof long[])) {
                final long[] longs = (long[]) this.objectValues[aKey];
                final long[] results = new long[longs.length];
                System.arraycopy(longs, 0, results, 0, longs.length);
                return results;
            } else {
                return null;
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#getAsFloats(int)
     */
    @Override
    public final float[] getAsFloats(final int aKey) {
        try {
            if (((this.types[aKey] & TYPE_MASK) == ARRAY_FIXED_VALUE_TYPE && (this.objectValues[aKey] instanceof float[]))) {
                final float[] floats = (float[]) this.objectValues[aKey];
                final float[] results = new float[floats.length];
                System.arraycopy(floats, 0, results, 0, floats.length);
                return results;
            } else {
                return null;
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#getAsDoubles(int)
     */
    @Override
    public final double[] getAsDoubles(final int aKey) {
        try {
            if ((this.types[aKey] & TYPE_MASK) == ARRAY_FIXED_VALUE_TYPE && (this.objectValues[aKey] instanceof double[])) {
                final double[] doubles = (double[]) this.objectValues[aKey];
                final double[] results = new double[doubles.length];
                System.arraycopy(doubles, 0, results, 0, doubles.length);
                return results;
            } else {
                return null;
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#getAsStrings(int)
     */
    @Override
    public final String[] getAsStrings(final int aKey) {
        try {
            if (((this.types[aKey] & TYPE_MASK) == ARRAY_VARIABLE_VALUE_TYPE && (this.objectValues[aKey] instanceof String[]))) {
                final String[] strings = (String[]) this.objectValues[aKey];
                final String[] results = new String[strings.length];
                System.arraycopy(strings, 0, results, 0, strings.length);
                return results;
            } else {
                return null;
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#getAsMsgs(int)
     */
    @Override
    public final IMsg[] getAsMsgs(final int aKey) {
        try {
            if ((this.types[aKey] & TYPE_MASK) == ARRAY_VARIABLE_VALUE_TYPE && (this.objectValues[aKey] instanceof IMsg[])) {
                final IMsg[] msgs = (IMsg[]) this.objectValues[aKey];
                final IMsg[] results = new KeyObjectMsg[msgs.length];
                for (int i = 0; i < msgs.length; i++) {
                    results[i] = new KeyObjectMsg(msgs[i]);
                }
                return results;
            }
            return ((this.types[aKey] & TYPE_MASK) == ARRAY_VARIABLE_VALUE_TYPE && (this.objectValues[aKey] instanceof IMsg[])) ? (IMsg[]) this.objectValues[aKey] : null;
        } catch (final ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#getAsMsgs(int, com.github.hermod.ser.IMsg[])
     */
    @Override
    public final void getAsMsgs(final int aKey, IMsg... aDestMsgs) {
        try {
            if ((this.types[aKey] & TYPE_MASK) == ARRAY_VARIABLE_VALUE_TYPE && (this.objectValues[aKey] instanceof IMsg[])) {
                final IMsg[] msgs = (IMsg[]) this.objectValues[aKey];
                if (msgs.length == aDestMsgs.length) {
                    for (int i = 0; i < msgs.length; i++) {
                        aDestMsgs[i].setAll(msgs[i]);
                    }
                } else {
                    throw new IllegalArgumentException("The destMsgs with length=" + aDestMsgs.length + " must be have the same field array length=" + msgs.length + ". Use getArrayLength() to know the length before call this method.");
                }
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#getAll()
     */
    @Override
    public final IMsg getAll() {
        // TODO to implement
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#getType(int)
     */
    @Override
    public final EType getType(final int aKey) {
        try {
            return EType.valueOf((byte) (this.types[aKey] & TYPE_MASK));
        } catch (ArrayIndexOutOfBoundsException e) {
            return EType.NULL;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#getTypeAsByte(int)
     */
    @Override
    public final byte getTypeAsByte(final int aKey) {
        try {
            return (byte) (this.types[aKey] & TYPE_MASK);
        } catch (ArrayIndexOutOfBoundsException e) {
            return Types.NULL_TYPE;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#isArray(int)
     */
    @Override
    public final boolean isArray(final int aKey) {
        try {
            return (((this.types[aKey] & TYPE_MASK) == ARRAY_FIXED_VALUE_TYPE) || ((this.types[aKey] & TYPE_MASK) == ARRAY_VARIABLE_VALUE_TYPE)) ? true
                    : false;
        } catch (final ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#getArrayLength(int)
     */
    @Override
    public int getArrayLength(final int aKey) {
        try {
            return ((((this.types[aKey] & TYPE_MASK) == ARRAY_FIXED_VALUE_TYPE) || ((this.types[aKey] & TYPE_MASK) == ARRAY_VARIABLE_VALUE_TYPE)) && ((this.objectValues[aKey] instanceof Object[]))) ? ((Object[]) this.objectValues[aKey]).length
                    : 0;
        } catch (final ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser3.intmap.ReadIntMap#getKeys()
     */
    @Override
    public final int[] getKeys() {
        final int[] keys = new int[countKeys()];
        int index = 0;
        for (int i = 0; i < this.types.length; i++) {
            if (this.types[i] != NULL_TYPE) {
                keys[index++] = i;
            }
        }
        return keys;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#countKeys()
     */
    @Override
    public int countKeys() {
        int nbKey = 0;
        for (int i = 0; i < this.types.length; i++) {
            if (this.types[i] != NULL_TYPE) {
                nbKey++;
            }
        }
        return nbKey;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.types.length; i++) {
            if (this.types[i] != NULL_TYPE) {
                return false;
            }
        }
        return true;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#set(int, boolean)
     */
    @Override
    public final void set(final int aKey, final boolean aBoolean) {
        try {
            this.primitiveValues[aKey] = aBoolean ? 1 : 0;
            this.types[aKey] = BYTE_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aBoolean);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#set(int, byte)
     */
    @Override
    public final void set(final int aKey, final byte aByte) {
        try {
            this.primitiveValues[aKey] = aByte;
            this.types[aKey] = BYTE_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aByte);
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
            this.primitiveValues[aKey] = aShort;
            this.types[aKey] = (aShort == (byte) aShort) ? BYTE_TYPE : SHORT_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aShort);
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
            this.primitiveValues[aKey] = aInt;
            this.types[aKey] = (aInt == (byte) aInt) ? BYTE_TYPE : (aInt == (short) aInt) ? SHORT_TYPE : INT_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aInt);
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
            this.primitiveValues[aKey] = aLong;
            this.types[aKey] = LONG_TYPE;
            this.types[aKey] = (aLong == (byte) aLong) ? BYTE_TYPE : (aLong == (short) aLong) ? SHORT_TYPE : (aLong == (int) aLong) ? INT_TYPE
                    : LONG_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aLong);
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
            this.types[aKey] = FLOAT_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aFloat);
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
            final boolean isEncodeableInAFloat = (aDouble == (float) aDouble) ? true : false;
            this.primitiveValues[aKey] = (isEncodeableInAFloat) ? Float.floatToIntBits((float) aDouble) : Double.doubleToLongBits(aDouble);
            this.types[aKey] = (isEncodeableInAFloat) ? FLOAT_TYPE : DOUBLE_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aDouble);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.WriteIntMap#set(int, double, int)
     */
    @Override
    public final void set(final int aKey, final double aDouble, final int nbDigit) {
        try {
            final double d = (aDouble * DOZENS[nbDigit]) + HALF;

            // precision.HUNDREDTHS.calculateIntegerMantissa(aValue);
            // if (d >= Short.MIN_VALUE && d <= Short.MAX_VALUE)
            // {
            // this.primitiveValues[aKey] = (nbDigit) | (((short) (d)) << EIGHT);
            // this.types[aKey] = TYPE_3BITS_DECIMAL;
            // }
            // else
            {
                if (d >= Integer.MIN_VALUE && d <= Integer.MAX_VALUE) {
                    try {
                        this.primitiveValues[aKey] = (nbDigit) | (((long) (d)) << EIGHT);
                        this.types[aKey] = FIVE_BITS_DECIMAL_TYPE;
                    } catch (final ArrayIndexOutOfBoundsException e) {
                        increaseKeyMax(aKey);
                        set(aKey, aDouble, nbDigit);
                    }
                } else {
                    this.set(aKey, aDouble);
                }
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("NbDigit must be between 0 and " + (DOZENS.length - 1), e);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#set(int, double, com.github.hermod.ser.EPrecision)
     */
    @Override
    public final void set(final int aKey, final double aDouble, final EPrecision aPrecision) {
        final double mantissa = aPrecision.calculateIntegerMantissa(aDouble);
        if (!Double.isNaN(mantissa)) {
            try {
                this.primitiveValues[aKey] = (aPrecision.getNbDigit()) | (((long) (mantissa)) << EIGHT);
                this.types[aKey] = FIVE_BITS_DECIMAL_TYPE;
            } catch (final ArrayIndexOutOfBoundsException e) {
                increaseKeyMax(aKey);
                set(aKey, aDouble, aPrecision.getNbDigit());
            }
        } else {
            this.set(aKey, aDouble);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.WriteIntMap#set(int, java.lang.String)
     */
    @Override
    public final void set(final int aKey, final String aString) {
        //TODO
        try {
            this.objectValues[aKey] = aString;
            this.types[aKey] = STRING_ISO_8859_1_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aString);
        }
    }
    
    /**
     * set.
     *
     * @param aKey
     * @param aString
     * @param stringType
     */
    private void set(final int aKey, final String aString, final byte stringType) {
        try {
            this.objectValues[aKey] = aString;
            this.types[aKey] = stringType;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aString);
        }
    }
    

    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.IMsg#set(int, java.lang.String, boolean)
     */
    @Override
    public final void set(final int aKey, final String aString, final boolean aForceIso88591Charset) {
        if (aForceIso88591Charset) { 
            set(aKey, aString, STRING_ISO_8859_1_TYPE);
        } else {
            set(aKey, aString, STRING_UTF_16_TYPE);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#set(int, com.github.hermod.ser.IMsg)
     */
    @Override
    public final void set(final int aKey, final IMsg aMsg) {
        try {
            this.objectValues[aKey] = aMsg;
            this.types[aKey] = MSG_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aMsg);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#set(int, java.lang.Object[])
     */
    public final void set(final int aKey, final Object[] aObjectArray) {
        try {
            this.objectValues[aKey] = aObjectArray;
            this.types[aKey] = ARRAY_VARIABLE_VALUE_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aObjectArray);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#set(int, java.lang.Object)
     */
    @Override
    public final void set(final int aKey, final Object aObject) {
        try {

            if (aObject instanceof Byte | aObject instanceof Short | aObject instanceof Integer | aObject instanceof Long) {
                if (aObject != null) {
                    set(aKey, ((Number) aObject).longValue());
                } else {
                    this.types[aKey] = INTEGER_TYPE;
                    this.objectValues[aKey] = DEFAULT_VALUE;
                }
            } else if (aObject instanceof Float | aObject instanceof Double) {
                if (aObject != null) {
                    set(aKey, ((Number) aObject).doubleValue());
                } else {
                    this.types[aKey] = DECIMAL_TYPE;
                    this.objectValues[aKey] = DEFAULT_VALUE;
                }
            } else if (aObject instanceof String) {
                set(aKey, (String) aObject);
            } else if (aObject instanceof IMsg) {
                set(aKey, (IMsg) aObject);
            } else if (aObject instanceof Object[]) {
                set(aKey, (Object[]) aObject);
            } else if (aObject instanceof Boolean) {
                set(aKey, (Boolean) aObject);
            } else {
                throw new IllegalArgumentException("Impossible to set this type of value=" + aObject.getClass());
            }
            this.objectValues[aKey] = aObject;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aObject);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#setAll(com.github.hermod.ser.IMsg)
     */
    @Override
    public final void setAll(final IMsg aMsg) {
        // TODO to optimize with getType
        if (aMsg != null) {
            final int[] keys = aMsg.getKeys();
            for (int i = 0; i < keys.length; i++) {
                set(keys[i], aMsg.getAsObject(keys[i]));
            }
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.IMsg#set(int, boolean[])
     */
    @Override
    public final void set(final int aKey, final boolean... aBooleans) {
        try {
            this.objectValues[aKey] = aBooleans;
            this.types[aKey] = ARRAY_FIXED_VALUE_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aBooleans);
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.IMsg#set(int, byte[])
     */
    @Override
    public final void set(final int aKey, final byte... aBytes) {
        try {
            this.objectValues[aKey] = aBytes;
            this.types[aKey] = ARRAY_FIXED_VALUE_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aBytes);
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.IMsg#set(int, short[])
     */
    @Override
    public final void set(final int aKey, final short... aShorts) {
        try {
            this.objectValues[aKey] = aShorts;
            this.types[aKey] = ARRAY_FIXED_VALUE_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aShorts);
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.IMsg#set(int, int[])
     */
    @Override
    public final void set(final int aKey, final int... aInts) {
        try {
            this.objectValues[aKey] = aInts;
            this.types[aKey] = ARRAY_FIXED_VALUE_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aInts);
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.IMsg#set(int, long[])
     */
    @Override
    public final void set(final int aKey, final long... aLongs) {
        try {
            this.objectValues[aKey] = aLongs;
            this.types[aKey] = ARRAY_FIXED_VALUE_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aLongs);
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.IMsg#set(int, float[])
     */
    @Override
    public final void set(final int aKey, final float... aFloats) {
        try {
            this.objectValues[aKey] = aFloats;
            this.types[aKey] = ARRAY_FIXED_VALUE_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aFloats);
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.IMsg#set(int, double[])
     */
    @Override
    public final void set(final int aKey, final double... aDoubles) {
        try {
            this.objectValues[aKey] = aDoubles;
            this.types[aKey] = ARRAY_FIXED_VALUE_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aDoubles);
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.IMsg#set(int, java.lang.String[], boolean)
     */
    @Override
    public final void set(final int aKey, final String[] aStrings, final boolean aForceIso88591Charset) {
        //TODO copy or not, Caution pb with aForceIso88591Charset, must transform String[] with the good charset
        try {
            this.objectValues[aKey] = aStrings;
            this.types[aKey] = ARRAY_VARIABLE_VALUE_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aStrings);
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.IMsg#set(int, com.github.hermod.ser.IMsg[])
     */
    @Override
    public void set(final int aKey, final IMsg... aMsgs) {
        try {
            this.objectValues[aKey] = aMsgs;
            this.types[aKey] = ARRAY_VARIABLE_VALUE_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aMsgs);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsg#remove(int)
     */
    @Override
    public final void remove(final int... aKeys) {
        for (final int aKey : aKeys) {
            try {
                this.types[aKey] = NULL_TYPE;
                this.objectValues[aKey] = null;
                this.primitiveValues[aKey] = DEFAULT_VALUE;
            } catch (final ArrayIndexOutOfBoundsException e) {
                increaseKeyMax(aKey);
                remove(aKey);
            }
        }
    }

    /**
     * writeVariableSize.
     * 
     * @param bytes
     * @param pos
     * @param length
     * @param forceEncodingZeroOn2Bits TODO
     * @return
     */
    //TODO potential for to 1, 2, 4 bits
    private int writeVariableLength(final byte[] bytes, int pos, final int length, boolean forceEncodingZeroOn2Bits) {
        if (length < LENGTH_ENCODED_IN_A_BIT && !(forceEncodingZeroOn2Bits && length == 0)) {
            bytes[pos++] |= (byte) length;
        } else {
            final boolean isEncodedInAnInt = (length > Byte.MAX_VALUE);
            bytes[pos++] |= (byte) ((isEncodedInAnInt) ? LENGTH_ENCODED_IN_AN_INT : LENGTH_ENCODED_IN_A_BIT);
            bytes[pos++] = (byte) (length);
            if (isEncodedInAnInt) {
                bytes[pos++] = (byte) (length >> EIGHT);
                bytes[pos++] = (byte) (length >> SIXTEEN);
                bytes[pos++] = (byte) (length >> TWENTY_FOUR);
            }
        }
        return pos;
    }
    
    
    /**
     * writeVariableLength2.
     *
     * @param bytes
     * @param pos
     * @param length
     * @param forceEncodingAtLeastOn
     * @return
     */
    private int writeVariableLength(final byte[] bytes, int pos, final int length, final int forceEncodingAtLeastOn) {
        //TODO implement it
        if (length < LENGTH_ENCODED_IN_A_BIT && !(forceEncodingAtLeastOn == 0 && length == 0)) {
            bytes[pos++] |= (byte) length;
        } else {
            final boolean isEncodedInAnInt = (length > Byte.MAX_VALUE);
            bytes[pos++] |= (byte) ((isEncodedInAnInt) ? LENGTH_ENCODED_IN_AN_INT : LENGTH_ENCODED_IN_A_BIT);
            bytes[pos++] = (byte) (length);
            if (isEncodedInAnInt) {
                bytes[pos++] = (byte) (length >> EIGHT);
                bytes[pos++] = (byte) (length >> SIXTEEN);
                bytes[pos++] = (byte) (length >> TWENTY_FOUR);
            }
        }
        return pos;
    }

    /**
     * <p>getVariableLength (with the length of the type).</p>
     * 
     * @param length
     * @return
     */
    private int getVariableLength(final int length, final boolean forceEncodingZeroOn2Bits) {
        return (length < LENGTH_ENCODED_IN_A_BIT && !(forceEncodingZeroOn2Bits && length == 0)) ? ONE : (length <= Byte.MAX_VALUE) ? TWO : FIVE;
    }
    
    
    /**
     * getVariableLength.
     *
     * @param length
     * @param forceEncodingAtLeastOn
     * @return
     */
    //TODO implement it
    private int getVariableLength(final int length, final int forceEncodingAtLeastOn) {
        return (length < LENGTH_ENCODED_IN_A_BIT && !(forceEncodingAtLeastOn == 0 && length == 0)) ? ONE : (length <= Byte.MAX_VALUE) ? TWO : FIVE;
    }


    /**
     * getValueLength (with the length of the type).
     * 
     * @param key
     * @return
     */
    // TODO to optimize
    private int getValueLength(final int key) {
        final int lengthMask = (this.types[key] & LENGTH_MASK);
        // Fixed value
        if (lengthMask != 0) {
            return lengthMask + 1;
            // Non Fixed value
        } else {
            switch (this.types[key]) {
            // TODO refactor it
            case STRING_ISO_8859_1_TYPE:
                final int stringIso88591length = (this.objectValues[key] != null) ? ((String) this.objectValues[key]).length() : 0;
                return getVariableLength(stringIso88591length, FORCE_ENCODING_ZERO_ON_2BITS) + stringIso88591length;
             
            case STRING_UTF_16_TYPE:
                final int stringUtf16length = (this.objectValues[key] != null) ? ((String) this.objectValues[key]).getBytes(UTF_16_CHARSET).length : 0;
                return getVariableLength(stringUtf16length, FORCE_ENCODING_ZERO_ON_2BITS) + stringUtf16length;
                
            case MSG_TYPE:
                // TODO change to Serializer.getSize();
                final int msgLength = (this.objectValues[key] != null) ? ((IBytesSerializable) this.objectValues[key]).getLength() : 0;
                return getVariableLength(msgLength, FORCE_ENCODING_ZERO_ON_2BITS) + msgLength;

            case ARRAY_FIXED_VALUE_TYPE:
                // TODO Array
                
            case ARRAY_VARIABLE_VALUE_TYPE:
                // TODO Array

            default:
                return 0;
            }
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < this.types.length; i++) {
            if (this.types[i] != NULL_TYPE) {
                sb.append(i);
                sb.append("=");
                // TODO manage better array, double/float, use just get()
                sb.append((this.types[i] == STRING_ISO_8859_1_TYPE || this.types[i] == MSG_TYPE || this.types[i] == ARRAY_FIXED_VALUE_TYPE) ? this.objectValues[i]
                        : this.primitiveValues[i]);
                // if (this.types[i] == BYTE || this.types[i] == SHORT || this.types[i] == INT || this.types[i] == LONG) {
                // sb.append(this.getAsLong(i));
                // } else if (this.types[i] == STRING_ISO_8859_1 || this.types[i] == MSG) {
                // sb.append(this.objectValues[i].toString());
                // } else {
                // if (this.types[i] == ARRAY) {
                // final Object[] objects = (Object[]) this.objectValues[i];
                // sb.append("[");
                // for (final Object object : objects) {
                // sb.append(object);
                // sb.append(",");
                // }
                // sb.deleteCharAt(sb.length() - 1);
                // sb.append("]");
                //
                // } else {
                // sb.append("Type Not Managed");
                // }
                // }
                sb.append(",");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
        return sb.toString();
    }

    /**
     * getLength.
     * 
     * @return length
     */
    @Override
    public final int getLength() {
        int length = 0;
        int consecutiveNullKey = 0;
        for (int i = 0; i < this.types.length; i++) {
            if (this.types[i] != NULL_TYPE) {
                length += getValueLength(i);
                if (consecutiveNullKey != 0) {
                    length += getVariableLength(consecutiveNullKey, false);
                    consecutiveNullKey = 0;
                }
            } else {
                consecutiveNullKey++;
            }
        }
        return length;
    }

    /**
     * serializeToBytes.
     * 
     * @return
     */
    @Override
    public final byte[] serializeToBytes() {
        final int length = this.getLength();
        final byte[] bytes = new byte[length];
        this.serializeToBytes(bytes, 0);
        return bytes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.WriteMsg#writeTo(byte[], int)
     */
    @Override
    public final int serializeToBytes(final byte[] bytes, final int offset) {

        // Calculate length
        // TODO to optimize with a try catch, generally we know (called by serializeToBytes())
        final int length = getLength();
        if (bytes.length - offset < length) {
            throw new IllegalArgumentException("Bytes array too small from the offset");
        }

        int pos = offset;
        int consecutiveNullKey = 0;

        // Write Type / Values
        for (int i = 0; i < this.types.length; i++) {
            if (this.types[i] != NULL_TYPE) {
                if (consecutiveNullKey != 0) {
                    pos = writeVariableLength(bytes, pos, consecutiveNullKey - 1, false);
                    consecutiveNullKey = 0;
                }

                final byte type = this.types[i];
                bytes[pos++] = type;
                switch (type) {
                //  all fixed type
                case BYTE_TYPE:
                    bytes[pos++] = (byte) this.primitiveValues[i];
                    break;

                case SHORT_TYPE:
                    final int aShort = (short) this.primitiveValues[i];
                    bytes[pos++] = (byte) (aShort);
                    bytes[pos++] = (byte) (aShort >> EIGHT);
                    break;

                case INT_TYPE:
                    final int aInt = (int) this.primitiveValues[i];
                    bytes[pos++] = (byte) (aInt);
                    bytes[pos++] = (byte) (aInt >> EIGHT);
                    bytes[pos++] = (byte) (aInt >> SIXTEEN);
                    bytes[pos++] = (byte) (aInt >> TWENTY_FOUR);
                    break;

                case DOUBLE_TYPE:
                    final long longBits = this.primitiveValues[i];
                    bytes[pos++] = (byte) (longBits);
                    bytes[pos++] = (byte) (longBits >> EIGHT);
                    bytes[pos++] = (byte) (longBits >> SIXTEEN);
                    bytes[pos++] = (byte) (longBits >> TWENTY_FOUR);
                    bytes[pos++] = (byte) (longBits >> THIRTY_TWO);
                    bytes[pos++] = (byte) (longBits >> FORTY);
                    bytes[pos++] = (byte) (longBits >> FORTY_EIGHT);
                    bytes[pos++] = (byte) (longBits >> FIFTY_SIX);
                    break;

                case FIVE_BITS_DECIMAL_TYPE:
                    final int anInt = (int) (this.primitiveValues[i] >> EIGHT);
                    bytes[pos++] = (byte) (anInt);
                    bytes[pos++] = (byte) (anInt >> EIGHT);
                    bytes[pos++] = (byte) (anInt >> SIXTEEN);
                    bytes[pos++] = (byte) (anInt >> TWENTY_FOUR);
                    bytes[pos++] = (byte) (this.primitiveValues[i]);
                    break;

                // case 3BITS_DECIMAL:
                // final short aShort1 = (short) (this.primitiveValues[i] >> EIGHT);
                // bytes[pos++] = (byte) (aShort1);
                // bytes[pos++] = (byte) (aShort1 >> EIGHT);
                // bytes[pos++] = (byte) (this.primitiveValues[i]);
                //
                // break;

                case FLOAT_TYPE:
                    final int intBits = (int) (this.primitiveValues[i]);
                    bytes[pos++] = (byte) (intBits);
                    bytes[pos++] = (byte) (intBits >> EIGHT);
                    bytes[pos++] = (byte) (intBits >> SIXTEEN);
                    bytes[pos++] = (byte) (intBits >> TWENTY_FOUR);
                    break;

                case LONG_TYPE:
                    final long aLong = this.primitiveValues[i];
                    bytes[pos++] = (byte) (aLong);
                    bytes[pos++] = (byte) (aLong >> EIGHT);
                    bytes[pos++] = (byte) (aLong >> SIXTEEN);
                    bytes[pos++] = (byte) (aLong >> TWENTY_FOUR);
                    bytes[pos++] = (byte) (aLong >> THIRTY_TWO);
                    bytes[pos++] = (byte) (aLong >> FORTY);
                    bytes[pos++] = (byte) (aLong >> FORTY_EIGHT);
                    bytes[pos++] = (byte) (aLong >> FIFTY_SIX);
                    break;

                case STRING_ISO_8859_1_TYPE:
                    final String aAsciiString = (String) this.objectValues[i];
                    if (aAsciiString != null) {
                        final int stringLength = aAsciiString.length();
                        pos = writeVariableLength(bytes, pos - 1, stringLength, FORCE_ENCODING_ZERO_ON_2BITS);
                        for (int j = 0; j < stringLength; j++) {
                            bytes[pos++] = (byte) aAsciiString.charAt(j);
                        }
                    }
                    break;
                    
                case STRING_UTF_16_TYPE:
                    final String aString = (String) this.objectValues[i];
                    if (aString != null) {
                        final byte[] stringBytes = aString.getBytes(UTF_16_CHARSET);
                        final int stringLength = stringBytes.length;
                        pos = writeVariableLength(bytes, pos - 1, stringLength, FORCE_ENCODING_ZERO_ON_2BITS);
                        for (int j = 0; j < stringLength; j++) {
                            bytes[pos++] = stringBytes[j];
                        }
                    }
                    break;

                case MSG_TYPE:
                    final IMsg aMsg = (IMsg) this.objectValues[i];
                    if (aMsg != null) {
                        final int msgLength = ((IBytesSerializable) aMsg).getLength();
                        pos = writeVariableLength(bytes, pos - 1, msgLength, FORCE_ENCODING_ZERO_ON_2BITS);
                        pos = ((IBytesSerializable) aMsg).serializeToBytes(bytes, pos);
                    }
                    break;

                case ARRAY_FIXED_VALUE_TYPE:
                    //TODO
                    break;
                    
                case ARRAY_VARIABLE_VALUE_TYPE:
                    // TODO
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
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.ISerializable#readFrom(byte[], int, int)
     */

    public final void deserializeFrom(final byte[] bytes, final int offset, final int length) {
        int pos = offset;
        int key = 0;

        // find max key
        // not need for feed
        // Calculate max key value
        // TODO to optimize with a try catch, lastPos and relaunch readFrom
        while (pos < offset + length) {
            if ((bytes[pos] & TYPE_MASK) == NULL_TYPE) {
                final int sizeMask = bytes[pos++] & LENGTH_MASK;
                key += (((sizeMask < LENGTH_ENCODED_IN_A_BIT) ? sizeMask : (sizeMask == LENGTH_ENCODED_IN_A_BIT) ? bytes[pos++]
                        : (bytes[pos++] & XFF) | ((bytes[pos++] & XFF) << EIGHT) | ((bytes[pos++] & XFF) << SIXTEEN) | ((bytes[pos++] & XFF) << TWENTY_FOUR))) + 1;
            } else {
                final int sizeMask = bytes[pos++] & LENGTH_MASK;
                // TODO to optimize
                pos += (((sizeMask < LENGTH_ENCODED_IN_A_BIT) ? sizeMask : (sizeMask == LENGTH_ENCODED_IN_A_BIT) ? bytes[pos] : ((bytes[pos] & XFF)
                        | ((bytes[pos + ONE] & XFF) << EIGHT) | ((bytes[pos + TWO] & XFF) << SIXTEEN) | ((bytes[pos + THREE] & XFF) << TWENTY_FOUR))));
                pos += (sizeMask < LENGTH_ENCODED_IN_A_BIT) ? 0 : (sizeMask == LENGTH_ENCODED_IN_A_BIT) ? ONE : FOUR;

                key++;
            }
        }
        removeAll(key);

        // Decoding
        key = 0;
        pos = offset;
        while (pos < offset + length) {
            final byte type = bytes[pos++];

            // Skip null key
            if ((type & TYPE_MASK) == NULL_TYPE) {
                final int sizeMask = type & LENGTH_MASK;
                key += ((sizeMask < LENGTH_ENCODED_IN_A_BIT) ? sizeMask : (sizeMask == LENGTH_ENCODED_IN_A_BIT) ? bytes[pos++]
                        : (bytes[pos++] & XFF) | ((bytes[pos++] & XFF) << EIGHT) | ((bytes[pos++] & XFF) << SIXTEEN) | ((bytes[pos++] & XFF) << TWENTY_FOUR)) + 1;
            }
            // Decode values
            else {
                this.types[key] = type;

                switch (type) {
                //  All fixed type
                case BYTE_TYPE:
                    this.primitiveValues[key] = (byte) ((bytes[pos++] & XFF));
                    break;

                case SHORT_TYPE:
                    this.primitiveValues[key] = (((short) bytes[pos++] & XFF)) | (((short) bytes[pos++] & XFF) << EIGHT);
                    break;

                case INT_TYPE:
                    this.primitiveValues[key] = (((int) bytes[pos++] & XFF)) | (((int) bytes[pos++] & XFF) << EIGHT)
                            | (((int) bytes[pos++] & XFF) << SIXTEEN) | (((int) bytes[pos++] & XFF) << TWENTY_FOUR);
                    break;

                case DOUBLE_TYPE:
                    this.primitiveValues[key] = (((long) bytes[pos++] & XFF) | (((long) bytes[pos++] & XFF) << EIGHT)
                            | (((long) bytes[pos++] & XFF) << SIXTEEN) | (((long) bytes[pos++] & XFF) << TWENTY_FOUR) | (((long) bytes[pos++] & XFF) << THIRTY_TWO)
                            | (((long) bytes[pos++] & XFF) << FORTY) | (((long) bytes[pos++] & XFF) << FORTY_EIGHT) | (((long) bytes[pos++] & XFF) << FIFTY_SIX));
                    break;

                case FIVE_BITS_DECIMAL_TYPE:
                    this.primitiveValues[key] = (((long) bytes[pos++] & XFF) << EIGHT) | (((long) bytes[pos++] & XFF) << SIXTEEN)
                            | (((long) bytes[pos++] & XFF) << TWENTY_FOUR) | (((long) bytes[pos++] & XFF) << THIRTY_TWO) | ((byte) bytes[pos++] & XFF);
                    break;

                // case THREE_BITS_DECIMAL_TYPE:
                // this.primitiveValues[key] = this.primitiveValues[key] =
                // ((bytes[pos++] & XFF) << EIGHT)
                // | ((bytes[pos++] & XFF) << SIXTEEN) | (bytes[pos++] & XFF);
                // break;

                case FLOAT_TYPE:
                    this.primitiveValues[key] = (((int) bytes[pos++] & XFF)) | (((int) bytes[pos++] & XFF) << EIGHT)
                            | (((int) bytes[pos++] & XFF) << SIXTEEN) | (((int) bytes[pos++] & XFF) << TWENTY_FOUR);
                    break;

                case LONG_TYPE:
                    this.primitiveValues[key] = (((long) bytes[pos++] & XFF)) | (((long) bytes[pos++] & XFF) << EIGHT)
                            | (((long) bytes[pos++] & XFF) << SIXTEEN) | (((long) bytes[pos++] & XFF) << TWENTY_FOUR) | (((long) bytes[pos++] & XFF) << THIRTY_TWO)
                            | (((long) bytes[pos++] & XFF) << FORTY) | (((long) bytes[pos++] & XFF) << FORTY_EIGHT) | (((long) bytes[pos++] & XFF) << FIFTY_SIX);
                    break;

                // All non fixed type
                default:
                    final byte typeMask = (byte) (type & TYPE_MASK);
                    this.types[key] = typeMask;
                    final int lengthMask = (LENGTH_MASK & type);
                    final int fieldLength = (lengthMask < LENGTH_ENCODED_IN_A_BIT) ? lengthMask : (lengthMask == LENGTH_ENCODED_IN_A_BIT) ? bytes[pos++]
                            : (bytes[pos++] & XFF) | ((bytes[pos++] & XFF) << EIGHT) | ((bytes[pos++] & XFF) << SIXTEEN) | ((bytes[pos++] & XFF) << TWENTY_FOUR);

                    switch (typeMask) {
                    case STRING_ISO_8859_1_TYPE:
                        // TODO manage null value
                        if (lengthMask != 0) {
                            final char[] chars = new char[fieldLength];
                            for (int i = 0; i < fieldLength; i++) {
                                chars[i] = (char) bytes[pos++];
                            }
                            this.objectValues[key] = new String(chars);
                        }
                        break;
                        
                    case STRING_UTF_16_TYPE:
                        // TODO manage null value
                        if (lengthMask != 0) {    
                            this.objectValues[key] = new String(bytes, pos, fieldLength, UTF_16_CHARSET);
                        }
                        break;

                    case MSG_TYPE:
                        // TODO manage null value
                        if (lengthMask != 0) {
                            final KeyObjectMsg msg = new KeyObjectMsg();
                            msg.deserializeFrom(bytes, pos, fieldLength);
                            pos += fieldLength;
                            this.objectValues[key] = msg;
                        }
                        break;

                    case ARRAY_FIXED_VALUE_TYPE:
                        final byte arrayType = bytes[pos++];
                        if (arrayType == NULL_TYPE) {
                            // For variable type

                        } else {
                            // For Fixed typed
                            switch (arrayType) {
                            case BYTE_TYPE:
                                final byte[] byteArray = new byte[fieldLength - 1];
                                // TODO to optimize
                                for (int i = 0; i < fieldLength - 1; i++) {
                                    byteArray[i] = bytes[pos++];
                                }
                                break;
                            case SHORT_TYPE:
                                // TODO
                                break;
                            case INT_TYPE:
                                // TODO
                                break;
                            case LONG_TYPE:
                                // TODO
                                break;
                            case FLOAT_TYPE:
                                // TODO
                                break;
                            case DOUBLE_TYPE:
                                // TODO
                                break;
                            default:
                                break;
                            }
                        }

                    default:
                        break;
                    }

                    break;
                }
                key++;
            }
        }

    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IByteBufferSerializable#serializeToByteBuffer(java.nio.ByteBuffer)
     */
    @Override
    public void serializeToByteBuffer(final ByteBuffer aDestByteBuffer) {
        aDestByteBuffer.put(this.serializeToBytes());
    }


    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IByteBufferSerializable#deserializeFrom(java.nio.ByteBuffer, int)
     */
    @Override
    public void deserializeFrom(final ByteBuffer aSrcByteBuffer) {
        final byte[] bytes = new byte[aSrcByteBuffer.remaining()];
        aSrcByteBuffer.get(bytes);
        this.deserializeFrom(bytes, 0, bytes.length);
    }

}
