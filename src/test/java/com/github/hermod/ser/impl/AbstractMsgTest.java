package com.github.hermod.ser.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.github.hermod.ser.Type;
import com.github.hermod.ser.ByteBufferMsgSerializer;
import com.github.hermod.ser.ByteBufferSerializable;
import com.github.hermod.ser.BytesMsgSerializer;
import com.github.hermod.ser.BytesSerializable;
import com.github.hermod.ser.Msg;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * AbstractMsgTest.
 * 
 * @author anavarro - Jan 20, 2013
 * 
 */
public abstract class AbstractMsgTest {

    protected static final boolean BOOLEAN_TEST = true;
    protected static final byte BYTE_TEST = Byte.MIN_VALUE;
    protected static final short SHORT_TEST = Short.MAX_VALUE;
    protected static final int INT_TEST = Integer.MIN_VALUE;
    protected static final long LONG_TEST = Long.MAX_VALUE / 2;
    protected static final float FLOAT_TEST = 10.10f;
    protected static final double DOUBLE_TEST = 11.11;
    protected static final double DOUBLE_TEST1 = 1.0;
    protected static final double DOUBLE_TEST2 = Double.MAX_VALUE / 2;
    protected static final String STRING_TEST_UTF16 = "€" + new String(Character.toChars(119070));
    protected static final String STRING_TEST = "string";
    protected static final String STRING_TEST32 = "12345678901234567890123456789012";
    protected static final String STRING_TEST132 = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012";
    protected static final Object[] OBJECT_ARRAY_TEST = {};
    protected static final boolean[] BOOLEANS_TEST = { true, false };
    protected static final Boolean[] BOOLEANS_TEST2 = { true, false };
    protected static final byte[] BYTES_TEST = { 0, 1, 2, 3, Byte.MAX_VALUE };
    protected static final Byte[] BYTES_TEST2 = { 0, 1, 2, 3, Byte.MAX_VALUE };
    protected static final short[] SHORTS_TEST = { 0, 1, 2, 3, Short.MAX_VALUE };
    protected static final Short[] SHORTS_TEST2 = { 0, 1, 2, 3, Short.MAX_VALUE };
    protected static final int[] INTS_TEST = { 0, 1, 2, 3, Integer.MAX_VALUE };
    protected static final Integer[] INTS_TEST2 = { 0, 1, 2, 3, Integer.MAX_VALUE };
    protected static final long[] LONGS_TEST = { 0, 1, 2, 3, Long.MAX_VALUE };
    protected static final Long[] LONGS_TEST2 = { 0l, 1l, 2l, 3l, Long.MAX_VALUE };
    protected static final Long[] LONGS_TEST3 = { null, 1l, 2l, 3l, Long.MAX_VALUE };
    protected static final long[] LONGS_TEST_EMPTY = {};
    protected static final float[] FLOATS_TEST = { 0, 1, 2, 3, Float.MAX_VALUE };
    protected static final Float[] FLOATS_TEST2 = { 0f, 1f, 2f, 3f, Float.MAX_VALUE };
    protected static final double[] DOUBLES_TEST = { 0, 1, 2, 3, Double.MAX_VALUE };
    protected static final Double[] DOUBLES_TEST2 = { 0d, 1d, 2d, 3d, Double.MAX_VALUE };

    protected static final String[] STRINGS_TEST = { "string1", "string2", "string3", "string4" };

    protected static final int KEY_MINUS_ONE = -1;
    protected static final int KEY_ZERO = 0;
    protected static final int KEY_ONE = 1;
    protected static final int KEY_TWO = 2;
    protected static final int KEY_THREE = 3;
    protected static final int KEY_FOUR = 4;
    protected static final int KEY_FIVE = 5;
    protected static final int KEY_SIX = 6;
    protected static final int KEY_SEVEN = 7;
    protected static final int KEY_EIGHT = 8;
    protected static final int KEY_NINE = 9;
    protected static final int KEY_TEN = 10;
    protected static final int KEY_ELEVEN = 11;
    protected static final int KEY_TWELVE = 12;
    protected static final int KEY_THIRTEEN = 13;
    protected static final int KEY_NINETY = 90;
    protected static final int KEY_NINETYTWO = 92;
    protected static final int KEY_NINETYNINE = 99;
    protected static final int KEY_THREE_HUNDRED = 300;
    protected static final int KEY_FOUR_HUNDRED = 400;

    protected static final double PRECISION = 0.00001;

    
    public static void main(String[] args) {
        
    }
    
    
    protected BytesMsgSerializer bytesMsgSerializer;
    protected ByteBufferMsgSerializer byteBufferSerializer;

    protected final Msg msgTest;

    protected Msg srcMsg;
    protected Msg destMsg;

    /**
     * newMsg.
     * 
     * @return
     */
    public abstract Msg newMsg();

    /**
     * Constructor.
     * 
     */
    public AbstractMsgTest() {
        super();
        this.msgTest = newMsg();
        this.msgTest.set(KEY_ONE, BYTE_TEST);
    }

    /**
     * setUp.
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        this.srcMsg = newMsg();
        this.destMsg = newMsg();
    }

    /**
     * testClear.
     * 
     */
    @Test
    public void testRemoveAll() {
        srcMsg.set(KEY_ONE, INT_TEST);
        assertThat(srcMsg.getKeysArray()).hasSize(1);
        srcMsg.removeAll();
        assertThat(srcMsg.getKeysArray()).hasSize(0);
    }

    /**
     * testContains.
     * 
     */
    @Test
    public void testContains() {
        srcMsg.set(1, 1);
        assertThat(srcMsg.contains(KEY_ONE)).isTrue();
        assertThat(srcMsg.contains(KEY_MINUS_ONE)).isFalse();
        assertThat(srcMsg.contains(KEY_ZERO)).isFalse();
        assertThat(srcMsg.contains(KEY_NINETYNINE)).isFalse();
    }

    /**
     * testGetKeys.
     * 
     */
    @Test
    public void testGetKeys() {
        srcMsg.set(KEY_ONE, BYTE_TEST);
        srcMsg.set(KEY_TWO, SHORT_TEST);
        final byte[] bytes = this.bytesMsgSerializer.serializeToBytes(srcMsg);

        this.bytesMsgSerializer.deserializeFromBytes(bytes, 0, bytes.length, destMsg);
        final int[] keys = destMsg.getKeysArray();

        assertThat(keys).isEqualTo(new int[] { KEY_ONE, KEY_TWO });
    }

