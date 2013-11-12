package com.github.hermod.ser.impl;

import com.github.hermod.ser.ByteBufferMsgSerializer;
import com.github.hermod.ser.BytesMsgSerializer;
import com.github.hermod.ser.Msg;
import com.github.hermod.ser.Null;

/**
 * KeyObjectMsgTest.
 * 
 * @author anavarro - Jan 20, 2013
 * 
 */
public final class IndexedObjectsMsgTest // extends AbstractMsgTest 
{

    /**
     * Constructor.
     * 
     */
    public IndexedObjectsMsgTest() {
        super();
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.impl.AbstractMsgTest#newMsg()
     */
    //@Override
    public Msg createMsg() {
        return IndexedObjectsMsg.create();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.impl.AbstractMsgTest#createBytesMsgSerializer()
     */
    //@Override
    public BytesMsgSerializer createBytesMsgSerializer() {
        return new DefaultMsgSerializer();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.impl.AbstractMsgTest#createByteBufferMsgSerializer()
     */
    //@Override
    public ByteBufferMsgSerializer createByteBufferMsgSerializer() {
        return new DefaultMsgSerializer();
    }
    
    /**
     * main.
     *
     * @param args
     */
    public static void main(String[] args) {
        final Msg srcMsg = IndexedObjectsMsg.create();
        
        srcMsg.set(1,  Boolean.TRUE);
        
        System.out.println(" srcMsg=" + srcMsg.toString());
        final byte[] bytes = DefaultMsgSerializer.get().serializeToBytes(srcMsg);
        
        final Msg destMsg = IndexedObjectsMsg.create();
        DefaultMsgSerializer.get().deserializeFromBytes(bytes, 0, bytes.length, destMsg);
        System.out.println("destMsg=" + destMsg);
        
        
    }

}
