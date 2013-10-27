package com.github.hermod.ser.impl;

import static com.github.hermod.ser.Types.ARRAY_FIXED_VALUE_TYPE;
import static com.github.hermod.ser.Types.ARRAY_VARIABLE_VALUE_TYPE;
import static com.github.hermod.ser.Types.DECIMAL_TYPE;
import static com.github.hermod.ser.Types.INTEGER_TYPE;
import static com.github.hermod.ser.Types.MSG_TYPE;
import static com.github.hermod.ser.Types.STRING_ISO_8859_1_TYPE;
import static com.github.hermod.ser.Types.STRING_UTF_16_TYPE;
import static com.github.hermod.ser.impl.Msgs.BYTE_TYPE;
import static com.github.hermod.ser.impl.Msgs.DEFAULT_MAX_KEY;
import static com.github.hermod.ser.impl.Msgs.DOUBLE_TYPE;
import static com.github.hermod.ser.impl.Msgs.ERROR_WHEN_KEY_NOT_PRESENT;
import static com.github.hermod.ser.impl.Msgs.FIVE_BITS_DECIMAL_TYPE;
import static com.github.hermod.ser.impl.Msgs.FLOAT_TYPE;
import static com.github.hermod.ser.impl.Msgs.INT_TYPE;
import static com.github.hermod.ser.impl.Msgs.LONG_TYPE;
import static com.github.hermod.ser.impl.Msgs.SHORT_TYPE;

import java.nio.ByteBuffer;

import checkers.nullness.quals.Nullable;

import com.github.hermod.ser.Msg;
import com.github.hermod.ser.Precision;
import com.github.hermod.ser.Type;

/**
 * <p>IndexedObjectsMsg. </p>
 * 
 * @author anavarro - Oct 23, 2013
 * 
 */
public class IndexedObjectsMsg implements Msg {

    private Object[] objectValues;

    // TODO set as private
    private IndexedObjectsMsg() {
        this(DEFAULT_MAX_KEY);
    }

    /**
     * Constructor.
     * 
     * @param aDescriptor
     */
    // TODO set as private
    private IndexedObjectsMsg(final int aKeyMax) {
        this.objectValues = new Object[aKeyMax + 1];
    }

    /**
     * Constructor.
     * 
     * @param aMsg
     */
    // TODO set as private
    private IndexedObjectsMsg(final Msg aMsg) {
        if (aMsg instanceof IndexedObjectsMsg) {
            final IndexedObjectsMsg indexedObjectsMsg = (IndexedObjectsMsg) aMsg;
            this.objectValues = new Object[indexedObjectsMsg.objectValues.length];
            System.arraycopy(indexedObjectsMsg.objectValues, 0, this.objectValues, 0, indexedObjectsMsg.objectValues.length);
        } else {
            // TODO to optimize with getType
            if (aMsg != null) {
                final int[] keys = aMsg.retrieveKeys();
                final int aKeyMax = keys[keys.length - 1];
                this.objectValues = new Object[aKeyMax + 1];
                for (int i = 0; i < keys.length; i++) {
                    set(keys[i], aMsg.get(keys[i]));
                }
            } else {
                this.objectValues = new Object[DEFAULT_MAX_KEY + 1];
            }
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
            final int nextPow2 = Msgs.calculateNextPowerOf2(keyMax + 1);
            final Object[] destObjectValues = new Object[nextPow2];
            System.arraycopy(this.objectValues, 0, destObjectValues, 0, this.objectValues.length);
            this.objectValues = destObjectValues;
        }
    }

    /**
     * newMsg.
     * 
     * @return
     */
    public static IndexedObjectsMsg create() {
        return new IndexedObjectsMsg(DEFAULT_MAX_KEY);
    }

    /**
     * newMsg.
     * 
     * @param keyMax
     * @return
     */
    public static IndexedObjectsMsg createWithKeyMax(final int keyMax) {
        return new IndexedObjectsMsg(keyMax);
    }

