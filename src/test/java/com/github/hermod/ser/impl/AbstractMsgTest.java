package com.github.hermod.ser.impl;

import static org.junit.Assert.fail;
import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Assert;
import org.junit.Test;

import com.github.hermod.ser.IMsg;
import com.github.hermod.ser.IMsgFactory;

/**
 * AbstractMsgTest.
 * 
 * @author anavarro - Jan 20, 2013
 * 
 */
public abstract class AbstractMsgTest {

    private static final byte BYTE_TEST = Byte.MIN_VALUE;
    private static final short SHORT_TEST = Short.MAX_VALUE;
    private static final int INT_TEST = Integer.MIN_VALUE;
    private static final long LONG_TEST = Long.MAX_VALUE / 2;
    private static final float FLOAT_TEST = 10.10f;
    private static final double DOUBLE_TEST = 11.11;
    private static final double DOUBLE_TEST1 = 1.0;
    private static final double DOUBLE_TEST2 = Double.MAX_VALUE / 2;
    private static final String STRING_TEST = "string";
    private static final String STRING_TEST32 = "12345678901234567890123456789012";
    private static final String STRING_TEST132 = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012";
    private static final Object[] OBJECT_ARRAY_TEST = {};

    private static final int KEY_MINUS_ONE = -1;
    private static final int KEY_ZERO = 0;
    private static final int KEY_ONE = 1;
    private static final int KEY_TWO = 2;
    private static final int KEY_THREE = 3;
    private static final int KEY_FOUR = 4;
    private static final int KEY_FIVE = 5;
    private static final int KEY_SIX = 6;
    private static final int KEY_SEVEN = 7;
    private static final int KEY_EIGHT = 8;
    private static final int KEY_TEN = 10;
    private static final int KEY_ELEVEN = 11;
    private static final int KEY_TWELVE = 12;
    private static final int KEY_THIRTEEN = 13;
    private static final int KEY_NINETY = 90;
    private static final int KEY_NINETYTWO = 92;
    private static final int KEY_NINETYNINE = 99;
    private static final int KEY_THREE_HUNDRED = 300;

    private static final double PRECISION = 0.00001;

    protected final IMsgFactory msgFactory;

    protected final IMsg msgTest;

    /**
     * Constructor.
     * 
     * @param msgFactory
     */
    public AbstractMsgTest(final IMsgFactory msgFactory) {
        super();
        this.msgFactory = msgFactory;
        this.msgTest = this.msgFactory.create();
        this.msgTest.set(KEY_ONE, BYTE_TEST);
    }

    /**
     * testClear.
     * 
     */
    @Test
    public void testClear() {
        final IMsg msg = this.msgFactory.create();
        msg.set(KEY_ONE, INT_TEST);
        Assert.assertEquals(msg.getKeys().length, 1);
        msg.clear();
        Assert.assertEquals(msg.getKeys().length, 0);
    }

    /**
     * testContains.
     * 
     */
    @Test
    public void testContains() {
        final IMsg msg = this.msgFactory.create();
        msg.set(1, 1);

        Assert.assertTrue(msg.contains(KEY_ONE));
        Assert.assertFalse(msg.contains(KEY_MINUS_ONE));
        Assert.assertFalse(msg.contains(KEY_ZERO));
        Assert.assertFalse(msg.contains(KEY_NINETYNINE));
    }

    /**
     * testGetKeys.
     * 
     */
    @Test
    public void testGetKeys() {
        final IMsg msg = this.msgFactory.create();
        msg.set(KEY_ONE, BYTE_TEST);
        msg.set(KEY_TWO, SHORT_TEST);
        final byte[] bytes = msg.writeTo();

        final IMsg msg2 = this.msgFactory.create();
        msg2.readFrom(bytes, 0, bytes.length);
        final int[] keys = msg2.getKeys();

        Assert.assertEquals(2, keys.length);
        Assert.assertEquals(1, keys[0]);
        Assert.assertEquals(2, keys[1]);

    }

