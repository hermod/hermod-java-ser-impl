package com.github.hermod.ser.impl;

import static com.github.hermod.ser.impl.MsgConstants.DEFAULT_MAX_KEY;

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
	return new KeyObjectMsg(DEFAULT_MAX_KEY);
    }
    
    /**
     * create.
     *
     * @param size
     * @return
     */
    @Override
    public Msg create(final int size) {
        return new KeyObjectMsg(size >> 2);
    }

}
