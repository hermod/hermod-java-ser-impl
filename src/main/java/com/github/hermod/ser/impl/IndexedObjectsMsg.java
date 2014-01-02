package com.github.hermod.ser.impl;

import static com.github.hermod.ser.Types.ARRAY_FIXED_VALUE_TYPE;
import static com.github.hermod.ser.Types.ARRAY_VARIABLE_VALUE_TYPE;
import static com.github.hermod.ser.Types.BYTE_TYPE;
import static com.github.hermod.ser.Types.DECIMAL_TYPE;
import static com.github.hermod.ser.Types.DOUBLE_TYPE;
import static com.github.hermod.ser.Types.FIVE_BITS_DECIMAL_TYPE;
import static com.github.hermod.ser.Types.FLOAT_TYPE;
import static com.github.hermod.ser.Types.INTEGER_TYPE;
import static com.github.hermod.ser.Types.INT_TYPE;
import static com.github.hermod.ser.Types.LONG_TYPE;
import static com.github.hermod.ser.Types.MSG_TYPE;
import static com.github.hermod.ser.Types.NULL_TYPE;
import static com.github.hermod.ser.Types.SHORT_TYPE;
import static com.github.hermod.ser.Types.STRING_UTF_8_TYPE;
import static com.github.hermod.ser.impl.Msgs.DEFAULT_MAX_KEY;
import static com.github.hermod.ser.impl.Msgs.EIGHT;
import static com.github.hermod.ser.impl.Msgs.ERROR_WHEN_KEY_NOT_PRESENT;
import static com.github.hermod.ser.impl.Msgs.FOUR;
import static com.github.hermod.ser.impl.Msgs.ONE;
import static com.github.hermod.ser.impl.Msgs.TWO;

import java.nio.ByteBuffer;

import com.github.hermod.ser.Msg;
import com.github.hermod.ser.Null;
import com.github.hermod.ser.Type;

/**
 * <p>IndexedObjectsMsg. </p>
 * 
 * @author anavarro - Oct 23, 2013
 * 
 */
public class IndexedObjectsMsg implements Msg {

    private Object[] objectValues;

    private IndexedObjectsMsg() {
        this(DEFAULT_MAX_KEY);
    }

    /**
     * Constructor.
     * 
     * @param aDescriptor
     */
    private IndexedObjectsMsg(final int aKeyMax) {
        this.objectValues = new Object[aKeyMax + 1];
    }