    /**
     * testGetAsByte.
     * 
     */
    @Test
    public void testSetGetAsByte() {
        final IMsg msg = this.msgFactory.create();
        msg.set(KEY_ZERO, new Byte((byte) BYTE_TEST));
        msg.set(KEY_ONE, BYTE_TEST);
        msg.set(KEY_NINETY, BYTE_TEST);
        msg.set(KEY_THREE_HUNDRED, BYTE_TEST);

        final byte[] bytes = msg.writeTo();

        final IMsg msg2 = this.msgFactory.create();
        msg2.readFrom(bytes, 0, bytes.length);
        Assert.assertEquals(BYTE_TEST, msg2.getAsByte(KEY_ONE));
        Assert.assertEquals(BYTE_TEST, msg2.getAsByte(KEY_NINETY));
        Assert.assertEquals(BYTE_TEST, msg2.getAsByte(KEY_THREE_HUNDRED));
        Assert.assertTrue(msg2.contains(KEY_ZERO));
        Assert.assertEquals(MsgConstants.DEFAULT_BYTE_VALUE, msg2.getAsByte(KEY_NINETYNINE));
        Assert.assertEquals(BYTE_TEST, msg2.getAsByte(KEY_ZERO));
        Assert.assertEquals(MsgConstants.DEFAULT_BYTE_VALUE, msg2.getAsByte(KEY_MINUS_ONE));
    }

    /**
     * testSetGetAsByteWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetAsByteWithIllegalArgument() {
        final IMsg msg = this.msgFactory.create();
        msg.set(KEY_MINUS_ONE, BYTE_TEST);
    }

    /**
     * testGetAsShort.
     * 
     */
    @Test
    public void testSetGetAsShort() {
        final IMsg msg = this.msgFactory.create();
        msg.set(KEY_ONE, SHORT_TEST);
        msg.set(KEY_TWO, BYTE_TEST);
        msg.set(KEY_THREE, (short) BYTE_TEST);
        msg.set(KEY_NINETY, SHORT_TEST);
        msg.set(KEY_THREE_HUNDRED, SHORT_TEST);
        final byte[] bytes = msg.writeTo();

        final IMsg msg2 = this.msgFactory.create();
        msg2.readFrom(bytes, 0, bytes.length);
        Assert.assertEquals(SHORT_TEST, msg2.getAsShort(KEY_ONE));
        Assert.assertEquals(SHORT_TEST, msg2.getAsShort(KEY_NINETY));
        Assert.assertEquals(SHORT_TEST, msg2.getAsShort(KEY_THREE_HUNDRED));
        Assert.assertEquals(BYTE_TEST, msg2.getAsShort(KEY_TWO));
        Assert.assertEquals(BYTE_TEST, msg2.getAsShort(KEY_THREE));
        Assert.assertFalse(msg2.contains(KEY_ZERO));
        Assert.assertEquals(MsgConstants.DEFAULT_SHORT_VALUE, msg2.getAsShort(KEY_NINETYNINE));
        Assert.assertEquals(MsgConstants.DEFAULT_SHORT_VALUE, msg2.getAsShort(KEY_ZERO));
        Assert.assertEquals(MsgConstants.DEFAULT_SHORT_VALUE, msg2.getAsShort(KEY_MINUS_ONE));
    }

