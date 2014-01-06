package com.github.hermod.ser.impl;

import java.nio.ByteBuffer;

import com.github.hermod.ser.ByteBufferMsgSerializer;
import com.github.hermod.ser.ByteBufferSerializable;
import com.github.hermod.ser.BytesMsgSerializer;
import com.github.hermod.ser.BytesSerializable;
import com.github.hermod.ser.Msg;
import com.github.hermod.ser.Serializable;

/**
 * <p>DelegateMsgSerializer.</p>
 * 
 * @author anavarro - Jan 6, 2014
 * 
 */
public class DelegateMsgSerializer implements BytesMsgSerializer, ByteBufferMsgSerializer {

    private static final DelegateMsgSerializer DELEGATE_MSG_SERIALIZER = new DelegateMsgSerializer();

    /**
     * create.
     * 
     * @return
     */
    public static DelegateMsgSerializer create() {
        return new DelegateMsgSerializer();
    }

    /**
     * get.
     * 
     * @return
     */
    public static DelegateMsgSerializer get() {
        return DELEGATE_MSG_SERIALIZER;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.github.hermod.ser.MsgSerializer#getLength(com.github.hermod.ser.Msg)
     */
    @Override
    public int getLength(final Msg aMsg) {
        if (!aMsg.isSerializable()) {
            throw new UnsupportedOperationException("Impossible to call this method because aMsg does not implements Serializable.");
        }
        return ((Serializable) aMsg).getLength();
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.github.hermod.ser.ByteBufferMsgSerializer#deserializeFromByteBuffer(java.nio.ByteBuffer, com.github.hermod.ser.Msg)
     */
    @Override
    public void deserializeFromByteBuffer(final ByteBuffer aSrcByteBuffer, final Msg aDestMsg) {
        if (!aDestMsg.isByteBufferSerializable()) {
            throw new UnsupportedOperationException("Impossible to call this method because aMsg does not implements ByteBufferSerializable.");
        }
        ((ByteBufferSerializable) aDestMsg).deserializeFromByteBuffer(aSrcByteBuffer);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.github.hermod.ser.ByteBufferMsgSerializer#serializeToByteBuffer(com.github.hermod.ser.Msg)
     */
    @Override
    public ByteBuffer serializeToByteBuffer(final Msg aSrcMsg) {
        if (!aSrcMsg.isByteBufferSerializable()) {
            throw new UnsupportedOperationException("Impossible to call this method because aMsg does not implements ByteBufferSerializable.");
        }
        return ((ByteBufferSerializable) aSrcMsg).serializeToByteBuffer();
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.github.hermod.ser.ByteBufferMsgSerializer#serializeToByteBuffer(com.github.hermod.ser.Msg, java.nio.ByteBuffer)
     */
    @Override
    public void serializeToByteBuffer(final Msg aSrcMsg, final ByteBuffer aDestByteBuffer) {
        if (!aSrcMsg.isByteBufferSerializable()) {
            throw new UnsupportedOperationException("Impossible to call this method because aMsg does not implements ByteBufferSerializable.");
        }
        ((ByteBufferSerializable) aSrcMsg).serializeToByteBuffer(aDestByteBuffer);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.github.hermod.ser.BytesMsgSerializer#deserializeFromBytes(byte[], int, int, com.github.hermod.ser.Msg)
     */
    @Override
    public void deserializeFromBytes(final byte[] aSrcBytes, final int aSrcOffset, final int aSrcLength, Msg aDestMsg) {
        if (!aDestMsg.isBytesSerializable()) {
            throw new UnsupportedOperationException("Impossible to call this method because aMsg does not implements BytesSerializable.");
        }
        ((BytesSerializable) aDestMsg).deserializeFromBytes(aSrcBytes, aSrcOffset, aSrcLength);

    }

    /**
     * {@inheritDoc}
     * 
     * @see com.github.hermod.ser.BytesMsgSerializer#serializeToBytes(com.github.hermod.ser.Msg)
     */
    @Override
    public byte[] serializeToBytes(Msg aSrcMsg) {
        if (!aSrcMsg.isBytesSerializable()) {
            throw new UnsupportedOperationException("Impossible to call this method because aMsg does not implements BytesSerializable.");
        }
        return ((BytesSerializable) aSrcMsg).serializeToBytes();
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.github.hermod.ser.BytesMsgSerializer#serializeToBytes(com.github.hermod.ser.Msg, byte[], int)
     */
    @Override
    public int serializeToBytes(final Msg aSrcMsg, byte[] aDestBytes, int aDestOffset) {
        if (!aSrcMsg.isBytesSerializable()) {
            throw new UnsupportedOperationException("Impossible to call this method because aMsg does not implements BytesSerializable.");
        }
        return ((BytesSerializable) aSrcMsg).serializeToBytes(aDestBytes, aDestOffset);
    }

}