    /**
     * createFromBytes.
     * 
     * @param bytes
     * @param offset
     * @param length
     * @return
     */
    public static IndexedObjectsMsg createFromBytes(final byte[] aSrcBytes, final int offset, final int length) {
        final IndexedObjectsMsg msg = new IndexedObjectsMsg();
        DefaultMsgSerializer.get().deserializeFromBytes(aSrcBytes, offset, length, msg);
        return msg;
    }

    /**
     * createFromByteBuffer.
     * 
     * @param byteBuffer
     * @return
     */
    public static IndexedObjectsMsg createFromByteBuffer(final ByteBuffer aSrcByteBuffer) {
        final IndexedObjectsMsg msg = new IndexedObjectsMsg();
        DefaultMsgSerializer.get().deserializeFromByteBuffer(aSrcByteBuffer, msg);
        return msg;
    }

    /**
     * createFromMsg.
     * 
     * @param aMsg
     * @return
     */
    public static IndexedObjectsMsg createFromMsg(final Msg aMsg) {
        return new IndexedObjectsMsg(aMsg);
    }

    /**
     * createFromValues.
     * 
     * @param values
     * @return
     */
    public static IndexedObjectsMsg createFromValues(final Object... values) {
        final IndexedObjectsMsg msg = new IndexedObjectsMsg(values.length);
        msg.setAll(values);
        return msg;
    }
    
    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.Msg#isEmpty()
     */
    @Override
    public final boolean isEmpty() {
        for (final Object value : this.objectValues) {
            if (value != null) {
                return true;
            }
        }            
        return false;
    }
    
    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.Msg#retrieveKeys()
     */
    @Override
    public final int[] retrieveKeys() {
        final int[] keys = new int[countKeys()];
        int index = 0;
        for (int i = 0; i < this.objectValues.length; i++) {
            if (this.objectValues != null) {
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
    @Override
    public final int retrieveKeyMax() {
        for (int i = this.objectValues.length; i-- != 0; ) {
            if (this.objectValues != null) {
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
    public final int countKeys() {
        int nbKey = 0;
        for (int i = 0; i < this.objectValues.length; i++) {
            if (this.objectValues != null) {
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
    public final Type getType(final int aKey) {
        try {
            return Type.valueOf(this.objectValues[aKey].getClass());
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
            return Type.valueOf(this.objectValues[aKey].getClass()).getId();
        } catch (ArrayIndexOutOfBoundsException e) {
            return Type.NULL.getId();
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#isArray(int)
     */
    @Override
    public final boolean isArray(final int aKey) {
         return (getType(aKey).equals(Type.ARRAY_FIXED_VALUE) || getType(aKey).equals(Type.ARRAY_VARIABLE_VALUE)) ? true : false;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getArrayLength(int)
     */
    @Override
    public final int getArrayLength(final int aKey) {
        try {
            return (this.objectValues[aKey] instanceof Object[]) ?
                 ((Object[]) this.objectValues[aKey]).length : 0;
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
            return (this.objectValues[aKey] != null) ? true : false;
        } catch (final ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getAsByte(int)
     */
    @Override
    public final boolean getAsBoolean(final int aKey) {
        final Boolean value = getAsNullableBoolean(aKey);
        if (value != null) {
            return value.booleanValue();
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
            final Object value = this.objectValues[aKey];
            if (value instanceof Boolean) {
                return (Boolean) this.objectValues[aKey];
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
        }
        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getAsByte(int)
     */
    @Override
    public final byte getAsByte(final int aKey) {
        final Byte value = getAsNullableByte(aKey);
        if (value != null) {
            return value.byteValue();
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
    Byte getAsNullableByte(final int aKey) {
        try {
            final Object value = this.objectValues[aKey];
            if (value instanceof Byte) {
                return (Byte) this.objectValues[aKey];
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
        }
        return null;
    }

    /**
     * getAsShort.
     * 
     * @param aKey
     * @return
     */
    @Override
    public final short getAsShort(final int aKey) {
        final Short value = getAsNullableShort(aKey);
        if (value != null) {
            return value.shortValue();
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
    Short getAsNullableShort(final int aKey) {
        try {
            final Object value = this.objectValues[aKey];
            if (value instanceof Short) {
                return (Short) this.objectValues[aKey];
            }
            if (value instanceof Byte) {
                return Short.valueOf(((Byte) this.objectValues[aKey]).shortValue());
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
        }
        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getAsInt(int)
     */
    @Override
    public final int getAsInt(final int aKey) {
        final Integer value = getAsNullableInteger(aKey);
        if (value != null) {
            return value.intValue();
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
    public final Integer getAsNullableInteger(final int aKey) {
        try {
            final Object value = this.objectValues[aKey];
            if (value instanceof Integer) {
                return (Integer) this.objectValues[aKey];
            }
            if (value instanceof Short) {
                return Integer.valueOf(((Short) this.objectValues[aKey]).intValue());
            }
            if (value instanceof Byte) {
                return Integer.valueOf(((Byte) this.objectValues[aKey]).intValue());
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
        }
        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getAsLong(int)
     */
    @Override
    public final long getAsLong(final int aKey) {
        final Long value = getAsNullableLong(aKey);
        if (value != null) {
            return value.longValue();
        }
        throw new IllegalArgumentException(String.format(ERROR_WHEN_KEY_NOT_PRESENT, aKey));
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getAsNullableLong(int)
     */
    @Override
    public final Long getAsNullableLong(final int aKey) {
        try {
            final Object value = this.objectValues[aKey];
            if (value instanceof Long) {
                return (Long) this.objectValues[aKey];
            }
            if (value instanceof Integer) {
                return Long.valueOf(((Integer) this.objectValues[aKey]).longValue());
            }
            if (value instanceof Short) {
                return Long.valueOf(((Short) this.objectValues[aKey]).longValue());
            }
            if (value instanceof Byte) {
                return Long.valueOf(((Byte) this.objectValues[aKey]).longValue());
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
        }
        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getAsFloat(int)
     */
    @Override
    public final float getAsFloat(final int aKey) {
        final Float value = getAsNullableFloat(aKey);
        if (value != null) {
            return value.floatValue();
        }
        throw new IllegalArgumentException(String.format(ERROR_WHEN_KEY_NOT_PRESENT, aKey));
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getAsNullableFloat(int)
     */
    @Override
    public final Float getAsNullableFloat(final int aKey) {
        try {
            final Object value = this.objectValues[aKey];
            if (value instanceof Float) {
                return (Float) this.objectValues[aKey];
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
        }
        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getAsDouble(int)
     */
    @Override
    public final double getAsDouble(final int aKey) {
        final Double value = getAsNullableDouble(aKey);
        if (value != null) {
            return value.doubleValue();
        }
        throw new IllegalArgumentException(String.format(ERROR_WHEN_KEY_NOT_PRESENT, aKey));
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getAsNullableDouble(int)
     */
    @Override
    public final Double getAsNullableDouble(final int aKey) {
        try {
            final Object value = this.objectValues[aKey];
            if (value instanceof Double) {
                return (Double) this.objectValues[aKey];
            }
            if (value instanceof Float) {
                return ((Float) this.objectValues[aKey]).doubleValue();
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
        }
        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getAsString(int)
     */
    @Override
    public final String getAsString(final int aKey) {
        try {
            final Object value = this.objectValues[aKey];
            if (value instanceof String) {
                return (String) this.objectValues[aKey];
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
        }
        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getAsMsg(int)
     */
    @Override
    public final Msg getAsMsg(final int aKey) {
        try {
            final Object value = this.objectValues[aKey];
            if (value instanceof Msg) {
                return new IndexedObjectsMsg((Msg) this.objectValues[aKey]);
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
        }
        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getAsMsg(int, com.github.hermod.ser.Msg)
     */
    @Override
    public final void getAsMsg(final int aKey, Msg aDestMsg) {
        try {
            final Object value = this.objectValues[aKey];
            if (value instanceof Msg) {
                aDestMsg.setAll((Msg) this.objectValues[aKey]);
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getAsObject(int)
     */
    @Override
    public final Object get(final int aKey) {
        return get(aKey, Object.class);
    }

    /**
     * getAsObject.
     *
     * @param aKey
     * @param aClazz
     * @return
     */
    @Override
    public final <T> T get(final int aKey, final Class<T> aClazz) {
        try {
            final byte type = getTypeAsByte(aKey);
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

            case STRING_ISO_8859_1_TYPE:
            case STRING_UTF_16_TYPE:
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

    @Override
    public boolean[] getAsBooleans(int aKey) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not Yet Implemented.");
        //return null;
        
    }

    @Override
    public Boolean[] getAsNullableBooleans(int aKey) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not Yet Implemented.");
        //return null;
    }

    @Override
    public byte[] getAsBytes(int aKey) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not Yet Implemented.");
        //return null;
    }

    @Override
    public Byte[] getAsNullableBytes(int aKey) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not Yet Implemented.");
        //return null;
    }

    @Override
    public short[] getAsShorts(int aKey) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not Yet Implemented.");
        //return null;
    }

    @Override
    public Short[] getAsNullableShorts(int aKey) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not Yet Implemented.");
        //return null;
    }

    @Override
    public int[] getAsInts(int aKey) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not Yet Implemented.");
        //return null;
    }

    @Override
    public Integer[] getAsNullableIntegers(int aKey) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not Yet Implemented.");
        //return null;
    }

    @Override
    public long[] getAsLongs(int aKey) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not Yet Implemented.");
        //return null;
    }

    @Override
    public Long[] getAsNullableLongs(int aKey) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not Yet Implemented.");
        //return null;
    }

    @Override
    public float[] getAsFloats(int aKey) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not Yet Implemented.");
        //return null;
    }

    @Override
    public Float[] getAsNullableFloats(int aKey) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not Yet Implemented.");
        //return null;
    }

    @Override
    public double[] getAsDoubles(int aKey) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not Yet Implemented.");
        //return null;
    }

    @Override
    public Double[] getAsNullableDoubles(int aKey) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not Yet Implemented.");
        //return null;
    }

    @Override
    public String[] getAsStrings(int aKey) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not Yet Implemented.");
        //return null;
    }

    @Override
    public Msg[] getAsMsgs(int aKey) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not Yet Implemented.");
        //return null;
    }

    @Override
    public void getAsMsgs(int aKey, Msg... aDestMsgs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Override
    public final @Nullable
    Object[] getAsObjects(final int aKey) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not Yet Implemented.");
        //return null;
    }
    
    @Override
    public final @Nullable
    void getAsObjects(final int aKey, final Object... aDestObjects ) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not Yet Implemented.");
        //return null;
    }

    @Override
    public Msg getAllAsMsg() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not Yet Implemented.");
        //return null;
    }

    @Override
    public Object[] getAllAsObjects() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not Yet Implemented.");
        //return null;
    }

    @Override
    public void getAllAsObjects(Object... aObjects) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not Yet Implemented.");
        //return null;
    }

    @Override
    public void set(int aKey, boolean aBoolean) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, Boolean aBoolean) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, byte aByte) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, Byte aByte) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, short aShort) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, Short aShort) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, int aInt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, Integer aInt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, long aLong) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, Long aLong) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, float aFloat) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, Float aFloat) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, double aDouble) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, Double aDouble) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, double aDouble, int aNbDigit) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, double aDouble, Precision aPrecision) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, Double aDouble, int aNbDigit) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, Double aDouble, Precision aPrecision) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, String aString) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, String aString, boolean aForceIso88591Charset) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, Msg aMsg) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, Object aAnObject) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, boolean... aBooleans) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, Boolean... aBooleans) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, byte... aBytes) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, Byte... aBytes) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, short... aShorts) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, Short... aShorts) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, int... aInts) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, Integer... aInts) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, long... aLongs) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, Long... aLongs) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, float... aFloats) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, Float... aFloats) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, double... aDoubles) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, Double... aDoubles) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, String... aStrings) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, String[] aStrings, boolean aForceIso88591Charset) {
        // TODO Auto-generated method stub

    }

    @Override
    public void set(int aKey, Msg... aMsgs) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setAll(Msg aMsg) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setAll(Object... aObjects) {
        // TODO Auto-generated method stub

    }

    @Override
    public void remove(int... aKeys) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeAll() {
        // TODO Auto-generated method stub

    }

}
