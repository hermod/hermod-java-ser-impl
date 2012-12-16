package com.github.hermod.ser.intmap.impl;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import com.github.hermod.ser.intmap.ReadWriteIntMapMsg;

/**
 * KeyObjectValueIntMapTest.
 * 
 * @author anavarro - Dec 7, 2012
 * 
 */
public abstract class AbstractIntMapTest
{
    
    abstract public ReadWriteIntMapMsg create();

    
    private static final byte BYTE_TEST = Byte.MIN_VALUE;
    private static final short SHORT_TEST = Short.MAX_VALUE;
    private static final int INT_TEST = Integer.MAX_VALUE;
    private static final long LONG_TEST = Long.MAX_VALUE / 2;
    private static final float FLOAT_TEST = 10.10f;
    private static final double DOUBLE_TEST = 11.11;
    private static final String STRING_TEST = "string";
    
    private static final int KEY_MINUS_ONE = -1;
    private static final int KEY_ZERO = 0;
    private static final int KEY_ONE = 1;
    private static final int KEY_TWO = 2;
    private static final int KEY_THREE = 3;
    private static final int KEY_FOUR = 4;
    private static final int KEY_EIGHT = 8;
    private static final int KEY_TEN = 10;
    private static final int KEY_ELEVEN = 11;
    private static final int KEY_TWELVE = 12;
    private static final int KEY_THIRTEEN = 13;
    private static final int KEY_NINETYNINE = 99;
    
    private static final double PRECISION = 0.00001; 
    
    
    
    /**
     * testClear.
     * 
     */
    @Test
    public void testClear()
    {
        final ReadWriteIntMapMsg intMapMsg = create();
        intMapMsg.set(KEY_ONE, INT_TEST);
        Assert.assertEquals(intMapMsg.getKeys().length, 1);
        intMapMsg.clear();
        Assert.assertEquals(intMapMsg.getKeys().length, 0);
    }
    
    /**
     * testContains.
     * 
     */
    @Test
    public void testContains()
    {
        final ReadWriteIntMapMsg intMapMsg = create();
        intMapMsg.set(1,1);
        
        Assert.assertTrue(intMapMsg.contains(KEY_ONE));
        Assert.assertFalse(intMapMsg.contains(KEY_MINUS_ONE));
        Assert.assertFalse(intMapMsg.contains(KEY_ZERO));
        Assert.assertFalse(intMapMsg.contains(KEY_NINETYNINE));
    }
    

    /**
     * testGetKeys.
     * 
     */
    @Test
    public void testGetKeys()
    {

        final ReadWriteIntMapMsg readWriteIntMapMsg = create();
        readWriteIntMapMsg.set(KEY_ONE, BYTE_TEST);
        readWriteIntMapMsg.set(KEY_TWO, SHORT_TEST);
        final byte[] bytes = readWriteIntMapMsg.writeTo();
        
        final ReadWriteIntMapMsg readWriteIntMapMsg2 = create();
        readWriteIntMapMsg2.readFrom(bytes, 0, bytes.length);
        final int[] keys = readWriteIntMapMsg2.getKeys();
        
        Assert.assertEquals(2, keys.length);
        Assert.assertEquals(1, keys[0]);
        Assert.assertEquals(2, keys[1]);

    }
    

    /**
     * testGetAsByte.
     * 
     */
    @Test
    public void testSetGetAsByte()
    {
        final ReadWriteIntMapMsg readWriteIntMapMsg = create();
        readWriteIntMapMsg.set(KEY_ONE, BYTE_TEST);
        final byte[] bytes = readWriteIntMapMsg.writeTo();
        
        final ReadWriteIntMapMsg readWriteIntMapMsg2 = create();
        readWriteIntMapMsg2.readFrom(bytes, 0, bytes.length);
        Assert.assertEquals(BYTE_TEST, readWriteIntMapMsg2.getAsByte(KEY_ONE));
        Assert.assertFalse(readWriteIntMapMsg2.contains(KEY_ZERO));
        Assert.assertEquals(IntMapConstants.DEFAULT_BYTE_VALUE, readWriteIntMapMsg2.getAsByte(KEY_NINETYNINE));
        Assert.assertEquals(IntMapConstants.DEFAULT_BYTE_VALUE, readWriteIntMapMsg2.getAsByte(KEY_ZERO));
        Assert.assertEquals(IntMapConstants.DEFAULT_BYTE_VALUE, readWriteIntMapMsg2.getAsByte(KEY_MINUS_ONE));
    }
    
