package com.github.hermod.ser.impl;


import static com.github.hermod.ser.Precision.HALF;
import static com.github.hermod.ser.Types.ARRAY_FIXED_VALUE_TYPE;
import static com.github.hermod.ser.Types.ARRAY_VARIABLE_VALUE_TYPE;
import static com.github.hermod.ser.Types.DECIMAL_TYPE;
import static com.github.hermod.ser.Types.INTEGER_TYPE;
import static com.github.hermod.ser.Types.MSG_TYPE;
import static com.github.hermod.ser.Types.NULL_TYPE;
import static com.github.hermod.ser.Types.STRING_UTF_8_TYPE;
import static com.github.hermod.ser.Types.TYPE_MASK;
import static com.github.hermod.ser.Types.UTF_8_CHARSET;
import static com.github.hermod.ser.Types.UTF_8_CHARSET_NAME;
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
import static com.github.hermod.ser.impl.Msgs.FORTY;
import static com.github.hermod.ser.impl.Msgs.FORTY_EIGHT;
import static com.github.hermod.ser.impl.Msgs.FOUR;
import static com.github.hermod.ser.impl.Msgs.INT_TYPE;
import static com.github.hermod.ser.impl.Msgs.LENGTH_ENCODED_IN_AN_INT;
import static com.github.hermod.ser.impl.Msgs.LENGTH_ENCODED_IN_A_BIT;
import static com.github.hermod.ser.impl.Msgs.LENGTH_MASK;
import static com.github.hermod.ser.impl.Msgs.LONG_TYPE;
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
import static com.github.hermod.ser.impl.Msgs.ZERO;
import static com.github.hermod.ser.impl.Msgs.ERROR_WHEN_KEY_NOT_PRESENT;

import java.nio.ByteBuffer;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;

import checkers.nullness.quals.NonNull;
import checkers.nullness.quals.Nullable;

import com.github.hermod.ser.Precision;
import com.github.hermod.ser.Type;
import com.github.hermod.ser.ByteBufferSerializable;
import com.github.hermod.ser.BytesSerializable;
import com.github.hermod.ser.Msg;
import com.github.hermod.ser.Types;

/**
 * <p>IndexedPrimitiveObjectsMsg. </p>
 * 
 * @author anavarro - Jan 21, 2013
 * 
 */
public class IndexedPrimitivesObjectsMsg implements Msg, BytesSerializable, ByteBufferSerializable {

    private byte[] types;
    private long[] primitiveValues;
    private Object[] objectValues;


    /**
     * Constructor.
     * 
     */
    //TODO set as private
    private IndexedPrimitivesObjectsMsg() {
        this(DEFAULT_MAX_KEY);
    }

    /**
     * Constructor.
     * 
     * @param aDescriptor
     */
    //TODO set as private
    private IndexedPrimitivesObjectsMsg(final int aKeyMax) {
        this.types = new byte[aKeyMax + 1];
        this.primitiveValues = new long[aKeyMax + 1];
        this.objectValues = new Object[aKeyMax + 1];
    }

