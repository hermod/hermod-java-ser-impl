package com.github.hermod.ser.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import com.github.hermod.ser.ByteBufferMsgSerializer;
import com.github.hermod.ser.BytesMsgSerializer;
import com.github.hermod.ser.Msg;
import com.github.hermod.ser.Null;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * KeyObjectMsgTest.
 * 
 * @author anavarro - Jan 20, 2013
 * 
 */
public final class IndexedObjectsMsgTest extends AbstractMsgTest 
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
    @Override
    public Msg createMsg() {
        return IndexedObjectsMsg.create();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.impl.AbstractMsgTest#createBytesMsgSerializer()
     */
    @Override
    public BytesMsgSerializer createBytesMsgSerializer() {
        return new DefaultMsgSerializer();
    }

    /**
     * (non-Javadoc)
     *
     * @see com.github.hermod.ser.impl.AbstractMsgTest#createByteBufferMsgSerializer()
     */
    @Override
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
        final Msg destMsg = IndexedObjectsMsg.create();
        
        //srcMsg.set(1,  Boolean.TRUE);
        srcMsg.set(0, (Boolean) null);
        
        //pb getObjects (null);
//        int size = 2;
//        srcMsg.set(KEY_ONE, new Byte[size]);        
//        assertThat(srcMsg.isArray(KEY_ONE)).isEqualTo(true);
//        assertThat(srcMsg.getArrayLength(KEY_ONE)).isEqualTo(size);
        
        
        final byte[] bytes = DefaultMsgSerializer.get().serializeToBytes(srcMsg);
        DefaultMsgSerializer.get().deserializeFromBytes(bytes, 0, bytes.length, destMsg);
        
        System.out.println(" srcMsg=" + srcMsg.toString());
        System.out.println("destMsg=" + destMsg);
        
        
        /*

        srcMsg.set(KEY_ONE, BYTE_TEST);
        srcMsg.set(KEY_TWO, DOUBLE_TEST);
        srcMsg.set(KEY_THREE, (Integer) null);
        srcMsg.set(KEY_FOUR, STRING_TEST);
        //srcMsg.set(KEY_FIVE, msgTest);
        srcMsg.set(KEY_SIX, LONGS_TEST2);
        srcMsg.set(KEY_SEVEN, DOUBLES_TEST);
        srcMsg.set(KEY_EIGHT, (String) null);
        srcMsg.set(KEY_NINE, (Msg) null);
        srcMsg.set(KEY_TEN, (Integer[]) null);
        srcMsg.set(KEY_ELEVEN, (float[]) null);
        srcMsg.set(KEY_TWELVE, STRING_TEST_UTF16);
        //srcMsg.set(KEY_THIRTEEN, LONGS_TEST_EMPTY);
        srcMsg.set(KEY_NINETY, Null.valueOf(1));

        System.out.println(" srcMsg=" + srcMsg.toString());
        final byte[] bytes = DefaultMsgSerializer.get().serializeToBytes(srcMsg);
        
        DefaultMsgSerializer.get().deserializeFromBytes(bytes, 0, bytes.length, destMsg);
        System.out.println("destMsg=" + destMsg);
        

        final Map<Integer, Object> map = new HashMap<Integer, Object>();
        map.put(KEY_ONE, BYTE_TEST);
        map.put(KEY_TWO, DOUBLE_TEST);
        map.put(KEY_THREE, null);
        map.put(KEY_FOUR, STRING_TEST);
        map.put(KEY_SIX, LONGS_TEST2);
        map.put(KEY_SEVEN, DOUBLES_TEST);
        map.put(KEY_EIGHT, null);
        map.put(KEY_NINE, null);
        map.put(KEY_TEN, null);
        map.put(KEY_ELEVEN, null);
        map.put(KEY_TWELVE, STRING_TEST_UTF16);
        //map.put(KEY_THIRTEEN, LONGS_TEST_EMPTY);
        map.put(KEY_NINETY, null);

        final Gson gson = new GsonBuilder().serializeNulls().create();
        final String jsonMap = gson.toJson(map);
        assertThat(destMsg.toString()).isEqualTo(jsonMap);

        final Msg srcMsg2 = IndexedObjectsMsg.create();
        final String jsonMap2 = gson.toJson(new HashMap<String, Object>());
        assertThat(srcMsg2.toString()).isEqualTo(jsonMap2);
        
        */

    
    }

}
