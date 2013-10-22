package com.github.hermod.ser.impl;

import static com.github.hermod.ser.Types.ARRAY_FIXED_VALUE_TYPE;
import static com.github.hermod.ser.Types.MSG_TYPE;
import static com.github.hermod.ser.Types.NULL_TYPE;
import static com.github.hermod.ser.Types.STRING_ISO_8859_1_TYPE;
import static com.github.hermod.ser.Types.TYPE_MASK;
import static com.github.hermod.ser.impl.Msgs.BYTE_TYPE;
import static com.github.hermod.ser.impl.Msgs.DOUBLE_TYPE;
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
import static com.github.hermod.ser.impl.Msgs.ONE;
import static com.github.hermod.ser.impl.Msgs.SHORT_TYPE;
import static com.github.hermod.ser.impl.Msgs.SIXTEEN;
import static com.github.hermod.ser.impl.Msgs.THIRTY_TWO;
import static com.github.hermod.ser.impl.Msgs.THREE;
import static com.github.hermod.ser.impl.Msgs.TWENTY_FOUR;
import static com.github.hermod.ser.impl.Msgs.TWO;
import static com.github.hermod.ser.impl.Msgs.XFF;

import java.nio.ByteBuffer;

import com.github.hermod.ser.ByteBufferMsgSerializer;
import com.github.hermod.ser.BytesMsgSerializer;
import com.github.hermod.ser.BytesSerializable;
import com.github.hermod.ser.Msg;

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
        if (aDestMsg instanceof BytesSerializable) {
            ((BytesSerializable) aDestMsg).deserializeFromBytes(bytes, offset, length);
        } else {
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
                            : (bytes[pos++] & XFF) | ((bytes[pos++] & XFF) << EIGHT) | ((bytes[pos++] & XFF) << SIXTEEN) | ((bytes[pos++] & XFF) << TWENTY_FOUR))) + 1;
                } else {
                    final int lengthMask = bytes[pos++] & LENGTH_MASK;
                    // TODO to optimize
                    pos += (((lengthMask < LENGTH_ENCODED_IN_A_BIT) ? lengthMask
                            : (lengthMask == LENGTH_ENCODED_IN_A_BIT) ? bytes[pos] : ((bytes[pos] & XFF) | ((bytes[pos + ONE] & XFF) << EIGHT)
                                    | ((bytes[pos + TWO] & XFF) << SIXTEEN) | ((bytes[pos + THREE] & XFF) << TWENTY_FOUR))));
                    pos += (lengthMask < LENGTH_ENCODED_IN_A_BIT) ? 0 : (lengthMask == LENGTH_ENCODED_IN_A_BIT) ? 1 : FOUR;

                    key++;
                }
            }
            aDestMsg.removeAll();

            // Decoding
            key = 0;
            pos = offset;
            while (pos < offset + length) {
                final byte type = bytes[pos++];

                // Skip null key
                if ((type & TYPE_MASK) == NULL_TYPE) {
                    final int lengthMask = type & LENGTH_MASK;
                    key += ((lengthMask < LENGTH_ENCODED_IN_A_BIT) ? lengthMask : (lengthMask == LENGTH_ENCODED_IN_A_BIT) ? bytes[pos++]
                            : (bytes[pos++] & XFF) | ((bytes[pos++] & XFF) << EIGHT) | ((bytes[pos++] & XFF) << SIXTEEN) | ((bytes[pos++] & XFF) << TWENTY_FOUR)) + 1;
                }
                // Decode values
                else {
                    switch (type) {
                    //  All fixed type
                    case BYTE_TYPE:
                        aDestMsg.set(key, (byte) ((bytes[pos++] & XFF)));
                        break;

                    case SHORT_TYPE:
                        aDestMsg.set(key, (((short) bytes[pos++] & XFF)) | (((short) bytes[pos++] & XFF) << EIGHT));
                        break;

                    case INT_TYPE:
                        aDestMsg.set(key, (((int) bytes[pos++] & XFF)) | (((int) bytes[pos++] & XFF) << EIGHT) | (((int) bytes[pos++] & XFF) << SIXTEEN)
                                | (((int) bytes[pos++] & XFF) << TWENTY_FOUR));
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
                                        | (((long) bytes[pos++] & XFF) << TWENTY_FOUR) | (((long) bytes[pos++] & XFF) << THIRTY_TWO)), ((byte) bytes[pos++] & XFF));
                        break;

                    case FLOAT_TYPE:
                        aDestMsg.set(
                                key,
                                Float.intBitsToFloat((((int) bytes[pos++] & XFF)) | (((int) bytes[pos++] & XFF) << EIGHT)
                                        | (((int) bytes[pos++] & XFF) << SIXTEEN) | (((int) bytes[pos++] & XFF) << TWENTY_FOUR)));
                        break;

                    case LONG_TYPE:
                        aDestMsg.set(key, (((long) bytes[pos++] & XFF)) | (((long) bytes[pos++] & XFF) << EIGHT) | (((long) bytes[pos++] & XFF) << SIXTEEN)
                                | (((long) bytes[pos++] & XFF) << TWENTY_FOUR) | (((long) bytes[pos++] & XFF) << THIRTY_TWO) | (((long) bytes[pos++] & XFF) << FORTY)
                                | (((long) bytes[pos++] & XFF) << FORTY_EIGHT) | (((long) bytes[pos++] & XFF) << FIFTY_SIX));
                        break;

                    // All non fixed type
                    default:
                        final byte typeMask = (byte) (type & TYPE_MASK);
                        final int lengthMask = (LENGTH_MASK & type);
                        final int fieldLength = (lengthMask < LENGTH_ENCODED_IN_A_BIT) ? lengthMask
                                : (lengthMask == LENGTH_ENCODED_IN_A_BIT) ? bytes[pos++] : (bytes[pos++] & XFF) | ((bytes[pos++] & XFF) << EIGHT)
                                        | ((bytes[pos++] & XFF) << SIXTEEN) | ((bytes[pos++] & XFF) << TWENTY_FOUR);

                        switch (typeMask) {
                        case STRING_ISO_8859_1_TYPE:
                            // TODO manage null value
                            if (lengthMask != 0) {
                                final char[] chars = new char[fieldLength];
                                for (int i = 0; i < fieldLength; i++) {
                                    chars[i] = (char) bytes[pos++];
                                }
                                aDestMsg.set(key, new String(chars));
                            }
                            break;

                        case MSG_TYPE:
                            // TODO manage null value
                            if (lengthMask != 0) {
                                final Msg msg = IndexedPrimitivesObjectsMsg.create();
                                ((BytesSerializable) msg).deserializeFromBytes(bytes, pos, fieldLength);
                                pos += fieldLength;
                                aDestMsg.set(key, msg);
                            }
                            break;

                        case ARRAY_FIXED_VALUE_TYPE:
                            final byte arrayType = bytes[pos++];
                            if (arrayType == NULL_TYPE) {
                                // For variable type
                                // TODO 

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
        if (aSrcMsg instanceof BytesSerializable) {
            return ((BytesSerializable) aSrcMsg).serializeToBytes(aDestBytes, aDestOffset);
        } else {
            // Calculate length
            // TODO to optimize with a try catch
            final int msgLength = getLength(aSrcMsg);
            if (aDestBytes.length - aDestOffset < msgLength) {
                throw new IllegalArgumentException("Bytes array too small from the offset");
            }
            int pos = aDestOffset;

            // Write Type / Values
            final int[] keys = aSrcMsg.retrieveKeys();
            int previousKey = 0;
            for (int j = 0; j < keys.length; j++) {
                final int key = keys[j];
                final int delta = key - previousKey;
                if (delta > 1) {
                    pos = writeVariableLength(aDestBytes, pos, delta - 1, false);
                }
                previousKey = key;

                final Object object = aSrcMsg.getAsObject(key);
                //  all fixed type
                if (object instanceof Byte) {
                    aDestBytes[pos++] = BYTE_TYPE;
                    aDestBytes[pos++] = (byte) aSrcMsg.getAsByte(key);
                } else if (object instanceof Short) {
                    final int aShort = (short) aSrcMsg.getAsShort(key);
                    aDestBytes[pos++] = SHORT_TYPE;
                    aDestBytes[pos++] = (byte) (aShort);
                    aDestBytes[pos++] = (byte) (aShort >> EIGHT);
                } else if (object instanceof Byte) {
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
                    final String aString = (String) aSrcMsg.getAsString(key);
                    if (aString != null) {
                        final int length = aString.length();
                        pos = writeVariableLength(aDestBytes, pos - 1, length, FORCE_ENCODING_ZERO_ON_2BITS);
                        for (int k = 0; k < length; k++) {
                            aDestBytes[pos++] = (byte) aString.charAt(k);
                        }
                    }
                } else if (object instanceof Msg) {
                    final Msg aMsg = (Msg) aSrcMsg.getAsMsg(key);
                    if (aMsg != null) {
                        final int length = ((BytesSerializable) aMsg).getLength();
                        pos = writeVariableLength(aDestBytes, pos - 1, length, FORCE_ENCODING_ZERO_ON_2BITS);
                        pos = ((BytesSerializable) aMsg).serializeToBytes(aDestBytes, pos);
                    }
                    break;

                } else if (object instanceof Object[]) {
                    // TODO
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
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.MsgSerializer#getLength(com.github.hermod.ser.Msg)
     */
    @Override
    public int getLength(final Msg aMsg) {
        if (aMsg instanceof BytesSerializable) {
            return ((BytesSerializable) aMsg).getLength();
        } else {
            int length = 0;
            // Size of the different values
            final int[] keys = aMsg.retrieveKeys();
            for (int i = 0; i < keys.length; i++) {
                length += getValueLength(aMsg.getAsObject(i));
            }
            // Add length of skip keys
            for (int i = 1; i < keys.length; i++) {
                final int delta = keys[i] - keys[i - 1];
                if (delta != 1) {
                    length += getVariableLength(delta, false);
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
    private int getVariableLength(final int length, final boolean forceEncodingZeroOn2Bits) {
        return (length < LENGTH_ENCODED_IN_A_BIT && !(forceEncodingZeroOn2Bits && length == 0)) ? ONE : (length <= Byte.MAX_VALUE) ? TWO : FIVE;
    }

    /**
     * getValueSize.
     * 
     * @param object
     * @return
     */
    private int getValueLength(final Object object) {
        if (object instanceof Byte) {
            return 1;
        } else if (object instanceof Short) {
            return 2;
        } else if (object instanceof Integer || object instanceof Float) {
            return FOUR;
        } else if (object instanceof Long || object instanceof Double) {
            return EIGHT;
        } else if (object instanceof String) {
            final int length = ((String) object).length();
            return getVariableLength(length, FORCE_ENCODING_ZERO_ON_2BITS) + length;
        } else if (object instanceof Msg) {
            final int length = getLength((Msg) object);
            return getVariableLength(length, FORCE_ENCODING_ZERO_ON_2BITS) + length;
        } else {
            return 0;
        }
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

    @Override
    public ByteBuffer serializeToByteBuffer(Msg aSrcMsg)
    {
        // TODO Auto-generated method stub
        return null;
    }


    

}
