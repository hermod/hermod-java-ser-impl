package com.github.hermod.ser.impl;

import static com.github.hermod.ser.Types.ARRAY_FIXED_VALUE_TYPE;
import static com.github.hermod.ser.Types.MSG_TYPE;
import static com.github.hermod.ser.Types.NULL_TYPE;
import static com.github.hermod.ser.Types.STRING_ISO_8859_1_TYPE;
import static com.github.hermod.ser.Types.TYPE_MASK;
import static com.github.hermod.ser.impl.Msgs.BYTE_TYPE;
import static com.github.hermod.ser.impl.Msgs.DOUBLE_TYPE;
import static com.github.hermod.ser.impl.Msgs.FIVE_BITS_DECIMAL_TYPE;
import static com.github.hermod.ser.impl.Msgs.FLOAT_TYPE;
import static com.github.hermod.ser.impl.Msgs.FORCE_ENCODING_ZERO_ON_2BITS;
import static com.github.hermod.ser.impl.Msgs.INT_TYPE;
import static com.github.hermod.ser.impl.Msgs.LENGTH_ENCODED_IN_A_BIT;
import static com.github.hermod.ser.impl.Msgs.LENGTH_MASK;
import static com.github.hermod.ser.impl.Msgs.LENGTH_ENCODED_IN_AN_INT;
import static com.github.hermod.ser.impl.Msgs.LONG_TYPE;
import static com.github.hermod.ser.impl.Msgs.SHORT_TYPE;

import com.github.hermod.ser.IBytesMsgSerializer;
import com.github.hermod.ser.IBytesSerializable;
import com.github.hermod.ser.IMsg;

/**
 * <p>DefaultSerializer. </p>
 * 
 * @author anavarro - Mar 11, 2013
 * 
 */
