package com.github.hermod.ser.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import com.github.hermod.ser.ByteBufferMsgSerializer;
import com.github.hermod.ser.BytesMsgSerializer;
import com.github.hermod.ser.Msg;

/**
 * KeyObjectMsgTest.
 * 
 * @author anavarro - Jan 20, 2013
 * 
 */
public final class IndexedPrimitivesObjectsMsgTest extends AbstractMsgTest {

    /**
     * Constructor.
     * 
     */
    public IndexedPrimitivesObjectsMsgTest() {
        super();
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.impl.AbstractMsgTest#newMsg()
     */
    @Override
    public Msg createMsg() {
        return IndexedPrimitivesObjectsMsg.create();
    }


    

}