    /**
     * testSetGetAsShortWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetAsShortWithIllegalArgument() {
        final IMsg msg = this.msgFactory.create();
        msg.set(KEY_MINUS_ONE, SHORT_TEST);
    }

    /**
     * testGetAsInt.
     * 
     */
    @Test
    public void testSetGetAsInt() {
        final IMsg msg = this.msgFactory.create();
        msg.set(KEY_ONE, INT_TEST);
        msg.set(KEY_TWO, SHORT_TEST);
        msg.set(KEY_THREE, BYTE_TEST);
        msg.set(KEY_FOUR, (short) BYTE_TEST);
        msg.set(KEY_FIVE, (int) SHORT_TEST);
        msg.set(KEY_NINETY, INT_TEST);
        msg.set(KEY_THREE_HUNDRED, INT_TEST);
        final byte[] bytes = msg.writeTo();

        final IMsg msg2 = this.msgFactory.create();
        msg2.readFrom(bytes, 0, bytes.length);
        Assert.assertEquals(INT_TEST, msg2.getAsInt(KEY_ONE));
        Assert.assertEquals(SHORT_TEST, msg2.getAsInt(KEY_TWO));
        Assert.assertEquals(BYTE_TEST, msg2.getAsInt(KEY_THREE));
        Assert.assertEquals(BYTE_TEST, msg2.getAsInt(KEY_FOUR));
        Assert.assertEquals(SHORT_TEST, msg2.getAsInt(KEY_FIVE));
        Assert.assertEquals(INT_TEST, msg2.getAsInt(KEY_NINETY));
        Assert.assertEquals(INT_TEST, msg2.getAsInt(KEY_THREE_HUNDRED));
        Assert.assertFalse(msg2.contains(KEY_ZERO));
        Assert.assertEquals(MsgConstants.DEFAULT_INT_VALUE, msg2.getAsInt(KEY_NINETYNINE));
        Assert.assertEquals(MsgConstants.DEFAULT_INT_VALUE, msg2.getAsInt(KEY_ZERO));
        Assert.assertEquals(MsgConstants.DEFAULT_INT_VALUE, msg2.getAsInt(KEY_MINUS_ONE));
    }

    /**
     * testSetGetAsIntWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetAsIntWithIllegalArgument() {
        final IMsg msg = this.msgFactory.create();
        msg.set(KEY_MINUS_ONE, INT_TEST);
    }

    /**
     * testSetGetAsLong.
     * 
     */
    @Test
    public void testSetGetAsLong() {
        final IMsg msg = this.msgFactory.create();
        msg.set(KEY_ONE, LONG_TEST);
        msg.set(KEY_TWO, INT_TEST);
        msg.set(KEY_THREE, SHORT_TEST);
        msg.set(KEY_FOUR, BYTE_TEST);
        msg.set(KEY_FIVE, (long) BYTE_TEST);
        msg.set(KEY_SIX, (long) SHORT_TEST);
        msg.set(KEY_SEVEN, (long) INT_TEST);
        msg.set(KEY_NINETY, LONG_TEST);
        msg.set(KEY_THREE_HUNDRED, LONG_TEST);

        final byte[] bytes = msg.writeTo();

        final IMsg msg2 = this.msgFactory.create();
        msg2.readFrom(bytes, 0, bytes.length);
        Assert.assertEquals(LONG_TEST, msg2.getAsLong(KEY_ONE));
        Assert.assertEquals(INT_TEST, msg2.getAsLong(KEY_TWO));
        Assert.assertEquals(SHORT_TEST, msg2.getAsLong(KEY_THREE));
        Assert.assertEquals(BYTE_TEST, msg2.getAsLong(KEY_FOUR));
        Assert.assertEquals(BYTE_TEST, msg2.getAsLong(KEY_FIVE));
        Assert.assertEquals(SHORT_TEST, msg2.getAsLong(KEY_SIX));
        Assert.assertEquals(INT_TEST, msg2.getAsLong(KEY_SEVEN));
        Assert.assertEquals(LONG_TEST, msg2.getAsLong(KEY_NINETY));
        Assert.assertEquals(LONG_TEST, msg2.getAsLong(KEY_THREE_HUNDRED));

        Assert.assertFalse(msg2.contains(KEY_ZERO));
        Assert.assertEquals(MsgConstants.DEFAULT_LONG_VALUE, msg2.getAsLong(KEY_NINETYNINE));
        Assert.assertEquals(MsgConstants.DEFAULT_LONG_VALUE, msg2.getAsLong(KEY_ZERO));
        Assert.assertEquals(MsgConstants.DEFAULT_LONG_VALUE, msg2.getAsLong(KEY_MINUS_ONE));
    }

