package com.github.hermod.ser.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import org.junit.Test;

import com.github.hermod.ser.IByteableMsg;
import com.github.hermod.ser.IMsg;

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

    protected final IMsg msgTest;

    /**
     * newMsg.
     *
     * @return
     */
    public abstract IByteableMsg newByteableMsg();
    

    /**
     * Constructor.
     *
     */
    public AbstractMsgTest() {
        super();
        this.msgTest = newByteableMsg();
        this.msgTest.set(KEY_ONE, BYTE_TEST);
    }

    /**
     * testClear.
     * 
     */
    @Test
    public void testRemoveAll() {
        final IMsg msg = newByteableMsg();
        msg.set(KEY_ONE, INT_TEST);
        assertThat(msg.getKeys()).hasSize(1);
        msg.removeAll();
        assertThat(msg.getKeys()).hasSize(0);
    }

    /**
     * testContains.
     * 
     */
    @Test
    public void testContains() {
        final IMsg msg = newByteableMsg();
        msg.set(1, 1);
        assertThat(msg.contains(KEY_ONE)).isTrue();
        assertThat(msg.contains(KEY_MINUS_ONE)).isFalse();
        assertThat(msg.contains(KEY_ZERO)).isFalse();
        assertThat(msg.contains(KEY_NINETYNINE)).isFalse();
    }

    /**
     * testGetKeys.
     * 
     */
    @Test
    public void testGetKeys() {
        final IByteableMsg msg = newByteableMsg();
        msg.set(KEY_ONE, BYTE_TEST);
        msg.set(KEY_TWO, SHORT_TEST);
        final byte[] bytes = msg.serializeToBytes();

        final IByteableMsg msg2 = newByteableMsg();
        msg2.deserializeFrom(bytes, 0, bytes.length);
        final int[] keys = msg2.getKeys();

        assertThat(keys).isEqualTo(new int[]{KEY_ONE, KEY_TWO});
    }

    /**
     * testGetAsByte.
     * 
     */
    @Test
    public void testSetGetAsByte() {
        final IByteableMsg msg = newByteableMsg();
        msg.set(KEY_ZERO, new Byte((byte) BYTE_TEST));
        msg.set(KEY_ONE, BYTE_TEST);
        msg.set(KEY_NINETY, BYTE_TEST);
        msg.set(KEY_THREE_HUNDRED, BYTE_TEST);

        final byte[] bytes = msg.serializeToBytes();

        final IByteableMsg msg2 = newByteableMsg();
        msg2.deserializeFrom(bytes, 0, bytes.length);
        
        assertThat(msg2.getAsByte(KEY_ONE)).isEqualTo(BYTE_TEST);
        assertThat(msg2.getAsByte(KEY_NINETY)).isEqualTo(BYTE_TEST);
        assertThat(msg2.getAsByte(KEY_THREE_HUNDRED)).isEqualTo(BYTE_TEST);
        assertThat(msg2.contains(KEY_ZERO)).isTrue();
        
        try {
            assertThat(msg2.getAsByte(KEY_NINETYNINE));
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        try {
            assertThat(msg2.getAsByte(KEY_NINETYNINE));
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        
        try {
            assertThat(msg2.getAsByte(KEY_MINUS_ONE));
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        assertThat(msg2.getAsByte(KEY_ZERO)).isEqualTo(BYTE_TEST);
    }

    /**
     * testSetGetAsByteWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetAsByteWithIllegalArgument() {
        final IMsg msg = newByteableMsg();
        msg.set(KEY_MINUS_ONE, BYTE_TEST);
    }

    /**
     * testGetAsShort.
     * 
     */
    @Test
    public void testSetGetAsShort() {
        final IByteableMsg msg = newByteableMsg();
        msg.set(KEY_ONE, SHORT_TEST);
        msg.set(KEY_TWO, BYTE_TEST);
        msg.set(KEY_THREE, (short) BYTE_TEST);
        msg.set(KEY_NINETY, SHORT_TEST);
        msg.set(KEY_THREE_HUNDRED, SHORT_TEST);
        final byte[] bytes = msg.serializeToBytes();

        final IByteableMsg msg2 = newByteableMsg();
        msg2.deserializeFrom(bytes, 0, bytes.length);
        
        assertThat(msg2.getAsShort(KEY_ONE)).isEqualTo(SHORT_TEST);
        assertThat(msg2.getAsShort(KEY_NINETY)).isEqualTo(SHORT_TEST);
        assertThat(msg2.getAsShort(KEY_THREE_HUNDRED)).isEqualTo(SHORT_TEST);
        assertThat(msg2.getAsShort(KEY_TWO)).isEqualTo(BYTE_TEST);
        assertThat(msg2.getAsShort(KEY_THREE)).isEqualTo(BYTE_TEST);
        assertThat(msg2.contains(KEY_ZERO)).isFalse();
        
        try {
            msg2.getAsShort(KEY_NINETYNINE);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        try {
            msg2.getAsShort(KEY_ZERO);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        try {
            msg2.getAsShort(KEY_MINUS_ONE);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    /**
     * testSetGetAsShortWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetAsShortWithIllegalArgument() {
        final IByteableMsg msg = newByteableMsg();
        msg.set(KEY_MINUS_ONE, SHORT_TEST);
    }

    /**
     * testGetAsInt.
     * 
     */
    @Test
    public void testSetGetAsInt() {
        final IByteableMsg msg = newByteableMsg();
        msg.set(KEY_ONE, INT_TEST);
        msg.set(KEY_TWO, SHORT_TEST);
        msg.set(KEY_THREE, BYTE_TEST);
        msg.set(KEY_FOUR, (short) BYTE_TEST);
        msg.set(KEY_FIVE, (int) SHORT_TEST);
        msg.set(KEY_NINETY, INT_TEST);
        msg.set(KEY_THREE_HUNDRED, INT_TEST);
        final byte[] bytes = msg.serializeToBytes();

        final IByteableMsg msg2 = newByteableMsg();
        msg2.deserializeFrom(bytes, 0, bytes.length);
        
        assertThat(msg2.getAsInt(KEY_ONE)).isEqualTo(INT_TEST);
        assertThat(msg2.getAsInt(KEY_TWO)).isEqualTo(SHORT_TEST);
        assertThat(msg2.getAsInt(KEY_THREE)).isEqualTo(BYTE_TEST);
        assertThat(msg2.getAsInt(KEY_FOUR)).isEqualTo(BYTE_TEST);
        assertThat(msg2.getAsInt(KEY_FIVE)).isEqualTo(SHORT_TEST);
        assertThat(msg2.getAsInt(KEY_NINETY)).isEqualTo(INT_TEST);
        assertThat(msg2.getAsInt(KEY_THREE_HUNDRED)).isEqualTo(INT_TEST);
        assertThat(msg2.contains(KEY_ZERO)).isFalse();
        
        try {
            msg2.getAsInt(KEY_NINETYNINE);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        try {
            msg2.getAsInt(KEY_ZERO);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        try {
            msg2.getAsInt(KEY_MINUS_ONE);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    /**
     * testSetGetAsIntWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetAsIntWithIllegalArgument() {
        final IMsg msg = newByteableMsg();
        msg.set(KEY_MINUS_ONE, INT_TEST);
    }

    /**
     * testSetGetAsLong.
     * 
     */
    @Test
    public void testSetGetAsLong() {
        final IByteableMsg msg = newByteableMsg();
        msg.set(KEY_ONE, LONG_TEST);
        msg.set(KEY_TWO, INT_TEST);
        msg.set(KEY_THREE, SHORT_TEST);
        msg.set(KEY_FOUR, BYTE_TEST);
        msg.set(KEY_FIVE, (long) BYTE_TEST);
        msg.set(KEY_SIX, (long) SHORT_TEST);
        msg.set(KEY_SEVEN, (long) INT_TEST);
        msg.set(KEY_NINETY, LONG_TEST);
        msg.set(KEY_THREE_HUNDRED, LONG_TEST);

        final byte[] bytes = msg.serializeToBytes();

        final IByteableMsg msg2 = newByteableMsg();
        msg2.deserializeFrom(bytes, 0, bytes.length);
        assertThat(msg2.getAsLong(KEY_ONE)).isEqualTo(LONG_TEST);
        assertThat(msg2.getAsLong(KEY_TWO)).isEqualTo(INT_TEST);
        assertThat(msg2.getAsLong(KEY_THREE)).isEqualTo(SHORT_TEST);
        assertThat(msg2.getAsLong(KEY_FOUR)).isEqualTo(BYTE_TEST);
        assertThat(msg2.getAsLong(KEY_FIVE)).isEqualTo(BYTE_TEST);
        assertThat(msg2.getAsLong(KEY_SIX)).isEqualTo(SHORT_TEST);
        assertThat(msg2.getAsLong(KEY_SEVEN)).isEqualTo(INT_TEST);
        assertThat(msg2.getAsLong(KEY_NINETY)).isEqualTo(LONG_TEST);
        assertThat(msg2.getAsLong(KEY_THREE_HUNDRED)).isEqualTo(LONG_TEST);
        assertThat(msg2.contains(KEY_ZERO)).isFalse();
        
        try {
            msg2.getAsLong(KEY_NINETYNINE);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        try {
            msg2.getAsLong(KEY_ZERO);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        try {
            msg2.getAsLong(KEY_MINUS_ONE);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    /**
     * testSetGetAsIntWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetAsLongWithIllegalArgument() {
        final IMsg msg = newByteableMsg();
        msg.set(KEY_MINUS_ONE, LONG_TEST);
    }

    /**
     * testSetGetAsFloat.
     * 
     */
    @Test
    public void testSetGetAsFloat() {
        final IByteableMsg msg = newByteableMsg();
        msg.set(KEY_ONE, FLOAT_TEST);
        msg.set(KEY_NINETY, FLOAT_TEST);
        msg.set(KEY_THREE_HUNDRED, FLOAT_TEST);
        final byte[] bytes = msg.serializeToBytes();

        final IByteableMsg msg2 = newByteableMsg();
        msg2.deserializeFrom(bytes, 0, bytes.length);
        
        assertThat(msg2.getAsFloat(KEY_ONE)).isEqualTo(FLOAT_TEST);
        assertThat(msg2.getAsFloat(KEY_ONE)).isEqualTo(FLOAT_TEST);
        assertThat(msg2.getAsDouble(KEY_NINETY)).isEqualTo(FLOAT_TEST);
        assertThat(msg2.getAsDouble(KEY_THREE_HUNDRED)).isEqualTo(FLOAT_TEST);
        assertThat(msg2.contains(KEY_ZERO)).isFalse();
        
        try {
            msg2.getAsFloat(KEY_NINETYNINE);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        try {
            msg2.getAsFloat(KEY_ZERO);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        try {
            msg2.getAsFloat(KEY_MINUS_ONE);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        
    }

    /**
     * testSetGetAsFloatWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetAsFloatWithIllegalArgument() {
        final IMsg msg = newByteableMsg();
        msg.set(KEY_MINUS_ONE, FLOAT_TEST);
    }

    /**
     * testGetAsDouble.
     * 
     */
    @Test
    public void testSetGetAsDouble() {
        final IByteableMsg msg = newByteableMsg();
        msg.set(KEY_ONE, FLOAT_TEST);
        msg.set(KEY_TWO, DOUBLE_TEST);
        msg.set(KEY_THREE, DOUBLE_TEST, 5);
        msg.set(KEY_FOUR, (double) FLOAT_TEST);
        msg.set(KEY_FIVE, DOUBLE_TEST2, 5);

        for (int i = 0; i < Msgs.DOZENS.length; i++) {
            msg.set(KEY_TEN + i, DOUBLE_TEST1, i);
        }
        
        msg.set(KEY_NINETY, DOUBLE_TEST);
        msg.set(KEY_NINETYTWO, DOUBLE_TEST, 5);
        msg.set(KEY_THREE_HUNDRED, DOUBLE_TEST);

        final byte[] bytes = msg.serializeToBytes();

        final IByteableMsg msg2 = newByteableMsg();
        msg2.deserializeFrom(bytes, 0, bytes.length);
        
        assertThat(msg2.getAsFloat(KEY_ONE)).isEqualTo(FLOAT_TEST);
        assertThat(msg2.getAsDouble(KEY_TWO)).isEqualTo(DOUBLE_TEST);
        //assertThat(msg2.getAsDouble(KEY_THREE)).isEqualTo(DOUBLE_TEST);
        assertThat(msg2.getAsDouble(KEY_FOUR)).isEqualTo(FLOAT_TEST);
        assertThat(msg2.getAsDouble(KEY_FIVE)).isEqualTo(DOUBLE_TEST2);
        
        for (int i = 0; i < Msgs.DOZENS.length; i++) {
            assertThat(msg2.getAsDouble(KEY_TEN + i)).isEqualTo(DOUBLE_TEST1);
            //Assert.assertEquals(DOUBLE_TEST1, msg2.getAsDouble(KEY_TEN + i), (i == 0) ? 1 : (1 / (10 * i)));
        }
        
        assertThat(msg2.contains(KEY_ZERO)).isFalse();

        try {
            msg2.getAsDouble(KEY_NINETYNINE);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        try {
            msg2.getAsDouble(KEY_ZERO);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        try {
            msg2.getAsDouble(KEY_MINUS_ONE);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        
    }

    /**
     * testSetGetAsFloatWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetAsDoubleWithIllegalArgument() {
        final IMsg msg = newByteableMsg();
        msg.set(KEY_MINUS_ONE, DOUBLE_TEST);
    }

    /**
     * testSetGetAsDoubleWithDigitWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetAsDoubleWithDigitWithKeyIllegalArgument() {
        final IMsg msg = newByteableMsg();
        msg.set(KEY_MINUS_ONE, DOUBLE_TEST, 1);
    }

    /**
     * testSetGetAsDoubleWithDigitWithNbDigitIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetAsDoubleWithDigitWithNbDigitIllegalArgument() {
        final IMsg msg = newByteableMsg();
        msg.set(KEY_ONE, DOUBLE_TEST, Msgs.DOZENS.length);
    }

    /**
     * testGetAsString.
     * 
     */
    @Test
    public void testSetGetAsString() {
        final IByteableMsg msg = newByteableMsg();
        msg.set(KEY_ONE, STRING_TEST);
        msg.set(KEY_TWO, STRING_TEST32);
        msg.set(KEY_THREE, STRING_TEST132);
        msg.set(KEY_FOUR, (String) null);
        msg.set(KEY_NINETY, STRING_TEST);
        msg.set(KEY_THREE_HUNDRED, STRING_TEST);

        final byte[] bytes = msg.serializeToBytes();
        final IByteableMsg msg2 = newByteableMsg();
        msg2.deserializeFrom(bytes, 0, bytes.length);
        
        assertThat(msg2.getAsString(KEY_ONE)).isEqualTo(STRING_TEST);
        assertThat(msg2.getAsString(KEY_TWO)).isEqualTo(STRING_TEST32);
        assertThat(msg2.getAsString(KEY_THREE)).isEqualTo(STRING_TEST132);
        assertThat(msg2.getAsString(KEY_FOUR)).isNull();
        assertThat(msg2.getAsString(KEY_NINETY)).isEqualTo(STRING_TEST);
        assertThat(msg2.getAsString(KEY_THREE_HUNDRED)).isEqualTo(STRING_TEST);
        assertThat(msg2.contains(KEY_ZERO)).isFalse();
        assertThat(msg2.getAsString(KEY_NINETYNINE)).isNull();
        assertThat(msg2.getAsString(KEY_ZERO)).isNull();
        assertThat(msg2.getAsString(KEY_MINUS_ONE)).isNull();
        
    }

    /**
     * testSetGetAsStringWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetAsStringWithIllegalArgument() {
        final IMsg msg = newByteableMsg();
        msg.set(KEY_MINUS_ONE, STRING_TEST);
    }

    /**
     * testSetGetAsMsg.
     * 
     */
    @Test
    public void testSetGetAsMsg() {
        final IByteableMsg msg = newByteableMsg();
        msg.set(KEY_ONE, this.msgTest);
        msg.set(KEY_TWO, BYTE_TEST);
        msg.set(KEY_FOUR, (IMsg) null);
        msg.set(KEY_NINETY, this.msgTest);
        msg.set(KEY_THREE_HUNDRED, this.msgTest);
        final byte[] bytes = msg.serializeToBytes();

        final IByteableMsg msg2 = newByteableMsg();
        msg2.deserializeFrom(bytes, 0, bytes.length);
        
        assertThat(this.msgTest.getAsByte(KEY_ONE)).isEqualTo(msg2.getAsMsg(KEY_ONE).getAsByte(KEY_ONE));
        assertThat(this.msgTest.getAsByte(KEY_ONE)).isEqualTo(msg2.getAsMsg(KEY_NINETY).getAsByte(KEY_ONE));
        assertThat(this.msgTest.getAsByte(KEY_ONE)).isEqualTo(msg2.getAsMsg(KEY_THREE_HUNDRED).getAsByte(KEY_ONE));
        assertThat(msg2.getAsMsg(KEY_FOUR)).isNull();
        assertThat(msg2.contains(KEY_ZERO)).isFalse();
        
        assertThat(msg2.getAsMsg(KEY_NINETYNINE)).isNull();
        assertThat(msg2.getAsMsg(KEY_ZERO)).isNull();
        assertThat(msg2.getAsMsg(KEY_MINUS_ONE)).isNull();
        
    }

    /**
     * testSetGetAsMsgWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetAsMsgWithIllegalArgument() {
        final IMsg msg = newByteableMsg();
        msg.set(KEY_MINUS_ONE, this.msgTest);
    }

    /**
     * testSetGetAsObjectArray.
     * 
     */
    @Test
    public void testSetGetAsObjectArray() {
        // final Msg msg = newMsg();
        // msg.set(KEY_ONE, STRING_TEST);
        // msg.set(KEY_TWO, STRING_TEST32);
        // msg.set(KEY_THREE, STRING_TEST132);
        // //msg.set(KEY_NINETY, STRING_TEST);
        //
        // final byte[] bytes = msg.serializeToBytes();
        //
        // final Msg msg2 = newMsg();
        // msg2.deserializeFrom(bytes, 0, bytes.length);
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
        final IMsg msg = newByteableMsg();
        msg.set(KEY_MINUS_ONE, OBJECT_ARRAY_TEST);
    }
    
    /**
     * testSetGet.
     *
     */
    @Test
    public void testSetGet() {

        final IByteableMsg msg = newByteableMsg();
        msg.set(KEY_ONE, Long.valueOf(LONG_TEST));
        msg.set(KEY_TWO, Integer.valueOf(INT_TEST));
        msg.set(KEY_THREE, Short.valueOf(SHORT_TEST));
        msg.set(KEY_FOUR, Byte.valueOf(BYTE_TEST));
        msg.set(KEY_FIVE, Long.valueOf(BYTE_TEST));
        msg.set(KEY_SIX, Long.valueOf(SHORT_TEST));
        msg.set(KEY_SEVEN, Long.valueOf(INT_TEST));
        msg.set(KEY_NINETY, LONG_TEST);
        msg.set(KEY_THREE_HUNDRED, LONG_TEST);

        final byte[] bytes = msg.serializeToBytes();

        final IByteableMsg msg2 = newByteableMsg();
        msg2.deserializeFrom(bytes, 0, bytes.length);
        
        assertThat(msg2.getAsObject(KEY_ONE)).isEqualTo(LONG_TEST);
        assertThat(msg2.getAsObject(KEY_TWO)).isEqualTo(INT_TEST);
        assertThat(msg2.getAsObject(KEY_THREE)).isEqualTo(SHORT_TEST);
        assertThat(msg2.getAsObject(KEY_FOUR)).isEqualTo(BYTE_TEST);
        
        assertThat(msg2.getAsObject(KEY_FIVE)).isEqualTo(BYTE_TEST);
        assertThat(msg2.getAsObject(KEY_SIX)).isEqualTo(SHORT_TEST);
        assertThat(msg2.getAsObject(KEY_SEVEN)).isEqualTo(INT_TEST);
        assertThat(msg2.getAsObject(KEY_NINETY)).isEqualTo(LONG_TEST);
        assertThat(msg2.getAsObject(KEY_THREE_HUNDRED)).isEqualTo(LONG_TEST);
        assertThat(msg2.contains(KEY_ZERO)).isFalse();

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
        final IMsg msg = newByteableMsg();
        msg.set(KEY_ONE, INT_TEST);
        assertThat(msg.contains(KEY_ONE)).isTrue();
        msg.remove(KEY_ONE);
        assertThat(msg.contains(KEY_ONE)).isFalse();
        msg.remove(KEY_NINETY);
        assertThat(msg.contains(KEY_ONE)).isFalse();

    }

    /**
     * testRemoveWi.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveWithIllegalArgument() {
        final IMsg msg = newByteableMsg();
        msg.remove(KEY_MINUS_ONE);
    }

    /**
     * testReadFrom.
     * 
     */
    @Test
    public void testDeserializeFrom() {
        final IByteableMsg msg = newByteableMsg();
        msg.set(KEY_ONE, BYTE_TEST);
        msg.set(KEY_TWO, SHORT_TEST);
        msg.set(KEY_FOUR, INT_TEST);
        msg.set(KEY_EIGHT, LONG_TEST);
        msg.set(KEY_TEN, FLOAT_TEST);
        msg.set(KEY_ELEVEN, DOUBLE_TEST);
        msg.set(KEY_TWELVE, DOUBLE_TEST, 2);
        msg.set(KEY_THIRTEEN, STRING_TEST);
        final byte[] bytes = msg.serializeToBytes();

        final IByteableMsg msg2 = newByteableMsg();
        msg2.deserializeFrom(bytes, 0, bytes.length);
        
        assertThat(msg2.getAsByte(KEY_ONE)).isEqualTo(BYTE_TEST);
        assertThat(msg2.getAsShort(KEY_TWO)).isEqualTo(SHORT_TEST);
        assertThat(msg2.getAsInt(KEY_FOUR)).isEqualTo(INT_TEST);
        assertThat(msg2.getAsLong(KEY_EIGHT)).isEqualTo(LONG_TEST);
        assertThat(msg2.getAsFloat(KEY_TEN)).isEqualTo(FLOAT_TEST);
        assertThat(msg2.getAsDouble(KEY_ELEVEN)).isEqualTo(DOUBLE_TEST);
        assertThat(msg2.getAsDouble(KEY_TWELVE)).isEqualTo(DOUBLE_TEST);
        assertThat(msg2.getAsString(KEY_THIRTEEN)).isEqualTo(STRING_TEST);
    }

    /**
     * testWriteToWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSerialiseToBytesWithIllegalArgument() {
        final IByteableMsg msg = newByteableMsg();
        msg.set(KEY_ONE, BYTE_TEST);
        final byte[] bytes = new byte[0];
        msg.serializeToBytes(bytes, 0);
    }

}