public final class DefaultMsgSerializer implements IBytesMsgSerializer {

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsgSerializer#readFrom(byte[], int, int, com.github.hermod.ser.IMsg)
     */
    @Override
    public void deserializeFrom(final byte[] bytes, final int offset, final int length, IMsg aDestMsg) {
        if (aDestMsg instanceof IBytesSerializable) {
            ((IBytesSerializable) aDestMsg).deserializeFrom(bytes, offset, length);
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
                            : (bytes[pos++] & 0xFF) | ((bytes[pos++] & 0xFF) << 8) | ((bytes[pos++] & 0xFF) << 16) | ((bytes[pos++] & 0xFF) << 24))) + 1;
                } else {
                    final int lengthMask = bytes[pos++] & LENGTH_MASK;
                    // TODO to optimize
                    pos += (((lengthMask < LENGTH_ENCODED_IN_A_BIT) ? lengthMask
                            : (lengthMask == LENGTH_ENCODED_IN_A_BIT) ? bytes[pos] : ((bytes[pos] & 0xFF) | ((bytes[pos + 1] & 0xFF) << 8)
                                    | ((bytes[pos + 2] & 0xFF) << 16) | ((bytes[pos + 3] & 0xFF) << 24))));
                    pos += (lengthMask < LENGTH_ENCODED_IN_A_BIT) ? 0 : (lengthMask == LENGTH_ENCODED_IN_A_BIT) ? 1 : 4;

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
                            : (bytes[pos++] & 0xFF) | ((bytes[pos++] & 0xFF) << 8) | ((bytes[pos++] & 0xFF) << 16) | ((bytes[pos++] & 0xFF) << 24)) + 1;
                }
                // Decode values
                else {
                    switch (type) {
                    //  All fixed type
                    case BYTE_TYPE:
                        aDestMsg.set(key, (byte) ((bytes[pos++] & 0xFF)));
                        break;

                    case SHORT_TYPE:
                        aDestMsg.set(key, (((short) bytes[pos++] & 0xFF)) | (((short) bytes[pos++] & 0xFF) << 8));
                        break;

                    case INT_TYPE:
                        aDestMsg.set(key, (((int) bytes[pos++] & 0xFF)) | (((int) bytes[pos++] & 0xFF) << 8) | (((int) bytes[pos++] & 0xFF) << 16)
                                | (((int) bytes[pos++] & 0xFF) << 24));
                        break;

                    case DOUBLE_TYPE:
                        aDestMsg.set(
                                key,
                                Double.longBitsToDouble((((long) bytes[pos++] & 0xFF) | (((long) bytes[pos++] & 0xFF) << 8)
                                        | (((long) bytes[pos++] & 0xFF) << 16) | (((long) bytes[pos++] & 0xFF) << 24)
                                        | (((long) bytes[pos++] & 0xFF) << 32) | (((long) bytes[pos++] & 0xFF) << 40)
                                        | (((long) bytes[pos++] & 0xFF) << 48) | (((long) bytes[pos++] & 0xFF) << 56))));
                        break;

                    case FIVE_BITS_DECIMAL_TYPE:
                        aDestMsg.set(
                                key,
                                Double.longBitsToDouble((((long) bytes[pos++] & 0xFF) << 8) | (((long) bytes[pos++] & 0xFF) << 16)
                                        | (((long) bytes[pos++] & 0xFF) << 24) | (((long) bytes[pos++] & 0xFF) << 32)), ((byte) bytes[pos++] & 0xFF));
                        break;

                    case FLOAT_TYPE:
                        aDestMsg.set(
                                key,
                                Float.intBitsToFloat((((int) bytes[pos++] & 0xFF)) | (((int) bytes[pos++] & 0xFF) << 8)
                                        | (((int) bytes[pos++] & 0xFF) << 16) | (((int) bytes[pos++] & 0xFF) << 24)));
                        break;

                    case LONG_TYPE:
                        aDestMsg.set(key, (((long) bytes[pos++] & 0xFF)) | (((long) bytes[pos++] & 0xFF) << 8) | (((long) bytes[pos++] & 0xFF) << 16)
                                | (((long) bytes[pos++] & 0xFF) << 24) | (((long) bytes[pos++] & 0xFF) << 32) | (((long) bytes[pos++] & 0xFF) << 40)
                                | (((long) bytes[pos++] & 0xFF) << 48) | (((long) bytes[pos++] & 0xFF) << 56));
                        break;

                    // All non fixed type
                    default:
                        final byte typeMask = (byte) (type & TYPE_MASK);
                        final int lengthMask = (LENGTH_MASK & type);
                        final int fieldLength = (lengthMask < LENGTH_ENCODED_IN_A_BIT) ? lengthMask
                                : (lengthMask == LENGTH_ENCODED_IN_A_BIT) ? bytes[pos++] : (bytes[pos++] & 0xFF) | ((bytes[pos++] & 0xFF) << 8)
                                        | ((bytes[pos++] & 0xFF) << 16) | ((bytes[pos++] & 0xFF) << 24);

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
                                final IMsg msg = new KeyObjectMsg();
                                ((IBytesSerializable) msg).deserializeFrom(bytes, pos, fieldLength);
                                pos += fieldLength;
                                aDestMsg.set(key, msg);
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
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsgSerializer#writeTo(com.github.hermod.ser.IMsg)
     */
    @Override
    public byte[] serializeToBytes(final IMsg aSrcMsg) {
        final byte[] bytes = new byte[getLength(aSrcMsg)];
        serializeToBytes(aSrcMsg, bytes, 0);
        return bytes;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsgSerializer#writeTo(com.github.hermod.ser.IMsg, byte[], int)
     */
    @Override
    public int serializeToBytes(final IMsg aSrcMsg, byte[] aDestBytes, int aDestOffset) {
        if (aSrcMsg instanceof IBytesSerializable) {
            return ((IBytesSerializable) aSrcMsg).serializeToBytes(aDestBytes, aDestOffset);
        } else {
            // Calculate length
            // TODO to optimize with a try catch
            final int msgLength = getLength(aSrcMsg);
            if (aDestBytes.length - aDestOffset < msgLength) {
                throw new IllegalArgumentException("Bytes array too small from the offset");
            }
            int pos = aDestOffset;

            // Write Type / Values
            final int[] keys = aSrcMsg.getKeys();
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
                    aDestBytes[pos++] = (byte) (aShort >> 8);
                } else if (object instanceof Byte) {
                    final int aInt = (int) aSrcMsg.getAsInt(key);
                    aDestBytes[pos++] = INT_TYPE;
                    aDestBytes[pos++] = (byte) (aInt);
                    aDestBytes[pos++] = (byte) (aInt >> 8);
                    aDestBytes[pos++] = (byte) (aInt >> 16);
                    aDestBytes[pos++] = (byte) (aInt >> 24);
                } else if (object instanceof Double) {
                    final long longBits = Double.doubleToLongBits(aSrcMsg.getAsDouble(key));
                    aDestBytes[pos++] = DOUBLE_TYPE;
                    aDestBytes[pos++] = (byte) (longBits);
                    aDestBytes[pos++] = (byte) (longBits >> 8);
                    aDestBytes[pos++] = (byte) (longBits >> 16);
                    aDestBytes[pos++] = (byte) (longBits >> 24);
                    aDestBytes[pos++] = (byte) (longBits >> 32);
                    aDestBytes[pos++] = (byte) (longBits >> 40);
                    aDestBytes[pos++] = (byte) (longBits >> 48);
                    aDestBytes[pos++] = (byte) (longBits >> 56);
                } else if (object instanceof Float) {
                    final int intBits = Float.floatToIntBits(aSrcMsg.getAsFloat(key));
                    aDestBytes[pos++] = FLOAT_TYPE;
                    aDestBytes[pos++] = (byte) (intBits);
                    aDestBytes[pos++] = (byte) (intBits >> 8);
                    aDestBytes[pos++] = (byte) (intBits >> 16);
                    aDestBytes[pos++] = (byte) (intBits >> 24);
                } else if (object instanceof Long) {
                    final long aLong = aSrcMsg.getAsLong(key);
                    aDestBytes[pos++] = LONG_TYPE;
                    aDestBytes[pos++] = (byte) (aLong);
                    aDestBytes[pos++] = (byte) (aLong >> 8);
                    aDestBytes[pos++] = (byte) (aLong >> 16);
                    aDestBytes[pos++] = (byte) (aLong >> 24);
                    aDestBytes[pos++] = (byte) (aLong >> 32);
                    aDestBytes[pos++] = (byte) (aLong >> 40);
                    aDestBytes[pos++] = (byte) (aLong >> 48);
                    aDestBytes[pos++] = (byte) (aLong >> 56);
                } else if (object instanceof String) {
                    final String aString = (String) aSrcMsg.getAsString(key);
                    if (aString != null) {
                        final int length = aString.length();
                        pos = writeVariableLength(aDestBytes, pos - 1, length, FORCE_ENCODING_ZERO_ON_2BITS);
                        for (int k = 0; k < length; k++) {
                            aDestBytes[pos++] = (byte) aString.charAt(k);
                        }
                    }
                } else if (object instanceof IMsg) {
                    final IMsg aMsg = (IMsg) aSrcMsg.getAsMsg(key);
                    if (aMsg != null) {
                        final int length = ((IBytesSerializable) aMsg).getLength();
                        pos = writeVariableLength(aDestBytes, pos - 1, length, FORCE_ENCODING_ZERO_ON_2BITS);
                        pos = ((IBytesSerializable) aMsg).serializeToBytes(aDestBytes, pos);
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
    private final int writeVariableLength(final byte[] bytes, int pos, final int length, boolean forceEncodingZeroOn2Bits) {
        if (length < LENGTH_ENCODED_IN_A_BIT && !(forceEncodingZeroOn2Bits && length == 0)) {
            bytes[pos++] |= (byte) length;
        } else {
            final boolean isEncodedInAnInt = (length > Byte.MAX_VALUE);
            bytes[pos++] |= (byte) ((isEncodedInAnInt) ? LENGTH_ENCODED_IN_AN_INT : LENGTH_ENCODED_IN_A_BIT);
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
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsgSerializer#getLength(com.github.hermod.ser.IMsg)
     */
    @Override
    public int getLength(final IMsg aMsg) {
        if (aMsg instanceof IBytesSerializable) {
            return ((IBytesSerializable) aMsg).getLength();
        } else {
            int length = 0;
            // Size of the different values
            final int[] keys = aMsg.getKeys();
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
    private final int getVariableLength(final int length, final boolean forceEncodingZeroOn2Bits) {
        return (length < LENGTH_ENCODED_IN_A_BIT && !(forceEncodingZeroOn2Bits && length == 0)) ? 1 : (length <= Byte.MAX_VALUE) ? 2 : 5;
    }

    /**
     * getValueSize.
     * 
     * @param object
     * @return
     */
    private final int getValueLength(final Object object) {
        if (object instanceof Byte) {
            return 1;
        } else if (object instanceof Short) {
            return 2;
        } else if (object instanceof Integer || object instanceof Float) {
            return 4;
        } else if (object instanceof Long || object instanceof Double) {
            return 8;
        } else if (object instanceof String) {
            final int length = (object != null) ? ((String) object).length() : 0;
            return getVariableLength(length, FORCE_ENCODING_ZERO_ON_2BITS) + length;
        } else if (object instanceof IMsg) {
            // TODO remove getSize
            final int length = (object != null) ? getLength((IMsg) object) : 0;
            return getVariableLength(length, FORCE_ENCODING_ZERO_ON_2BITS) + length;
        } else {
            return 0;
        }

    }

}
