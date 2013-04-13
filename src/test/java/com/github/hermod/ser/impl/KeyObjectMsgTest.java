package com.github.hermod.ser.impl;

import org.junit.Ignore;

import com.github.hermod.ser.IByteableMsg;



/**
 * KeyObjectMsgTest.
 * 
 * @author anavarro - Jan 20, 2013
 * 
 */
@Ignore
public final class KeyObjectMsgTest
    extends AbstractMsgTest
{

    /**
     * Constructor.
     *
     */
    public KeyObjectMsgTest() {
        super();
    }
    
    
    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.impl.AbstractMsgTest#newByteableMsg()
     */
    @Override
    public IByteableMsg newByteableMsg() {
        return new KeyObjectMsg();
    }


    

}
