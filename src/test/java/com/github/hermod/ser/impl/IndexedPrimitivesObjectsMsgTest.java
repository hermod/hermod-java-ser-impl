package com.github.hermod.ser.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.github.hermod.ser.Msg;



/**
 * KeyObjectMsgTest.
 * 
 * @author anavarro - Jan 20, 2013
 * 
 */
public final class IndexedPrimitivesObjectsMsgTest
    extends AbstractMsgTest
{

    /**
     * Constructor.
     *
     */
    public IndexedPrimitivesObjectsMsgTest() {
        super();
        this.bytesMsgSerializer = DefaultMsgSerializer.create();
        this.byteBufferSerializer = DefaultMsgSerializer.create();
    }
    
    
    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.impl.AbstractMsgTest#newMsg()
     */
    @Override
    public Msg create() {
        return IndexedPrimitivesObjectsMsg.create();
    }
    
    
    
    //TODO use a virtual invoction instead
//    final MethodHandle methodHandle2 = MethodHandles.lookup().findStatic(Main.class, "create", MethodType.methodType(String.class));
//    String s2 = (String) methodHandle2.invoke();
//    String s3 = String.class.cast(methodHandle2.invoke());
    /**
     * testCreateWithKeyMax.
     *
     */
    @Test
    public void testCreateWithKeyMax() {
        int keyMax = 10;
        final Msg msg = IndexedPrimitivesObjectsMsg.createWithKeyMax(keyMax);
        msg.set(keyMax, true);
        assertThat(msg.getKeyMax()).isEqualTo(keyMax);
    }
    
    /**
     * createFromValues.
     *
     */
    @Test
    public void testCreateFromValues() {
        final Msg msg = IndexedPrimitivesObjectsMsg.createFromValues(0, 1, 2, 3);
        for (final int key : msg.getKeysArray()) {
            assertThat(msg.getAsInt(key)).isEqualTo(key);
        }
    }
    
    @Test
    public void testCreateFromMsg() {
        final Msg msg1 = IndexedPrimitivesObjectsMsg.createFromValues(0, 1, 2, 3);
        final Msg msg2 = IndexedPrimitivesObjectsMsg.createFromMsg(msg1);
        assertThat(msg1).isEqualTo(msg2);
    }


    

}
