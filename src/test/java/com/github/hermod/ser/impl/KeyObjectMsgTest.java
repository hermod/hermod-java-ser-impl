package com.github.hermod.ser.impl;

import com.github.hermod.ser.Msg;



/**
 * KeyObjectMsgTest.
 * 
 * @author anavarro - Jan 20, 2013
 * 
 */
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
     * @see com.github.hermod.ser.impl.AbstractMsgTest#newMsg()
     */
    @Override
    public Msg newMsg() {
        return KeyObjectMsg.create();
    }


    

}
