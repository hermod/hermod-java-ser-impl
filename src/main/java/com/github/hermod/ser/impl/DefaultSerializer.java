package com.github.hermod.ser.impl;

import static com.github.hermod.ser.impl.MsgConstants.FORCE_ENCODING_ZERO_ON_2BITS;
import static com.github.hermod.ser.impl.MsgConstants.SIZE_ENCODED_IN_AN_INT;
import static com.github.hermod.ser.impl.MsgConstants.SIZE_ENCODED_IN_A_BIT;
import static com.github.hermod.ser.impl.MsgConstants.SIZE_MASK;
import static com.github.hermod.ser.impl.MsgConstants.TYPE_5BITS_DECIMAL;
import static com.github.hermod.ser.impl.MsgConstants.TYPE_ARRAY;
import static com.github.hermod.ser.impl.MsgConstants.TYPE_BYTE;
import static com.github.hermod.ser.impl.MsgConstants.TYPE_DOUBLE;
import static com.github.hermod.ser.impl.MsgConstants.TYPE_FLOAT;
import static com.github.hermod.ser.impl.MsgConstants.TYPE_INT;
import static com.github.hermod.ser.impl.MsgConstants.TYPE_LONG;
import static com.github.hermod.ser.impl.MsgConstants.TYPE_MASK;
import static com.github.hermod.ser.impl.MsgConstants.TYPE_MSG;
import static com.github.hermod.ser.impl.MsgConstants.TYPE_NULL_KEY;
import static com.github.hermod.ser.impl.MsgConstants.TYPE_SHORT;
import static com.github.hermod.ser.impl.MsgConstants.TYPE_STRING_ISO_8859_1;

import com.github.hermod.ser.IMsg;
import com.github.hermod.ser.ISerializable;
import com.github.hermod.ser.ISerializer;

/**
 * <p>DefaultSerializer. </p>
 * 
 * @author anavarro - Mar 11, 2013
 * 
 */
