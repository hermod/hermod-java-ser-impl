package com.github.hermod.ser.impl;

import static com.github.hermod.ser.impl.Msgs.DEFAULT_MAX_KEY;

import com.github.hermod.ser.Msg;

/**
 * KeyObjectMsgs.
 * 
 * @author anavarro - Jan 20, 2013
 * 
 */
public final class KeyObjectMsgs {

    /**
     * Constructor.
     *
     */
    private KeyObjectMsgs() {
        super();
    }


    /**
     * newMsg.
     *
     * @return
     */
    public static Msg newMsg() {
	return new KeyObjectMsg(DEFAULT_MAX_KEY);
    }
    

    /**
     * newMsg.
     *
     * @param keyMax
     * @return
     */
    public static Msg newMsg(final int keyMax) {
        return new KeyObjectMsg(keyMax);
    }

}