    /**
     * testSetGetAsIntWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetAsLongWithIllegalArgument() {
        final IMsg msg = this.msgFactory.create();
        msg.set(KEY_MINUS_ONE, LONG_TEST);
    }

    /**
     * testSetGetAsFloat.
     * 
     */
    @Test
    public void testSetGetAsFloat() {
        final IMsg msg = this.msgFactory.create();
        msg.set(KEY_ONE, FLOAT_TEST);
        msg.set(KEY_NINETY, FLOAT_TEST);
        msg.set(KEY_THREE_HUNDRED, FLOAT_TEST);
        final byte[] bytes = msg.writeTo();

        final IMsg msg2 = this.msgFactory.create();
        msg2.readFrom(bytes, 0, bytes.length);
        Assert.assertEquals(FLOAT_TEST, msg2.getAsFloat(KEY_ONE), PRECISION);
        Assert.assertEquals(FLOAT_TEST, msg2.getAsFloat(KEY_ONE), PRECISION);
        Assert.assertEquals(FLOAT_TEST, msg2.getAsDouble(KEY_NINETY), PRECISION);
        Assert.assertEquals(FLOAT_TEST, msg2.getAsDouble(KEY_THREE_HUNDRED), PRECISION);

        Assert.assertEquals(MsgConstants.DEFAULT_FLOAT_VALUE, msg2.getAsFloat(KEY_NINETYNINE), PRECISION);
        Assert.assertEquals(MsgConstants.DEFAULT_FLOAT_VALUE, msg2.getAsFloat(KEY_ZERO), PRECISION);
        Assert.assertEquals(MsgConstants.DEFAULT_FLOAT_VALUE, msg2.getAsFloat(KEY_MINUS_ONE), PRECISION);
        Assert.assertFalse(msg2.contains(KEY_ZERO));
    }

    /**
     * testSetGetAsFloatWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetAsFloatWithIllegalArgument() {
        final IMsg msg = this.msgFactory.create();
        msg.set(KEY_MINUS_ONE, FLOAT_TEST);
    }

    /**
     * testGetAsDouble.
     * 
     */
    @Test
    public void testSetGetAsDouble() {
        final IMsg msg = this.msgFactory.create();
        msg.set(KEY_ONE, FLOAT_TEST);
        msg.set(KEY_TWO, DOUBLE_TEST);
        msg.set(KEY_THREE, DOUBLE_TEST, 5);
        msg.set(KEY_FOUR, (double) FLOAT_TEST);
        msg.set(KEY_FIVE, DOUBLE_TEST2, 5);

        for (int i = 0; i < MsgConstants.DOZENS.length; i++) {
            msg.set(KEY_TEN + i, DOUBLE_TEST1, i);
        }
        
        msg.set(KEY_NINETY, DOUBLE_TEST);
        msg.set(KEY_NINETYTWO, DOUBLE_TEST, 5);
        msg.set(KEY_THREE_HUNDRED, DOUBLE_TEST);

        final byte[] bytes = msg.writeTo();

        final IMsg msg2 = this.msgFactory.create();
        msg2.readFrom(bytes, 0, bytes.length);
        Assert.assertEquals(FLOAT_TEST, msg2.getAsFloat(KEY_ONE), PRECISION);
        Assert.assertEquals(DOUBLE_TEST, msg2.getAsDouble(KEY_TWO), PRECISION);
        Assert.assertEquals(DOUBLE_TEST, msg2.getAsDouble(KEY_THREE), PRECISION);
        Assert.assertEquals(FLOAT_TEST, msg2.getAsDouble(KEY_FOUR), PRECISION);
        Assert.assertEquals(DOUBLE_TEST2, msg2.getAsDouble(KEY_FIVE), PRECISION);
        for (int i = 0; i < MsgConstants.DOZENS.length; i++) {
            Assert.assertEquals(DOUBLE_TEST1, msg2.getAsDouble(KEY_TEN + i), (i == 0) ? 1 : (1 / (10 * i)));
        }
        
        Assert.assertEquals(DOUBLE_TEST, msg2.getAsDouble(KEY_NINETY), PRECISION);
        Assert.assertEquals(DOUBLE_TEST, msg2.getAsDouble(KEY_NINETYTWO), PRECISION);
        Assert.assertEquals(DOUBLE_TEST, msg2.getAsDouble(KEY_THREE_HUNDRED), PRECISION);

        Assert.assertEquals(MsgConstants.DEFAULT_DOUBLE_VALUE, msg2.getAsDouble(KEY_NINETYNINE), PRECISION);
        Assert.assertEquals(MsgConstants.DEFAULT_DOUBLE_VALUE, msg2.getAsDouble(KEY_ZERO), PRECISION);
        Assert.assertEquals(MsgConstants.DEFAULT_DOUBLE_VALUE, msg2.getAsDouble(KEY_MINUS_ONE), PRECISION);

        Assert.assertFalse(msg2.contains(KEY_ZERO));
    }

