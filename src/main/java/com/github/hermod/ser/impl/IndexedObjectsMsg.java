package com.github.hermod.ser.impl;

import java.nio.ByteBuffer;

import checkers.nullness.quals.Nullable;

import com.github.hermod.ser.Msg;
import com.github.hermod.ser.Precision;
import com.github.hermod.ser.Type;

import static com.github.hermod.ser.impl.Msgs.DEFAULT_MAX_KEY;
import static com.github.hermod.ser.impl.Msgs.ERROR_WHEN_KEY_NOT_PRESENT;

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
                    set(keys[i], aMsg.getAsObject(keys[i]));
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
        try {
            final Object value = this.objectValues[aKey];
            if (value != null && value instanceof Boolean) {
                return (Boolean) this.objectValues[aKey];
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
            final Object value = this.objectValues[aKey];
            if (value instanceof Boolean) {
                return (Boolean) this.objectValues[aKey];
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
        }
        throw new IllegalArgumentException(String.format(ERROR_WHEN_KEY_NOT_PRESENT, aKey));
    }

    @Override
    public final byte getAsByte(final int aKey) {
        try {
            final Object value = this.objectValues[aKey];
            if (value != null && value instanceof Byte) {
                return (Byte) this.objectValues[aKey];
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
    Byte getAsNullableByte(final int aKey) {
        try {
            final Object value = this.objectValues[aKey];
            if (value instanceof Byte) {
                return (Byte) this.objectValues[aKey];
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
        }
        throw new IllegalArgumentException(String.format(ERROR_WHEN_KEY_NOT_PRESENT, aKey));
    }

    /**
     * getAsShort.
     *
     * @param aKey
     * @return
     */
    @Override
    public final short getAsShort(final int aKey) {
        try {
            final Object value = this.objectValues[aKey];
            if (value != null) {
                if (value instanceof Short) {
                    return (Short) this.objectValues[aKey];
                }
                if (value instanceof Byte) {
                    return ((Byte) this.objectValues[aKey]).shortValue();
                }
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
        throw new IllegalArgumentException(String.format(ERROR_WHEN_KEY_NOT_PRESENT, aKey));
    }

    @Override
    public int getAsInt(int aKey) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Integer getAsNullableInteger(int aKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getAsLong(int aKey) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Long getAsNullableLong(int aKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public float getAsFloat(int aKey) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Float getAsNullableFloat(int aKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double getAsDouble(int aKey) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Double getAsNullableDouble(int aKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getAsString(int aKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Msg getAsMsg(int aKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void getAsMsg(int aKey, Msg aDestMsg) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Object getAsObject(int aKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T getAsObject(int aKey, Class<T> aClazz) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean[] getAsBooleans(int aKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean[] getAsNullableBooleans(int aKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public byte[] getAsBytes(int aKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Byte[] getAsNullableBytes(int aKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public short[] getAsShorts(int aKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Short[] getAsNullableShorts(int aKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int[] getAsInts(int aKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer[] getAsNullableIntegers(int aKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long[] getAsLongs(int aKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long[] getAsNullableLongs(int aKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public float[] getAsFloats(int aKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Float[] getAsNullableFloats(int aKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double[] getAsDoubles(int aKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Double[] getAsNullableDoubles(int aKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getAsStrings(int aKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Msg[] getAsMsgs(int aKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void getAsMsgs(int aKey, Msg... aDestMsgs) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Msg getAllAsMsg() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object[] getAllAsObjects() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void getAllAsObjects(Object... aObjects) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Type getType(int aKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public byte getTypeAsByte(int aKey) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isArray(int aKey) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getArrayLength(int aKey) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int[] retrieveKeys() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int retrieveKeyMax() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int countKeys() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
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