    /**
     * testGetAsShort.
     * 
     */
    @Test
    public void testSetGetAsShort()
    {
        final ReadWriteIntMapMsg readWriteIntMapMsg = create();
        readWriteIntMapMsg.set(KEY_ONE, SHORT_TEST);
        readWriteIntMapMsg.set(KEY_TWO, BYTE_TEST);
        final byte[] bytes = readWriteIntMapMsg.writeTo();
        
        final ReadWriteIntMapMsg readWriteIntMapMsg2 = create();
        readWriteIntMapMsg2.readFrom(bytes, 0, bytes.length);
        Assert.assertEquals(SHORT_TEST, readWriteIntMapMsg2.getAsShort(KEY_ONE));
        Assert.assertEquals(BYTE_TEST, readWriteIntMapMsg2.getAsShort(KEY_TWO));
        Assert.assertFalse(readWriteIntMapMsg2.contains(KEY_ZERO));
        Assert.assertEquals(IntMapConstants.DEFAULT_SHORT_VALUE, readWriteIntMapMsg2.getAsShort(KEY_NINETYNINE));
        Assert.assertEquals(IntMapConstants.DEFAULT_SHORT_VALUE, readWriteIntMapMsg2.getAsShort(KEY_ZERO));
        Assert.assertEquals(IntMapConstants.DEFAULT_SHORT_VALUE, readWriteIntMapMsg2.getAsShort(KEY_MINUS_ONE));
    }
    
    /**
     * testGetAsInt.
     * 
     */
    @Test
    public void testSetGetAsInt()
    {
        final ReadWriteIntMapMsg readWriteIntMapMsg = create();
        readWriteIntMapMsg.set(KEY_ONE, INT_TEST);
        readWriteIntMapMsg.set(KEY_TWO, SHORT_TEST);
        readWriteIntMapMsg.set(KEY_THREE, BYTE_TEST);
        final byte[] bytes = readWriteIntMapMsg.writeTo();
        
        final ReadWriteIntMapMsg readWriteIntMapMsg2 = create();
        readWriteIntMapMsg2.readFrom(bytes, 0, bytes.length);
        Assert.assertEquals(INT_TEST, readWriteIntMapMsg2.getAsInt(KEY_ONE));
        Assert.assertEquals(SHORT_TEST, readWriteIntMapMsg2.getAsInt(KEY_TWO));
        Assert.assertEquals(BYTE_TEST, readWriteIntMapMsg2.getAsInt(KEY_THREE));
        Assert.assertFalse(readWriteIntMapMsg2.contains(KEY_ZERO));
        Assert.assertEquals(IntMapConstants.DEFAULT_INT_VALUE, readWriteIntMapMsg2.getAsInt(KEY_NINETYNINE));
        Assert.assertEquals(IntMapConstants.DEFAULT_INT_VALUE, readWriteIntMapMsg2.getAsInt(KEY_ZERO));
        Assert.assertEquals(IntMapConstants.DEFAULT_INT_VALUE, readWriteIntMapMsg2.getAsInt(KEY_MINUS_ONE));
    }
    
    /**
     * testSetGetAsLong.
     * 
     */
    @Test
    public void testSetGetAsLong()
    {
        final ReadWriteIntMapMsg readWriteIntMapMsg = create();
        readWriteIntMapMsg.set(KEY_ONE, LONG_TEST);
        readWriteIntMapMsg.set(KEY_TWO, INT_TEST);
        readWriteIntMapMsg.set(KEY_THREE, SHORT_TEST);
        readWriteIntMapMsg.set(KEY_FOUR, BYTE_TEST);
        final byte[] bytes = readWriteIntMapMsg.writeTo();
        
        final ReadWriteIntMapMsg readWriteIntMapMsg2 = create();
        readWriteIntMapMsg2.readFrom(bytes, 0, bytes.length);
        Assert.assertEquals(LONG_TEST, readWriteIntMapMsg2.getAsLong(KEY_ONE));
        Assert.assertEquals(INT_TEST, readWriteIntMapMsg2.getAsLong(KEY_TWO));
        Assert.assertEquals(SHORT_TEST, readWriteIntMapMsg2.getAsLong(KEY_THREE));
        Assert.assertEquals(BYTE_TEST, readWriteIntMapMsg2.getAsLong(KEY_FOUR));
        Assert.assertFalse(readWriteIntMapMsg2.contains(KEY_ZERO));
        Assert.assertEquals(IntMapConstants.DEFAULT_LONG_VALUE, readWriteIntMapMsg2.getAsLong(KEY_NINETYNINE));
        Assert.assertEquals(IntMapConstants.DEFAULT_LONG_VALUE, readWriteIntMapMsg2.getAsLong(KEY_ZERO));
        Assert.assertEquals(IntMapConstants.DEFAULT_LONG_VALUE, readWriteIntMapMsg2.getAsLong(KEY_MINUS_ONE));
    }
    
    
    /**
     * testSetGetAsFloat.
     * 
     */
    @Test
    public void testSetGetAsFloat()
    {
        fail("Not yet implemented");
    }
    