    /**
     * testSetGetAsFloatWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetAsDoubleWithIllegalArgument() {
        final IMsg msg = this.msgFactory.create();
        msg.set(KEY_MINUS_ONE, DOUBLE_TEST);
    }

    /**
     * testSetGetAsDoubleWithDigitWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetAsDoubleWithDigitWithKeyIllegalArgument() {
        final IMsg msg = this.msgFactory.create();
        msg.set(KEY_MINUS_ONE, DOUBLE_TEST, 1);
    }

    /**
     * testSetGetAsDoubleWithDigitWithNbDigitIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetAsDoubleWithDigitWithNbDigitIllegalArgument() {
        final IMsg msg = this.msgFactory.create();
        msg.set(KEY_ONE, DOUBLE_TEST, MsgConstants.DOZENS.length);
    }

    /**
     * testGetAsString.
     * 
     */
    @Test
    public void testSetGetAsString() {
        final IMsg msg = this.msgFactory.create();
        msg.set(KEY_ONE, STRING_TEST);
        msg.set(KEY_TWO, STRING_TEST32);
        msg.set(KEY_THREE, STRING_TEST132);
        msg.set(KEY_FOUR, (String) null);
        msg.set(KEY_NINETY, STRING_TEST);
        msg.set(KEY_THREE_HUNDRED, STRING_TEST);

        final byte[] bytes = msg.writeTo();
        final IMsg msg2 = this.msgFactory.create();
        msg2.readFrom(bytes, 0, bytes.length);
        Assert.assertEquals(STRING_TEST, msg2.getAsString(KEY_ONE));
        Assert.assertEquals(STRING_TEST32, msg2.getAsString(KEY_TWO));
        Assert.assertEquals(STRING_TEST132, msg2.getAsString(KEY_THREE));
        Assert.assertNull(msg2.getAsString(KEY_FOUR));
        Assert.assertEquals(STRING_TEST, msg2.getAsString(KEY_NINETY));
        Assert.assertEquals(STRING_TEST, msg2.getAsString(KEY_THREE_HUNDRED));

        Assert.assertEquals(MsgConstants.DEFAULT_STRING_VALUE, msg2.getAsString(KEY_NINETYNINE));
        Assert.assertEquals(MsgConstants.DEFAULT_STRING_VALUE, msg2.getAsString(KEY_ZERO));
        Assert.assertEquals(MsgConstants.DEFAULT_STRING_VALUE, msg2.getAsString(KEY_MINUS_ONE));
        Assert.assertFalse(msg2.contains(KEY_ZERO));
    }

