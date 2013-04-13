package com.github.hermod.ser.impl;

import static com.github.hermod.ser.impl.Msgs.DEFAULT_MAX_KEY;

import com.github.hermod.ser.IByteableMsg;
import com.github.hermod.ser.IMsg;

/**
 * KeyObjectMsgs.
 * 
 * @author anavarro - Jan 20, 2013
 * 
 */
public final class KeyObjectMsgs {


    /**
     * newMsg.
     *
     * @return
     */
    public static IByteableMsg newMsg() {
	return new KeyObjectMsg(DEFAULT_MAX_KEY);
    }
    

    /**
     * newMsg.
     *
     * @param keyMax
     * @return
     */
    public static IByteableMsg newMsg(final int keyMax) {
        return new KeyObjectMsg(keyMax);
    }

}
