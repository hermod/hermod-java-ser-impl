package com.github.hermod.ser.impl;

import static com.github.hermod.ser.Types.ARRAY_FIXED_VALUE_TYPE;
import static com.github.hermod.ser.Types.ARRAY_VARIABLE_VALUE_TYPE;
import static com.github.hermod.ser.Types.BYTE_TYPE;
import static com.github.hermod.ser.Types.DOUBLE_TYPE;
import static com.github.hermod.ser.Types.FIVE_BITS_DECIMAL_TYPE;
import static com.github.hermod.ser.Types.FLOAT_TYPE;
import static com.github.hermod.ser.Types.INT_TYPE;
import static com.github.hermod.ser.Types.LONG_TYPE;
import static com.github.hermod.ser.Types.MSG_TYPE;
import static com.github.hermod.ser.Types.NULL_TYPE;
import static com.github.hermod.ser.Types.SHORT_TYPE;
import static com.github.hermod.ser.Types.SKIPPED_KEYS_TYPE;
import static com.github.hermod.ser.Types.STRING_UTF_8_TYPE;
import static com.github.hermod.ser.Types.TYPE_MASK;
import static com.github.hermod.ser.Types.UTF_8_CHARSET;
import static com.github.hermod.ser.impl.Msgs.EIGHT;
import static com.github.hermod.ser.impl.Msgs.FIFTY_SIX;
import static com.github.hermod.ser.impl.Msgs.FIVE;
import static com.github.hermod.ser.impl.Msgs.FORTY;
import static com.github.hermod.ser.impl.Msgs.FORTY_EIGHT;
import static com.github.hermod.ser.impl.Msgs.FOUR;
import static com.github.hermod.ser.impl.Msgs.LENGTH_ENCODED_IN_AN_INT;
import static com.github.hermod.ser.impl.Msgs.LENGTH_ENCODED_IN_AN_UNSIGNED_BYTE;
import static com.github.hermod.ser.impl.Msgs.LENGTH_MASK;
import static com.github.hermod.ser.impl.Msgs.MAX_VALUE_FOR_UNSIGNED_BYTE;
import static com.github.hermod.ser.impl.Msgs.NINE;
import static com.github.hermod.ser.impl.Msgs.ONE;
import static com.github.hermod.ser.impl.Msgs.SIXTEEN;
import static com.github.hermod.ser.impl.Msgs.THIRTY_TWO;
import static com.github.hermod.ser.impl.Msgs.THREE;
import static com.github.hermod.ser.impl.Msgs.TWENTY_FOUR;
import static com.github.hermod.ser.impl.Msgs.TWO;
import static com.github.hermod.ser.impl.Msgs.XFF;
import static com.github.hermod.ser.impl.Msgs.ZERO;

import java.nio.ByteBuffer;

import com.github.hermod.ser.ByteBufferMsgSerializer;
import com.github.hermod.ser.BytesMsgSerializer;
import com.github.hermod.ser.BytesSerializable;
import com.github.hermod.ser.Msg;
import com.github.hermod.ser.Null;
import com.github.hermod.ser.Serializable;
import com.github.hermod.ser.Type;

/**
 * <p>DefaultSerializer. </p>
 * 
 * @author anavarro - Mar 11, 2013
 * 
 */
public final class DefaultMsgSerializer implements BytesMsgSerializer, ByteBufferMsgSerializer {

    private static final DefaultMsgSerializer DEFAULT_MSG_SERIALIZER = new DefaultMsgSerializer();

    /**
     * create.
     * 
     * @return
     */
    public static DefaultMsgSerializer create() {
        return new DefaultMsgSerializer();
    }