    /**
     * testGetAsByte.
     * 
     */
    @Test
    public void testSetGetAsBoolean() {
        srcMsg.set(KEY_ZERO, BOOLEAN_TEST);
        srcMsg.set(KEY_ONE, BOOLEAN_TEST);
        srcMsg.set(KEY_TWO, Boolean.valueOf(BOOLEAN_TEST));
        srcMsg.set(KEY_THREE, (Boolean) null);
        srcMsg.set(KEY_NINETY, !BOOLEAN_TEST);
        srcMsg.set(KEY_THREE_HUNDRED, !BOOLEAN_TEST);
        srcMsg.set(KEY_FOUR_HUNDRED, (Boolean) null);

        final byte[] bytes = this.bytesMsgSerializer.serializeToBytes(srcMsg);

        this.bytesMsgSerializer.deserializeFromBytes(bytes, 0, bytes.length, destMsg);

        assertThat(destMsg.getAsBoolean(KEY_ONE)).isEqualTo(BOOLEAN_TEST);
        assertThat(destMsg.getAsNullableBoolean(KEY_ONE)).isEqualTo(BOOLEAN_TEST);
        assertThat(destMsg.getAsNullableBoolean(KEY_TWO)).isEqualTo(Boolean.valueOf(BOOLEAN_TEST));
        assertThat(destMsg.getAsNullableBoolean(KEY_THREE)).isNull();
        assertThat(destMsg.getAsBoolean(KEY_NINETY)).isEqualTo(!BOOLEAN_TEST);
        assertThat(destMsg.getAsBoolean(KEY_THREE_HUNDRED)).isEqualTo(!BOOLEAN_TEST);
        assertThat(destMsg.getAsNullableBoolean(KEY_FOUR_HUNDRED)).isNull();
        assertThat(destMsg.contains(KEY_ZERO)).isTrue();

        try {
            assertThat(destMsg.getAsBoolean(KEY_NINETYNINE));
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        assertThat(destMsg.getAsNullableBoolean(KEY_NINETYNINE)).isNull();

        try {
            assertThat(destMsg.getAsBoolean(KEY_NINETYNINE));
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }

        try {
            assertThat(destMsg.getAsBoolean(KEY_MINUS_ONE));
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        assertThat(destMsg.getAsBoolean(KEY_ZERO)).isEqualTo(BOOLEAN_TEST);

    }

    /**
     * testGetAsByte.
     * 
     */
    @Test
    public void testSetGetAsByte() {
        srcMsg.set(KEY_ZERO, new Byte((byte) BYTE_TEST));
        srcMsg.set(KEY_ONE, BYTE_TEST);
        srcMsg.set(KEY_TWO, Byte.valueOf(BYTE_TEST));
        srcMsg.set(KEY_THREE, (Byte) null);
        srcMsg.set(KEY_NINETY, BYTE_TEST);
        srcMsg.set(KEY_THREE_HUNDRED, BYTE_TEST);
        srcMsg.set(KEY_FOUR_HUNDRED, (Byte) null);

        final byte[] bytes = this.bytesMsgSerializer.serializeToBytes(srcMsg);

        this.bytesMsgSerializer.deserializeFromBytes(bytes, 0, bytes.length, destMsg);

        assertThat(destMsg.getAsByte(KEY_ONE)).isEqualTo(BYTE_TEST);
        assertThat(destMsg.getAsNullableByte(KEY_ONE)).isEqualTo(BYTE_TEST);
        assertThat(destMsg.getAsNullableByte(KEY_TWO)).isEqualTo(Byte.valueOf(BYTE_TEST));
        assertThat(destMsg.getAsNullableByte(KEY_THREE)).isNull();
        assertThat(destMsg.getAsByte(KEY_NINETY)).isEqualTo(BYTE_TEST);
        assertThat(destMsg.getAsByte(KEY_THREE_HUNDRED)).isEqualTo(BYTE_TEST);
        assertThat(destMsg.getAsNullableByte(KEY_FOUR_HUNDRED)).isNull();
        assertThat(destMsg.contains(KEY_ZERO)).isTrue();

        try {
            assertThat(destMsg.getAsByte(KEY_NINETYNINE));
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        assertThat(destMsg.getAsNullableByte(KEY_NINETYNINE));
        try {
            assertThat(destMsg.getAsByte(KEY_NINETYNINE));
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }

        try {
            assertThat(destMsg.getAsByte(KEY_MINUS_ONE));
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        assertThat(destMsg.getAsByte(KEY_ZERO)).isEqualTo(BYTE_TEST);
    }

    /**
     * testSetGetAsByteWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetAsByteWithIllegalArgument() {
        srcMsg.set(KEY_MINUS_ONE, BYTE_TEST);
    }

    /**
     * testGetAsShort.
     * 
     */
    @Test
    public void testSetGetAsShort() {
        srcMsg.set(KEY_ONE, SHORT_TEST);
        srcMsg.set(KEY_TWO, BYTE_TEST);
        srcMsg.set(KEY_THREE, (short) BYTE_TEST);
        srcMsg.set(KEY_FOUR, Short.valueOf(SHORT_TEST));
        srcMsg.set(KEY_FIVE, (Short) null);
        srcMsg.set(KEY_NINETY, SHORT_TEST);
        srcMsg.set(KEY_THREE_HUNDRED, SHORT_TEST);
        srcMsg.set(KEY_FOUR_HUNDRED, (Short) null);
        final byte[] bytes = this.bytesMsgSerializer.serializeToBytes(srcMsg);

        this.bytesMsgSerializer.deserializeFromBytes(bytes, 0, bytes.length, destMsg);

        assertThat(destMsg.getAsShort(KEY_ONE)).isEqualTo(SHORT_TEST);
        assertThat(destMsg.getAsNullableShort(KEY_ONE)).isEqualTo(SHORT_TEST);
        assertThat(destMsg.getAsShort(KEY_NINETY)).isEqualTo(SHORT_TEST);
        assertThat(destMsg.getAsShort(KEY_THREE_HUNDRED)).isEqualTo(SHORT_TEST);
        assertThat(destMsg.getAsShort(KEY_TWO)).isEqualTo(BYTE_TEST);
        assertThat(destMsg.getAsShort(KEY_THREE)).isEqualTo(BYTE_TEST);
        assertThat(destMsg.getAsNullableShort(KEY_FIVE)).isNull();
        assertThat(destMsg.getAsNullableShort(KEY_FOUR_HUNDRED)).isNull();
        assertThat(destMsg.getAsNullableShort(KEY_FOUR)).isEqualTo(Short.valueOf(SHORT_TEST));
        assertThat(destMsg.contains(KEY_ZERO)).isFalse();

        try {
            destMsg.getAsShort(KEY_NINETYNINE);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        assertThat(destMsg.getAsNullableShort(KEY_NINETYNINE)).isNull();
        try {
            destMsg.getAsShort(KEY_ZERO);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        try {
            destMsg.getAsShort(KEY_MINUS_ONE);
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
        srcMsg.set(KEY_MINUS_ONE, SHORT_TEST);
    }

    /**
     * testGetAsInt.
     * 
     */
    @Test
    public void testSetGetAsInt() {
        srcMsg.set(KEY_ONE, INT_TEST);
        srcMsg.set(KEY_TWO, SHORT_TEST);
        srcMsg.set(KEY_THREE, BYTE_TEST);
        srcMsg.set(KEY_FOUR, (short) BYTE_TEST);
        srcMsg.set(KEY_FIVE, (int) SHORT_TEST);
        srcMsg.set(KEY_SIX, Integer.valueOf(INT_TEST));
        srcMsg.set(KEY_SEVEN, (Integer) null);
        srcMsg.set(KEY_NINETY, INT_TEST);
        srcMsg.set(KEY_THREE_HUNDRED, INT_TEST);
        srcMsg.set(KEY_FOUR_HUNDRED, (Integer) null);
        final byte[] bytes = this.bytesMsgSerializer.serializeToBytes(srcMsg);

        this.bytesMsgSerializer.deserializeFromBytes(bytes, 0, bytes.length, destMsg);

        assertThat(destMsg.getAsInt(KEY_ONE)).isEqualTo(INT_TEST);
        assertThat(destMsg.getAsNullableInteger(KEY_ONE)).isEqualTo(INT_TEST);
        assertThat(destMsg.getAsInt(KEY_TWO)).isEqualTo(SHORT_TEST);
        assertThat(destMsg.getAsInt(KEY_THREE)).isEqualTo(BYTE_TEST);
        assertThat(destMsg.getAsInt(KEY_FOUR)).isEqualTo(BYTE_TEST);
        assertThat(destMsg.getAsInt(KEY_FIVE)).isEqualTo(SHORT_TEST);
        assertThat(destMsg.getAsInt(KEY_SIX)).isEqualTo(Integer.valueOf(INT_TEST));
        assertThat(destMsg.getAsNullableInteger(KEY_SEVEN)).isNull();
        assertThat(destMsg.getAsInt(KEY_NINETY)).isEqualTo(INT_TEST);
        assertThat(destMsg.getAsInt(KEY_THREE_HUNDRED)).isEqualTo(INT_TEST);
        assertThat(destMsg.getAsNullableInteger(KEY_FOUR_HUNDRED)).isNull();
        assertThat(destMsg.contains(KEY_ZERO)).isFalse();

        try {
            destMsg.getAsInt(KEY_NINETYNINE);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        assertThat(destMsg.getAsNullableInteger(KEY_NINETYNINE)).isNull();
        try {
            destMsg.getAsInt(KEY_ZERO);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        try {
            destMsg.getAsInt(KEY_MINUS_ONE);
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
        srcMsg.set(KEY_MINUS_ONE, INT_TEST);
    }

    /**
     * testSetGetAsLong.
     * 
     */
    @Test
    public void testSetGetAsLong() {
        srcMsg.set(KEY_ONE, LONG_TEST);
        srcMsg.set(KEY_TWO, INT_TEST);
        srcMsg.set(KEY_THREE, SHORT_TEST);
        srcMsg.set(KEY_FOUR, BYTE_TEST);
        srcMsg.set(KEY_FIVE, (long) BYTE_TEST);
        srcMsg.set(KEY_SIX, (long) SHORT_TEST);
        srcMsg.set(KEY_SEVEN, (long) INT_TEST);
        srcMsg.set(KEY_EIGHT, Long.valueOf(LONG_TEST));
        srcMsg.set(KEY_NINE, (Long) null);
        srcMsg.set(KEY_NINETY, LONG_TEST);
        srcMsg.set(KEY_THREE_HUNDRED, LONG_TEST);
        srcMsg.set(KEY_FOUR_HUNDRED, (Long) null);

        final byte[] bytes = this.bytesMsgSerializer.serializeToBytes(srcMsg);

        this.bytesMsgSerializer.deserializeFromBytes(bytes, 0, bytes.length, destMsg);
        assertThat(destMsg.getAsLong(KEY_ONE)).isEqualTo(LONG_TEST);
        assertThat(destMsg.getAsNullableLong(KEY_ONE)).isEqualTo(LONG_TEST);
        assertThat(destMsg.getAsLong(KEY_TWO)).isEqualTo(INT_TEST);
        assertThat(destMsg.getAsLong(KEY_THREE)).isEqualTo(SHORT_TEST);
        assertThat(destMsg.getAsLong(KEY_FOUR)).isEqualTo(BYTE_TEST);
        assertThat(destMsg.getAsLong(KEY_FIVE)).isEqualTo(BYTE_TEST);
        assertThat(destMsg.getAsLong(KEY_SIX)).isEqualTo(SHORT_TEST);
        assertThat(destMsg.getAsLong(KEY_SEVEN)).isEqualTo(INT_TEST);
        assertThat(destMsg.getAsLong(KEY_SEVEN)).isEqualTo(INT_TEST);
        assertThat(destMsg.getAsLong(KEY_EIGHT)).isEqualTo(Long.valueOf(LONG_TEST));
        assertThat(destMsg.getAsNullableLong(KEY_NINE)).isNull();
        assertThat(destMsg.getAsLong(KEY_THREE_HUNDRED)).isEqualTo(LONG_TEST);
        // assertThat(destMsg.getAsNullableLong(KEY_FOUR_HUNDRED)).isNull();
        assertThat(destMsg.contains(KEY_ZERO)).isFalse();

        try {
            destMsg.getAsLong(KEY_NINETYNINE);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        assertThat(destMsg.getAsNullableLong(KEY_NINETYNINE)).isNull();
        try {
            destMsg.getAsLong(KEY_ZERO);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        try {
            destMsg.getAsLong(KEY_MINUS_ONE);
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
        srcMsg.set(KEY_MINUS_ONE, LONG_TEST);
    }

    /**
     * testSetGetAsFloat.
     * 
     */
    @Test
    public void testSetGetAsFloat() {
        srcMsg.set(KEY_ONE, FLOAT_TEST);
        srcMsg.set(KEY_TWO, Float.valueOf(FLOAT_TEST));
        srcMsg.set(KEY_THREE, (Float) null);
        srcMsg.set(KEY_NINETY, FLOAT_TEST);
        srcMsg.set(KEY_THREE_HUNDRED, FLOAT_TEST);
        srcMsg.set(KEY_FOUR_HUNDRED, (Float) null);
        final byte[] bytes = this.bytesMsgSerializer.serializeToBytes(srcMsg);

        this.bytesMsgSerializer.deserializeFromBytes(bytes, 0, bytes.length, destMsg);

        assertThat(destMsg.getAsFloat(KEY_ONE)).isEqualTo(FLOAT_TEST);
        assertThat(destMsg.getAsNullableFloat(KEY_ONE)).isEqualTo(FLOAT_TEST);
        assertThat(destMsg.getAsFloat(KEY_ONE)).isEqualTo(FLOAT_TEST);
        assertThat(destMsg.getAsNullableFloat(KEY_TWO)).isEqualTo(Float.valueOf(FLOAT_TEST));
        assertThat(destMsg.getAsNullableFloat(KEY_THREE)).isNull();
        assertThat(destMsg.getAsDouble(KEY_NINETY)).isEqualTo(FLOAT_TEST);
        assertThat(destMsg.getAsDouble(KEY_THREE_HUNDRED)).isEqualTo(FLOAT_TEST);
        assertThat(destMsg.getAsNullableFloat(KEY_FOUR_HUNDRED)).isNull();
        assertThat(destMsg.contains(KEY_ZERO)).isFalse();

        try {
            destMsg.getAsFloat(KEY_NINETYNINE);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        assertThat(destMsg.getAsNullableFloat(KEY_NINETYNINE)).isNull();
        try {
            destMsg.getAsFloat(KEY_ZERO);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        try {
            destMsg.getAsFloat(KEY_MINUS_ONE);
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
        srcMsg.set(KEY_MINUS_ONE, FLOAT_TEST);
    }

    /**
     * testGetAsDouble.
     * 
     */
    @Test
    public void testSetGetAsDouble() {
        srcMsg.set(KEY_ONE, FLOAT_TEST);
        srcMsg.set(KEY_TWO, DOUBLE_TEST);
        srcMsg.set(KEY_THREE, DOUBLE_TEST, 5);
        srcMsg.set(KEY_FOUR, (double) FLOAT_TEST);
        srcMsg.set(KEY_FIVE, DOUBLE_TEST2, 5);
        srcMsg.set(KEY_SIX, Double.valueOf(DOUBLE_TEST2));
        srcMsg.set(KEY_SEVEN, (Double) null);

        for (int i = 0; i < 9; i++) {
            srcMsg.set(KEY_TEN + i, DOUBLE_TEST1, i);
        }

        srcMsg.set(KEY_NINETY, DOUBLE_TEST);
        srcMsg.set(KEY_NINETYTWO, DOUBLE_TEST, 5);
        srcMsg.set(KEY_THREE_HUNDRED, DOUBLE_TEST);
        srcMsg.set(KEY_FOUR_HUNDRED, (Double) null);

        final byte[] bytes = this.bytesMsgSerializer.serializeToBytes(srcMsg);

        this.bytesMsgSerializer.deserializeFromBytes(bytes, 0, bytes.length, destMsg);

        assertThat(destMsg.getAsFloat(KEY_ONE)).isEqualTo(FLOAT_TEST);
        assertThat(destMsg.getAsDouble(KEY_TWO)).isEqualTo(DOUBLE_TEST);
        // assertThat(msg2.getAsDouble(KEY_THREE)).isEqualTo(DOUBLE_TEST);
        assertThat(destMsg.getAsDouble(KEY_FOUR)).isEqualTo(FLOAT_TEST);
        assertThat(destMsg.getAsDouble(KEY_FIVE)).isEqualTo(DOUBLE_TEST2);
        assertThat(destMsg.getAsNullableDouble(KEY_FIVE)).isEqualTo(DOUBLE_TEST2);
        assertThat(destMsg.getAsNullableDouble(KEY_SIX)).isEqualTo(Double.valueOf(DOUBLE_TEST2));
        assertThat(destMsg.getAsNullableDouble(KEY_SEVEN)).isNull();

        for (int i = 0; i < 9; i++) {
            assertThat(destMsg.getAsDouble(KEY_TEN + i)).isEqualTo(DOUBLE_TEST1);
            assertThat(destMsg.getAsNullableDouble(KEY_TEN + i)).isEqualTo(DOUBLE_TEST1);
            // Assert.assertEquals(DOUBLE_TEST1, msg2.getAsDouble(KEY_TEN + i), (i == 0) ? 1 : (1 / (10 * i)));
        }

        assertThat(destMsg.getAsNullableDouble(KEY_FOUR_HUNDRED)).isNull();
        assertThat(destMsg.contains(KEY_ZERO)).isFalse();

        try {
            destMsg.getAsDouble(KEY_NINETYNINE);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        assertThat(destMsg.getAsNullableDouble(KEY_NINETYNINE)).isNull();
        try {
            destMsg.getAsDouble(KEY_ZERO);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
        try {
            destMsg.getAsDouble(KEY_MINUS_ONE);
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
        srcMsg.set(KEY_MINUS_ONE, DOUBLE_TEST);
    }

    /**
     * testSetGetAsDoubleWithDigitWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetAsDoubleWithDigitWithKeyIllegalArgument() {
        srcMsg.set(KEY_MINUS_ONE, DOUBLE_TEST, 1);
    }

    /**
     * testSetGetAsDoubleWithDigitWithNbDigitIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetAsDoubleWithDigitWithNbDigitIllegalArgument() {
        srcMsg.set(KEY_ONE, DOUBLE_TEST, 9);
    }

    /**
     * testGetAsString.
     * 
     */
    @Test
    public void testSetGetAsString() {
        srcMsg.set(KEY_ONE, STRING_TEST);
        srcMsg.set(KEY_TWO, STRING_TEST32);
        srcMsg.set(KEY_THREE, STRING_TEST132);
        srcMsg.set(KEY_FOUR, (String) null);

        srcMsg.set(KEY_SEVEN, STRING_TEST);
        
        srcMsg.set(KEY_NINETY, STRING_TEST);
        srcMsg.set(KEY_THREE_HUNDRED, STRING_TEST);
        srcMsg.set(KEY_FOUR_HUNDRED, (String) null);

        final byte[] bytes = this.bytesMsgSerializer.serializeToBytes(srcMsg);
        this.bytesMsgSerializer.deserializeFromBytes(bytes, 0, bytes.length, destMsg);

        assertThat(destMsg.getAsString(KEY_ONE)).isEqualTo(STRING_TEST);
        assertThat(destMsg.getAsString(KEY_TWO)).isEqualTo(STRING_TEST32);
        assertThat(destMsg.getAsString(KEY_THREE)).isEqualTo(STRING_TEST132);
        assertThat(destMsg.getAsString(KEY_FOUR)).isNull();
        assertThat(destMsg.getAsString(KEY_SEVEN)).isEqualTo(STRING_TEST);
        assertThat(destMsg.getAsString(KEY_NINETY)).isEqualTo(STRING_TEST);
        assertThat(destMsg.getAsString(KEY_THREE_HUNDRED)).isEqualTo(STRING_TEST);
        assertThat(destMsg.getAsString(KEY_FOUR_HUNDRED)).isNull();
        assertThat(destMsg.contains(KEY_ZERO)).isFalse();
        assertThat(destMsg.getAsString(KEY_NINETYNINE)).isNull();
        assertThat(destMsg.getAsString(KEY_ZERO)).isNull();
        assertThat(destMsg.getAsString(KEY_MINUS_ONE)).isNull();
    }
    
    @Test
    public void testSetGetAsUTF16String() {
        srcMsg.set(KEY_ONE, STRING_TEST);
        srcMsg.set(KEY_TWO, STRING_TEST32);
        srcMsg.set(KEY_THREE, STRING_TEST132);
        srcMsg.set(KEY_FOUR, (String) null);
        srcMsg.set(KEY_FIVE, STRING_TEST_UTF16);
        srcMsg.set(KEY_SIX, STRING_TEST_UTF16);
        srcMsg.set(KEY_SEVEN, STRING_TEST);
        
        srcMsg.set(KEY_NINETY, STRING_TEST);
        srcMsg.set(KEY_THREE_HUNDRED, STRING_TEST);
        srcMsg.set(KEY_FOUR_HUNDRED, (String) null);

        final byte[] bytes = this.bytesMsgSerializer.serializeToBytes(srcMsg);
        this.bytesMsgSerializer.deserializeFromBytes(bytes, 0, bytes.length, destMsg);

        assertThat(destMsg.getAsString(KEY_ONE)).isEqualTo(STRING_TEST);
        assertThat(destMsg.getAsString(KEY_TWO)).isEqualTo(STRING_TEST32);
        assertThat(destMsg.getAsString(KEY_THREE)).isEqualTo(STRING_TEST132);
        assertThat(destMsg.getAsString(KEY_FOUR)).isNull();
        assertThat(destMsg.getAsString(KEY_FIVE)).isEqualTo(STRING_TEST_UTF16);
        assertThat(destMsg.getAsString(KEY_SIX)).isEqualTo(STRING_TEST_UTF16);
        assertThat(destMsg.getAsString(KEY_SEVEN)).isEqualTo(STRING_TEST);
        assertThat(destMsg.getAsString(KEY_NINETY)).isEqualTo(STRING_TEST);
        assertThat(destMsg.getAsString(KEY_THREE_HUNDRED)).isEqualTo(STRING_TEST);
        assertThat(destMsg.getAsString(KEY_FOUR_HUNDRED)).isNull();
        assertThat(destMsg.contains(KEY_ZERO)).isFalse();
        assertThat(destMsg.getAsString(KEY_NINETYNINE)).isNull();
        assertThat(destMsg.getAsString(KEY_ZERO)).isNull();
        assertThat(destMsg.getAsString(KEY_MINUS_ONE)).isNull();

    }

    /**
     * testSetGetAsStringWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetAsStringWithIllegalArgument() {
        srcMsg.set(KEY_MINUS_ONE, STRING_TEST);
    }

    /**
     * testSetGetAsMsg.
     * 
     */
    @Test
    public void testSetGetAsMsg() {
        srcMsg.set(KEY_ONE, this.msgTest);
        srcMsg.set(KEY_TWO, BYTE_TEST);
        srcMsg.set(KEY_FOUR, (Msg) null);
        srcMsg.set(KEY_NINETY, this.msgTest);
        srcMsg.set(KEY_THREE_HUNDRED, this.msgTest);
        final byte[] bytes = this.bytesMsgSerializer.serializeToBytes(srcMsg);

        this.bytesMsgSerializer.deserializeFromBytes(bytes, 0, bytes.length, destMsg);

        assertThat(this.msgTest.getAsByte(KEY_ONE)).isEqualTo(destMsg.getAsMsg(KEY_ONE).getAsByte(KEY_ONE));
        assertThat(this.msgTest.getAsByte(KEY_ONE)).isEqualTo(destMsg.getAsMsg(KEY_NINETY).getAsByte(KEY_ONE));
        assertThat(this.msgTest.getAsByte(KEY_ONE)).isEqualTo(destMsg.getAsMsg(KEY_THREE_HUNDRED).getAsByte(KEY_ONE));
        assertThat(destMsg.getAsMsg(KEY_FOUR)).isNull();
        assertThat(destMsg.contains(KEY_ZERO)).isFalse();

        final Msg msg = newMsg();
        destMsg.getAsMsg(KEY_NINETY, msg);

        // assertThat(this.msgTest.getAsByte(KEY_ONE)).isEqualTo(msg.getAsMsg(KEY_ONE).getAsByte(KEY_ONE));
        // assertThat(this.msgTest.getAsByte(KEY_ONE)).isEqualTo(msg.getAsMsg(KEY_NINETY).getAsByte(KEY_ONE));
        // assertThat(this.msgTest.getAsByte(KEY_ONE)).isEqualTo(msg.getAsMsg(KEY_THREE_HUNDRED).getAsByte(KEY_ONE));

        assertThat(destMsg.getAsMsg(KEY_NINETYNINE)).isNull();
        assertThat(destMsg.getAsMsg(KEY_ZERO)).isNull();
        assertThat(destMsg.getAsMsg(KEY_MINUS_ONE)).isNull();

    }

    /**
     * testSetGetAsMsgWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetAsMsgWithIllegalArgument() {
        srcMsg.set(KEY_MINUS_ONE, this.msgTest);
    }

    /**
     * testSetGetAsBytes.
     * 
     */
    @Test
    public void testSetGetAsBooleans() {
        srcMsg.set(KEY_ONE, BOOLEANS_TEST);
        srcMsg.set(KEY_TWO, (boolean[]) null);
        srcMsg.set(KEY_THREE, (Boolean[]) null);
        srcMsg.set(KEY_FOUR, SHORTS_TEST);
        srcMsg.set(KEY_FIVE, BYTES_TEST);
        srcMsg.set(KEY_SIX, BYTES_TEST2);
        srcMsg.set(KEY_NINETY, BOOLEANS_TEST2);
        srcMsg.set(KEY_THREE_HUNDRED, BOOLEANS_TEST);
        

        final byte[] bytes = this.bytesMsgSerializer.serializeToBytes(srcMsg);
        this.bytesMsgSerializer.deserializeFromBytes(bytes, 0, bytes.length, destMsg);

        assertThat(destMsg.getAsBooleans(KEY_ONE)).isEqualTo(BOOLEANS_TEST);
        assertThat(destMsg.getAsNullableBooleans(KEY_ONE)).isEqualTo(BOOLEANS_TEST2);
        assertThat(destMsg.getAsNullableBooleans(KEY_NINETY)).isEqualTo(BOOLEANS_TEST2);
        assertThat(destMsg.getAsBooleans(KEY_ZERO)).isNull();
        assertThat(destMsg.getAsNullableBooleans(KEY_TWO)).isNull();
        assertThat(destMsg.getAsNullableBooleans(KEY_THREE)).isNull();
        assertThat(destMsg.getAsBooleans(KEY_FOUR)).isNull();
        assertThat(destMsg.getAsBooleans(KEY_FIVE)).isNull();
        assertThat(destMsg.getAsNullableBooleans(KEY_FIVE)).isNull();
        assertThat(destMsg.getAsNullableBooleans(KEY_SIX)).isNull();
        assertThat(destMsg.getAsBooleans(KEY_NINETYNINE)).isNull();
        assertThat(destMsg.getAsNullableBooleans(KEY_NINETYNINE)).isNull();
        assertThat(destMsg.getAsBooleans(KEY_THREE_HUNDRED)).isEqualTo(BOOLEANS_TEST);
        assertThat(destMsg.getAsBooleans(KEY_MINUS_ONE)).isNull();
        assertThat(destMsg.getAsNullableBooleans(KEY_FOUR_HUNDRED)).isNull();
    }

    /**
     * testSetGetAsBytes.
     * 
     */
    @Test
    public void testSetGetAsBytes() {
        srcMsg.set(KEY_ONE, BYTES_TEST);
        srcMsg.set(KEY_TWO, (byte[]) null);
        srcMsg.set(KEY_THREE, (Byte[]) null);
        srcMsg.set(KEY_FOUR, SHORTS_TEST);
        srcMsg.set(KEY_NINETY, BYTES_TEST2);
        srcMsg.set(KEY_THREE_HUNDRED, BYTES_TEST);

        final byte[] bytes = this.bytesMsgSerializer.serializeToBytes(srcMsg);
        this.bytesMsgSerializer.deserializeFromBytes(bytes, 0, bytes.length, destMsg);

        assertThat(destMsg.getAsBytes(KEY_ONE)).isEqualTo(BYTES_TEST);
        assertThat(destMsg.getAsNullableBytes(KEY_ONE)).isEqualTo(BYTES_TEST2);
        assertThat(destMsg.getAsNullableBytes(KEY_NINETY)).isEqualTo(BYTES_TEST2);
        assertThat(destMsg.getAsBytes(KEY_ZERO)).isNull();
        assertThat(destMsg.getAsNullableBytes(KEY_TWO)).isNull();
        assertThat(destMsg.getAsNullableBytes(KEY_THREE)).isNull();
        assertThat(destMsg.getAsBytes(KEY_FOUR)).isNull();
        assertThat(destMsg.getAsBytes(KEY_NINETYNINE)).isNull();
        assertThat(destMsg.getAsNullableBytes(KEY_NINETYNINE)).isNull();
        assertThat(destMsg.getAsBytes(KEY_THREE_HUNDRED)).isEqualTo(BYTES_TEST);
        assertThat(destMsg.getAsBytes(KEY_MINUS_ONE)).isNull();
        assertThat(destMsg.getAsNullableBytes(KEY_FOUR_HUNDRED)).isNull();
    }

    /**
     * testSetGetAsShorts.
     * 
     */
    @Test
    public void testSetGetAsShorts() {
        srcMsg.set(KEY_ONE, SHORTS_TEST);
        srcMsg.set(KEY_TWO, (short[]) null);
        srcMsg.set(KEY_THREE, (Short[]) null);
        srcMsg.set(KEY_FOUR, DOUBLES_TEST);
        srcMsg.set(KEY_FIVE, SHORTS_TEST2);
        srcMsg.set(KEY_NINETY, SHORTS_TEST2);
        srcMsg.set(KEY_THREE_HUNDRED, SHORTS_TEST);

        final byte[] bytes = this.bytesMsgSerializer.serializeToBytes(srcMsg);
        this.bytesMsgSerializer.deserializeFromBytes(bytes, 0, bytes.length, destMsg);

        assertThat(destMsg.getAsShorts(KEY_ONE)).isEqualTo(SHORTS_TEST);
        assertThat(destMsg.getAsNullableShorts(KEY_ONE)).isEqualTo(SHORTS_TEST2);
        assertThat(destMsg.getAsNullableShorts(KEY_NINETY)).isEqualTo(SHORTS_TEST2);
        assertThat(destMsg.getAsShorts(KEY_ZERO)).isNull();
        assertThat(destMsg.getAsNullableShorts(KEY_TWO)).isNull();
        assertThat(destMsg.getAsNullableShorts(KEY_THREE)).isNull();
        assertThat(destMsg.getAsShorts(KEY_FOUR)).isNull();
        assertThat(destMsg.getAsShorts(KEY_NINETYNINE)).isNull();
        assertThat(destMsg.getAsNullableShorts(KEY_NINETYNINE)).isNull();
        assertThat(destMsg.getAsShorts(KEY_THREE_HUNDRED)).isEqualTo(SHORTS_TEST);
        assertThat(destMsg.getAsShorts(KEY_MINUS_ONE)).isNull();
        assertThat(destMsg.getAsNullableShorts(KEY_FOUR_HUNDRED)).isNull();
    }

    /**
     * testSetGetAsLongs.
     * 
     */
    @Test
    public void testSetGetAsInts() {
        srcMsg.set(KEY_ONE, INTS_TEST);
        srcMsg.set(KEY_TWO, (int[]) null);
        srcMsg.set(KEY_THREE, (Integer[]) null);
        srcMsg.set(KEY_FOUR, SHORTS_TEST);
        srcMsg.set(KEY_FIVE, INTS_TEST2);
        srcMsg.set(KEY_NINETY, INTS_TEST2);
        srcMsg.set(KEY_THREE_HUNDRED, INTS_TEST);

        final byte[] bytes = this.bytesMsgSerializer.serializeToBytes(srcMsg);
        this.bytesMsgSerializer.deserializeFromBytes(bytes, 0, bytes.length, destMsg);

        assertThat(destMsg.getAsInts(KEY_ONE)).isEqualTo(INTS_TEST);
        assertThat(destMsg.getAsNullableIntegers(KEY_ONE)).isEqualTo(INTS_TEST2);
        assertThat(destMsg.getAsNullableIntegers(KEY_NINETY)).isEqualTo(INTS_TEST2);
        assertThat(destMsg.getAsInts(KEY_ZERO)).isNull();
        assertThat(destMsg.getAsNullableIntegers(KEY_TWO)).isNull();
        assertThat(destMsg.getAsNullableIntegers(KEY_THREE)).isNull();
        assertThat(destMsg.getAsInts(KEY_FOUR)).isNull();
        assertThat(destMsg.getAsInts(KEY_NINETYNINE)).isNull();
        assertThat(destMsg.getAsNullableIntegers(KEY_NINETYNINE)).isNull();
        assertThat(destMsg.getAsInts(KEY_THREE_HUNDRED)).isEqualTo(INTS_TEST);
        assertThat(destMsg.getAsInts(KEY_MINUS_ONE)).isNull();
        assertThat(destMsg.getAsNullableIntegers(KEY_FOUR_HUNDRED)).isNull();
    }

    /**
     * testSetGetAsLongs.
     * 
     */
    @Test
    public void testSetGetAsLongs() {
        srcMsg.set(KEY_ONE, LONGS_TEST);
        srcMsg.set(KEY_TWO, (long[]) null);
        srcMsg.set(KEY_THREE, (Long[]) null);
        srcMsg.set(KEY_FOUR, SHORTS_TEST);
        srcMsg.set(KEY_FIVE, LONGS_TEST2);
        srcMsg.set(KEY_NINETY, LONGS_TEST2);
        srcMsg.set(KEY_THREE_HUNDRED, LONGS_TEST);

        final byte[] bytes = this.bytesMsgSerializer.serializeToBytes(srcMsg);
        this.bytesMsgSerializer.deserializeFromBytes(bytes, 0, bytes.length, destMsg);

        assertThat(destMsg.getAsLongs(KEY_ONE)).isEqualTo(LONGS_TEST);
        assertThat(destMsg.getAsNullableLongs(KEY_ONE)).isEqualTo(LONGS_TEST2);
        assertThat(destMsg.getAsNullableLongs(KEY_NINETY)).isEqualTo(LONGS_TEST2);
        assertThat(destMsg.getAsLongs(KEY_ZERO)).isNull();
        assertThat(destMsg.getAsNullableLongs(KEY_TWO)).isNull();
        assertThat(destMsg.getAsNullableLongs(KEY_THREE)).isNull();
        assertThat(destMsg.getAsLongs(KEY_FOUR)).isNull();
        assertThat(destMsg.getAsLongs(KEY_NINETYNINE)).isNull();
        assertThat(destMsg.getAsNullableLongs(KEY_NINETYNINE)).isNull();
        assertThat(destMsg.getAsLongs(KEY_THREE_HUNDRED)).isEqualTo(LONGS_TEST);
        assertThat(destMsg.getAsLongs(KEY_MINUS_ONE)).isNull();
        assertThat(destMsg.getAsNullableLongs(KEY_FOUR_HUNDRED)).isNull();
    }

    /**
     * testSetGetAsFloats.
     * 
     */
    @Test
    public void testSetGetAsFloats() {
        srcMsg.set(KEY_ONE, FLOATS_TEST);
        srcMsg.set(KEY_TWO, (float[]) null);
        srcMsg.set(KEY_THREE, (Float[]) null);
        srcMsg.set(KEY_FOUR, SHORTS_TEST);
        srcMsg.set(KEY_NINETY, FLOATS_TEST2);
        srcMsg.set(KEY_THREE_HUNDRED, FLOATS_TEST);

        final byte[] bytes = this.bytesMsgSerializer.serializeToBytes(srcMsg);
        this.bytesMsgSerializer.deserializeFromBytes(bytes, 0, bytes.length, destMsg);

        assertThat(destMsg.getAsFloats(KEY_ONE)).isEqualTo(FLOATS_TEST);
        assertThat(destMsg.getAsNullableFloats(KEY_ONE)).isEqualTo(FLOATS_TEST2);
        assertThat(destMsg.getAsNullableFloats(KEY_NINETY)).isEqualTo(FLOATS_TEST2);
        assertThat(destMsg.getAsFloats(KEY_ZERO)).isNull();
        assertThat(destMsg.getAsNullableFloats(KEY_TWO)).isNull();
        assertThat(destMsg.getAsNullableFloats(KEY_THREE)).isNull();
        assertThat(destMsg.getAsFloats(KEY_FOUR)).isNull();
        assertThat(destMsg.getAsFloats(KEY_NINETYNINE)).isNull();
        assertThat(destMsg.getAsNullableFloats(KEY_NINETYNINE)).isNull();
        assertThat(destMsg.getAsFloats(KEY_THREE_HUNDRED)).isEqualTo(FLOATS_TEST);
        assertThat(destMsg.getAsFloats(KEY_MINUS_ONE)).isNull();
        assertThat(destMsg.getAsNullableFloats(KEY_FOUR_HUNDRED)).isNull();
    }

    /**
     * testSetGetAsDoubles.
     * 
     */
    @Test
    public void testSetGetAsDoubles() {
        srcMsg.set(KEY_ONE, DOUBLES_TEST);
        srcMsg.set(KEY_TWO, (double[]) null);
        srcMsg.set(KEY_THREE, (Double[]) null);
        srcMsg.set(KEY_FOUR, SHORTS_TEST);
        srcMsg.set(KEY_NINETY, DOUBLES_TEST2);
        srcMsg.set(KEY_THREE_HUNDRED, DOUBLES_TEST);

        final byte[] bytes = this.bytesMsgSerializer.serializeToBytes(srcMsg);
        this.bytesMsgSerializer.deserializeFromBytes(bytes, 0, bytes.length, destMsg);

        assertThat(destMsg.getAsDoubles(KEY_ONE)).isEqualTo(DOUBLES_TEST);
        assertThat(destMsg.getAsNullableDoubles(KEY_ONE)).isEqualTo(DOUBLES_TEST2);
        assertThat(destMsg.getAsNullableDoubles(KEY_NINETY)).isEqualTo(DOUBLES_TEST2);
        assertThat(destMsg.getAsDoubles(KEY_ZERO)).isNull();
        assertThat(destMsg.getAsNullableDoubles(KEY_TWO)).isNull();
        assertThat(destMsg.getAsNullableDoubles(KEY_THREE)).isNull();
        assertThat(destMsg.getAsDoubles(KEY_FOUR)).isNull();
        assertThat(destMsg.getAsDoubles(KEY_NINETYNINE)).isNull();
        assertThat(destMsg.getAsNullableDoubles(KEY_NINETYNINE)).isNull();
        assertThat(destMsg.getAsDoubles(KEY_THREE_HUNDRED)).isEqualTo(DOUBLES_TEST);
        assertThat(destMsg.getAsDoubles(KEY_MINUS_ONE)).isNull();
        assertThat(destMsg.getAsNullableDoubles(KEY_FOUR_HUNDRED)).isNull();
    }

    /**
     * testSetGetAsStrings.
     * 
     */
    @Test
    public void testSetGetAsStrings() {
        srcMsg.set(KEY_ONE, STRINGS_TEST);
        srcMsg.set(KEY_TWO, BYTES_TEST2);
        srcMsg.set(KEY_NINETY, STRINGS_TEST);

        final byte[] bytes = this.bytesMsgSerializer.serializeToBytes(srcMsg);
        this.bytesMsgSerializer.deserializeFromBytes(bytes, 0, bytes.length, destMsg);

        assertThat(destMsg.getAsStrings(KEY_ONE)).isEqualTo(STRINGS_TEST);
        assertThat(destMsg.getAsStrings(KEY_NINETY)).isEqualTo(STRINGS_TEST);
        assertThat(destMsg.getAsStrings(KEY_NINETYNINE)).isNull();
        assertThat(destMsg.getAsStrings(KEY_ZERO)).isNull();
        assertThat(destMsg.getAsStrings(KEY_MINUS_ONE)).isNull();
        assertThat(destMsg.getAsStrings(KEY_TWO)).isNull();

    }

    /**
     * testSetGetAsObjectArrayWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGetAsObjectArrayWithIllegalArgument() {
        srcMsg.set(KEY_MINUS_ONE, OBJECT_ARRAY_TEST);
    }

    /**
     * testSetGet.
     * 
     */
    @Test
    public void testSetGet() {
        srcMsg.set(KEY_ONE, Long.valueOf(LONG_TEST));
        srcMsg.set(KEY_TWO, Integer.valueOf(INT_TEST));
        srcMsg.set(KEY_THREE, Short.valueOf(SHORT_TEST));
        srcMsg.set(KEY_FOUR, Byte.valueOf(BYTE_TEST));
        srcMsg.set(KEY_FIVE, Long.valueOf(BYTE_TEST));
        srcMsg.set(KEY_SIX, Long.valueOf(SHORT_TEST));
        srcMsg.set(KEY_SEVEN, Long.valueOf(INT_TEST));
        srcMsg.set(KEY_NINETY, LONG_TEST);
        srcMsg.set(KEY_THREE_HUNDRED, LONG_TEST);

        final byte[] bytes = this.bytesMsgSerializer.serializeToBytes(srcMsg);

        this.bytesMsgSerializer.deserializeFromBytes(bytes, 0, bytes.length, destMsg);

        assertThat(destMsg.get(KEY_ONE)).isEqualTo(LONG_TEST);
        assertThat(destMsg.get(KEY_TWO)).isEqualTo(INT_TEST);
        assertThat(destMsg.get(KEY_THREE)).isEqualTo(SHORT_TEST);
        assertThat(destMsg.get(KEY_FOUR)).isEqualTo(BYTE_TEST);

        assertThat(destMsg.get(KEY_FIVE)).isEqualTo(BYTE_TEST);
        assertThat(destMsg.get(KEY_SIX)).isEqualTo(SHORT_TEST);
        assertThat(destMsg.get(KEY_SEVEN)).isEqualTo(INT_TEST);
        assertThat(destMsg.get(KEY_NINETY)).isEqualTo(LONG_TEST);
        assertThat(destMsg.get(KEY_THREE_HUNDRED)).isEqualTo(LONG_TEST);
        assertThat(destMsg.contains(KEY_ZERO)).isFalse();

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
        srcMsg.set(KEY_ONE, INT_TEST);
        assertThat(srcMsg.contains(KEY_ONE)).isTrue();
        srcMsg.remove(KEY_ONE);
        assertThat(srcMsg.contains(KEY_ONE)).isFalse();
        srcMsg.remove(KEY_NINETY);
        assertThat(srcMsg.contains(KEY_ONE)).isFalse();

    }

    /**
     * testRemoveWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveWithIllegalArgument() {
        srcMsg.remove(KEY_MINUS_ONE);
    }

    /**
     * testIsEmpty.
     * 
     */
    @Test
    public void testIsEmpty() {
        assertThat(srcMsg.isEmpty()).isTrue();
        srcMsg.set(KEY_ONE, true);
        assertThat(srcMsg.isEmpty()).isFalse();
        srcMsg.remove(KEY_ONE);
        assertThat(srcMsg.isEmpty()).isTrue();
    }

    /**
     * testGetType.
     * 
     */
    @Test
    public void testGetType() {
        assertThat(srcMsg.getType(KEY_NINETY)).isEqualTo(Type.NULL);
        assertThat(srcMsg.getTypeAsByte(KEY_THREE_HUNDRED)).isEqualTo(Type.NULL.getId());
        srcMsg.set(KEY_ONE, 1);
        assertThat(srcMsg.getType(KEY_ONE)).isEqualTo(Type.INTEGER);
        assertThat(srcMsg.getTypeAsByte(KEY_ONE)).isEqualTo(Type.INTEGER.getId());
    }

    /**
     * testIsArray.
     * 
     */
    @Test
    public void testIsArray() {
        assertThat(srcMsg.isArray(KEY_ZERO)).isEqualTo(false);
        assertThat(srcMsg.isArray(KEY_NINETY)).isEqualTo(false);
        final byte[] bytes = new byte[1];
        srcMsg.set(KEY_ONE, bytes);
        assertThat(srcMsg.isArray(KEY_ONE)).isEqualTo(true);
    }

    /**
     * testGetArrayLength.
     * 
     */
    @Test
    public void testGetArrayLength() {
        assertThat(srcMsg.getArrayLength(KEY_ZERO)).isEqualTo(0);
        assertThat(srcMsg.getArrayLength(KEY_NINETY)).isEqualTo(0);
        int size = 2;

        final Byte[] bytes1 = new Byte[size];
        srcMsg.set(KEY_ZERO, bytes1);
        assertThat(srcMsg.isArray(KEY_ZERO)).isEqualTo(true);
        assertThat(srcMsg.getArrayLength(KEY_ZERO)).isEqualTo(size);

        final boolean[] booleans = new boolean[size];
        srcMsg.set(KEY_ONE, booleans);
        assertThat(srcMsg.isArray(KEY_ONE)).isEqualTo(true);
        assertThat(srcMsg.getArrayLength(KEY_ONE)).isEqualTo(size);

        final byte[] bytes = new byte[size];
        srcMsg.set(KEY_TWO, bytes);
        assertThat(srcMsg.isArray(KEY_TWO)).isEqualTo(true);
        assertThat(srcMsg.getArrayLength(KEY_TWO)).isEqualTo(size);

        final short[] shorts = new short[size];
        srcMsg.set(KEY_THREE, shorts);
        assertThat(srcMsg.isArray(KEY_THREE)).isEqualTo(true);
        assertThat(srcMsg.getArrayLength(KEY_THREE)).isEqualTo(size);

        final int[] ints = new int[size];
        srcMsg.set(KEY_FOUR, ints);
        assertThat(srcMsg.isArray(KEY_FOUR)).isEqualTo(true);
        assertThat(srcMsg.getArrayLength(KEY_FOUR)).isEqualTo(size);

        final long[] longs = new long[size];
        srcMsg.set(KEY_FIVE, longs);
        assertThat(srcMsg.isArray(KEY_FIVE)).isEqualTo(true);
        assertThat(srcMsg.getArrayLength(KEY_FIVE)).isEqualTo(size);

        final float[] floats = new float[size];
        srcMsg.set(KEY_SIX, floats);
        assertThat(srcMsg.isArray(KEY_SIX)).isEqualTo(true);
        assertThat(srcMsg.getArrayLength(KEY_SIX)).isEqualTo(size);

        final double[] doubles = new double[size];
        srcMsg.set(KEY_SEVEN, doubles);
        assertThat(srcMsg.isArray(KEY_SEVEN)).isEqualTo(true);
        assertThat(srcMsg.getArrayLength(KEY_SEVEN)).isEqualTo(size);

    }

    /**
     * testReadFrom.
     * 
     */
    @Test
    public void testSerializeToBytesDeserializeFromBytes() {
        srcMsg.set(KEY_ONE, BYTE_TEST);
        srcMsg.set(KEY_TWO, SHORT_TEST);
        srcMsg.set(KEY_FOUR, INT_TEST);
        srcMsg.set(KEY_EIGHT, LONG_TEST);
        srcMsg.set(KEY_TEN, FLOAT_TEST);
        srcMsg.set(KEY_ELEVEN, DOUBLE_TEST);
        srcMsg.set(KEY_TWELVE, DOUBLE_TEST, 2);
        srcMsg.set(KEY_THIRTEEN, STRING_TEST);
        final byte[] bytes = (srcMsg instanceof BytesSerializable) ? ((BytesSerializable) srcMsg).serializeToBytes() : this.bytesMsgSerializer
                .serializeToBytes(srcMsg);

        if (destMsg instanceof BytesSerializable) {
            ((BytesSerializable) destMsg).deserializeFromBytes(bytes, 0, bytes.length);
        } else {
            this.bytesMsgSerializer.deserializeFromBytes(bytes, 0, bytes.length, destMsg);
        }

        assertThat(destMsg.getAsByte(KEY_ONE)).isEqualTo(BYTE_TEST);
        assertThat(destMsg.getAsShort(KEY_TWO)).isEqualTo(SHORT_TEST);
        assertThat(destMsg.getAsInt(KEY_FOUR)).isEqualTo(INT_TEST);
        assertThat(destMsg.getAsLong(KEY_EIGHT)).isEqualTo(LONG_TEST);
        assertThat(destMsg.getAsFloat(KEY_TEN)).isEqualTo(FLOAT_TEST);
        assertThat(destMsg.getAsDouble(KEY_ELEVEN)).isEqualTo(DOUBLE_TEST);
        assertThat(destMsg.getAsDouble(KEY_TWELVE)).isEqualTo(DOUBLE_TEST);
        assertThat(destMsg.getAsString(KEY_THIRTEEN)).isEqualTo(STRING_TEST);
    }

    /**
     * testSerializeToByteBufferDeserializeFromByteBuffer.
     * 
     */
    @Test
    public void testSerializeToByteBufferDeserializeFromByteBuffer() {
        srcMsg.set(KEY_ONE, BYTE_TEST);
        srcMsg.set(KEY_TWO, SHORT_TEST);
        srcMsg.set(KEY_FOUR, INT_TEST);
        srcMsg.set(KEY_EIGHT, LONG_TEST);
        srcMsg.set(KEY_TEN, FLOAT_TEST);
        srcMsg.set(KEY_ELEVEN, DOUBLE_TEST);
        srcMsg.set(KEY_TWELVE, DOUBLE_TEST, 2);
        srcMsg.set(KEY_THIRTEEN, STRING_TEST);

        final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        if (srcMsg instanceof ByteBufferSerializable) {
            ((ByteBufferSerializable) srcMsg).serializeToByteBuffer(byteBuffer);
        } else {
            this.byteBufferSerializer.serializeToByteBuffer(srcMsg, byteBuffer);
        }
        byteBuffer.flip();

        if (destMsg instanceof ByteBufferSerializable) {
            ((ByteBufferSerializable) destMsg).deserializeFromByteBuffer(byteBuffer);
        } else {
            this.byteBufferSerializer.deserializeFromByteBuffer(byteBuffer, destMsg);
        }

        assertThat(destMsg.getAsByte(KEY_ONE)).isEqualTo(BYTE_TEST);
        assertThat(destMsg.getAsShort(KEY_TWO)).isEqualTo(SHORT_TEST);
        assertThat(destMsg.getAsInt(KEY_FOUR)).isEqualTo(INT_TEST);
        assertThat(destMsg.getAsLong(KEY_EIGHT)).isEqualTo(LONG_TEST);
        assertThat(destMsg.getAsFloat(KEY_TEN)).isEqualTo(FLOAT_TEST);
        assertThat(destMsg.getAsDouble(KEY_ELEVEN)).isEqualTo(DOUBLE_TEST);
        assertThat(destMsg.getAsDouble(KEY_TWELVE)).isEqualTo(DOUBLE_TEST);
        assertThat(destMsg.getAsString(KEY_THIRTEEN)).isEqualTo(STRING_TEST);
    }

    /**
     * testWriteToWithIllegalArgument.
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeToBytesWithIllegalArgument() {
        final Msg srcMsg = newMsg();
        srcMsg.set(KEY_ONE, BYTE_TEST);
        final byte[] bytes = new byte[0];
        this.bytesMsgSerializer.serializeToBytes(srcMsg, bytes, 0);
    }

    /**
     * testToString.
     * 
     */
    @Test
    public void testToString() {
        final Msg srcMsg = newMsg();
        srcMsg.set(KEY_ONE, BYTE_TEST);
        srcMsg.set(KEY_TWO, DOUBLE_TEST);
        srcMsg.set(KEY_THREE, (Integer) null);
        srcMsg.set(KEY_FOUR, STRING_TEST);
        srcMsg.set(KEY_FIVE, msgTest);
        srcMsg.set(KEY_SIX, LONGS_TEST2);
        srcMsg.set(KEY_SEVEN, DOUBLES_TEST);
        srcMsg.set(KEY_EIGHT, (String) null);
        srcMsg.set(KEY_NINE, (Msg) null);
        srcMsg.set(KEY_TEN, (Integer[]) null);
        srcMsg.set(KEY_ELEVEN, (float[]) null);
        srcMsg.set(KEY_TWELVE, STRING_TEST_UTF16);
        srcMsg.set(KEY_THIRTEEN, LONGS_TEST_EMPTY);

        final byte[] bytes = this.bytesMsgSerializer.serializeToBytes(srcMsg);
        this.bytesMsgSerializer.deserializeFromBytes(bytes, 0, bytes.length, destMsg);

        final Map<Integer, Object> map = new HashMap<>();
        map.put(KEY_ONE, BYTE_TEST);
        map.put(KEY_TWO, DOUBLE_TEST);
        map.put(KEY_THREE, null);
        map.put(KEY_FOUR, STRING_TEST);
        map.put(KEY_FIVE, ((IndexedPrimitivesObjectsMsg) msgTest).getAllAsMap());
        map.put(KEY_SIX, LONGS_TEST2);
        map.put(KEY_SEVEN, DOUBLES_TEST);
        map.put(KEY_EIGHT, null);
        map.put(KEY_NINE, null);
        map.put(KEY_TEN, null);
        map.put(KEY_ELEVEN, null);
        map.put(KEY_TWELVE, STRING_TEST_UTF16);
        map.put(KEY_THIRTEEN, LONGS_TEST_EMPTY);

        final Gson gson = new GsonBuilder().serializeNulls().create();
        final String jsonMap = gson.toJson(map);
        assertThat(destMsg.toString()).isEqualTo(jsonMap);

        final Msg srcMsg2 = newMsg();
        final String jsonMap2 = gson.toJson(new HashMap<>());
        assertThat(srcMsg2.toString()).isEqualTo(jsonMap2);

    }

    /**
     * testEqualsHashcode.
     * 
     */
    @Test
    public void testEqualsHashcode() {
        final Msg srcMsg = newMsg();
        srcMsg.set(KEY_ONE, BYTE_TEST);
        srcMsg.set(KEY_TWO, DOUBLE_TEST);

        final byte[] bytes = this.bytesMsgSerializer.serializeToBytes(srcMsg);
        this.bytesMsgSerializer.deserializeFromBytes(bytes, 0, bytes.length, destMsg);

        assertThat(srcMsg.hashCode()).isEqualTo(destMsg.hashCode());
        assertThat(srcMsg.equals(destMsg)).isTrue();

        srcMsg.set(KEY_TWO, BYTE_TEST);
        assertThat(srcMsg.hashCode()).isNotEqualTo(destMsg.hashCode());
        assertThat(srcMsg.equals(destMsg)).isFalse();

        srcMsg.set(KEY_THREE, BYTE_TEST);
        assertThat(srcMsg.hashCode()).isNotEqualTo(destMsg.hashCode());
        assertThat(srcMsg.equals(destMsg)).isFalse();

        assertThat(srcMsg.equals(new Integer(1))).isFalse();
        assertThat(srcMsg.equals(null)).isFalse();

    }

}
