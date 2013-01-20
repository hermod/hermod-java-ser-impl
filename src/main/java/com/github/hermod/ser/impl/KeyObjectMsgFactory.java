package com.github.hermod.ser.impl;

import com.github.hermod.ser.Msg;
import com.github.hermod.ser.MsgFactory;

/**
 * KeyObjectMsgFactory.
 * 
 * @author anavarro - Jan 20, 2013
 * 
 */
public class KeyObjectMsgFactory implements MsgFactory {

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.MsgFactory#create()
     */
    @Override
    public Msg create() {
	return KeyObjectMsg.create();
    }

}