    /**
     * testSetGetAsStringWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetAsStringWithIllegalArgument() {
        final IMsg msg = this.msgFactory.create();
        msg.set(KEY_MINUS_ONE, STRING_TEST);
    }

    /**
     * testSetGetAsMsg.
     * 
     */
    @Test
    public void testSetGetAsMsg() {
        final IMsg msg = this.msgFactory.create();
        msg.set(KEY_ONE, this.msgTest);
        msg.set(KEY_TWO, BYTE_TEST);
        msg.set(KEY_FOUR, (IMsg) null);
        msg.set(KEY_NINETY, this.msgTest);
        msg.set(KEY_THREE_HUNDRED, this.msgTest);
        final byte[] bytes = msg.writeTo();

        System.out.println("" + this.msgTest);
        final IMsg msg2 = this.msgFactory.create();
        msg2.readFrom(bytes, 0, bytes.length);
        System.out.println(msg2);
        Assert.assertEquals(this.msgTest.getAsByte(KEY_ONE), msg2.getAsMsg(KEY_ONE).getAsByte(KEY_ONE));
        Assert.assertEquals(this.msgTest.getAsByte(KEY_ONE), msg2.getAsMsg(KEY_NINETY).getAsByte(KEY_ONE));
        Assert.assertEquals(this.msgTest.getAsByte(KEY_ONE), msg2.getAsMsg(KEY_THREE_HUNDRED).getAsByte(KEY_ONE));
        Assert.assertNull(msg2.getAsMsg(KEY_FOUR));

        Assert.assertEquals(MsgConstants.DEFAULT_MSG_VALUE, msg2.getAsMsg(KEY_NINETYNINE));

        Assert.assertEquals(MsgConstants.DEFAULT_MSG_VALUE, msg2.getAsMsg(KEY_ZERO));
        Assert.assertEquals(MsgConstants.DEFAULT_MSG_VALUE, msg2.getAsMsg(KEY_MINUS_ONE));
        Assert.assertFalse(msg2.contains(KEY_ZERO));
    }

    /**
     * testSetGetAsMsgWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetAsMsgWithIllegalArgument() {
        final IMsg msg = this.msgFactory.create();
        msg.set(KEY_MINUS_ONE, this.msgTest);
    }

    /**
     * testSetGetAsObjectArray.
     * 
     */
    @Test
    public void testSetGetAsObjectArray() {
        // final Msg msg = this.msgFactory.create();
        // msg.set(KEY_ONE, STRING_TEST);
        // msg.set(KEY_TWO, STRING_TEST32);
        // msg.set(KEY_THREE, STRING_TEST132);
        // //msg.set(KEY_NINETY, STRING_TEST);
        //
        // final byte[] bytes = msg.writeTo();
        //
        // final Msg msg2 = this.msgFactory.create();
        // msg2.readFrom(bytes, 0, bytes.length);
        // Assert.assertEquals(STRING_TEST, msg2.getAsString(KEY_ONE));
        // Assert.assertEquals(STRING_TEST32, msg2.getAsString(KEY_TWO));
        // Assert.assertEquals(STRING_TEST132, msg2.getAsString(KEY_THREE));
        // //Assert.assertEquals(STRING_TEST, msg2.getAsString(KEY_NINETY));
        // Assert.assertEquals(MsgConstants.DEFAULT_STRING_VALUE, msg2.getAsString(KEY_NINETYNINE));
        // Assert.assertEquals(MsgConstants.DEFAULT_STRING_VALUE, msg2.getAsString(KEY_ZERO));
        // Assert.assertEquals(MsgConstants.DEFAULT_STRING_VALUE, msg2.getAsString(KEY_MINUS_ONE));
        // Assert.assertFalse(msg2.contains(KEY_ZERO));
    }