    /**
     * Constructor.
     * 
     * @param aMsg
     */
    private IndexedObjectsMsg(final Msg aMsg) {
        if (aMsg instanceof IndexedObjectsMsg) {
            final IndexedObjectsMsg indexedObjectsMsg = (IndexedObjectsMsg) aMsg;
            this.objectValues = new Object[indexedObjectsMsg.objectValues.length];
            System.arraycopy(indexedObjectsMsg.objectValues, 0, this.objectValues, 0, indexedObjectsMsg.objectValues.length);
        } else {
            if (aMsg != null) {
                final int[] keys = aMsg.getKeysArray();
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
    private void increaseKeyMax(final int aKey) {
        if (aKey < 0) {
            throw new IllegalArgumentException("The key=" + aKey + " must be positive.");
        } else {
            final int nextPow2 = Msgs.calculateNextPowerOf2(aKey + 1);
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
                return false;
            }
        }
        return true;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#isSerializable()
     */
    @Override
    public final boolean isSerializable() {
        return false;
    }

    /**
     * isBytesSerializable.
     * 
     * @return
     */
    @Override
    public final boolean isBytesSerializable() {
        return false;
    }

    /**
     * isByteBufferSerializable.
     * 
     * @return
     */
    @Override
    public final boolean isByteBufferSerializable() {
        return false;
    }

    /**
     * isByteBufSerializable.
     * 
     * @return
     */
    @Override
    public final boolean isByteBufSerializable() {
        return false;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#retrieveKeys()
     */
    @Override
    public final int[] getKeysArray() {
        final int[] keys = new int[getKeysLength()];
        int index = 0;
        for (int i = 0; i < this.objectValues.length; i++) {
            if (this.objectValues[i] != null) {
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
    public final int getKeyMax() {
        for (int i = this.objectValues.length; i-- != 0;) {
            if (this.objectValues[i] != null) {
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
    public final int getKeysLength() {
        int nbKey = 0;
        for (int i = 0; i < this.objectValues.length; i++) {
            if (this.objectValues[i] != null) {
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
            return (this.objectValues[aKey].getClass().equals(Null.class)) ? ((Null) this.objectValues[aKey]).getType() : Type
            .valueOf(this.objectValues[aKey].getClass());
        } catch (ArrayIndexOutOfBoundsException e) {
            return Type.NULL;
        } catch (NullPointerException e) {
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
        // return getType(aKey).getId();

        try {
            return Type.valueOf(this.objectValues[aKey].getClass()).getId();
        } catch (ArrayIndexOutOfBoundsException e) {
            return Type.NULL.getId();
        }

        // try {
        // return (this.objectValues[aKey].getClass().equals(Null.class)) ? ((Null) this.objectValues[aKey]).getType().getId() :
        // Type.valueOf(this.objectValues[aKey].getClass()).getId();
        // } catch (ArrayIndexOutOfBoundsException e) {
        // return Type.NULL.getId();
        // } catch (NullPointerException e) {
        // return Type.NULL.getId();
        // }

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

    @Override
    public final Null getAsNull(final int aKey) {
        try {
            final Object value = this.objectValues[aKey];
            if (value instanceof Null) {
                return (Null) this.objectValues[aKey];
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
    public final Boolean getAsNullableBoolean(final int aKey) {
        try {
            final Object value = this.objectValues[aKey];
            if (value == null) {
                return (Boolean) null;
            } else {
                if (value instanceof Byte) {
                    final byte b = ((Byte) this.objectValues[aKey]).byteValue();
                    return b == 0 ? Boolean.FALSE : Boolean.TRUE;
                }
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
    public final Byte getAsNullableByte(final int aKey) {
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
    public final Short getAsNullableShort(final int aKey) {
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
                    return aClazz.cast((Long) null);

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
                    if (this.objectValues[aKey] instanceof byte[]) {
                        return aClazz.cast(getAsBytes(aKey));
                    } else if (this.objectValues[aKey] instanceof short[]) {
                        return aClazz.cast(getAsShorts(aKey));
                    } else if (this.objectValues[aKey] instanceof int[]) {
                        return aClazz.cast(getAsInts(aKey));
                    } else if (this.objectValues[aKey] instanceof long[]) {
                        return aClazz.cast(getAsLongs(aKey));
                    } else if (this.objectValues[aKey] instanceof float[]) {
                        return aClazz.cast(getAsFloats(aKey));
                    } else if (this.objectValues[aKey] instanceof double[]) {
                        return aClazz.cast(getAsDoubles(aKey));
                    }

                case ARRAY_VARIABLE_VALUE_TYPE:
                    return aClazz.cast(getAsObjects(aKey));

                case NULL_TYPE:
                    return aClazz.cast(getAsNull(aKey));

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
     * @see com.github.hermod.ser.Msg#getAsBooleans(int)
     */
    @Override
    public final boolean[] getAsBooleans(final int aKey) {
        try {
            if ((this.objectValues[aKey] instanceof byte[])) {
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
    public final Boolean[] getAsNullableBooleans(final int aKey) {
        try {
            if ((this.objectValues[aKey] instanceof byte[])) {
                final byte[] bytes = (byte[]) this.objectValues[aKey];
                final Boolean[] results = new Boolean[bytes.length];
                for (int i = 0; i < bytes.length; i++) {
                    if (bytes[i] != 0 && bytes[i] != 1) {
                        return null;
                    }
                    results[i] = (bytes[i] == 0) ? false : true;
                }
                return results;
            } else if ((this.objectValues[aKey] instanceof Byte[])) {
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
    public final byte[] getAsBytes(final int aKey) {
        try {
            if ((this.objectValues[aKey] instanceof byte[])) {
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
    public final Byte[] getAsNullableBytes(final int aKey) {
        try {
            if ((this.objectValues[aKey] instanceof byte[])) {
                final byte[] bytes = (byte[]) this.objectValues[aKey];
                final int byteLength = bytes.length;
                final Byte[] results = new Byte[byteLength];
                // System.arraycopy(bytes, 0, results, 0, bytes.length);
                for (int i = 0; i < byteLength; i++) {
                    results[i] = bytes[i];
                }
                return results;
            } else if ((this.objectValues[aKey] instanceof Byte[])) {
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
    public final short[] getAsShorts(final int aKey) {
        try {
            if ((this.objectValues[aKey] instanceof short[])) {
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
     * @see com.github.hermod.ser.Msg#getAsNullableShorts(int)
     */
    @Override
    public final Short[] getAsNullableShorts(final int aKey) {
        try {
            if ((this.objectValues[aKey] instanceof short[])) {
                final short[] shorts = (short[]) this.objectValues[aKey];
                final int shortLength = shorts.length;
                final Short[] results = new Short[shortLength];
                for (int i = 0; i < shortLength; i++) {
                    results[i] = shorts[i];
                }
                return results;
            } else if ((this.objectValues[aKey] instanceof Short[])) {
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
    public final int[] getAsInts(final int aKey) {
        try {
            if (((this.objectValues[aKey] instanceof int[]))) {
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
    public final Integer[] getAsNullableIntegers(final int aKey) {
        try {
            if (((this.objectValues[aKey] instanceof int[]))) {
                final int[] ints = (int[]) this.objectValues[aKey];
                final int intsLength = ints.length;
                final Integer[] results = new Integer[intsLength];
                for (int i = 0; i < intsLength; i++) {
                    results[i] = ints[i];
                }
                return results;
            } else if (((this.objectValues[aKey] instanceof Integer[]))) {
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
    public final long[] getAsLongs(final int aKey) {
        try {
            if ((this.objectValues[aKey] instanceof long[])) {
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
    public final Long[] getAsNullableLongs(final int aKey) {
        try {
            if ((this.objectValues[aKey] instanceof long[])) {
                final long[] longs = (long[]) this.objectValues[aKey];
                final int longsLength = longs.length;
                final Long[] results = new Long[longsLength];
                for (int i = 0; i < longsLength; i++) {
                    results[i] = longs[i];
                }
                return results;
            } else if ((this.objectValues[aKey] instanceof Long[])) {
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
    public final float[] getAsFloats(final int aKey) {
        try {
            if (((this.objectValues[aKey] instanceof float[]))) {
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
    public final Float[] getAsNullableFloats(final int aKey) {
        try {
            if (((this.objectValues[aKey] instanceof float[]))) {
                final float[] floats = (float[]) this.objectValues[aKey];
                final int floatsLength = floats.length;
                final Float[] results = new Float[floatsLength];
                for (int i = 0; i < floatsLength; i++) {
                    results[i] = floats[i];
                }
                return results;
            } else if (((this.objectValues[aKey] instanceof Float[]))) {
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
    public final double[] getAsDoubles(final int aKey) {
        try {
            if ((this.objectValues[aKey] instanceof double[])) {
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
    public final Double[] getAsNullableDoubles(final int aKey) {
        try {
            if ((this.objectValues[aKey] instanceof double[])) {
                final double[] doubles = (double[]) this.objectValues[aKey];
                final int doublesLength = doubles.length;
                final Double[] results = new Double[doublesLength];
                for (int i = 0; i < doublesLength; i++) {
                    results[i] = doubles[i];
                }
                return results;
            } else if ((this.objectValues[aKey] instanceof Double[])) {
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
    public final String[] getAsStrings(final int aKey) {
        try {
            if (((this.objectValues[aKey] instanceof String[]))) {
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
    public final Msg[] getAsMsgs(final int aKey) {
        try {
            if ((this.objectValues[aKey] instanceof Msg[])) {
                final Msg[] msgs = (Msg[]) this.objectValues[aKey];
                final Msg[] results = new IndexedPrimitivesObjectsMsg[msgs.length];
                for (int i = 0; i < msgs.length; i++) {
                    results[i] = new IndexedObjectsMsg(msgs[i]);
                }
                return results;
            }
            return ((this.objectValues[aKey] instanceof Msg[])) ? (Msg[]) this.objectValues[aKey] : null;
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
    public final void getAsMsgs(final int aKey, final Msg... aDestMsgs) {
        try {
            if ((this.objectValues[aKey] instanceof Msg[])) {
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

    /**
     * {@inheritDoc}
     * 
     * @see com.github.hermod.ser.Msg#getAsObjects(int)
     */
    @Override
    public final Object[] getAsObjects(final int aKey) {
        try {
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
            } else if ((this.objectValues[aKey] instanceof Object[])) {
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
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getAllAsMsg()
     */
    @Override
    public final Msg getAllAsMsg() {
        // TODO to optimize
        final int[] keys = this.getKeysArray();
        final Msg msg = new IndexedObjectsMsg(keys[keys.length - 1]);
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
    public Object[] getAllAsObjects() {
        // TODO to optimize, copy direct the objectsValue
        final Object[] anObjects = new Object[this.getKeyMax() + 1];
        final int[] keys = this.getKeysArray();
        for (final int key : keys) {
            anObjects[key] = this.get(key);
        }
        return anObjects;
    }

    /**
     * <p>getInternalAllAsObjects.</p>
     * 
     * @return
     */
    Object[] getInternalAllAsObjects() {
        return this.objectValues;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#getAllAsObjects(java.lang.Object[])
     */
    @Override
    public final void getAllAsObjects(final Object... aObjects) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not Yet Implemented.");
        // return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Object)
     */
    @Override
    public final void set(final int aKey, final Object aObject) {
        try {

            if (aObject instanceof Byte) {
                set(aKey, (Byte) aObject);
            } else if (aObject instanceof Short) {
                set(aKey, (Short) aObject);
            } else if (aObject instanceof Integer) {
                set(aKey, (Integer) aObject);
            } else if (aObject instanceof Long) {
                set(aKey, (Long) aObject);
            } else if (aObject instanceof Float) {
                set(aKey, (Float) aObject);
            } else if (aObject instanceof Double) {
                set(aKey, (Double) aObject);
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
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aObject);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Object, boolean)
     */
    @Override
    public final void set(final int aKey, final Object aObject, final boolean optimizeLength) {
        if (optimizeLength) {
            set(aKey, aObject);
        } else {
            try {
                // TODO to refactor
                if (aObject instanceof Boolean | aObject instanceof Byte | aObject instanceof Short | aObject instanceof Integer
                | aObject instanceof Long) {
                    if (aObject != null) {
                        final byte integerType = (aObject instanceof Byte || aObject instanceof Boolean) ? BYTE_TYPE
                        : (aObject instanceof Short) ? SHORT_TYPE : (aObject instanceof Integer) ? INT_TYPE : LONG_TYPE;
                        set(aKey, ((Number) aObject).longValue(), integerType);
                    } else {
                        if (aObject instanceof Boolean | aObject instanceof Byte) {
                            set(aKey, Null.valueOf(ONE));
                        } else if (aObject instanceof Short) {
                            set(aKey, Null.valueOf(TWO));
                        } else if (aObject instanceof Integer) {
                            set(aKey, Null.valueOf(FOUR));
                        } else if (aObject instanceof Long) {
                            set(aKey, Null.valueOf(EIGHT));
                        }
                    }
                } else if (aObject instanceof Float | aObject instanceof Double) {
                    if (aObject != null) {
                        if (aObject instanceof Double) {
                            set(aKey, ((Double) aObject).doubleValue(), false);
                        } else {
                            set(aKey, ((Float) aObject).floatValue());
                        }
                    } else {
                        if (aObject instanceof Float) {
                            set(aKey, Null.valueOf(FOUR));
                        } else if (aObject instanceof Double) {
                            set(aKey, Null.valueOf(EIGHT));
                        }
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
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, com.github.hermod.ser.Null)
     */
    @Override
    public final void set(final int aKey, final Null aNull) {
        try {
            if (!Null.NULL.equals(aNull)) {
                this.objectValues[aKey] = aNull;
            } else {
                throw new IllegalArgumentException(Msgs.ERROR_WHEN_YOU_SET_NULL_WITH_LENGTH_0);
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aNull);
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
            this.objectValues[aKey] = (aBoolean) ? Byte.valueOf((byte) ONE) : Byte.valueOf((byte) 0);
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, Boolean.valueOf(aBoolean));
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Boolean)
     */
    @Override
    public final void set(final int aKey, final Boolean aBoolean) {
        try {
            this.objectValues[aKey] = (aBoolean != null) ? ((aBoolean.booleanValue()) ? Byte.valueOf((byte) ONE) : Byte.valueOf((byte) 0)) : Null
            .valueOf(Type.INTEGER);
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aBoolean);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Boolean, boolean)
     */
    @Override
    public final void set(final int aKey, final Boolean aBoolean, final boolean optimizeLength) {
        if (optimizeLength) {
            set(aKey, aBoolean);
        } else if (aBoolean == null) {
            set(aKey, Null.valueOf(ONE));
        } else {
            set(aKey, aBoolean);
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
            this.objectValues[aKey] = Byte.valueOf(aByte);
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, Byte.valueOf(aByte));
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Byte)
     */
    @Override
    public final void set(final int aKey, final Byte aByte) {
        try {
            this.objectValues[aKey] = (aByte != null) ? aByte : Null.INTEGER_NULL;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aByte);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Byte, boolean)
     */
    @Override
    public final void set(final int aKey, final Byte aByte, final boolean optimizeLength) {
        if (optimizeLength) {
            set(aKey, aByte);
        } else if (aByte == null) {
            set(aKey, Null.valueOf(ONE));
        } else {
            set(aKey, aByte);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, short)
     */
    @Override
    public final void set(final int aKey, final short aShort) {
        try {
            this.objectValues[aKey] = (aShort == (byte) aShort) ? Byte.valueOf((byte) aShort) : Short.valueOf(aShort);
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, Short.valueOf(aShort));
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Short)
     */
    @Override
    public final void set(final int aKey, final Short aShort1) {
        try {
            if (aShort1 == null) {
                this.objectValues[aKey] = Null.INTEGER_NULL;
            } else {
                final short aShort = aShort1.shortValue();
                this.objectValues[aKey] = (aShort == (byte) aShort) ? Byte.valueOf((byte) aShort) : aShort;
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aShort1);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, short, boolean)
     */
    @Override
    public final void set(final int aKey, final short aShort, final boolean optimizeLength) {
        if (optimizeLength) {
            set(aKey, aShort);
        } else {
            try {
                this.objectValues[aKey] = Short.valueOf(aShort);
            } catch (final ArrayIndexOutOfBoundsException e) {
                increaseKeyMax(aKey);
                set(aKey, aShort);
            }
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Short, boolean)
     */
    @Override
    public final void set(final int aKey, final Short aShort, final boolean optimizeLength) {
        if (optimizeLength) {
            set(aKey, aShort);
        } else if (aShort == null) {
            set(aKey, Null.valueOf(TWO));
        } else {
            try {
                this.objectValues[aKey] = aShort;
            } catch (final ArrayIndexOutOfBoundsException e) {
                increaseKeyMax(aKey);
                set(aKey, aShort);
            }
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, int)
     */
    @Override
    public final void set(final int aKey, final int aInt) {
        try {
            this.objectValues[aKey] = (aInt == (byte) aInt) ? Byte.valueOf((byte) aInt) : (aInt == (short) aInt) ? Short.valueOf((short) aInt)
            : Integer.valueOf(aInt);
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, Integer.valueOf(aInt));
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Integer)
     */
    @Override
    public final void set(final int aKey, final Integer aInteger) {
        try {
            if (aInteger == null) {
                this.objectValues[aKey] = Null.INTEGER_NULL;
            } else {
                final int aInt = aInteger.intValue();
                this.objectValues[aKey] = (aInt == (byte) aInt) ? Byte.valueOf((byte) aInt) : (aInt == (short) aInt) ? Short.valueOf((short) aInt)
                : Integer.valueOf(aInt);
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aInteger);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Integer, boolean)
     */
    @Override
    public final void set(final int aKey, final int aInt, final boolean optimizeLength) {
        if (optimizeLength) {
            set(aKey, aInt);
        } else {
            try {
                this.objectValues[aKey] = Integer.valueOf(aInt);
            } catch (final ArrayIndexOutOfBoundsException e) {
                increaseKeyMax(aKey);
                set(aKey, aInt);
            }
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Integer, boolean)
     */
    @Override
    public final void set(final int aKey, final Integer aInt, final boolean optimizeLength) {
        if (optimizeLength) {
            set(aKey, aInt);
        } else if (aInt == null) {
            set(aKey, Null.valueOf(FOUR));
        } else {
            try {
                this.objectValues[aKey] = aInt;
            } catch (final ArrayIndexOutOfBoundsException e) {
                increaseKeyMax(aKey);
                set(aKey, aInt);
            }
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, long)
     */
    @Override
    public final void set(final int aKey, final long aLong) {
        try {
            this.objectValues[aKey] = (aLong == (byte) aLong) ? Byte.valueOf((byte) aLong) : (aLong == (short) aLong) ? Short.valueOf((short) aLong)
            : (aLong == (short) aLong) ? Integer.valueOf((int) aLong) : Long.valueOf(aLong);
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, Long.valueOf(aLong));
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Long)
     */
    @Override
    public final void set(final int aKey, final Long aLong1) {
        try {
            if (aLong1 == null) {
                this.objectValues[aKey] = Null.INTEGER_NULL;
            } else {
                final long aLong = aLong1.longValue();

                if (aLong == (byte) aLong) {
                    this.objectValues[aKey] = Byte.valueOf((byte) aLong);
                } else if (aLong == (short) aLong) {
                    this.objectValues[aKey] = Short.valueOf((short) aLong);
                } else if (aLong == (int) aLong) {
                    this.objectValues[aKey] = Integer.valueOf((int) aLong);
                } else {
                    this.objectValues[aKey] = Long.valueOf(aLong);
                }

                // Does not work, don't know why
                // this.objectValues[aKey] = (aLong == (byte) aLong) ? Byte.valueOf((byte) aLong) : (aLong == (short) aLong) ? Short
                // .valueOf((short) aLong) : (aLong == (int) aLong) ? Integer.valueOf((int) aLong) : Long.valueOf(aLong);
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aLong1);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, long, boolean)
     */
    @Override
    public final void set(final int aKey, final long aLong, final boolean optimizeLength) {
        if (optimizeLength) {
            set(aKey, aLong);
        } else {
            try {
                this.objectValues[aKey] = Long.valueOf(aLong);
            } catch (final ArrayIndexOutOfBoundsException e) {
                increaseKeyMax(aKey);
                set(aKey, aLong);
            }
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Long, boolean)
     */
    @Override
    public final void set(final int aKey, final Long aLong, final boolean optimizeLength) {
        if (optimizeLength) {
            set(aKey, aLong);
        } else if (aLong == null) {
            set(aKey, Null.valueOf(EIGHT));
        } else {
            try {
                this.objectValues[aKey] = aLong;
            } catch (final ArrayIndexOutOfBoundsException e) {
                increaseKeyMax(aKey);
                set(aKey, aLong);
            }
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, float)
     */
    @Override
    public final void set(final int aKey, final float aFloat) {
        try {
            this.objectValues[aKey] = aFloat;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aFloat);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Float)
     */
    @Override
    public final void set(final int aKey, final Float aFloat) {
        try {
            this.objectValues[aKey] = (aFloat != null) ? aFloat : Null.DECIMAL_NULL;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aFloat);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Float, boolean)
     */
    @Override
    public final void set(final int aKey, final Float aFloat, final boolean optimizeLength) {
        if (optimizeLength) {
            set(aKey, aFloat);
        } else if (aFloat == null) {
            set(aKey, Null.valueOf(FOUR));
        } else {
            try {
                this.objectValues[aKey] = aFloat;
            } catch (final ArrayIndexOutOfBoundsException e) {
                increaseKeyMax(aKey);
                set(aKey, aFloat);
            }
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, double)
     */
    @Override
    public final void set(final int aKey, final double aDouble) {
        try {
            final boolean isEncodeableInAFloat = (aDouble == (float) aDouble) ? true : false;
            this.objectValues[aKey] = (isEncodeableInAFloat) ? (float) aDouble : aDouble;
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aDouble);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Double)
     */
    @Override
    public final void set(final int aKey, final Double aDouble) {
        try {
            if (aDouble == null) {
                this.objectValues[aKey] = Null.DECIMAL_NULL;
            } else {
                final boolean isEncodeableInAFloat = (aDouble == aDouble.floatValue()) ? true : false;
                this.objectValues[aKey] = (isEncodeableInAFloat) ? aDouble.floatValue() : aDouble;
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aDouble);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Double, int, boolean)
     */
    @Override
    public final void set(final int aKey, final double aDouble, final boolean optimizeLength) {
        if (optimizeLength) {
            set(aKey, aDouble);
        } else {
            try {
                this.objectValues[aKey] = aDouble;
            } catch (final ArrayIndexOutOfBoundsException e) {
                increaseKeyMax(aKey);
                set(aKey, aDouble);
            }
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Double, int, boolean)
     */
    @Override
    public final void set(final int aKey, final Double aDouble, final boolean optimizeLength) {
        if (optimizeLength) {
            set(aKey, aDouble);
        } else if (aDouble == null) {
            set(aKey, Null.valueOf(EIGHT));
        } else {
            try {
                this.objectValues[aKey] = aDouble;
            } catch (final ArrayIndexOutOfBoundsException e) {
                increaseKeyMax(aKey);
                set(aKey, aDouble);
            }
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, double, int)
     */
    @Override
    public final void set(final int aKey, final double aDouble, final int aScale) {
        // TODO to really implement it
        set(aKey, aDouble);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Double, int)
     */
    @Override
    public final void set(final int aKey, final Double aDouble, final int aScale) {
        // TODO to really implement it
        set(aKey, aDouble);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.Double, int, boolean)
     */
    @Override
    public final void set(final int aKey, final Double aDouble, final int aScale, final boolean optimizeLength) {
        if (aDouble == null) {
            set(aKey, Null.DECIMAL_NULL);
        } else {
            set(aKey, aDouble, aScale);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.String)
     */
    @Override
    public final void set(final int aKey, final String aString) {
        try {
            if (aString == null) {
                this.objectValues[aKey] = Null.STRING_UTF8_NULL;
            } else {
                this.objectValues[aKey] = aString;
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aString);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, java.lang.String, boolean)
     */
    @Override
    public final void set(final int aKey, final String aString, final boolean forceAsciiEncoding) {
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
            if (aMsg == null) {
                this.objectValues[aKey] = Null.MSG_NULL;
            } else {
                this.objectValues[aKey] = aMsg;
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aMsg);
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
                this.objectValues[aKey] = Null.ARRAY_FIXED_VALUE_NULL;
            }
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
                this.objectValues[aKey] = Null.ARRAY_VARIABLE_VALUE_NULL;
            }
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
            if (aBytes != null) {
                this.objectValues[aKey] = aBytes;
            } else {
                this.objectValues[aKey] = Null.ARRAY_FIXED_VALUE_NULL;
            }
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
            if (aBytes != null) {
                this.objectValues[aKey] = aBytes;
            } else {
                this.objectValues[aKey] = Null.ARRAY_VARIABLE_VALUE_NULL;
            }
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
            if (aShorts != null) {
                this.objectValues[aKey] = aShorts;
            } else {
                this.objectValues[aKey] = Null.ARRAY_FIXED_VALUE_NULL;
            }
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
            if (aShorts != null) {
                this.objectValues[aKey] = aShorts;
            } else {
                this.objectValues[aKey] = Null.ARRAY_VARIABLE_VALUE_NULL;
            }
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
            if (aInts != null) {
                this.objectValues[aKey] = aInts;
            } else {
                this.objectValues[aKey] = Null.ARRAY_FIXED_VALUE_NULL;
            }
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
            if (aInts != null) {
                this.objectValues[aKey] = aInts;
            } else {
                this.objectValues[aKey] = Null.ARRAY_VARIABLE_VALUE_NULL;
            }
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
            if (aLongs != null) {
                this.objectValues[aKey] = aLongs;
            } else {
                this.objectValues[aKey] = Null.ARRAY_FIXED_VALUE_NULL;
            }
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
            if (aLongs != null) {
                this.objectValues[aKey] = aLongs;
            } else {
                this.objectValues[aKey] = Null.ARRAY_VARIABLE_VALUE_NULL;
            }
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
            if (aFloats != null) {
                this.objectValues[aKey] = aFloats;
            } else {
                this.objectValues[aKey] = Null.ARRAY_FIXED_VALUE_NULL;
            }
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
            if (aFloats != null) {
                this.objectValues[aKey] = aFloats;
            } else {
                this.objectValues[aKey] = Null.ARRAY_VARIABLE_VALUE_NULL;
            }
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
            if (aDoubles != null) {
                this.objectValues[aKey] = aDoubles;
            } else {
                this.objectValues[aKey] = Null.ARRAY_FIXED_VALUE_NULL;
            }
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
            if (aDoubles != null) {
                this.objectValues[aKey] = aDoubles;
            } else {
                this.objectValues[aKey] = Null.ARRAY_VARIABLE_VALUE_NULL;
            }
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
        try {
            if (aStrings != null) {
                this.objectValues[aKey] = aStrings;
            } else {
                this.objectValues[aKey] = Null.ARRAY_VARIABLE_VALUE_NULL;
            }
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
    public final void set(final int aKey, final String[] aStrings, boolean forceAsciiEncoding) {
        set(aKey, aStrings);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#set(int, com.github.hermod.ser.Msg[])
     */
    @Override
    public final void set(final int aKey, final Msg... aMsgs) {
        try {
            if (aMsgs != null) {
                this.objectValues[aKey] = aMsgs;
            } else {
                this.objectValues[aKey] = Null.ARRAY_VARIABLE_VALUE_NULL;
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aMsgs);
        }
    }

    public final void set(final int aKey, final Object[] aObjectArray) {
        try {
            if (aObjectArray != null) {
                this.objectValues[aKey] = aObjectArray;
            } else {
                this.objectValues[aKey] = Null.ARRAY_VARIABLE_VALUE_NULL;
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            increaseKeyMax(aKey);
            set(aKey, aObjectArray);
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
            final int[] keys = aMsg.getKeysArray();
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
    @Override
    public final void setAll(final Object... anObjects) {
        for (int key = 0; key < anObjects.length; key++) {
            set(key, anObjects[key]);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.Msg#remove(int[])
     */
    @Override
    public final void remove(final int... aKeys) {
        if (aKeys != null) {
            for (final int aKey : aKeys) {
                try {
                    this.objectValues[aKey] = null;
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
        for (int i = 0; i < this.objectValues.length; i++) {
            this.objectValues[i] = null;
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