    /**
     * get.
     * 
     * @return
     */
    public static DefaultMsgSerializer get() {
        return DEFAULT_MSG_SERIALIZER;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.MsgSerializer#readFrom(byte[], int, int, com.github.hermod.ser.Msg)
     */
    @Override
    public void deserializeFromBytes(final byte[] bytes, final int offset, final int length, Msg aDestMsg) {
        if (aDestMsg.isBytesSerializable()) {
            ((BytesSerializable) aDestMsg).deserializeFromBytes(bytes, offset, length);
        } else {
            int pos = offset;
            int key = 0;

            // find max key
            // not need for feed
            // Calculate max key value
            // TODO to optimize with a try catch, lastPos and relaunch readFrom
            while (pos < offset + length) {
                if ((bytes[pos] & TYPE_MASK) == SKIPPED_KEYS_TYPE) {
                    final int lengthMask = bytes[pos++] & LENGTH_MASK;
                    key += (((lengthMask < LENGTH_ENCODED_IN_AN_UNSIGNED_BYTE) ? lengthMask
                            : (lengthMask == LENGTH_ENCODED_IN_AN_UNSIGNED_BYTE) ? (XFF & bytes[pos++]) : (bytes[pos++] & XFF)
                                    | ((bytes[pos++] & XFF) << EIGHT) | ((bytes[pos++] & XFF) << SIXTEEN) | ((bytes[pos++] & XFF) << TWENTY_FOUR))) + 1;
                } else {
                    final int lengthMask = bytes[pos++] & LENGTH_MASK;
                    // TODO to optimize
                    pos += (((lengthMask < LENGTH_ENCODED_IN_AN_UNSIGNED_BYTE) ? lengthMask
                            : (lengthMask == LENGTH_ENCODED_IN_AN_UNSIGNED_BYTE) ? XFF & bytes[pos]
                                    : ((bytes[pos] & XFF) | ((bytes[pos + ONE] & XFF) << EIGHT) | ((bytes[pos + TWO] & XFF) << SIXTEEN) | ((bytes[pos
                                            + THREE] & XFF) << TWENTY_FOUR))));
                    pos += (lengthMask < LENGTH_ENCODED_IN_AN_UNSIGNED_BYTE) ? 0 : (lengthMask == LENGTH_ENCODED_IN_AN_UNSIGNED_BYTE) ? ONE : FOUR;

                    key++;
                }
            }
            // maybe add a removeALl(key);
            aDestMsg.removeAll();

            // Decoding
            key = 0;
            pos = offset;
            while (pos < offset + length) {
                final byte type = bytes[pos++];

                // Skip keys
                if ((type & TYPE_MASK) == SKIPPED_KEYS_TYPE) {
                    final int sizeMask = type & LENGTH_MASK;
                    key += ((sizeMask < LENGTH_ENCODED_IN_AN_UNSIGNED_BYTE) ? sizeMask : (sizeMask == LENGTH_ENCODED_IN_AN_UNSIGNED_BYTE) ? XFF
                            & bytes[pos++] : (bytes[pos++] & XFF) | ((bytes[pos++] & XFF) << EIGHT) | ((bytes[pos++] & XFF) << SIXTEEN)
                            | ((bytes[pos++] & XFF) << TWENTY_FOUR)) + 1;
                }
                // Decode values
                else {
                    switch (type) {
                    //  All fixed type
                    case BYTE_TYPE:
                        aDestMsg.set(key, (byte) ((bytes[pos++] & XFF)));
                        break;

                    case SHORT_TYPE:
                        aDestMsg.set(key, (short) ((((short) bytes[pos++] & XFF)) | (((short) bytes[pos++]) << EIGHT)));
                        break;

                    case INT_TYPE:
                        aDestMsg.set(key, (((int) bytes[pos++] & XFF)) | (((int) bytes[pos++] & XFF) << EIGHT)
                                | (((int) bytes[pos++] & XFF) << SIXTEEN) | (((int) bytes[pos++]) << TWENTY_FOUR));
                        break;

                    case DOUBLE_TYPE:
                        aDestMsg.set(
                                key,
                                Double.longBitsToDouble((((long) bytes[pos++] & XFF) | (((long) bytes[pos++] & XFF) << EIGHT)
                                        | (((long) bytes[pos++] & XFF) << SIXTEEN) | (((long) bytes[pos++] & XFF) << TWENTY_FOUR)
                                        | (((long) bytes[pos++] & XFF) << THIRTY_TWO) | (((long) bytes[pos++] & XFF) << FORTY)
                                        | (((long) bytes[pos++] & XFF) << FORTY_EIGHT) | (((long) bytes[pos++] & XFF) << FIFTY_SIX))));
                        break;

                    case FIVE_BITS_DECIMAL_TYPE:
                        aDestMsg.set(
                                key,
                                Double.longBitsToDouble((((long) bytes[pos++] & XFF) << EIGHT) | (((long) bytes[pos++] & XFF) << SIXTEEN)
                                        | (((long) bytes[pos++] & XFF) << TWENTY_FOUR) | (((long) bytes[pos++] & XFF) << THIRTY_TWO)),
                                ((byte) bytes[pos++] & XFF));
                        break;

                    case FLOAT_TYPE:
                        aDestMsg.set(
                                key,
                                Float.intBitsToFloat((((int) bytes[pos++] & XFF)) | (((int) bytes[pos++] & XFF) << EIGHT)
                                        | (((int) bytes[pos++] & XFF) << SIXTEEN) | (((int) bytes[pos++] & XFF) << TWENTY_FOUR)));
                        break;

                    case LONG_TYPE:
                        aDestMsg.set(key, (long) ((((long) bytes[pos++] & XFF)) | (((long) bytes[pos++] & XFF) << EIGHT)
                                | (((long) bytes[pos++] & XFF) << SIXTEEN) | (((long) bytes[pos++] & XFF) << TWENTY_FOUR)
                                | (((long) bytes[pos++] & XFF) << THIRTY_TWO) | (((long) bytes[pos++] & XFF) << FORTY)
                                | (((long) bytes[pos++] & XFF) << FORTY_EIGHT) | (((long) bytes[pos++]) << FIFTY_SIX)));
                        break;

                    case NULL_TYPE:
                        // do nothing
                        break;

                    // All non fixed type
                    default:
                        final byte typeMask = (byte) (type & TYPE_MASK);
                        final int lengthMask = (LENGTH_MASK & type);
                        final int fieldLength = (lengthMask < LENGTH_ENCODED_IN_AN_UNSIGNED_BYTE) ? lengthMask
                                : (lengthMask == LENGTH_ENCODED_IN_AN_UNSIGNED_BYTE) ? XFF & bytes[pos++] : (bytes[pos++] & XFF)
                                        | ((bytes[pos++] & XFF) << EIGHT) | ((bytes[pos++] & XFF) << SIXTEEN) | ((bytes[pos++] & XFF) << TWENTY_FOUR);
                        if (lengthMask != 0) {
                            switch (typeMask) {

                            case NULL_TYPE:
                                aDestMsg.set(key, Null.valueOf(fieldLength));
                                pos += fieldLength;
                                break;

                            case STRING_UTF_8_TYPE:
                                aDestMsg.set(key, new String(bytes, pos, fieldLength, UTF_8_CHARSET));
                                pos += fieldLength;
                                break;

                            case MSG_TYPE:
                                final Msg msg = IndexedObjectsMsg.create();
                                this.deserializeFromBytes(bytes, pos, fieldLength, msg);
                                pos += fieldLength;
                                aDestMsg.set(key, msg);

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
                                    aDestMsg.set(key, byteArray);
                                    break;
                                case SHORT_TYPE:
                                    final short[] shorts = new short[fixedArrayLength >> ONE];
                                    for (int i = 0; i < shorts.length; i++) {
                                        shorts[i] = (short) ((((short) bytes[pos++] & XFF)) | (((short) bytes[pos++] & XFF) << EIGHT));
                                    }
                                    aDestMsg.set(key, shorts);
                                    break;
                                case INT_TYPE:
                                    final int[] ints = new int[fixedArrayLength >> TWO];
                                    for (int i = 0; i < ints.length; i++) {
                                        ints[i] = (((int) bytes[pos++] & XFF)) | (((int) bytes[pos++] & XFF) << EIGHT)
                                                | (((int) bytes[pos++] & XFF) << SIXTEEN) | (((int) bytes[pos++] & XFF) << TWENTY_FOUR);
                                    }
                                    aDestMsg.set(key, ints);
                                    break;
                                case DOUBLE_TYPE:
                                    final double[] doubles = new double[fixedArrayLength >> THREE];
                                    for (int i = 0; i < doubles.length; i++) {
                                        doubles[i] = Double.longBitsToDouble((((long) bytes[pos++] & XFF) | (((long) bytes[pos++] & XFF) << EIGHT)
                                                | (((long) bytes[pos++] & XFF) << SIXTEEN) | (((long) bytes[pos++] & XFF) << TWENTY_FOUR)
                                                | (((long) bytes[pos++] & XFF) << THIRTY_TWO) | (((long) bytes[pos++] & XFF) << FORTY)
                                                | (((long) bytes[pos++] & XFF) << FORTY_EIGHT) | (((long) bytes[pos++] & XFF) << FIFTY_SIX)));
                                    }
                                    aDestMsg.set(key, doubles);
                                    break;
                                case LONG_TYPE:
                                    final long[] longs = new long[fixedArrayLength >> THREE];
                                    for (int i = 0; i < longs.length; i++) {
                                        longs[i] = (((long) bytes[pos++] & XFF)) | (((long) bytes[pos++] & XFF) << EIGHT)
                                                | (((long) bytes[pos++] & XFF) << SIXTEEN) | (((long) bytes[pos++] & XFF) << TWENTY_FOUR)
                                                | (((long) bytes[pos++] & XFF) << THIRTY_TWO) | (((long) bytes[pos++] & XFF) << FORTY)
                                                | (((long) bytes[pos++] & XFF) << FORTY_EIGHT) | (((long) bytes[pos++] & XFF) << FIFTY_SIX);
                                    }
                                    aDestMsg.set(key, longs);
                                    break;
                                case FLOAT_TYPE:
                                    final float[] floats = new float[fixedArrayLength >> TWO];
                                    for (int i = 0; i < floats.length; i++) {
                                        floats[i] = Float.intBitsToFloat((((int) bytes[pos++] & XFF)) | (((int) bytes[pos++] & XFF) << EIGHT)
                                                | (((int) bytes[pos++] & XFF) << SIXTEEN) | (((int) bytes[pos++] & XFF) << TWENTY_FOUR));
                                    }
                                    aDestMsg.set(key, floats);
                                    break;
                                default:
                                    break;
                                }
                                break;

                            case ARRAY_VARIABLE_VALUE_TYPE:

                                // For variable type
                                // TODO
                                if (lengthMask != 0) {
                                    final IndexedObjectsMsg arrayAsMsg = IndexedObjectsMsg.create();
                                    this.deserializeFromBytes(bytes, pos, fieldLength, arrayAsMsg);
                                    pos += fieldLength;
                                    final int variableArrayLength = arrayAsMsg.getKeysLength();
                                    // TODO fix, check if the the first key not null (here we consider the first one is always not null)
                                    final Type arraytype = arrayAsMsg.getType(0);
                                    switch (arraytype) {

                                    case BYTE:
                                        final Byte[] byteArray = new Byte[variableArrayLength];
                                        for (int arrayKey = 0; arrayKey < variableArrayLength; arrayKey++) {
                                            byteArray[arrayKey] = arrayAsMsg.getAsNullableByte(arrayKey);
                                        }
                                        aDestMsg.set(key, byteArray);
                                        break;
                                        
                                    case SHORT:
                                        final Short[] shorts = new Short[variableArrayLength];
                                        for (int arrayKey = 0; arrayKey < variableArrayLength; arrayKey++) {
                                            shorts[arrayKey] = arrayAsMsg.getAsNullableShort(arrayKey);
                                        }
                                        aDestMsg.set(key, shorts);
                                        break;
                                        
                                    case INT:
                                        final Integer[] integers = new Integer[variableArrayLength];
                                        for (int arrayKey = 0; arrayKey < variableArrayLength; arrayKey++) {
                                            integers[arrayKey] = arrayAsMsg.getAsNullableInteger(arrayKey);
                                        }
                                        aDestMsg.set(key, integers);
                                        break;
                                        
                                    case LONG:
                                        final Long[] longs = new Long[variableArrayLength];
                                        for (int arrayKey = 0; arrayKey < variableArrayLength; arrayKey++) {
                                            longs[arrayKey] = arrayAsMsg.getAsNullableLong(arrayKey);
                                        }
                                        aDestMsg.set(key, longs);
                                        break;

                                    case FIVE_BITS_DECIMAL:
                                    case DOUBLE:
                                        final Double[] doubles = new Double[variableArrayLength];
                                        for (int arrayKey = 0; arrayKey < variableArrayLength; arrayKey++) {
                                            doubles[arrayKey] = arrayAsMsg.getAsNullableDouble(arrayKey);
                                        }
                                        aDestMsg.set(key, doubles);
                                        break;

                                    case FLOAT:
                                        final Float[] floats = new Float[variableArrayLength];
                                        for (int arrayKey = 0; arrayKey < variableArrayLength; arrayKey++) {
                                            floats[arrayKey] = arrayAsMsg.getAsNullableFloat(arrayKey);
                                        }
                                        aDestMsg.set(key, floats);
                                        break;

                                    case STRING_UTF8:
                                        final String[] strings = new String[variableArrayLength];
                                        for (int arrayKey = 0; arrayKey < variableArrayLength; arrayKey++) {
                                            strings[arrayKey] = arrayAsMsg.getAsString(arrayKey);
                                        }
                                        aDestMsg.set(key, strings);
                                        break;

                                    case MSG:
                                        final Msg[] msgs = new IndexedPrimitivesObjectsMsg[variableArrayLength];
                                        for (int arrayKey = 0; arrayKey < variableArrayLength; arrayKey++) {
                                            msgs[arrayKey] = arrayAsMsg.getAsMsg(arrayKey);
                                        }
                                        aDestMsg.set(key, msgs);
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
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.MsgSerializer#writeTo(com.github.hermod.ser.Msg)
     */
    @Override
    public byte[] serializeToBytes(final Msg aSrcMsg) {
        final byte[] bytes = new byte[getLength(aSrcMsg)];
        serializeToBytes(aSrcMsg, bytes, 0);
        return bytes;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.MsgSerializer#writeTo(com.github.hermod.ser.Msg, byte[], int)
     */
    @Override
    public int serializeToBytes(final Msg aSrcMsg, byte[] aDestBytes, int aDestOffset) {
        if (aSrcMsg.isBytesSerializable()) {
            return ((BytesSerializable) aSrcMsg).serializeToBytes(aDestBytes, aDestOffset);
        } else {
            // Calculate length
            // TODO to optimize with a try catch
            final int msgLength2 = getLength(aSrcMsg);
            if (aDestBytes.length - aDestOffset < msgLength2) {
                throw new IllegalArgumentException("Bytes array too small from the offset");
            }
            int pos = aDestOffset;
            int consecutiveNullKey = 0;

            // Write Type / Values
            final Object[] objects = aSrcMsg.getAllAsObjects();
            for (int key = 0; key < objects.length; key++) {
                if (objects[key] != null) {
                    if (consecutiveNullKey != 0) {
                        if (consecutiveNullKey == 1) {
                            aDestBytes[pos++] = NULL_TYPE;
                        } else {
                            aDestBytes[pos] = SKIPPED_KEYS_TYPE;
                            pos = writeVariableLength(aDestBytes, pos, consecutiveNullKey - 1, ONE);
                        }
                        consecutiveNullKey = 0;
                    }

                    final Object object = objects[key];
                    if (object instanceof Byte || object instanceof Boolean) {
                        aDestBytes[pos++] = BYTE_TYPE;
                        aDestBytes[pos++] = (byte) aSrcMsg.getAsByte(key);
                    } else if (object instanceof Short) {
                        final short aShort = (short) aSrcMsg.getAsShort(key);
                        aDestBytes[pos++] = SHORT_TYPE;
                        aDestBytes[pos++] = (byte) (aShort);
                        aDestBytes[pos++] = (byte) (aShort >> EIGHT);
                    } else if (object instanceof Integer) {
                        final int aInt = (int) aSrcMsg.getAsInt(key);
                        aDestBytes[pos++] = INT_TYPE;
                        aDestBytes[pos++] = (byte) (aInt);
                        aDestBytes[pos++] = (byte) (aInt >> EIGHT);
                        aDestBytes[pos++] = (byte) (aInt >> SIXTEEN);
                        aDestBytes[pos++] = (byte) (aInt >> TWENTY_FOUR);
                    } else if (object instanceof Double) {
                        final long longBits = Double.doubleToLongBits(aSrcMsg.getAsDouble(key));
                        aDestBytes[pos++] = DOUBLE_TYPE;
                        aDestBytes[pos++] = (byte) (longBits);
                        aDestBytes[pos++] = (byte) (longBits >> EIGHT);
                        aDestBytes[pos++] = (byte) (longBits >> SIXTEEN);
                        aDestBytes[pos++] = (byte) (longBits >> TWENTY_FOUR);
                        aDestBytes[pos++] = (byte) (longBits >> THIRTY_TWO);
                        aDestBytes[pos++] = (byte) (longBits >> FORTY);
                        aDestBytes[pos++] = (byte) (longBits >> FORTY_EIGHT);
                        aDestBytes[pos++] = (byte) (longBits >> FIFTY_SIX);
                    } else if (object instanceof Float) {
                        final int intBits = Float.floatToIntBits(aSrcMsg.getAsFloat(key));
                        aDestBytes[pos++] = FLOAT_TYPE;
                        aDestBytes[pos++] = (byte) (intBits);
                        aDestBytes[pos++] = (byte) (intBits >> EIGHT);
                        aDestBytes[pos++] = (byte) (intBits >> SIXTEEN);
                        aDestBytes[pos++] = (byte) (intBits >> TWENTY_FOUR);
                    } else if (object instanceof Long) {
                        final long aLong = aSrcMsg.getAsLong(key);
                        aDestBytes[pos++] = LONG_TYPE;
                        aDestBytes[pos++] = (byte) (aLong);
                        aDestBytes[pos++] = (byte) (aLong >> EIGHT);
                        aDestBytes[pos++] = (byte) (aLong >> SIXTEEN);
                        aDestBytes[pos++] = (byte) (aLong >> TWENTY_FOUR);
                        aDestBytes[pos++] = (byte) (aLong >> THIRTY_TWO);
                        aDestBytes[pos++] = (byte) (aLong >> FORTY);
                        aDestBytes[pos++] = (byte) (aLong >> FORTY_EIGHT);
                        aDestBytes[pos++] = (byte) (aLong >> FIFTY_SIX);
                    } else if (object instanceof String) {
                        final String aString = (String) object;
                        aDestBytes[pos++] = STRING_UTF_8_TYPE;
                        if (aString != null) {
                            if (Msgs.isAsciiString(aString)) {
                                final int stringLength = aString.length();
                                pos = writeVariableLength(aDestBytes, pos - 1, stringLength, (stringLength == 0) ? TWO : ONE);
                                for (int j = 0; j < stringLength; j++) {
                                    aDestBytes[pos++] = (byte) aString.charAt(j);
                                }
                            } else {
                                final byte[] stringBytes = aString.getBytes(UTF_8_CHARSET);
                                final int stringLength = stringBytes.length;
                                pos = writeVariableLength(aDestBytes, pos - 1, stringLength, (stringLength == 0) ? TWO : ONE);
                                System.arraycopy(stringBytes, 0, aDestBytes, pos, stringLength);
                                pos += stringBytes.length;
                            }

                        }
                    } else if (object instanceof Msg) {
                        final Msg aMsg = (Msg) object;
                        aDestBytes[pos++] = MSG_TYPE;
                        if (aMsg != null) {
                            final int msgLength = this.getLength(aMsg);
                            pos = writeVariableLength(aDestBytes, pos - 1, msgLength, (msgLength == 0) ? TWO : ONE);
                            pos = this.serializeToBytes(aSrcMsg, aDestBytes, pos);
                        }
                    } else if (object instanceof byte[]) {
                        aDestBytes[pos++] = ARRAY_FIXED_VALUE_TYPE;
                        final byte[] fieldBytes = (byte[]) object;
                        final int bytesLength = fieldBytes.length + 1;
                        pos = writeVariableLength(aDestBytes, pos - 1, bytesLength, (bytesLength == 0) ? TWO : ONE);
                        aDestBytes[pos++] = BYTE_TYPE;
                        System.arraycopy(fieldBytes, 0, aDestBytes, pos, fieldBytes.length);
                        pos += fieldBytes.length;
                    } else if (object instanceof short[]) {
                        aDestBytes[pos++] = ARRAY_FIXED_VALUE_TYPE;
                        final short[] shorts = (short[]) object;
                        final int shortsLength = (shorts.length << 1) + 1;
                        pos = writeVariableLength(aDestBytes, pos - 1, shortsLength, (shortsLength == 0) ? TWO : ONE);
                        aDestBytes[pos++] = SHORT_TYPE;
                        for (final short currentShort : shorts) {
                            aDestBytes[pos++] = (byte) (currentShort);
                            aDestBytes[pos++] = (byte) (currentShort >> EIGHT);
                        }
                    } else if (object instanceof int[]) {
                        aDestBytes[pos++] = ARRAY_FIXED_VALUE_TYPE;
                        final int[] ints = (int[]) object;
                        final int intsLength = (ints.length << TWO) + 1;
                        pos = writeVariableLength(aDestBytes, pos - 1, intsLength, (intsLength == 0) ? TWO : ONE);
                        aDestBytes[pos++] = INT_TYPE;
                        for (final int currentInt : ints) {
                            aDestBytes[pos++] = (byte) (currentInt);
                            aDestBytes[pos++] = (byte) (currentInt >> EIGHT);
                            aDestBytes[pos++] = (byte) (currentInt >> SIXTEEN);
                            aDestBytes[pos++] = (byte) (currentInt >> TWENTY_FOUR);
                        }
                    } else if (object instanceof long[]) {
                        aDestBytes[pos++] = ARRAY_FIXED_VALUE_TYPE;
                        final long[] longs = (long[]) object;
                        final int longsLength = (longs.length << THREE) + 1;
                        pos = writeVariableLength(aDestBytes, pos - 1, longsLength, (longsLength == 0) ? TWO : ONE);
                        aDestBytes[pos++] = LONG_TYPE;
                        for (final long currentLong : longs) {
                            aDestBytes[pos++] = (byte) (currentLong);
                            aDestBytes[pos++] = (byte) (currentLong >> EIGHT);
                            aDestBytes[pos++] = (byte) (currentLong >> SIXTEEN);
                            aDestBytes[pos++] = (byte) (currentLong >> TWENTY_FOUR);
                            aDestBytes[pos++] = (byte) (currentLong >> THIRTY_TWO);
                            aDestBytes[pos++] = (byte) (currentLong >> FORTY);
                            aDestBytes[pos++] = (byte) (currentLong >> FORTY_EIGHT);
                            aDestBytes[pos++] = (byte) (currentLong >> FIFTY_SIX);
                        }
                    } else if (object instanceof double[]) {
                        aDestBytes[pos++] = ARRAY_FIXED_VALUE_TYPE;
                        final double[] doubles = (double[]) object;
                        final int doublesLength = (doubles.length << THREE) + 1;
                        pos = writeVariableLength(aDestBytes, pos - 1, doublesLength, (doublesLength == 0) ? TWO : ONE);
                        aDestBytes[pos++] = DOUBLE_TYPE;
                        for (final double currentDouble : doubles) {
                            final long currentLong = Double.doubleToLongBits(currentDouble);
                            aDestBytes[pos++] = (byte) (currentLong);
                            aDestBytes[pos++] = (byte) (currentLong >> EIGHT);
                            aDestBytes[pos++] = (byte) (currentLong >> SIXTEEN);
                            aDestBytes[pos++] = (byte) (currentLong >> TWENTY_FOUR);
                            aDestBytes[pos++] = (byte) (currentLong >> THIRTY_TWO);
                            aDestBytes[pos++] = (byte) (currentLong >> FORTY);
                            aDestBytes[pos++] = (byte) (currentLong >> FORTY_EIGHT);
                            aDestBytes[pos++] = (byte) (currentLong >> FIFTY_SIX);
                        }
                    } else if (object instanceof float[]) {
                        aDestBytes[pos++] = ARRAY_FIXED_VALUE_TYPE;
                        final float[] floats = (float[]) object;
                        final int floatsLength = (floats.length << TWO) + 1;
                        pos = writeVariableLength(aDestBytes, pos - 1, floatsLength, (floatsLength == 0) ? TWO : ONE);
                        aDestBytes[pos++] = FLOAT_TYPE;
                        for (final float currentFloat : floats) {
                            final int currentInt = Float.floatToIntBits(currentFloat);
                            aDestBytes[pos++] = (byte) (currentInt);
                            aDestBytes[pos++] = (byte) (currentInt >> EIGHT);
                            aDestBytes[pos++] = (byte) (currentInt >> SIXTEEN);
                            aDestBytes[pos++] = (byte) (currentInt >> TWENTY_FOUR);
                        }
                    } else if (object instanceof Object[]) {
                        aDestBytes[pos++] = ARRAY_VARIABLE_VALUE_TYPE;
                        int arrayVariableValueLength = 0;
                        if (object != null) {
                            final Object[] objects2 = ((Object[]) object);
                            // TODO fix, check if the the first key not null (here we consider the first one is always not null)
                            final Msg msg = serializeArrayVariableValueAsMsg(objects2);
                            arrayVariableValueLength = this.getLength(msg);
                            pos = writeVariableLength(aDestBytes, pos - 1, arrayVariableValueLength, (arrayVariableValueLength == 0) ? TWO : ONE);
                            pos = this.serializeToBytes(msg, aDestBytes, pos);
                        }
                    } else if (object instanceof Null) {
                        aDestBytes[pos++] = NULL_TYPE;
                        final Null nul = (Null) object;
                        final int nullLength = nul.getLength();
                        pos = writeVariableLength(aDestBytes, pos - 1, nullLength, ONE);
                        for (int i = 0; i < nul.getLength(); i++) {
                            aDestBytes[pos++] = 0;
                        }
                    } else {
                        // Should not occured,
                    }
                } else {
                    consecutiveNullKey++;
                }
            }
            return pos;
        }
    }

    /**
     * writeVariableSize.
     * 
     * @param bytes
     * @param pos
     * @param length
     * @param forceEncodingZeroOn2Bits
     * @return
     */
    private int writeVariableLength(final byte[] bytes, int pos, final int length, final int forceEncodingAtLeastOn) {
        if (length < LENGTH_ENCODED_IN_AN_UNSIGNED_BYTE && forceEncodingAtLeastOn == ONE) {
            bytes[pos++] |= (byte) length;
        } else {
            final boolean isEncodedInAnInt = (length > MAX_VALUE_FOR_UNSIGNED_BYTE) || (forceEncodingAtLeastOn == FIVE);
            bytes[pos++] |= (byte) ((isEncodedInAnInt) ? LENGTH_ENCODED_IN_AN_INT : LENGTH_ENCODED_IN_AN_UNSIGNED_BYTE);
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
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.MsgSerializer#getLength(com.github.hermod.ser.Msg)
     */
    @Override
    public int getLength(final Msg aMsg) {
        if (aMsg.isSerializable()) {
            return ((Serializable) aMsg).getLength();
        } else {
            final Object[] objects = aMsg.getAllAsObjects();
            int length = 0;
            int consecutiveNullKey = 0;
            for (final Object object : objects) {
                if (object != null) {
                    length += getValueLength(object);
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
    }

    /**
     * getVariableSize.
     * 
     * @param length
     * @param forceEncodingZeroOn2Bits
     * @return
     */
    private int getVariableLength(final int length, final int forceEncodingAtLeastOn) {
        return (length < LENGTH_ENCODED_IN_AN_UNSIGNED_BYTE && (forceEncodingAtLeastOn == ONE)) ? ONE
                : (length > MAX_VALUE_FOR_UNSIGNED_BYTE || forceEncodingAtLeastOn == FIVE) ? FIVE : TWO;
    }

    /**
     * getValueSize.
     * 
     * @param object
     * @return
     */
    private int getValueLength(final Object object) {
        if (object instanceof Byte) {
            return TWO;
        } else if (object instanceof Short) {
            return THREE;
        } else if (object instanceof Integer || object instanceof Float) {
            return FIVE;
        } else if (object instanceof Long || object instanceof Double) {
            return NINE;
        } else if (object instanceof Boolean) {
            return TWO;
        } else if (object instanceof String) {
            final int stringUtf8length = (object != null) ? ((String) object).getBytes(UTF_8_CHARSET).length : 0;
            return getVariableLength(stringUtf8length, (stringUtf8length == 0) ? TWO : ONE) + stringUtf8length;
        } else if (object instanceof Msg) {
            final int msgLength = (object != null) ? this.getLength((Msg) object) : 0;
            return getVariableLength(msgLength, (msgLength == 0) ? TWO : ONE) + msgLength;
        } else if (object instanceof byte[]) {
            final int bytesLength = ((byte[]) object).length + 1;
            return getVariableLength(bytesLength, (bytesLength == 0) ? TWO : ONE) + bytesLength;
        } else if (object instanceof short[]) {
            final int shortsLength = ((short[]) object).length << ONE + 1;
            return getVariableLength(shortsLength, (shortsLength == 0) ? TWO : ONE) + shortsLength;
        } else if (object instanceof int[]) {
            final int intsLength = ((int[]) object).length << TWO + 1;
            return getVariableLength(intsLength, (intsLength == 0) ? TWO : ONE) + intsLength;
        } else if (object instanceof long[]) {
            final int longsLength = ((long[]) object).length << THREE + 1;
            return getVariableLength(longsLength, (longsLength == 0) ? TWO : ONE) + longsLength;
        } else if (object instanceof double[]) {
            final int doublesLength = ((double[]) object).length << THREE + 1;
            return getVariableLength(doublesLength, (doublesLength == 0) ? TWO : ONE) + doublesLength;
        } else if (object instanceof float[]) {
            final int floatsLength = ((float[]) object).length << TWO + 1;
            return getVariableLength(floatsLength, (floatsLength == 0) ? TWO : ONE) + floatsLength;
        } else if (object instanceof Object[]) {
            int arrayVariableValueLength = ONE;
            if (object != null) {
                final Object[] objects = ((Object[]) object);
                final Msg arrayVariableValueAsMsg = serializeArrayVariableValueAsMsg(objects);
                final int arrayVariableValueAsMsgLength = this.getLength(arrayVariableValueAsMsg);
                arrayVariableValueLength = getVariableLength(arrayVariableValueAsMsgLength, (arrayVariableValueAsMsgLength == 0) ? TWO : ONE)
                        + arrayVariableValueAsMsgLength;
            }
            return arrayVariableValueLength;
        } else if (object instanceof Null) {
            final int length = ((Null) object).getLength();
            return getVariableLength(length, ONE) + length;
        } else {
            return ZERO;
        }
    }

    /**
     * serializeArrayVariableValueAsMsg.
     * 
     * @param objects
     * @return
     */
    private Msg serializeArrayVariableValueAsMsg(final Object[] objects) {
        final IndexedObjectsMsg msg = IndexedObjectsMsg.createWithKeyMax(objects.length);
        if (objects != null) {
            int i = 0;
            boolean firstElementSet = false;
            //  Force first element without compression
            while (i < objects.length && objects[i] != null && !firstElementSet) {
                msg.set(i, objects[i], false);
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
     * @see com.github.hermod.ser.ByteBufferMsgSerializer#serializeTo(com.github.hermod.ser.Msg, java.nio.ByteBuffer)
     */
    @Override
    public int serializeToByteBuffer(Msg aSrcMsg, ByteBuffer aDestByteBuffer) {
        aDestByteBuffer.put(serializeToBytes(aSrcMsg));
        return aDestByteBuffer.position();
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.ByteBufferMsgSerializer#deserializeFromByteBuffer(java.nio.ByteBuffer, com.github.hermod.ser.Msg)
     */
    @Override
    public void deserializeFromByteBuffer(ByteBuffer aSrcByteBuffer, Msg aDestMsg) {
        final byte[] bytes = new byte[aSrcByteBuffer.remaining()];
        aSrcByteBuffer.get(bytes);
        deserializeFromBytes(bytes, 0, bytes.length, aDestMsg);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.ByteBufferMsgSerializer#serializeToByteBuffer(com.github.hermod.ser.Msg)
     */
    @Override
    public ByteBuffer serializeToByteBuffer(Msg aSrcMsg) {
        final int length = this.getLength(aSrcMsg);
        final ByteBuffer aDestByteBuffer = ByteBuffer.allocate(length);
        serializeToByteBuffer(aSrcMsg, aDestByteBuffer);
        return aDestByteBuffer;
    }

}
