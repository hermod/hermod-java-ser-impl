package com.github.hermod.ser.impl;

import static com.github.hermod.ser.impl.MsgConstants.DEFAULT_MAX_KEY;

import com.github.hermod.ser.IMsg;
import com.github.hermod.ser.IMsgFactory;

/**
 * KeyObjectMsgFactory.
 * 
 * @author anavarro - Jan 20, 2013
 * 
 */
public final class KeyObjectMsgFactory implements IMsgFactory {

    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.IMsgFactory#create()
     */
    @Override
    public IMsg create() {
	return new KeyObjectMsg(DEFAULT_MAX_KEY);
    }
    
    /**
     * create.
     *
     * @param size in bytes (if it does make sense for the implementation notably when keyMax, we consider it is the keyMax = size / 4)
     * @return
     */
    @Override
    public IMsg create(final int size) {
        return new KeyObjectMsg(size >> 2);
    }

}