    /**
     * testSetGetAsObjectArrayWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetAsObjectArrayWithIllegalArgument() {
        final IMsg msg = this.msgFactory.create();
        msg.set(KEY_MINUS_ONE, OBJECT_ARRAY_TEST);
    }
    
    /**
     * testSetGet.
     *
     */
    @Test
    public void testSetGet() {

        final IMsg msg = this.msgFactory.create();
        msg.set(KEY_ONE, Long.valueOf(LONG_TEST));
        msg.set(KEY_TWO, Integer.valueOf(INT_TEST));
        msg.set(KEY_THREE, Short.valueOf(SHORT_TEST));
        msg.set(KEY_FOUR, Byte.valueOf(BYTE_TEST));
        msg.set(KEY_FIVE, Long.valueOf(BYTE_TEST));
        msg.set(KEY_SIX, Long.valueOf(SHORT_TEST));
        msg.set(KEY_SEVEN, Long.valueOf(INT_TEST));
        msg.set(KEY_NINETY, LONG_TEST);
        msg.set(KEY_THREE_HUNDRED, LONG_TEST);

        final byte[] bytes = msg.writeTo();

        final IMsg msg2 = this.msgFactory.create();
        msg2.readFrom(bytes, 0, bytes.length);
        Assert.assertEquals(LONG_TEST, msg2.get(KEY_ONE));
        Assert.assertEquals(INT_TEST, msg2.get(KEY_TWO));
        Assert.assertEquals(SHORT_TEST, msg2.get(KEY_THREE));
        Assert.assertEquals(BYTE_TEST, msg2.get(KEY_FOUR));
        Assert.assertEquals(BYTE_TEST, msg2.get(KEY_FIVE));
        Assert.assertEquals(SHORT_TEST, msg2.get(KEY_SIX));
        Assert.assertEquals(INT_TEST, msg2.get(KEY_SEVEN));
        Assert.assertEquals(LONG_TEST, msg2.get(KEY_NINETY));
        Assert.assertEquals(LONG_TEST, msg2.get(KEY_THREE_HUNDRED));

        Assert.assertFalse(msg2.contains(KEY_ZERO));
//        Assert.assertEquals(MsgConstants.DEFAULT_LONG_VALUE, msg2.get(KEY_NINETYNINE));
//        Assert.assertEquals(MsgConstants.DEFAULT_LONG_VALUE, msg2.get(KEY_ZERO));
//        Assert.assertEquals(MsgConstants.DEFAULT_LONG_VALUE, msg2.get(KEY_MINUS_ONE));
    
        
    }

    /**
     * testSetAll.
     * 
     */
    @Test
    public void testSetAll() {
        // fail("Not yet implemented");
    }

    /**
     * testRemove.
     * 
     */
    @Test
    public void testRemove() {
        final IMsg msg = this.msgFactory.create();
        msg.set(KEY_ONE, INT_TEST);
        Assert.assertTrue(msg.contains(KEY_ONE));
        msg.remove(KEY_ONE);
        Assert.assertFalse(msg.contains(KEY_ONE));
        msg.remove(KEY_NINETY);
        Assert.assertFalse(msg.contains(KEY_ONE));

    }

    /**
     * testRemoveWi.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveWithIllegalArgument() {
        final IMsg msg = this.msgFactory.create();
        msg.remove(KEY_MINUS_ONE);
    }

    /**
     * testReadFrom.
     * 
     */
    @Test
    public void testReadFromWriteTo() {
        final IMsg msg = this.msgFactory.create();
        msg.set(KEY_ONE, BYTE_TEST);
        msg.set(KEY_TWO, SHORT_TEST);
        msg.set(KEY_FOUR, INT_TEST);
        msg.set(KEY_EIGHT, LONG_TEST);
        msg.set(KEY_TEN, FLOAT_TEST);
        msg.set(KEY_ELEVEN, DOUBLE_TEST);
        msg.set(KEY_TWELVE, DOUBLE_TEST, 2);
        msg.set(KEY_THIRTEEN, STRING_TEST);
        final byte[] bytes = msg.writeTo();

        final IMsg msg2 = this.msgFactory.create();
        msg2.readFrom(bytes, 0, bytes.length);
        Assert.assertEquals(BYTE_TEST, msg2.getAsByte(KEY_ONE));
        Assert.assertEquals(SHORT_TEST, msg2.getAsShort(KEY_TWO));
        Assert.assertEquals(INT_TEST, msg2.getAsInt(KEY_FOUR));

        Assert.assertEquals(LONG_TEST, msg2.getAsLong(KEY_EIGHT));
        Assert.assertEquals(FLOAT_TEST, msg2.getAsFloat(KEY_TEN), (float) PRECISION);
        Assert.assertEquals(DOUBLE_TEST, msg2.getAsDouble(KEY_ELEVEN), PRECISION);
        Assert.assertEquals(DOUBLE_TEST, msg2.getAsDouble(KEY_TWELVE), PRECISION);
        Assert.assertEquals(STRING_TEST, msg2.getAsString(KEY_THIRTEEN));
    }

    /**
     * testWriteToWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWriteToWithIllegalArgument() {
        final IMsg msg = this.msgFactory.create();
        msg.set(KEY_ONE, BYTE_TEST);
        final byte[] bytes = new byte[0];
        msg.writeTo(bytes, 0);
    }

}
