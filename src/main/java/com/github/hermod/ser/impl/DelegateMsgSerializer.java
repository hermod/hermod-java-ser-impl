package com.github.hermod.ser.impl;

import com.github.hermod.ser.IBytesMsgSerializer;
import com.github.hermod.ser.IBytesSerializable;
import com.github.hermod.ser.IMsg;

/**
 * <p>DelegateMsgSerializer. </p>
 *
 * @author anavarro - Apr 21, 2013
 *
 */
public final class DelegateMsgSerializer implements IBytesMsgSerializer {

    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.IMsgSerializer#getLength(com.github.hermod.ser.IMsg)
     */
    @Override
    public final int getLength(final IMsg aSrcMsg) {
        if (aSrcMsg instanceof IBytesSerializable) {
            return ((IBytesSerializable) aSrcMsg).getLength();
        }
        throw new IllegalArgumentException("This serializer can getLength only if msg instanceof IBytesSerializable.");
    }

    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.IBytesMsgSerializer#serializeToBytes(com.github.hermod.ser.IMsg)
     */
    @Override
    public byte[] serializeToBytes(final IMsg aSrcMsg) {
        if (aSrcMsg instanceof IBytesSerializable) {
            return ((IBytesSerializable) aSrcMsg).serializeToBytes();
        }
        throw new IllegalArgumentException("This serializer can serializeToBytes only if msg instanceof IBytesSerializable.");
    }

    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.IBytesMsgSerializer#serializeToBytes(com.github.hermod.ser.IMsg, byte[], int)
     */
    @Override
    public int serializeToBytes(final IMsg aSrcMsg, byte[] aDestBytes, int aDestOffset) {
        if (aSrcMsg instanceof IBytesSerializable) {
            return ((IBytesSerializable) aSrcMsg).serializeToBytes(aDestBytes, aDestOffset);
        }
        throw new IllegalArgumentException("This serializer can serializeToBytes only if msg instanceof IBytesSerializable.");
    }

    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.IBytesMsgSerializer#deserializeFrom(byte[], int, int, com.github.hermod.ser.IMsg)
     */
    @Override
    public void deserializeFrom(final byte[] aSrcBytes, final int aSrcOffset, final int aSrcLength, IMsg aDestMsg) {
        if (aDestMsg instanceof IBytesSerializable) {
           ((IBytesSerializable) aDestMsg).deserializeFrom(aSrcBytes, aSrcOffset, aSrcLength);
        }
        throw new IllegalArgumentException("This serializer can deserializeFrom only if msg instanceof IBytesSerializable.");
    }

}