    /**
     * testGetAsDouble.
     * 
     */
    @Test
    public void testSetGetAsDouble()
    {
        fail("Not yet implemented");
    }
    
    
    /**
     * testGetAsString.
     * 
     */
    @Test
    public void testSetGetAsString()
    {
        final ReadWriteIntMapMsg readWriteIntMapMsg = create();
        readWriteIntMapMsg.set(KEY_ONE, STRING_TEST);
        final byte[] bytes = readWriteIntMapMsg.writeTo();
        
        final ReadWriteIntMapMsg readWriteIntMapMsg2 = create();
        readWriteIntMapMsg2.readFrom(bytes, 0, bytes.length);
        readWriteIntMapMsg2.getAsString(1);
        Assert.assertEquals(STRING_TEST, readWriteIntMapMsg2.getAsString(KEY_ONE));
    }
//    
//    /**
//     * testSetGetAsMap.
//     * 
//     */
//    @Test
//    public void testSetGetAsMap()
//    {
//        fail("Not yet implemented");
//    }
    
    
//    @Test
//    public void testGetAsIntMapValue()
//    {
//        fail("Not yet implemented");
//    }
    
    
//    @Test
//    public void testGet()
//    {
//        //fail("Not yet implemented");
//        //TODO
//    }
    
    
//  @Test
//  public void testGetIterator()
//  {
//      fail("Not yet implemented");
//  }
    
    
    
    /**
     * testSetAll.
     * 
     */
    @Test
    public void testSetAll()
    {
        fail("Not yet implemented");
    }
    
    /**
     * testRemove.
     * 
     */
    @Test
    public void testRemove()
    {
        fail("Not yet implemented");
    }
    
    /**
     * testReadFrom.
     * 
     */
    @Test
    public void testReadFromWriteTo()
    {
        final ReadWriteIntMapMsg readWriteIntMapMsg = create();
        readWriteIntMapMsg.set(KEY_ONE, BYTE_TEST);
        readWriteIntMapMsg.set(KEY_TWO, SHORT_TEST);
        readWriteIntMapMsg.set(KEY_FOUR, INT_TEST);
        readWriteIntMapMsg.set(KEY_EIGHT, LONG_TEST);
        readWriteIntMapMsg.set(KEY_TEN, FLOAT_TEST);
        readWriteIntMapMsg.set(KEY_ELEVEN, DOUBLE_TEST);
        readWriteIntMapMsg.set(KEY_TWELVE, DOUBLE_TEST, 2);
        readWriteIntMapMsg.set(KEY_THIRTEEN, STRING_TEST);
        final byte[] bytes = readWriteIntMapMsg.writeTo();
        
        final ReadWriteIntMapMsg readWriteIntMapMsg2 = create();
        readWriteIntMapMsg2.readFrom(bytes, 0, bytes.length);
        Assert.assertEquals(BYTE_TEST, readWriteIntMapMsg2.getAsByte(KEY_ONE));
        Assert.assertEquals(SHORT_TEST, readWriteIntMapMsg2.getAsShort(KEY_TWO));
        Assert.assertEquals(INT_TEST, readWriteIntMapMsg2.getAsInt(KEY_FOUR));
        //Assert.assertEquals(LONG_TEST, readWriteIntMapMsg2.getAsLong(KEY_EIGHT));
        //Assert.assertEquals(FLOAT_TEST, readWriteIntMapMsg2.getAsFloat(KEY_TEN), (float) PRECISION);
        //Assert.assertEquals(DOUBLE_TEST, readWriteIntMapMsg2.getAsDouble(KEY_ELEVEN), PRECISION);
        //Assert.assertEquals(DOUBLE_TEST, readWriteIntMapMsg2.getAsDouble(KEY_TWELVE), PRECISION);
        //Assert.assertEquals(STRING_TEST, readWriteIntMapMsg2.getAsString(KEY_THIRTEEN));
    }
    
}
