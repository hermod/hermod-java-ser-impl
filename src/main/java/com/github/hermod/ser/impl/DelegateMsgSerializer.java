package com.github.hermod.ser.impl;

import com.github.hermod.ser.BytesMsgSerializer;
import com.github.hermod.ser.BytesSerializable;
import com.github.hermod.ser.Msg;

/**
 * <p>DelegateMsgSerializer. </p>
 *
 * @author anavarro - Apr 21, 2013
 *
 */
public final class DelegateMsgSerializer implements BytesMsgSerializer {

    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.MsgSerializer#getLength(com.github.hermod.ser.Msg)
     */
    @Override
    public final int getLength(final Msg aSrcMsg) {
        if (aSrcMsg instanceof BytesSerializable) {
            return ((BytesSerializable) aSrcMsg).getLength();
        }
        throw new IllegalArgumentException("This serializer can getLength only if msg instanceof IBytesSerializable.");
    }

    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.BytesMsgSerializer#serializeToBytes(com.github.hermod.ser.Msg)
     */
    @Override
    public byte[] serializeToBytes(final Msg aSrcMsg) {
        if (aSrcMsg instanceof BytesSerializable) {
            return ((BytesSerializable) aSrcMsg).serializeToBytes();
        }
        throw new IllegalArgumentException("This serializer can serializeToBytes only if msg instanceof IBytesSerializable.");
    }

    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.BytesMsgSerializer#serializeToBytes(com.github.hermod.ser.Msg, byte[], int)
     */
    @Override
    public int serializeToBytes(final Msg aSrcMsg, byte[] aDestBytes, int aDestOffset) {
        if (aSrcMsg instanceof BytesSerializable) {
            return ((BytesSerializable) aSrcMsg).serializeToBytes(aDestBytes, aDestOffset);
        }
        throw new IllegalArgumentException("This serializer can serializeToBytes only if msg instanceof IBytesSerializable.");
    }

    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.BytesMsgSerializer#deserializeFrom(byte[], int, int, com.github.hermod.ser.Msg)
     */
    @Override
    public void deserializeFrom(final byte[] aSrcBytes, final int aSrcOffset, final int aSrcLength, Msg aDestMsg) {
        if (aDestMsg instanceof BytesSerializable) {
           ((BytesSerializable) aDestMsg).deserializeFrom(aSrcBytes, aSrcOffset, aSrcLength);
        }
        throw new IllegalArgumentException("This serializer can deserializeFrom only if msg instanceof IBytesSerializable.");
    }

}