    /**
     * Constructor.
     * 
     * @param aMsg
     */
    //TODO set as private
    private IndexedPrimitivesObjectsMsg(final Msg aMsg) {
        if (aMsg instanceof IndexedPrimitivesObjectsMsg) {
            final IndexedPrimitivesObjectsMsg keyObjectMsg = (IndexedPrimitivesObjectsMsg) aMsg;
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
                final int[] keys = aMsg.retrieveKeys();
                final int aKeyMax = keys[keys.length - 1];
                this.types = new byte[aKeyMax + 1];
                this.primitiveValues = new long[aKeyMax + 1];
                this.objectValues = new Object[aKeyMax + 1];
                for (int i = 0; i < keys.length; i++) {
                    set(keys[i], aMsg.get(keys[i]));
                }
            } else {
                final int aKeyMax = DEFAULT_MAX_KEY;
                this.types = new byte[aKeyMax + 1];
                this.primitiveValues = new long[aKeyMax + 1];
                this.objectValues = new Object[aKeyMax + 1];
            }
        }
    }

    /**
     * increaseKeyMax.
     * 
     * @param keyMax
     */
    private void increaseKeyMax(final int aKey) {
        if (aKey < 0) {
            throw new IllegalArgumentException("The key=" + aKey + " must be positive.");
        } else {
            final int nextPow2 = Msgs.calculateNextPowerOf2(aKey + 1);
            final byte[] destTypes = new byte[nextPow2];
            final long[] destPrimitiveValues = new long[nextPow2];
            final Object[] destObjectValues = new Object[nextPow2];
            System.arraycopy(this.types, 0, destTypes, 0, this.types.length);
            System.arraycopy(this.primitiveValues, 0, destPrimitiveValues, 0, this.primitiveValues.length);
            System.arraycopy(this.objectValues, 0, destObjectValues, 0, this.objectValues.length);
            this.types = destTypes;
            this.primitiveValues = destPrimitiveValues;
            this.objectValues = destObjectValues;
        }
    }
    
    /**
     * newMsg.
     *
     * @return
     */
    public static IndexedPrimitivesObjectsMsg create() {
        return new IndexedPrimitivesObjectsMsg(DEFAULT_MAX_KEY);
    }
    

    /**
     * newMsg.
     *
     * @param keyMax
     * @return
     */
    public static IndexedPrimitivesObjectsMsg createWithKeyMax(final int keyMax) {
        return new IndexedPrimitivesObjectsMsg(keyMax);
    }
    
    /**
     * createFromBytes.
     *
     * @param bytes
     * @param offset
     * @param length
     * @return
     */
    //TODO createFromBytes?
    public static IndexedPrimitivesObjectsMsg createFromBytes(final byte[] aSrcBytes, final int offset, final int length) {
         final IndexedPrimitivesObjectsMsg msg = new IndexedPrimitivesObjectsMsg();
         msg.deserializeFromBytes(aSrcBytes, offset, length);
         return msg;
    }
    
    /**
     * createFromByteBuffer.
     *
     * @param byteBuffer
     * @return
     */
    public static IndexedPrimitivesObjectsMsg createFromByteBuffer(final ByteBuffer aSrcByteBuffer) {
        final IndexedPrimitivesObjectsMsg msg = new IndexedPrimitivesObjectsMsg();
        msg.deserializeFromByteBuffer(aSrcByteBuffer);
        return msg;
   }
    
    /**
     * createFromMsg.
     *
     * @param aMsg
     * @return
     */
    public static IndexedPrimitivesObjectsMsg createFromMsg(final Msg aMsg) {
        return new IndexedPrimitivesObjectsMsg(aMsg);
   }
    
    /**
     * createFromValues.
     *
     * @param values
     * @return
     */
    public static IndexedPrimitivesObjectsMsg createFromValues(final Object... values) {
        final IndexedPrimitivesObjectsMsg msg = new IndexedPrimitivesObjectsMsg(values.length);
        msg.setAll(values);
        return msg;
   }
    
    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#isEmpty()
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
     * isBytesSerializable.
     *
     * @return
     */
    @Override
    public final boolean isBytesSerializable() {
        return true;
    }
    
    /**
     * isByteBufferSerializable.
     *
     * @return
     */
    @Override
    public final boolean isByteBufferSerializable(){
        return true;
    }
    
    /**
     * isByteBufSerializable.
     *
     * @return
     */
    @Override
    public final boolean isByteBufSerializable(){
        return false;
    }
    
    
    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser3.intmap.ReadIntMap#retrieveKeys()
     */
    @Override
    public final @NonNull
    int[] retrieveKeys() {
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
     * @see com.github.hermod.ser.Msg#retrieveKeyMax()
     */
    public final int retrieveKeyMax() {
        for (int i = this.types.length; i-- != 0; ) {
            if (this.types[i] != NULL_TYPE) {
                return i;
            }
        }
        return -1;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#countKeys()
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
     * @see com.github.hermod.ser.Msg#getType(int)
     */
    @Override
    public final @NonNull
    Type getType(final int aKey) {
        try {
            return Type.valueOf((byte) (this.types[aKey] & TYPE_MASK));
        } catch (ArrayIndexOutOfBoundsException e) {
            return Type.NULL;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getTypeAsByte(int)
     */
    @Override
    public final byte getTypeAsByte(final int aKey) {
        try {
            return (byte) (this.types[aKey] & TYPE_MASK);
        } catch (ArrayIndexOutOfBoundsException e) {
            return Types.NULL_TYPE;
        }
    }

    // @Override
    private final byte getTypeWithLengthAsByte(final int aKey) {
        try {
            return (byte) (this.types[aKey]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return Types.NULL_TYPE;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#isArray(int)
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
     * @see com.github.hermod.ser.Msg#getArrayLength(int)
     */
    @Override
    public int getArrayLength(final int aKey) {
        try {
            if ((this.types[aKey] & TYPE_MASK) == ARRAY_VARIABLE_VALUE_TYPE || (this.types[aKey] & TYPE_MASK) == ARRAY_FIXED_VALUE_TYPE) {
                if ((this.objectValues[aKey] instanceof Object[])) {
                    return ((Object[]) this.objectValues[aKey]).length;
                } else if ((this.objectValues[aKey] instanceof byte[])) {
                    return ((byte[]) this.objectValues[aKey]).length;
                } else if ((this.objectValues[aKey] instanceof short[])) {
                    return ((short[]) this.objectValues[aKey]).length;
                } else if ((this.objectValues[aKey] instanceof int[])) {
                    return ((int[]) this.objectValues[aKey]).length;
                } else if ((this.objectValues[aKey] instanceof long[])) {
                    return ((long[]) this.objectValues[aKey]).length;
                } else if ((this.objectValues[aKey] instanceof float[])) {
                    return ((float[]) this.objectValues[aKey]).length;
                } else if ((this.objectValues[aKey] instanceof double[])) {
                    return ((double[]) this.objectValues[aKey]).length;
                } else {
                    // Should not occur
                    return 0;
                }
            } else {
                return 0;
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }


 
    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#contains(int)
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
     * @see com.github.hermod.ser.Msg#getAsObject(int)
     */
    @Override
    public final @Nullable
    Object get(final int aKey) {
        return get(aKey, Object.class);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#get(int, java.lang.Class)
     */
    @Override
    public final @Nullable
    <T> T get(final int aKey, final Class<T> aClazz) {
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

            case FIVE_BITS_DECIMAL_TYPE:
            case DOUBLE_TYPE:
                return aClazz.cast(getAsDouble(aKey));

            case DECIMAL_TYPE:
                return aClazz.cast((Double) null);

            case STRING_UTF_8_TYPE:
                return aClazz.cast(getAsString(aKey));

            case MSG_TYPE:
                return aClazz.cast(getAsMsg(aKey));

            case ARRAY_FIXED_VALUE_TYPE:
            case ARRAY_VARIABLE_VALUE_TYPE:
                return aClazz.cast(getAsObjects(aKey));

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
     * @see com.github.hermod.ser.Msg#getAsByte(int)
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
     * @see com.github.hermod.ser.Msg#getAsNullableBoolean(int)
     */
    @Override
    public final @Nullable
    Boolean getAsNullableBoolean(final int aKey) {
        try {
            return Boolean.valueOf(getAsBoolean(aKey));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getAsByte(int)
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
     * @see com.github.hermod.ser.Msg#getAsNullableByte(int)
     */
    @Override
    public final @Nullable
    Byte getAsNullableByte(final int aKey) {
        try {
            return Byte.valueOf(getAsByte(aKey));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getAsShort(int)
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
     * @see com.github.hermod.ser.Msg#getAsNullableShort(int)
     */
    @Override
    public final @Nullable
    Short getAsNullableShort(final int aKey) {
        try {
            return Short.valueOf(getAsShort(aKey));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getAsInt(int)
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
     * getAsNullableInteger.
     * 
     * @param aKey
     * @return
     */
    @Override
    public final @Nullable
    Integer getAsNullableInteger(final int aKey) {
        try {
            return Integer.valueOf(getAsInt(aKey));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getAsLong(int)
     */
    @Override
    public final long getAsLong(final int aKey) {
        try {
            if (this.types[aKey] == BYTE_TYPE || this.types[aKey] == SHORT_TYPE || this.types[aKey] == INT_TYPE || this.types[aKey] == LONG_TYPE) {
                return (long) this.primitiveValues[aKey];
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
        }
        throw new IllegalArgumentException(String.format(ERROR_WHEN_KEY_NOT_PRESENT, aKey));
    }

    /**
     * getAsLongClass.
     * 
     * @param aKey
     * @return
     */
    @Override
    public final @Nullable
    Long getAsNullableLong(final int aKey) {
        try {
            return Long.valueOf(getAsLong(aKey));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getAsFloat(int)
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
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getAsNullableFloat(int)
     */
    @Override
    public final @Nullable
    Float getAsNullableFloat(final int aKey) {
        try {
            return getAsFloat(aKey);
        } catch (IllegalArgumentException e) {
            return null;
        }
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
            // case THREE_BITS_DECIMAL_TYPE:
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
     * getAsDoubleClass.
     * 
     * @param aKey
     * @return
     */
    @Override
    public final @Nullable
    Double getAsNullableDouble(final int aKey) {
        try {
            return getAsDouble(aKey);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * getAs5BitsDecimal.
     * 
     * @param aKey
     * @return
     */
    private double getAs5BitsDecimal(final int aKey) {
        final int nbDigit = (int) (XFF & this.primitiveValues[aKey]);
        final double integerMantissa = (this.primitiveValues[aKey] >> EIGHT);
        // return Precisions.fromNbDigit((int) (XFF & this.primitiveValues[aKey])).calculateDouble((this.primitiveValues[aKey] >> EIGHT));
        switch (nbDigit) {
        case ZERO:
            return integerMantissa;
        case ONE:
            return integerMantissa * Precision.TENTHS.getPrecision();
        case TWO:
            return integerMantissa * Precision.HUNDREDTHS.getPrecision();
        case THREE:
            return integerMantissa * Precision.THOUSANDTHS.getPrecision();
        case FOUR:
            return integerMantissa * Precision.TEN_THOUSANDTHS.getPrecision();
        case FIVE:
            return integerMantissa * Precision.HUNDRED_THOUSANDTHS.getPrecision();
        case SIX:
            return integerMantissa * Precision.MILLIONTHS.getPrecision();
        case SEVEN:
            return integerMantissa * Precision.TEN_MILLIONTHS.getPrecision();
        case EIGHT:
            return integerMantissa * Precision.HUNDRED_MILLIONTHS.getPrecision();
        default:
            return Precision.valueOf(nbDigit).calculateIntegerMantissa(integerMantissa);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getAsString(int)
     */
    @Override
    public final @Nullable
    String getAsString(final int aKey) {
        try {
            return ((this.types[aKey] & TYPE_MASK) == STRING_UTF_8_TYPE) ? (String) this.objectValues[aKey]
                    : null;
        } catch (final ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getAsMsg(int)
     */
    @Override
    public final @Nullable
    Msg getAsMsg(final int aKey) {
        try {
            return ((this.types[aKey] & TYPE_MASK) == MSG_TYPE && this.objectValues[aKey] != null) ? new IndexedPrimitivesObjectsMsg((Msg) this.objectValues[aKey])
                    : null;
        } catch (final ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * getAsMap.
     * 
     * @param aKey
     * @return
     */
    // private final Map<Integer, Object> getAsMap(final int aKey) {
    // final IMsg msg = this.getAsMsg(aKey);
    // if (msg != null) {
    // final int[] keys = msg.retrieveKeys();
    // final Map<Integer, Object> map = new HashMap<>(keys.length);
    // for (final int key : keys) {
    // map.put(key, msg.getAsObject(key));
    // }
    // return map;
    // } else {
    // return null;
    // }
    // }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getAsMsgInto(int, com.github.hermod.ser.Msg)
     */
    @Override
    public final void getAsMsg(final int aKey, final Msg destValue) {
        try {
            if ((this.types[aKey] & TYPE_MASK) == MSG_TYPE) {
                destValue.setAll((Msg) this.objectValues[aKey]);
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
        }
    }



    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getAsBooleans(int)
     */
    @Override
    public final @Nullable
    boolean[] getAsBooleans(final int aKey) {
        try {
            if ((this.types[aKey] & TYPE_MASK) == ARRAY_FIXED_VALUE_TYPE && (this.objectValues[aKey] instanceof byte[])) {
                final byte[] bytes = (byte[]) this.objectValues[aKey];
                final boolean[] results = new boolean[bytes.length];
                for (int i = 0; i < bytes.length; i++) {
                    if (bytes[i] != 0 && bytes[i] != 1) {
                        return null;
                    }
                    results[i] = (bytes[i] == 0) ? false : true;
                }
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
     * @see com.github.hermod.ser.Msg#getAsNullableBooleans(int)
     */
    @Override
    public final @Nullable
    Boolean[] getAsNullableBooleans(final int aKey) {
        try {
            if ((this.types[aKey] & TYPE_MASK) == ARRAY_FIXED_VALUE_TYPE && (this.objectValues[aKey] instanceof byte[])) {
                final byte[] bytes = (byte[]) this.objectValues[aKey];
                final Boolean[] results = new Boolean[bytes.length];
                for (int i = 0; i < bytes.length; i++) {
                    if (bytes[i] != 0 && bytes[i] != 1) {
                        return null;
                    }
                    results[i] = (bytes[i] == 0) ? false : true;
                }
                return results;
            } else if ((this.types[aKey] & TYPE_MASK) == ARRAY_VARIABLE_VALUE_TYPE && (this.objectValues[aKey] instanceof Byte[])) {
                final Byte[] bytes = (Byte[]) this.objectValues[aKey];
                final Boolean[] results = new Boolean[bytes.length];
                for (int i = 0; i < bytes.length; i++) {
                    if (bytes[i] != 0 && bytes[i] != 1 && bytes[i] != null) {
                        return null;
                    }
                    results[i] = (bytes[i] == null) ? null : (bytes[i] == 0) ? false : true;
                }
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
     * @see com.github.hermod.ser.Msg#getAsBytes(int)
     */
    @Override
    public final @Nullable
    byte[] getAsBytes(final int aKey) {
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
     * @see com.github.hermod.ser.Msg#getAsNullableBytes(int)
     */
    @Override
    public final @Nullable
    Byte[] getAsNullableBytes(final int aKey) {
        try {
            if ((this.types[aKey] & TYPE_MASK) == ARRAY_FIXED_VALUE_TYPE && (this.objectValues[aKey] instanceof byte[])) {
                final byte[] bytes = (byte[]) this.objectValues[aKey];
                final int byteLength = bytes.length;
                final Byte[] results = new Byte[byteLength];
                // System.arraycopy(bytes, 0, results, 0, bytes.length);
                for (int i = 0; i < byteLength; i++) {
                    results[i] = bytes[i];
                }
                return results;
            } else if ((this.types[aKey] & TYPE_MASK) == ARRAY_VARIABLE_VALUE_TYPE && (this.objectValues[aKey] instanceof Byte[])) {
                final Byte[] bytes = (Byte[]) this.objectValues[aKey];
                final Byte[] results = new Byte[bytes.length];
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
     * @see com.github.hermod.ser.Msg#getAsShorts(int)
     */
    @Override
    public final @Nullable
    short[] getAsShorts(final int aKey) {
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
     * @see com.github.hermod.ser.Msg#getAsNullableBytes(int)
     */
    @Override
    public final @Nullable
    Short[] getAsNullableShorts(final int aKey) {
        try {
            if ((this.types[aKey] & TYPE_MASK) == ARRAY_FIXED_VALUE_TYPE && (this.objectValues[aKey] instanceof short[])) {
                final short[] shorts = (short[]) this.objectValues[aKey];
                final int shortLength = shorts.length;
                final Short[] results = new Short[shortLength];
                for (int i = 0; i < shortLength; i++) {
                    results[i] = shorts[i];
                }
                return results;
            } else if ((this.types[aKey] & TYPE_MASK) == ARRAY_VARIABLE_VALUE_TYPE && (this.objectValues[aKey] instanceof Short[])) {
                final Short[] shorts = (Short[]) this.objectValues[aKey];
                final Short[] results = new Short[shorts.length];
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
     * @see com.github.hermod.ser.Msg#getAsInts(int)
     */
    @Override
    public final @Nullable
    int[] getAsInts(final int aKey) {
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
     * @see com.github.hermod.ser.Msg#getAsNullableIntegers(int)
     */
    @Override
    public final @Nullable
    Integer[] getAsNullableIntegers(final int aKey) {
        try {
            if (((this.types[aKey] & TYPE_MASK) == ARRAY_FIXED_VALUE_TYPE && (this.objectValues[aKey] instanceof int[]))) {
                final int[] ints = (int[]) this.objectValues[aKey];
                final int intsLength = ints.length;
                final Integer[] results = new Integer[intsLength];
                for (int i = 0; i < intsLength; i++) {
                    results[i] = ints[i];
                }
                return results;
            } else if (((this.types[aKey] & TYPE_MASK) == ARRAY_VARIABLE_VALUE_TYPE && (this.objectValues[aKey] instanceof Integer[]))) {
                final Integer[] ints = (Integer[]) this.objectValues[aKey];
                final Integer[] results = new Integer[ints.length];
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
     * @see com.github.hermod.ser.Msg#getAsLongs(int)
     */
    @Override
    public final @Nullable
    long[] getAsLongs(final int aKey) {
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
     * @see com.github.hermod.ser.Msg#getAsNullableLongs(int)
     */
    @Override
    public final @Nullable
    Long[] getAsNullableLongs(final int aKey) {
        try {
            if ((this.types[aKey] & TYPE_MASK) == ARRAY_FIXED_VALUE_TYPE && (this.objectValues[aKey] instanceof long[])) {
                final long[] longs = (long[]) this.objectValues[aKey];
                final int longsLength = longs.length;
                final Long[] results = new Long[longsLength];
                for (int i = 0; i < longsLength; i++) {
                    results[i] = longs[i];
                }
                return results;
            } else if ((this.types[aKey] & TYPE_MASK) == ARRAY_VARIABLE_VALUE_TYPE && (this.objectValues[aKey] instanceof Long[])) {
                final Long[] longs = (Long[]) this.objectValues[aKey];
                final Long[] results = new Long[longs.length];
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
     * @see com.github.hermod.ser.Msg#getAsFloats(int)
     */
    @Override
    public final @Nullable
    float[] getAsFloats(final int aKey) {
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
     * @see com.github.hermod.ser.Msg#getAsNullableFloats(int)
     */
    @Override
    public final @Nullable
    Float[] getAsNullableFloats(final int aKey) {
        try {
            if (((this.types[aKey] & TYPE_MASK) == ARRAY_FIXED_VALUE_TYPE && (this.objectValues[aKey] instanceof float[]))) {
                final float[] floats = (float[]) this.objectValues[aKey];
                final int floatsLength = floats.length;
                final Float[] results = new Float[floatsLength];
                for (int i = 0; i < floatsLength; i++) {
                    results[i] = floats[i];
                }
                return results;
            } else if (((this.types[aKey] & TYPE_MASK) == ARRAY_VARIABLE_VALUE_TYPE && (this.objectValues[aKey] instanceof Float[]))) {
                final Float[] floats = (Float[]) this.objectValues[aKey];
                final Float[] results = new Float[floats.length];
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
     * @see com.github.hermod.ser.Msg#getAsDoubles(int)
     */
    @Override
    public final @Nullable
    double[] getAsDoubles(final int aKey) {
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
     * @see com.github.hermod.ser.Msg#getAsNullableDoubles(int)
     */
    @Override
    public final @Nullable
    Double[] getAsNullableDoubles(final int aKey) {
        try {
            if ((this.types[aKey] & TYPE_MASK) == ARRAY_FIXED_VALUE_TYPE && (this.objectValues[aKey] instanceof double[])) {
                final double[] doubles = (double[]) this.objectValues[aKey];
                final int doublesLength = doubles.length;
                final Double[] results = new Double[doublesLength];
                for (int i = 0; i < doublesLength; i++) {
                    results[i] = doubles[i];
                }
                return results;
            } else if ((this.types[aKey] & TYPE_MASK) == ARRAY_VARIABLE_VALUE_TYPE && (this.objectValues[aKey] instanceof Double[])) {
                final Double[] doubles = (Double[]) this.objectValues[aKey];
                final Double[] results = new Double[doubles.length];
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
     * @see com.github.hermod.ser.Msg#getAsStrings(int)
     */
    @Override
    public final @Nullable
    String[] getAsStrings(final int aKey) {
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
     * @see com.github.hermod.ser.Msg#getAsMsgs(int)
     */
    @Override
    public final @Nullable
    Msg[] getAsMsgs(final int aKey) {
        try {
            if ((this.types[aKey] & TYPE_MASK) == ARRAY_VARIABLE_VALUE_TYPE && (this.objectValues[aKey] instanceof Msg[])) {
                final Msg[] msgs = (Msg[]) this.objectValues[aKey];
                final Msg[] results = new IndexedPrimitivesObjectsMsg[msgs.length];
                for (int i = 0; i < msgs.length; i++) {
                    results[i] = new IndexedPrimitivesObjectsMsg(msgs[i]);
                }
                return results;
            }
            return ((this.types[aKey] & TYPE_MASK) == ARRAY_VARIABLE_VALUE_TYPE && (this.objectValues[aKey] instanceof Msg[])) ? (Msg[]) this.objectValues[aKey]
                    : null;
        } catch (final ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getAsMsgs(int, com.github.hermod.ser.Msg[])
     */
    @Override
    public final void getAsMsgs(final int aKey, Msg... aDestMsgs) {
        try {
            if ((this.types[aKey] & TYPE_MASK) == ARRAY_VARIABLE_VALUE_TYPE && (this.objectValues[aKey] instanceof Msg[])) {
                final Msg[] msgs = (Msg[]) this.objectValues[aKey];
                if (msgs.length == aDestMsgs.length) {
                    for (int i = 0; i < msgs.length; i++) {
                        aDestMsgs[i].setAll(msgs[i]);
                    }
                } else {
                    throw new IllegalArgumentException("The destMsgs with length=" + aDestMsgs.length + " must be have the same field array length="
                            + msgs.length + ". Use getArrayLength() to know the length before call this method.");
                }
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
        }
    }

    // @Override
    /**
     * getAsObjects.
     * 
     * @param aKey
     * @return
     */
    @Override
    public final @Nullable
    Object[] getAsObjects(final int aKey) {
        try {
            if ((this.types[aKey] & TYPE_MASK) == ARRAY_FIXED_VALUE_TYPE) {
                if (this.objectValues[aKey] instanceof byte[]) {
                    return getAsNullableBytes(aKey);
                } else if (this.objectValues[aKey] instanceof short[]) {
                    return getAsNullableShorts(aKey);
                } else if (this.objectValues[aKey] instanceof int[]) {
                    return getAsNullableIntegers(aKey);
                } else if (this.objectValues[aKey] instanceof long[]) {
                    return getAsNullableLongs(aKey);
                } else if (this.objectValues[aKey] instanceof float[]) {
                    return getAsNullableFloats(aKey);
                } else if (this.objectValues[aKey] instanceof double[]) {
                    return getAsNullableDoubles(aKey);
                } else {
                    // Should not occur
                    return null;
                }
            } else if ((this.types[aKey] & TYPE_MASK) == ARRAY_VARIABLE_VALUE_TYPE && (this.objectValues[aKey] instanceof Object[])) {
                final Object[] objects = (Object[]) this.objectValues[aKey];
                final Object[] results = new Object[objects.length];
                System.arraycopy(objects, 0, results, 0, objects.length);
                return results;
            } else {
                return null;
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            return null;
        }
        
//        final Object[] objects = new Object[retrieveKeyMax()];
//        getAsObjects(aKey, objects);
//        return objects;
    }
    
    
    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.Msg#getAsObjects(int, java.lang.Object[])
     */
    public final @Nullable
    void getAsObjects(final int aKey, Object[] aDestObjects) {
        //TODO to implement
        throw new UnsupportedOperationException("Not Implemented");
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getAll()
     */
    @Override
    public final @NonNull
    Msg getAllAsMsg() {
        // TODO to optimize
        final int[] keys = this.retrieveKeys();
        final Msg msg = new IndexedPrimitivesObjectsMsg(keys[keys.length - 1]);
        for (final int key : keys) {
            msg.set(key, this.get(key));
        }
        return msg;
    }
    
    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.Msg#getAllAsObjects()
     */
    @Override
    public final Object[] getAllAsObjects() {
        final Object[] anObjects = new Object[this.retrieveKeyMax() + 1];
        this.getAllAsObjects(anObjects);
        return anObjects;
    }
    
    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.Msg#getAllAsObjects(java.lang.Object[])
     */
    @Override 
    public final void getAllAsObjects(final Object[] anObjects) {
        // TODO to optimize
        final int keyMax = this.retrieveKeyMax();
        if (keyMax > anObjects.length) {
            throw new IllegalArgumentException("You must pass anObjects with a length=" + anObjects.length + " equals or superior than keyMax=" + keyMax);
        }
        final int[] keys = this.retrieveKeys();
        for (int i = 0; i < keys.length; i++) {
            anObjects[i] = this.get(i);
        }
    }

    /**
     * getAllAsMap.
     * 
     * @return
     */
    final @NonNull
    Map<Integer, Object> getAllAsMap() {
        // TODO to optimize
        final int[] keys = this.retrieveKeys();
        final Map<Integer, Object> map = new HashMap<>(keys.length);
        for (final int key : keys) {
            map.put(key, this.get(key));
        }
        return map;
    }


    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Object)
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
            } else if (aObject instanceof Msg) {
                set(aKey, (Msg) aObject);
            } else if (aObject instanceof Boolean) {
                set(aKey, (Boolean) aObject);
            } else if (aObject instanceof Object[]) {
                set(aKey, (Object[]) aObject);
            } else {
                throw new IllegalArgumentException("Impossible to set this type of value=" + aObject.getClass());
            }
            //this.objectValues[aKey] = aObject;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aObject);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, boolean)
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
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Boolean)
     */
    @Override
    public final void set(final int aKey, final Boolean aBoolean) {
        if (aBoolean != null) {
            this.set(aKey, aBoolean.booleanValue());
        } else {
            try {
                this.types[aKey] = INTEGER_TYPE;
            } catch (final ArrayIndexOutOfBoundsException e) {
                increaseKeyMax(aKey);
                set(aKey, aBoolean);
            }
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, byte)
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

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Byte)
     */
    @Override
    public final void set(final int aKey, final Byte aByte) {
        if (aByte != null) {
            this.set(aKey, aByte.byteValue());
        } else {
            try {
                this.types[aKey] = INTEGER_TYPE;
            } catch (final ArrayIndexOutOfBoundsException e) {
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
            this.primitiveValues[aKey] = aShort;
            this.types[aKey] = (aShort == (byte) aShort) ? BYTE_TYPE : SHORT_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aShort);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Byte)
     */
    @Override
    public final void set(final int aKey, final Short aShort) {
        if (aShort != null) {
            this.set(aKey, aShort.shortValue());
        } else {
            try {
                this.types[aKey] = INTEGER_TYPE;
            } catch (final ArrayIndexOutOfBoundsException e) {
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
            this.primitiveValues[aKey] = aInt;
            this.types[aKey] = (aInt == (byte) aInt) ? BYTE_TYPE : (aInt == (short) aInt) ? SHORT_TYPE : INT_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aInt);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Byte)
     */
    @Override
    public final void set(final int aKey, final Integer aInteger) {
        if (aInteger != null) {
            this.set(aKey, aInteger.intValue());
        } else {
            try {
                this.types[aKey] = INTEGER_TYPE;
            } catch (final ArrayIndexOutOfBoundsException e) {
                increaseKeyMax(aKey);
                set(aKey, aInteger);
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
            this.primitiveValues[aKey] = aLong;
            this.types[aKey] = (aLong == (byte) aLong) ? BYTE_TYPE : (aLong == (short) aLong) ? SHORT_TYPE : (aLong == (int) aLong) ? INT_TYPE
                    : LONG_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aLong);
        }
    }

    /**
     * set.
     * 
     * @param aKey
     * @param aLong
     * @param length
     */
    private void set(final int aKey, final long aLong, final byte integerType) {
        try {
            this.primitiveValues[aKey] = aLong;
            this.types[aKey] = integerType;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aLong, integerType);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Byte)
     */
    @Override
    public final void set(final int aKey, final Long aLong) {
        if (aLong != null) {
            this.set(aKey, aLong.longValue());
        } else {
            try {
                this.types[aKey] = INTEGER_TYPE;
            } catch (final ArrayIndexOutOfBoundsException e) {
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
            this.types[aKey] = FLOAT_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aFloat);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Byte)
     */
    @Override
    public final void set(final int aKey, final Float aFloat) {
        if (aFloat != null) {
            this.set(aKey, aFloat.floatValue());
        } else {
            try {
                this.types[aKey] = DECIMAL_TYPE;
            } catch (final ArrayIndexOutOfBoundsException e) {
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
            final boolean isEncodeableInAFloat = (aDouble == (float) aDouble) ? true : false;
            this.primitiveValues[aKey] = (isEncodeableInAFloat) ? Float.floatToIntBits((float) aDouble) : Double.doubleToLongBits(aDouble);
            this.types[aKey] = (isEncodeableInAFloat) ? FLOAT_TYPE : DOUBLE_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aDouble);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Byte)
     */
    @Override
    public final void set(final int aKey, final Double aDouble) {
        if (aDouble != null) {
            this.set(aKey, aDouble.doubleValue());
        } else {
            try {
                this.types[aKey] = DECIMAL_TYPE;
            } catch (final ArrayIndexOutOfBoundsException e) {
                increaseKeyMax(aKey);
                set(aKey, aDouble);
            }
        }
    }

    /**
     * set.
     * 
     * @param aKey
     * @param aDouble
     * @param forceNoLengthOptimization
     */
    private final void set(final int aKey, final double aDouble, final boolean forceNoLengthOptimization) {
        if (forceNoLengthOptimization) {
            try {
                this.primitiveValues[aKey] = Double.doubleToLongBits(aDouble);
                this.types[aKey] = DOUBLE_TYPE;
            } catch (final ArrayIndexOutOfBoundsException e) {
                increaseKeyMax(aKey);
                set(aKey, aDouble, true);
            }
        } else {
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
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Byte)
     */
    @Override
    public final void set(final int aKey, final Double aDouble, final int nbDigit) {
        if (aDouble != null) {
            this.set(aKey, aDouble.doubleValue(), nbDigit);
        } else {
            try {
                this.types[aKey] = DECIMAL_TYPE;
            } catch (final ArrayIndexOutOfBoundsException e) {
                increaseKeyMax(aKey);
                set(aKey, aDouble, nbDigit);
            }
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, double, com.github.hermod.ser.Precision)
     */
    @Override
    public final void set(final int aKey, final double aDouble, final Precision aPrecision) {
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

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Byte)
     */
    @Override
    public final void set(final int aKey, final Double aDouble, final Precision aPrecision) {
        if (aDouble != null) {
            this.set(aKey, aDouble.doubleValue(), aPrecision);
        } else {
            try {
                this.types[aKey] = DECIMAL_TYPE;
            } catch (final ArrayIndexOutOfBoundsException e) {
                increaseKeyMax(aKey);
                set(aKey, aDouble, aPrecision);
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
            this.types[aKey] = STRING_UTF_8_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aString);
        }
    }

    
    /**
     * checkIfEncodableInAscii.
     *
     * @param aString
     * @return
     */
    private static final boolean checkIfEncodableInAscii(final String aString) {
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
     * set.
     * 
     * @param aKey
     * @param aString
     * @param stringType
     */
//    private void set(final int aKey, final String aString, final byte stringType) {
//        try {
//            this.objectValues[aKey] = aString;
//            this.types[aKey] = stringType;
//        } catch (final ArrayIndexOutOfBoundsException e) {
//            increaseKeyMax(aKey);
//            set(aKey, aString);
//        }
//    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.String, boolean)
     */
    @Override
    //TODO to remove
    public final void set(final int aKey, final String aString, final boolean forceIso88591Charset) {
//        if (forceIso88591Charset) {
//            set(aKey, aString, STRING_ISO_8859_1_TYPE);
//        } else {
//            set(aKey, aString, STRING_UTF_16_TYPE);
//        }
        
        set(aKey, aString);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, com.github.hermod.ser.Msg)
     */
    @Override
    public final void set(final int aKey, final Msg aMsg) {
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
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Object[])
     */
    public final void set(final int aKey, final Object[] aObjectArray) {
        // TODO manage the
        try {
            this.objectValues[aKey] = aObjectArray;
            this.types[aKey] = ARRAY_VARIABLE_VALUE_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aObjectArray);
        }
    }



    // @Override
    public final void set(final int aKey, final Object aObject, final boolean forceNoLengthOptimization) {
        if (forceNoLengthOptimization) {
            try {

                if (aObject instanceof Boolean | aObject instanceof Byte | aObject instanceof Short | aObject instanceof Integer
                        | aObject instanceof Long) {
                    if (aObject != null) {
                        final byte integerType = (aObject instanceof Byte || aObject instanceof Boolean) ? BYTE_TYPE
                                : (aObject instanceof Short) ? SHORT_TYPE : (aObject instanceof Integer) ? INT_TYPE : LONG_TYPE;
                        set(aKey, ((Number) aObject).longValue(), integerType);
                    } else {
                        this.types[aKey] = INTEGER_TYPE;
                        this.objectValues[aKey] = DEFAULT_VALUE;
                    }
                } else if (aObject instanceof Float | aObject instanceof Double) {
                    if (aObject != null) {
                        if (aObject instanceof Double) {
                            set(aKey, ((Double) aObject).doubleValue(), true);
                        } else {
                            set(aKey, ((Float) aObject).floatValue());
                        }
                    } else {
                        this.types[aKey] = DECIMAL_TYPE;
                        this.objectValues[aKey] = DEFAULT_VALUE;
                    }
                } else if (aObject instanceof String) {
                    set(aKey, (String) aObject, true);
                } else if (aObject instanceof Msg) {
                    set(aKey, (Msg) aObject);
                } else {
                    throw new IllegalArgumentException("Impossible to set this type of value=" + aObject.getClass());
                }
                this.objectValues[aKey] = aObject;
            } catch (final ArrayIndexOutOfBoundsException e) {
                increaseKeyMax(aKey);
                set(aKey, aObject);
            }
        } else {
            set(aKey, aObject);
        }
    }



    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, boolean[])
     */
    @Override
    public final void set(final int aKey, final boolean... aBooleans) {
        try {
            if (aBooleans != null) {
                final byte[] bytes = new byte[aBooleans.length];
                for (int i = 0; i < aBooleans.length; i++) {
                    bytes[i] = (byte) ((aBooleans[i] == true) ? 1 : 0);
                }
                this.objectValues[aKey] = bytes;
            } else {
                this.objectValues[aKey] = null;
            }
            this.types[aKey] = ARRAY_FIXED_VALUE_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aBooleans);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Boolean[])
     */
    @Override
    public final void set(final int aKey, final Boolean... aBooleans) {
        try {
            if (aBooleans != null) {
                final Byte[] bytes = new Byte[aBooleans.length];
                for (int i = 0; i < aBooleans.length; i++) {
                    bytes[i] = Boolean.TRUE.equals(aBooleans[i]) ? (byte) 1 : (Boolean.FALSE.equals(aBooleans[i])) ? (byte) 0 : null;
                }
                this.objectValues[aKey] = bytes;
            } else {
                this.objectValues[aKey] = null;
            }
            this.types[aKey] = ARRAY_VARIABLE_VALUE_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aBooleans);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, byte[])
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
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Byte[])
     */
    @Override
    public final void set(final int aKey, final Byte... aBytes) {
        try {
            this.objectValues[aKey] = aBytes;
            this.types[aKey] = ARRAY_VARIABLE_VALUE_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aBytes);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, short[])
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
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Short[])
     */
    @Override
    public final void set(final int aKey, final Short... aShorts) {
        try {
            this.objectValues[aKey] = aShorts;
            this.types[aKey] = ARRAY_VARIABLE_VALUE_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aShorts);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, int[])
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
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Integer[])
     */
    @Override
    public final void set(final int aKey, final Integer... aInts) {
        try {
            this.objectValues[aKey] = aInts;
            this.types[aKey] = ARRAY_VARIABLE_VALUE_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aInts);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, long[])
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
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Long[])
     */
    @Override
    public final void set(final int aKey, final Long... aLongs) {
        try {
            this.objectValues[aKey] = aLongs;
            this.types[aKey] = ARRAY_VARIABLE_VALUE_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aLongs);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, float[])
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
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Float[])
     */
    @Override
    public final void set(final int aKey, final Float... aFloats) {
        try {
            this.objectValues[aKey] = aFloats;
            this.types[aKey] = ARRAY_VARIABLE_VALUE_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aFloats);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, double[])
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
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Double[])
     */
    @Override
    public final void set(final int aKey, final Double... aDoubles) {
        try {
            this.objectValues[aKey] = aDoubles;
            this.types[aKey] = ARRAY_VARIABLE_VALUE_TYPE;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aDoubles);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.String[])
     */
    @Override
    public final void set(final int aKey, final String... aStrings) {
        // TODO copy or not, Caution pb with aForceIso88591Charset, must transform String[] with the good charset
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
     * @see com.github.hermod.ser.Msg#set(int, java.lang.String[], boolean)
     */
    @Override
    public final void set(final int aKey, final String[] aStrings, final boolean forceNoLengthOptimization) {
        // TODO copy or not, Caution pb with aForceIso88591Charset, must transform String[] with the good charset
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
     * @see com.github.hermod.ser.Msg#set(int, com.github.hermod.ser.Msg[])
     */
    @Override
    public void set(final int aKey, final Msg... aMsgs) {
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
     * @see com.github.hermod.ser.Msg#setAll(com.github.hermod.ser.Msg)
     */
    @Override
    public final void setAll(final Msg aMsg) {
        // TODO to optimize with getType
        if (aMsg != null) {
            final int[] keys = aMsg.retrieveKeys();
            for (int i = 0; i < keys.length; i++) {
                set(keys[i], aMsg.get(keys[i]));
            }
        }
    }
    
    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.Msg#setAll(java.lang.Object[])
     */
    public final void setAll(final Object... anObjects) {
        //TODO to optimize
        for (int key = 0; key < anObjects.length; key++) {
            set(key, anObjects[key]);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#remove(int)
     */
    @Override
    public final void remove(final int... aKeys) {
        if (aKeys != null) {
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
    }
    
    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#clear()
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
     * removeAll.
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
        for (int key = 0; key < this.types.length; key++) {
            if (this.types[key] != NULL_TYPE) {
                if (consecutiveNullKey != 0) {
                    pos = writeVariableLength(bytes, pos, consecutiveNullKey - 1, ONE);
                    consecutiveNullKey = 0;
                }

                final byte type = this.types[key];
                bytes[pos++] = type;
                switch (type) {
                //  all fixed type
                case BYTE_TYPE:
                    bytes[pos++] = (byte) this.primitiveValues[key];
                    break;

                case SHORT_TYPE:
                    final int aShort = (short) this.primitiveValues[key];
                    bytes[pos++] = (byte) (aShort);
                    bytes[pos++] = (byte) (aShort >> EIGHT);
                    break;

                case INT_TYPE:
                    final int aInt = (int) this.primitiveValues[key];
                    bytes[pos++] = (byte) (aInt);
                    bytes[pos++] = (byte) (aInt >> EIGHT);
                    bytes[pos++] = (byte) (aInt >> SIXTEEN);
                    bytes[pos++] = (byte) (aInt >> TWENTY_FOUR);
                    break;

                case DOUBLE_TYPE:
                    final long longBits = this.primitiveValues[key];
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
                    final int anInt = (int) (this.primitiveValues[key] >> EIGHT);
                    bytes[pos++] = (byte) (anInt);
                    bytes[pos++] = (byte) (anInt >> EIGHT);
                    bytes[pos++] = (byte) (anInt >> SIXTEEN);
                    bytes[pos++] = (byte) (anInt >> TWENTY_FOUR);
                    bytes[pos++] = (byte) (this.primitiveValues[key]);
                    break;

                // case THREE_BITS_DECIMAL_TYPE:
                // final short aShort1 = (short) (this.primitiveValues[i] >> EIGHT);
                // bytes[pos++] = (byte) (aShort1);
                // bytes[pos++] = (byte) (aShort1 >> EIGHT);
                // bytes[pos++] = (byte) (this.primitiveValues[i]);
                // break;

                case FLOAT_TYPE:
                    final int intBits = (int) (this.primitiveValues[key]);
                    bytes[pos++] = (byte) (intBits);
                    bytes[pos++] = (byte) (intBits >> EIGHT);
                    bytes[pos++] = (byte) (intBits >> SIXTEEN);
                    bytes[pos++] = (byte) (intBits >> TWENTY_FOUR);
                    break;

                case LONG_TYPE:
                    final long aLong = this.primitiveValues[key];
                    bytes[pos++] = (byte) (aLong);
                    bytes[pos++] = (byte) (aLong >> EIGHT);
                    bytes[pos++] = (byte) (aLong >> SIXTEEN);
                    bytes[pos++] = (byte) (aLong >> TWENTY_FOUR);
                    bytes[pos++] = (byte) (aLong >> THIRTY_TWO);
                    bytes[pos++] = (byte) (aLong >> FORTY);
                    bytes[pos++] = (byte) (aLong >> FORTY_EIGHT);
                    bytes[pos++] = (byte) (aLong >> FIFTY_SIX);
                    break;

//                case STRING_ISO_8859_1_TYPE:
//                    final String aAsciiString = (String) this.objectValues[key];
//                    if (aAsciiString != null) {
//                        final int stringLength = aAsciiString.length();
//                        pos = writeVariableLength(bytes, pos - 1, stringLength, (stringLength == 0) ? TWO : ONE);
//                        for (int j = 0; j < stringLength; j++) {
//                            bytes[pos++] = (byte) aAsciiString.charAt(j);
//                        }
//                    }
//                    break;
//
//                case STRING_UTF_16_TYPE:
//                    final String aString = (String) this.objectValues[key];
//                    if (aString != null) {
//                        final byte[] stringBytes = aString.getBytes(UTF_16_CHARSET);
//                        final int stringLength = stringBytes.length;
//                        pos = writeVariableLength(bytes, pos - 1, stringLength, (stringLength == 0) ? TWO : ONE);
//                        System.arraycopy(stringBytes, 0, bytes, pos, stringLength);
//                        pos += stringBytes.length;
//                    }
//                    break;
                    
                case STRING_UTF_8_TYPE:
                    final String aString = (String) this.objectValues[key];
                    if (aString != null) {
                        if (checkIfEncodableInAscii(aString)) {
                            final int stringLength = aString.length();
                            pos = writeVariableLength(bytes, pos - 1, stringLength, (stringLength == 0) ? TWO : ONE);
                            for (int j = 0; j < stringLength; j++) {
                                bytes[pos++] = (byte) aString.charAt(j);
                            }
                        } else {
                          final byte[] stringBytes = aString.getBytes(UTF_8_CHARSET);
                          final int stringLength = stringBytes.length;
                          pos = writeVariableLength(bytes, pos - 1, stringLength, (stringLength == 0) ? TWO : ONE);
                          System.arraycopy(stringBytes, 0, bytes, pos, stringLength);
                          pos += stringBytes.length;
                        }
                            
                    }
                    break;

                case MSG_TYPE:
                    final Msg aMsg = (Msg) this.objectValues[key];
                    if (aMsg != null) {
                        final int msgLength = ((BytesSerializable) aMsg).getLength();
                        pos = writeVariableLength(bytes, pos - 1, msgLength, (msgLength == 0) ? TWO : ONE);
                        pos = ((BytesSerializable) aMsg).serializeToBytes(bytes, pos);
                    }
                    break;

                case ARRAY_FIXED_VALUE_TYPE:
                    if ((this.objectValues[key] != null)) {
                        if (this.objectValues[key] instanceof byte[]) {
                            final byte[] fieldBytes = (byte[]) this.objectValues[key];
                            final int bytesLength = fieldBytes.length + 1;
                            pos = writeVariableLength(bytes, pos - 1, bytesLength, (bytesLength == 0) ? TWO : ONE);
                            bytes[pos++] = BYTE_TYPE;
                            System.arraycopy(fieldBytes, 0, bytes, pos, fieldBytes.length);
                            pos += fieldBytes.length;
                        } else if (this.objectValues[key] instanceof short[]) {
                            final short[] shorts = (short[]) this.objectValues[key];
                            final int shortsLength = (shorts.length << 1) + 1;
                            pos = writeVariableLength(bytes, pos - 1, shortsLength, (shortsLength == 0) ? TWO : ONE);
                            bytes[pos++] = SHORT_TYPE;
                            for (final short currentShort : shorts) {
                                bytes[pos++] = (byte) (currentShort);
                                bytes[pos++] = (byte) (currentShort >> EIGHT);
                            }
                        } else if (this.objectValues[key] instanceof int[]) {
                            final int[] ints = (int[]) this.objectValues[key];
                            final int intsLength = (ints.length << TWO) + 1;
                            pos = writeVariableLength(bytes, pos - 1, intsLength, (intsLength == 0) ? TWO : ONE);
                            bytes[pos++] = INT_TYPE;
                            for (final int currentInt : ints) {
                                bytes[pos++] = (byte) (currentInt);
                                bytes[pos++] = (byte) (currentInt >> EIGHT);
                                bytes[pos++] = (byte) (currentInt >> SIXTEEN);
                                bytes[pos++] = (byte) (currentInt >> TWENTY_FOUR);
                            }
                        } else if (this.objectValues[key] instanceof long[]) {
                            final long[] longs = (long[]) this.objectValues[key];
                            final int longsLength = (longs.length << THREE) + 1;
                            pos = writeVariableLength(bytes, pos - 1, longsLength, (longsLength == 0) ? TWO : ONE);
                            bytes[pos++] = LONG_TYPE;
                            for (final long currentLong : longs) {
                                bytes[pos++] = (byte) (currentLong);
                                bytes[pos++] = (byte) (currentLong >> EIGHT);
                                bytes[pos++] = (byte) (currentLong >> SIXTEEN);
                                bytes[pos++] = (byte) (currentLong >> TWENTY_FOUR);
                                bytes[pos++] = (byte) (currentLong >> THIRTY_TWO);
                                bytes[pos++] = (byte) (currentLong >> FORTY);
                                bytes[pos++] = (byte) (currentLong >> FORTY_EIGHT);
                                bytes[pos++] = (byte) (currentLong >> FIFTY_SIX);
                            }
                        } else if (this.objectValues[key] instanceof double[]) {
                            final double[] doubles = (double[]) this.objectValues[key];
                            final int doublesLength = (doubles.length << THREE) + 1;
                            pos = writeVariableLength(bytes, pos - 1, doublesLength, (doublesLength == 0) ? TWO : ONE);
                            bytes[pos++] = DOUBLE_TYPE;
                            for (final double currentDouble : doubles) {
                                final long currentLong = Double.doubleToLongBits(currentDouble);
                                bytes[pos++] = (byte) (currentLong);
                                bytes[pos++] = (byte) (currentLong >> EIGHT);
                                bytes[pos++] = (byte) (currentLong >> SIXTEEN);
                                bytes[pos++] = (byte) (currentLong >> TWENTY_FOUR);
                                bytes[pos++] = (byte) (currentLong >> THIRTY_TWO);
                                bytes[pos++] = (byte) (currentLong >> FORTY);
                                bytes[pos++] = (byte) (currentLong >> FORTY_EIGHT);
                                bytes[pos++] = (byte) (currentLong >> FIFTY_SIX);
                            }
                        } else if (this.objectValues[key] instanceof float[]) {
                            final float[] floats = (float[]) this.objectValues[key];
                            final int floatsLength = (floats.length << TWO) + 1;
                            pos = writeVariableLength(bytes, pos - 1, floatsLength, (floatsLength == 0) ? TWO : ONE);
                            bytes[pos++] = FLOAT_TYPE;
                            for (final float currentFloat : floats) {
                                final int currentInt = Float.floatToIntBits(currentFloat);
                                bytes[pos++] = (byte) (currentInt);
                                bytes[pos++] = (byte) (currentInt >> EIGHT);
                                bytes[pos++] = (byte) (currentInt >> SIXTEEN);
                                bytes[pos++] = (byte) (currentInt >> TWENTY_FOUR);
                            }
                        } else {
                            // TODO should not occur, just log
                        }
                    }
                    break;

                case ARRAY_VARIABLE_VALUE_TYPE:
                    int arrayVariableValueLength = 0;
                    if (this.objectValues[key] != null && this.objectValues[key] instanceof Object[]) {
                        final Object[] objects = ((Object[]) this.objectValues[key]);
                        // TODO fix, check if the the first key not null (here we consider the first one is always not null)
                        final BytesSerializable msg = serializeArrayVariableValueAsMsg(objects);
                        arrayVariableValueLength = msg.getLength();
                        pos = writeVariableLength(bytes, pos - 1, arrayVariableValueLength, (arrayVariableValueLength == 0) ? TWO : ONE);
                        pos = msg.serializeToBytes(bytes, pos);
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
     * serializeArrayVariableValueAsMsg.
     * 
     * @param objects
     * @return
     */
    private BytesSerializable serializeArrayVariableValueAsMsg(final Object[] objects) {
        final IndexedPrimitivesObjectsMsg msg = new IndexedPrimitivesObjectsMsg();
        if (objects != null) {
            int i = 0;
            boolean firstElementSet = false;
            //  Force first element without compression
            while (i < objects.length && objects[i] != null && !firstElementSet) {
                msg.set(i, objects[i], true);
                i++;
                firstElementSet = true;
            }
            // Set the other elements with potential compression
            while (i < objects.length) {
                msg.set(i, objects[i]);
                i++;
            }
        }
        return msg;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.BytesSerializable#deserializeFromBytes(byte[], int, int)
     */
    public final void deserializeFromBytes(final byte[] bytes, final int offset, final int length) {
        int pos = offset;
        int key = 0;

        // find max key
        // not need for feed
        // Calculate max key value
        // TODO to optimize with a try catch, lastPos and relaunch readFrom
        while (pos < offset + length) {
            if ((bytes[pos] & TYPE_MASK) == NULL_TYPE) {
                final int lengthMask = bytes[pos++] & LENGTH_MASK;
                key += (((lengthMask < LENGTH_ENCODED_IN_A_BIT) ? lengthMask : (lengthMask == LENGTH_ENCODED_IN_A_BIT) ? bytes[pos++]
                        : (bytes[pos++] & XFF) | ((bytes[pos++] & XFF) << EIGHT) | ((bytes[pos++] & XFF) << SIXTEEN)
                                | ((bytes[pos++] & XFF) << TWENTY_FOUR))) + 1;
            } else {
                final int lengthMask = bytes[pos++] & LENGTH_MASK;
                // TODO to optimize
                pos += (((lengthMask < LENGTH_ENCODED_IN_A_BIT) ? lengthMask
                        : (lengthMask == LENGTH_ENCODED_IN_A_BIT) ? bytes[pos] : ((bytes[pos] & XFF) | ((bytes[pos + ONE] & XFF) << EIGHT)
                                | ((bytes[pos + TWO] & XFF) << SIXTEEN) | ((bytes[pos + THREE] & XFF) << TWENTY_FOUR))));
                pos += (lengthMask < LENGTH_ENCODED_IN_A_BIT) ? 0 : (lengthMask == LENGTH_ENCODED_IN_A_BIT) ? ONE : FOUR;

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
                key += ((sizeMask < LENGTH_ENCODED_IN_A_BIT) ? sizeMask : (sizeMask == LENGTH_ENCODED_IN_A_BIT) ? bytes[pos++] : (bytes[pos++] & XFF)
                        | ((bytes[pos++] & XFF) << EIGHT) | ((bytes[pos++] & XFF) << SIXTEEN) | ((bytes[pos++] & XFF) << TWENTY_FOUR)) + 1;
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
                            | (((long) bytes[pos++] & XFF) << SIXTEEN) | (((long) bytes[pos++] & XFF) << TWENTY_FOUR)
                            | (((long) bytes[pos++] & XFF) << THIRTY_TWO) | (((long) bytes[pos++] & XFF) << FORTY)
                            | (((long) bytes[pos++] & XFF) << FORTY_EIGHT) | (((long) bytes[pos++] & XFF) << FIFTY_SIX));
                    break;

                case FIVE_BITS_DECIMAL_TYPE:
                    this.primitiveValues[key] = (((long) bytes[pos++] & XFF) << EIGHT) | (((long) bytes[pos++] & XFF) << SIXTEEN)
                            | (((long) bytes[pos++] & XFF) << TWENTY_FOUR) | (((long) bytes[pos++] & XFF) << THIRTY_TWO)
                            | ((byte) bytes[pos++] & XFF);
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
                            | (((long) bytes[pos++] & XFF) << SIXTEEN) | (((long) bytes[pos++] & XFF) << TWENTY_FOUR)
                            | (((long) bytes[pos++] & XFF) << THIRTY_TWO) | (((long) bytes[pos++] & XFF) << FORTY)
                            | (((long) bytes[pos++] & XFF) << FORTY_EIGHT) | (((long) bytes[pos++] & XFF) << FIFTY_SIX);
                    break;

                // All non fixed type
                default:
                    final byte typeMask = (byte) (type & TYPE_MASK);
                    this.types[key] = typeMask;
                    final int lengthMask = (LENGTH_MASK & type);
                    final int fieldLength = (lengthMask < LENGTH_ENCODED_IN_A_BIT) ? lengthMask
                            : (lengthMask == LENGTH_ENCODED_IN_A_BIT) ? bytes[pos++] : (bytes[pos++] & XFF) | ((bytes[pos++] & XFF) << EIGHT)
                                    | ((bytes[pos++] & XFF) << SIXTEEN) | ((bytes[pos++] & XFF) << TWENTY_FOUR);

                    if (lengthMask != 0) {
                        switch (typeMask) {
//                        case STRING_ISO_8859_1_TYPE:
//                            final char[] chars = new char[fieldLength];
//                            for (int i = 0; i < fieldLength; i++) {
//                                chars[i] = (char) bytes[pos++];
//                            }
//                            this.objectValues[key] = new String(chars);
//                            break;
//
//                        case STRING_UTF_16_TYPE:
//                            this.objectValues[key] = new String(bytes, pos, fieldLength, UTF_16_CHARSET);
//                            pos += fieldLength;
//                            break;
                            
                        case STRING_UTF_8_TYPE:
                            this.objectValues[key] = new String(bytes, pos, fieldLength, UTF_8_CHARSET);
                            pos += fieldLength;
                            break;

                        case MSG_TYPE:
                            final IndexedPrimitivesObjectsMsg msg = new IndexedPrimitivesObjectsMsg();
                            msg.deserializeFromBytes(bytes, pos, fieldLength);
                            pos += fieldLength;
                            this.objectValues[key] = msg;
                            break;

                        case ARRAY_FIXED_VALUE_TYPE:
                            final byte arrayType = bytes[pos++];
                            final int fixedArrayLength = fieldLength - ONE;
                            // For Fixed typed
                            switch (arrayType) {
                            case BYTE_TYPE:
                                final byte[] byteArray = new byte[fixedArrayLength];
                                System.arraycopy(bytes, pos, byteArray, 0, fixedArrayLength);
                                pos += fixedArrayLength;
                                this.set(key, byteArray);
                                break;
                            case SHORT_TYPE:
                                final short[] shorts = new short[fixedArrayLength >> ONE];
                                for (int i = 0; i < shorts.length; i++) {
                                    shorts[i] = (short) ((((short) bytes[pos++] & XFF)) | (((short) bytes[pos++] & XFF) << EIGHT));
                                }
                                this.set(key, shorts);
                                break;
                            case INT_TYPE:
                                final int[] ints = new int[fixedArrayLength >> TWO];
                                for (int i = 0; i < ints.length; i++) {
                                    ints[i] = (((int) bytes[pos++] & XFF)) | (((int) bytes[pos++] & XFF) << EIGHT)
                                            | (((int) bytes[pos++] & XFF) << SIXTEEN) | (((int) bytes[pos++] & XFF) << TWENTY_FOUR);
                                }
                                this.set(key, ints);
                                break;
                            case DOUBLE_TYPE:
                                final double[] doubles = new double[fixedArrayLength >> THREE];
                                for (int i = 0; i < doubles.length; i++) {
                                    doubles[i] = Double.longBitsToDouble((((long) bytes[pos++] & XFF) | (((long) bytes[pos++] & XFF) << EIGHT)
                                            | (((long) bytes[pos++] & XFF) << SIXTEEN) | (((long) bytes[pos++] & XFF) << TWENTY_FOUR)
                                            | (((long) bytes[pos++] & XFF) << THIRTY_TWO) | (((long) bytes[pos++] & XFF) << FORTY)
                                            | (((long) bytes[pos++] & XFF) << FORTY_EIGHT) | (((long) bytes[pos++] & XFF) << FIFTY_SIX)));
                                }
                                this.set(key, doubles);
                                break;
                            case LONG_TYPE:
                                final long[] longs = new long[fixedArrayLength >> THREE];
                                for (int i = 0; i < longs.length; i++) {
                                    longs[i] = (((long) bytes[pos++] & XFF)) | (((long) bytes[pos++] & XFF) << EIGHT)
                                            | (((long) bytes[pos++] & XFF) << SIXTEEN) | (((long) bytes[pos++] & XFF) << TWENTY_FOUR)
                                            | (((long) bytes[pos++] & XFF) << THIRTY_TWO) | (((long) bytes[pos++] & XFF) << FORTY)
                                            | (((long) bytes[pos++] & XFF) << FORTY_EIGHT) | (((long) bytes[pos++] & XFF) << FIFTY_SIX);
                                }
                                this.set(key, longs);
                                break;
                            case FLOAT_TYPE:
                                final float[] floats = new float[fixedArrayLength >> TWO];
                                for (int i = 0; i < floats.length; i++) {
                                    floats[i] = Float.intBitsToFloat((((int) bytes[pos++] & XFF)) | (((int) bytes[pos++] & XFF) << EIGHT)
                                            | (((int) bytes[pos++] & XFF) << SIXTEEN) | (((int) bytes[pos++] & XFF) << TWENTY_FOUR));
                                }
                                this.set(key, floats);
                                break;
                            default:
                                break;
                            }
                            break;

                        case ARRAY_VARIABLE_VALUE_TYPE:

                            // For variable type
                            // TODO
                            if (lengthMask != 0) {
                                final IndexedPrimitivesObjectsMsg arrayAsMsg = new IndexedPrimitivesObjectsMsg();
                                arrayAsMsg.deserializeFromBytes(bytes, pos, fieldLength);
                                pos += fieldLength;
                                final int variableArrayLength = arrayAsMsg.countKeys();
                                // TODO fix, check if the the first key not null (here we consider the first one is always not null)
                                final Type arraytype = arrayAsMsg.getType(0);
                                final byte typeWithLength = arrayAsMsg.getTypeWithLengthAsByte(0);
                                switch (arraytype) {
                                case INTEGER:
                                    switch (typeWithLength) {
                                    case BYTE_TYPE:
                                        final Byte[] byteArray = new Byte[variableArrayLength];
                                        for (int arrayKey = 0; arrayKey < variableArrayLength; arrayKey++) {
                                            byteArray[arrayKey] = arrayAsMsg.getAsNullableByte(arrayKey);
                                        }
                                        this.objectValues[key] = byteArray;
                                        break;
                                    case SHORT_TYPE:
                                        final Short[] shorts = new Short[variableArrayLength];
                                        for (int arrayKey = 0; arrayKey < variableArrayLength; arrayKey++) {
                                            shorts[arrayKey] = arrayAsMsg.getAsNullableShort(arrayKey);
                                        }
                                        this.objectValues[key] = shorts;
                                        break;
                                    case INT_TYPE:
                                        final Integer[] integers = new Integer[variableArrayLength];
                                        for (int arrayKey = 0; arrayKey < variableArrayLength; arrayKey++) {
                                            integers[arrayKey] = arrayAsMsg.getAsNullableInteger(arrayKey);
                                        }
                                        this.objectValues[key] = integers;
                                        break;
                                    case LONG_TYPE:
                                        final Long[] longs = new Long[variableArrayLength];
                                        for (int arrayKey = 0; arrayKey < variableArrayLength; arrayKey++) {
                                            longs[arrayKey] = arrayAsMsg.getAsNullableLong(arrayKey);
                                        }
                                        this.objectValues[key] = longs;
                                        break;
                                    default:
                                        break;
                                    }
                                    break;

                                case DECIMAL:
                                    switch (typeWithLength) {
                                    case FIVE_BITS_DECIMAL_TYPE:
                                    case DOUBLE_TYPE:
                                        final Double[] doubles = new Double[variableArrayLength];
                                        for (int arrayKey = 0; arrayKey < variableArrayLength; arrayKey++) {
                                            doubles[arrayKey] = arrayAsMsg.getAsNullableDouble(arrayKey);
                                        }
                                        this.objectValues[key] = doubles;
                                        break;

                                    case FLOAT_TYPE:
                                        final Float[] floats = new Float[variableArrayLength];
                                        for (int arrayKey = 0; arrayKey < variableArrayLength; arrayKey++) {
                                            floats[arrayKey] = arrayAsMsg.getAsNullableFloat(arrayKey);
                                        }
                                        this.objectValues[key] = floats;
                                        break;

                                    default:
                                        break;
                                    }

                                    break;

                                case STRING_UTF8:   
                                    final String[] strings = new String[variableArrayLength];
                                    for (int arrayKey = 0; arrayKey < variableArrayLength; arrayKey++) {
                                        strings[arrayKey] = arrayAsMsg.getAsString(arrayKey);
                                    }
                                    this.objectValues[key] = strings;
                                    break;

                                case MSG:
                                    final Msg[] msgs = new IndexedPrimitivesObjectsMsg[variableArrayLength];
                                    for (int arrayKey = 0; arrayKey < variableArrayLength; arrayKey++) {
                                        msgs[arrayKey] = arrayAsMsg.getAsMsg(arrayKey);
                                    }
                                    this.objectValues[key] = msgs;
                                    break;

                                default:
                                    // TODO manage the other types like ARRAY_FIXED_VALUE_TYPE or ARRAY_VARIABLE_VALUE_TYPE
                                    break;
                                }
                            }

                        default:
                            break;
                        }
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
     * @see com.github.hermod.ser.ByteBufferSerializable#serializeToByteBuffer()
     */
    @Override
    public ByteBuffer serializeToByteBuffer()
    {
        final byte[] bytes = serializeToBytes();
        final ByteBuffer aDestByteBuffer = ByteBuffer.allocate(bytes.length);
        aDestByteBuffer.put(bytes);
        return aDestByteBuffer;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.ByteBufferSerializable#serializeToByteBuffer(java.nio.ByteBuffer)
     */
    @Override
    public void serializeToByteBuffer(final ByteBuffer aDestByteBuffer) {
        aDestByteBuffer.put(this.serializeToBytes());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.ByteBufferSerializable#deserializeFromByteBuffer(java.nio.ByteBuffer, int)
     */
    @Override
    public void deserializeFromByteBuffer(final ByteBuffer aSrcByteBuffer) {
        final byte[] bytes = new byte[aSrcByteBuffer.remaining()];
        aSrcByteBuffer.get(bytes);
        this.deserializeFromBytes(bytes, 0, bytes.length);
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
                    length += getVariableLength(consecutiveNullKey, ONE);
                    consecutiveNullKey = 0;
                }
            } else {
                consecutiveNullKey++;
            }
        }
        return length;
    }

    /**
     * writeVariableLength.
     * 
     * @param bytes
     * @param pos
     * @param length
     * @param forceEncodingAtLeastOn
     * @return
     */
    private int writeVariableLength(final byte[] bytes, int pos, final int length, final int forceEncodingAtLeastOn) {
        if (length < LENGTH_ENCODED_IN_A_BIT && forceEncodingAtLeastOn == ONE) {
            bytes[pos++] |= (byte) length;
        } else {
            final boolean isEncodedInAnInt = (length > Byte.MAX_VALUE) || (forceEncodingAtLeastOn == FIVE);
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
     * getVariableLength.
     * 
     * @param length
     * @param forceEncodingAtLeastOn
     * @return
     */
    private int getVariableLength(final int length, final int forceEncodingAtLeastOn) {
        return (length < LENGTH_ENCODED_IN_A_BIT && (forceEncodingAtLeastOn == ONE)) ? ONE
                : (length > Byte.MAX_VALUE || forceEncodingAtLeastOn == FIVE) ? FIVE : TWO;
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
            case STRING_UTF_8_TYPE:
                final int stringUtf8length = (this.objectValues[key] != null) ? ((String) this.objectValues[key]).getBytes(UTF_8_CHARSET).length
                        : 0;
                return getVariableLength(stringUtf8length, (stringUtf8length == 0) ? TWO : ONE) + stringUtf8length;

            case MSG_TYPE:
                // TODO change to Serializer.getLength();
                final int msgLength = (this.objectValues[key] != null) ? ((BytesSerializable) this.objectValues[key]).getLength() : 0;
                return getVariableLength(msgLength, (msgLength == 0) ? TWO : ONE) + msgLength;

            case ARRAY_FIXED_VALUE_TYPE:
                int arrayFixedValueLength = ONE;
                if ((this.objectValues[key] != null)) {
                    if (this.objectValues[key] instanceof byte[]) {
                        final int bytesLength = ((byte[]) this.objectValues[key]).length + 1;
                        arrayFixedValueLength = getVariableLength(bytesLength, (bytesLength == 0) ? TWO : ONE) + bytesLength;
                    } else if (this.objectValues[key] instanceof short[]) {
                        final int shortsLength = ((short[]) this.objectValues[key]).length << ONE + 1;
                        arrayFixedValueLength = getVariableLength(shortsLength, (shortsLength == 0) ? TWO : ONE) + shortsLength;
                    } else if (this.objectValues[key] instanceof int[]) {
                        final int intsLength = ((int[]) this.objectValues[key]).length << TWO + 1;
                        arrayFixedValueLength = getVariableLength(intsLength, (intsLength == 0) ? TWO : ONE) + intsLength;
                    } else if (this.objectValues[key] instanceof long[]) {
                        final int longsLength = ((long[]) this.objectValues[key]).length << THREE + 1;
                        arrayFixedValueLength = getVariableLength(longsLength, (longsLength == 0) ? TWO : ONE) + longsLength;
                    } else if (this.objectValues[key] instanceof double[]) {
                        final int doublesLength = ((double[]) this.objectValues[key]).length << THREE + 1;
                        arrayFixedValueLength = getVariableLength(doublesLength, (doublesLength == 0) ? TWO : ONE) + doublesLength;
                    } else if (this.objectValues[key] instanceof float[]) {
                        final int floatsLength = ((float[]) this.objectValues[key]).length << TWO + 1;
                        arrayFixedValueLength = getVariableLength(floatsLength, (floatsLength == 0) ? TWO : ONE) + floatsLength;
                    }
                }
                return arrayFixedValueLength;

            case ARRAY_VARIABLE_VALUE_TYPE:
                int arrayVariableValueLength = ONE;
                if (this.objectValues[key] != null && this.objectValues[key] instanceof Object[]) {
                    final Object[] objects = ((Object[]) this.objectValues[key]);
                    final BytesSerializable arrayVariableValueAsMsg = serializeArrayVariableValueAsMsg(objects);
                    final int arrayVariableValueAsMsgLength = arrayVariableValueAsMsg.getLength();
                    arrayVariableValueLength = getVariableLength(arrayVariableValueAsMsgLength, (arrayVariableValueAsMsgLength == 0) ? TWO : ONE)
                            + arrayVariableValueAsMsgLength;
                }
                return arrayVariableValueLength;

            default:
                return ONE;
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
        return Msgs.serializeToJson(this);
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Msgs.hashCode(this);
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object aObj) {
        return Msgs.equals(aObj, this);
    }
    
    

}