public final class DefaultSerializer implements ISerializer {

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.ISerializer#readFrom(byte[], com.github.hermod.ser.IMsg)
     */
    @Override
    public void readFrom(final byte[] aSrcBytes, IMsg aDestMsg) {
        readFrom(aSrcBytes, 0, aSrcBytes.length, aDestMsg);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.ISerializer#readFrom(byte[], int, int, com.github.hermod.ser.IMsg)
     */
    @Override
    public void readFrom(final byte[] bytes, final int offset, final int length, IMsg aDestMsg) {
        if (aDestMsg instanceof ISerializable) {
            ((ISerializable) aDestMsg).readFrom(bytes, offset, length);
        } else {

            int pos = offset;
            int key = 0;

            // find max key
            // not need for feed
            // Calculate max key value
            // TODO to optimize with a try catch, lastPos and relaunch readFrom
            while (pos < offset + length) {
                if ((bytes[pos] & TYPE_MASK) == TYPE_NULL_KEY) {
                    final int sizeMask = bytes[pos++] & SIZE_MASK;
                    key += (((sizeMask < SIZE_ENCODED_IN_A_BIT) ? sizeMask : (sizeMask == SIZE_ENCODED_IN_A_BIT) ? bytes[pos++]
                            : (bytes[pos++] & 0xFF) | ((bytes[pos++] & 0xFF) << 8) | ((bytes[pos++] & 0xFF) << 16) | ((bytes[pos++] & 0xFF) << 24))) + 1;
                } else {
                    final int sizeMask = bytes[pos++] & SIZE_MASK;
                    // TODO to optimize
                    pos += (((sizeMask < SIZE_ENCODED_IN_A_BIT) ? sizeMask : (sizeMask == SIZE_ENCODED_IN_A_BIT) ? bytes[pos] : ((bytes[pos] & 0xFF)
                            | ((bytes[pos + 1] & 0xFF) << 8) | ((bytes[pos + 2] & 0xFF) << 16) | ((bytes[pos + 3] & 0xFF) << 24))));
                    pos += (sizeMask < SIZE_ENCODED_IN_A_BIT) ? 0 : (sizeMask == SIZE_ENCODED_IN_A_BIT) ? 1 : 4;

                    key++;
                }
            }
            aDestMsg.clear();

            // Decoding
            key = 0;
            pos = offset;
            while (pos < offset + length) {
                final byte type = bytes[pos++];

                // Skip null key
                if ((type & TYPE_MASK) == TYPE_NULL_KEY) {
                    final int sizeMask = type & SIZE_MASK;
                    key += ((sizeMask < SIZE_ENCODED_IN_A_BIT) ? sizeMask : (sizeMask == SIZE_ENCODED_IN_A_BIT) ? bytes[pos++]
                            : (bytes[pos++] & 0xFF) | ((bytes[pos++] & 0xFF) << 8) | ((bytes[pos++] & 0xFF) << 16) | ((bytes[pos++] & 0xFF) << 24)) + 1;
                }
                // Decode values
                else {
                    switch (type) {
                    //  All fixed type
                    case TYPE_BYTE:
                        aDestMsg.set(key, (byte) ((bytes[pos++] & 0xFF)));
                        break;

                    case TYPE_SHORT:
                        aDestMsg.set(key, (((short) bytes[pos++] & 0xFF)) | (((short) bytes[pos++] & 0xFF) << 8));
                        break;

                    case TYPE_INT:
                        aDestMsg.set(key, (((int) bytes[pos++] & 0xFF)) | (((int) bytes[pos++] & 0xFF) << 8) | (((int) bytes[pos++] & 0xFF) << 16)
                                | (((int) bytes[pos++] & 0xFF) << 24));
                        break;

                    case TYPE_DOUBLE:
                        aDestMsg.set(
                                key,
                                Double.longBitsToDouble((((long) bytes[pos++] & 0xFF) | (((long) bytes[pos++] & 0xFF) << 8)
                                        | (((long) bytes[pos++] & 0xFF) << 16) | (((long) bytes[pos++] & 0xFF) << 24)
                                        | (((long) bytes[pos++] & 0xFF) << 32) | (((long) bytes[pos++] & 0xFF) << 40)
                                        | (((long) bytes[pos++] & 0xFF) << 48) | (((long) bytes[pos++] & 0xFF) << 56))));
                        break;

                    case TYPE_5BITS_DECIMAL:
                        aDestMsg.set(
                                key,
                                Double.longBitsToDouble((((long) bytes[pos++] & 0xFF) << 8) | (((long) bytes[pos++] & 0xFF) << 16)
                                        | (((long) bytes[pos++] & 0xFF) << 24) | (((long) bytes[pos++] & 0xFF) << 32)), ((byte) bytes[pos++] & 0xFF));
                        break;

                    case TYPE_FLOAT:
                        aDestMsg.set(
                                key,
                                Float.intBitsToFloat((((int) bytes[pos++] & 0xFF)) | (((int) bytes[pos++] & 0xFF) << 8)
                                        | (((int) bytes[pos++] & 0xFF) << 16) | (((int) bytes[pos++] & 0xFF) << 24)));
                        break;

                    case TYPE_LONG:
                        aDestMsg.set(key, (((long) bytes[pos++] & 0xFF)) | (((long) bytes[pos++] & 0xFF) << 8) | (((long) bytes[pos++] & 0xFF) << 16)
                                | (((long) bytes[pos++] & 0xFF) << 24) | (((long) bytes[pos++] & 0xFF) << 32) | (((long) bytes[pos++] & 0xFF) << 40)
                                | (((long) bytes[pos++] & 0xFF) << 48) | (((long) bytes[pos++] & 0xFF) << 56));
                        break;

                    // All non fixed type
                    default:
                        final byte typeMask = (byte) (type & TYPE_MASK);
                        final int sizeMask = (SIZE_MASK & type);
                        final int size = (sizeMask < SIZE_ENCODED_IN_A_BIT) ? sizeMask : (sizeMask == SIZE_ENCODED_IN_A_BIT) ? bytes[pos++]
                                : (bytes[pos++] & 0xFF) | ((bytes[pos++] & 0xFF) << 8) | ((bytes[pos++] & 0xFF) << 16)
                                        | ((bytes[pos++] & 0xFF) << 24);

                        switch (typeMask) {
                        case TYPE_STRING_ISO_8859_1:
                            // TODO manage null value
                            if (sizeMask != 0) {
                                final char[] chars = new char[size];
                                for (int i = 0; i < size; i++) {
                                    chars[i] = (char) bytes[pos++];
                                }
                                aDestMsg.set(key, new String(chars));
                            }
                            break;

                        case TYPE_MSG:
                            // TODO manage null value
                            if (sizeMask != 0) {
                                final IMsg msg = new KeyObjectMsg();
                                ((ISerializable) msg).readFrom(bytes, pos, size);
                                pos += size;
                                aDestMsg.set(key, msg);
                            }
                            break;

                        case TYPE_ARRAY:
                            final byte arrayType = bytes[pos++];
                            if (arrayType == TYPE_NULL_KEY) {
                                // For variable type

                            } else {
                                // For Fixed typed
                                switch (arrayType) {
                                case TYPE_BYTE:
                                    final byte[] byteArray = new byte[size - 1];
                                    // TODO to optimize
                                    for (int i = 0; i < size - 1; i++) {
                                        byteArray[i] = bytes[pos++];
                                    }
                                    break;
                                case TYPE_SHORT:
                                    // TODO
                                    break;
                                case TYPE_INT:
                                    // TODO
                                    break;
                                case TYPE_LONG:
                                    // TODO
                                    break;
                                case TYPE_FLOAT:
                                    // TODO
                                    break;
                                case TYPE_DOUBLE:
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
     * @see com.github.hermod.ser.ISerializer#writeTo(com.github.hermod.ser.IMsg)
     */
    @Override
    public byte[] writeTo(final IMsg aSrcMsg) {
        final byte[] bytes = new byte[aSrcMsg.getSize()];
        writeTo(aSrcMsg, bytes, 0);
        return bytes;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.ISerializer#writeTo(com.github.hermod.ser.IMsg, byte[], int)
     */
    @Override
    public int writeTo(final IMsg aSrcMsg, byte[] bytes, int offset) {

        // Calculate size
        // TODO to optimize with a try catch
        final int size = getSize(aSrcMsg);
        if (bytes.length - offset < size) {
            throw new IllegalArgumentException("Bytes array too small from the offset");
        }
        int pos = offset;
        
        // Write Type / Values
        final int[] keys = aSrcMsg.getKeys();
        int previousKey = 0;
        for (int j = 0; j < keys.length; j++) {
                final int key = keys[j];
                final int delta = key - previousKey;
                if (delta > 1) {
                    pos = writeVariableSize(bytes, pos, delta - 1, false);
                }
                previousKey = key;

                final Object object = aSrcMsg.get(key); 
                //  all fixed type
                if (object instanceof Byte) {
                    bytes[pos++] = TYPE_BYTE;
                    bytes[pos++] = (byte) aSrcMsg.getAsByte(key);
                } else if (object instanceof Short) {
                    final int aShort = (short) aSrcMsg.getAsShort(key);
                    bytes[pos++] = TYPE_SHORT;
                    bytes[pos++] = (byte) (aShort);
                    bytes[pos++] = (byte) (aShort >> 8);
                } else if (object instanceof Byte) {
                    final int aInt = (int) aSrcMsg.getAsInt(key);
                    bytes[pos++] = TYPE_INT;
                    bytes[pos++] = (byte) (aInt);
                    bytes[pos++] = (byte) (aInt >> 8);
                    bytes[pos++] = (byte) (aInt >> 16);
                    bytes[pos++] = (byte) (aInt >> 24);
                } else if (object instanceof Double) {
                    final long longBits = Double.doubleToLongBits(aSrcMsg.getAsDouble(key));
                    bytes[pos++] = TYPE_DOUBLE;
                    bytes[pos++] = (byte) (longBits);
                    bytes[pos++] = (byte) (longBits >> 8);
                    bytes[pos++] = (byte) (longBits >> 16);
                    bytes[pos++] = (byte) (longBits >> 24);
                    bytes[pos++] = (byte) (longBits >> 32);
                    bytes[pos++] = (byte) (longBits >> 40);
                    bytes[pos++] = (byte) (longBits >> 48);
                    bytes[pos++] = (byte) (longBits >> 56);
                } else if (object instanceof Float) {
                    final int intBits = Float.floatToIntBits(aSrcMsg.getAsFloat(key));
                    bytes[pos++] = TYPE_FLOAT;
                    bytes[pos++] = (byte) (intBits);
                    bytes[pos++] = (byte) (intBits >> 8);
                    bytes[pos++] = (byte) (intBits >> 16);
                    bytes[pos++] = (byte) (intBits >> 24);
                } else if (object instanceof Long) {
                    final long aLong = aSrcMsg.getAsLong(key);
                    bytes[pos++] = TYPE_LONG;
                    bytes[pos++] = (byte) (aLong);
                    bytes[pos++] = (byte) (aLong >> 8);
                    bytes[pos++] = (byte) (aLong >> 16);
                    bytes[pos++] = (byte) (aLong >> 24);
                    bytes[pos++] = (byte) (aLong >> 32);
                    bytes[pos++] = (byte) (aLong >> 40);
                    bytes[pos++] = (byte) (aLong >> 48);
                    bytes[pos++] = (byte) (aLong >> 56);
                } else if (object instanceof String) {
                    final String aString = (String) aSrcMsg.getAsString(key);
                    if (aString != null) {
                        final int length = aString.length();
                        pos = writeVariableSize(bytes, pos - 1, length, FORCE_ENCODING_ZERO_ON_2BITS);
                        for (int k = 0; k < length; k++) {
                            bytes[pos++] = (byte) aString.charAt(k);
                        }
                    }
                } else if (object instanceof IMsg) {
                    final IMsg aMsg = (IMsg) aSrcMsg.getAsMsg(key);
                    if (aMsg != null) {
                        final int length = ((ISerializable) aMsg).getSize();
                        pos = writeVariableSize(bytes, pos - 1, length, FORCE_ENCODING_ZERO_ON_2BITS);
                        pos = ((ISerializable) aMsg).writeTo(bytes, pos);
                    }
                    break;

                } else if (object instanceof Object[]) {
                    // TODO
                }
        }
        return pos;
    }

    /**
     * writeVariableSize.
     *
     * @param bytes
     * @param pos
     * @param size
     * @param forceEncodingZeroOn2Bits
     * @return
     */
    private final int writeVariableSize(final byte[] bytes, int pos, final int size, boolean forceEncodingZeroOn2Bits) {
        if (size < SIZE_ENCODED_IN_A_BIT && !(forceEncodingZeroOn2Bits && size == 0)) {
            bytes[pos++] |= (byte) size;
        } else {
            final boolean isEncodedInAnInt = (size > Byte.MAX_VALUE);
            bytes[pos++] |= (byte) ((isEncodedInAnInt) ? SIZE_ENCODED_IN_AN_INT : SIZE_ENCODED_IN_A_BIT);
            bytes[pos++] = (byte) (size);
            if (isEncodedInAnInt) {
                bytes[pos++] = (byte) (size >> 8);
                bytes[pos++] = (byte) (size >> 16);
                bytes[pos++] = (byte) (size >> 24);
            }
        }
        return pos;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.ISerializer#getSize(com.github.hermod.ser.IMsg)
     */
    @Override
    public int getSize(final IMsg aMsg) {
        if (aMsg instanceof ISerializable) {
            return ((ISerializable) aMsg).getSize();
        } else {
            int size = 0;
            // Size of the different values
            final int[] keys = aMsg.getKeys();
            for (int i = 0; i < keys.length; i++) {
                size += getValueSize(aMsg.get(i));
            }
            // Add size of skip keys
            for (int i = 1; i < keys.length; i++) {
                final int delta = keys[i] - keys[i - 1];
                if (delta != 1) {
                    size += getVariableSize(delta, false);
                }
            }
            return size;
        }
    }

    /**
     * getVariableSize.
     * 
     * @param size
     * @param forceEncodingZeroOn2Bits
     * @return
     */
    private final int getVariableSize(final int size, final boolean forceEncodingZeroOn2Bits) {
        return (size < SIZE_ENCODED_IN_A_BIT && !(forceEncodingZeroOn2Bits && size == 0)) ? 1 : (size <= Byte.MAX_VALUE) ? 2 : 5;
    }

    /**
     * getValueSize.
     * 
     * @param object
     * @return
     */
    private final int getValueSize(final Object object) {
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
            return getVariableSize(length, FORCE_ENCODING_ZERO_ON_2BITS) + length;
        } else if (object instanceof IMsg) {
            // TODO remove getSize
            final int length = (object != null) ? ((IMsg) object).getSize() : 0;
            return getVariableSize(length, FORCE_ENCODING_ZERO_ON_2BITS) + length;
        } else {
            return 0;
        }

    }

}
