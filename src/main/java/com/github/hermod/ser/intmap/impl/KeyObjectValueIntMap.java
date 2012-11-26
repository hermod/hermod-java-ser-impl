package com.github.hermod.ser.intmap.impl;

import static com.github.hermod.ser.intmap.impl.IntMapConstants.DOZENS;
import static com.github.hermod.ser.intmap.impl.IntMapConstants.SIZE_ENCODED_IN_AN_INT;
import static com.github.hermod.ser.intmap.impl.IntMapConstants.SIZE_MASK;
import static com.github.hermod.ser.intmap.impl.IntMapConstants.TYPE_5BITS_DECIMAL;
import static com.github.hermod.ser.intmap.impl.IntMapConstants.TYPE_BYTE;
import static com.github.hermod.ser.intmap.impl.IntMapConstants.TYPE_DOUBLE;
import static com.github.hermod.ser.intmap.impl.IntMapConstants.TYPE_FLOAT;
import static com.github.hermod.ser.intmap.impl.IntMapConstants.TYPE_INT;
import static com.github.hermod.ser.intmap.impl.IntMapConstants.TYPE_LONG;
import static com.github.hermod.ser.intmap.impl.IntMapConstants.TYPE_MASK;
import static com.github.hermod.ser.intmap.impl.IntMapConstants.TYPE_NULL_KEY;
import static com.github.hermod.ser.intmap.impl.IntMapConstants.TYPE_SHORT;

import com.github.hermod.ser.intmap.IntMapIterator;
import com.github.hermod.ser.intmap.IntMapValue;
import com.github.hermod.ser.intmap.ReadIntMapMsg;
import com.github.hermod.ser.intmap.ReadWriteIntMapMsg;

/**
 * @author anavarro
 * 
 */
public class KeyObjectValueIntMap implements ReadWriteIntMapMsg
{
    
    private long[] primitiveValues;
    private byte[] types;
    
    /**
     * Constructor.
     * 
     * @param aDescriptor
     */
    public KeyObjectValueIntMap(final int aKeyMax)
    {
	this.primitiveValues = new long[aKeyMax + 1];
	this.types = new byte[aKeyMax + 1];
    }
    
    /**
     * @param aKeyMax
     */
    public final void clear(final int aKeyMax)
    {
	if (aKeyMax > this.primitiveValues.length)
	{
	    this.primitiveValues = new long[aKeyMax + 1];
	    this.types = new byte[aKeyMax + 1];
	}
	else
	{
	    for (int i = 0; i < this.primitiveValues.length; i++)
	    {
		this.primitiveValues[i] = 0;
		this.types[i] = 0;
	    }
	}
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.WriteIntMap#clear()
     */
    public final void clear()
    {
	for (int i = 0; i < this.primitiveValues.length; i++)
	{
	    this.primitiveValues[i] = 0;
	    this.types[i] = 0;
	}
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.ReadIntMap#contains(int)
     */
    public final boolean contains(final int aKey)
    {
	try
	{
	    return (this.types[aKey] != 0) ? true : false;
	}
	catch (final ArrayIndexOutOfBoundsException e)
	{
	    return false;
	}
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.ReadIntMap#get(int)
     */
    public final Object get(final int aKey)
    {
	if (contains(aKey))
	{
	    // TODO
	}
	return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.ReadIntMap#getAsByte(int)
     */
    public final byte getAsByte(final int aKey)
    {
	try
	{
	    return (this.types[aKey] == TYPE_BYTE) ? (byte) this.primitiveValues[aKey] : 0;
	}
	catch (final ArrayIndexOutOfBoundsException e)
	{
	    return 0;
	}
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.ReadIntMap#getAsShort(int)
     */
    public final short getAsShort(final int aKey)
    {
	try
	{
	    return (this.types[aKey] == TYPE_BYTE) ? (byte) this.primitiveValues[aKey]
		    : (this.types[aKey] == TYPE_SHORT) ? (short) this.primitiveValues[aKey] : 0;
	}
	catch (final ArrayIndexOutOfBoundsException e)
	{
	    return 0;
	}
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.ReadIntMap#getAsInt(int)
     */
    public final int getAsInt(final int aKey)
    {
	try
	{
	    return (this.types[aKey] == TYPE_BYTE) ? (byte) this.primitiveValues[aKey]
		    : (this.types[aKey] == TYPE_SHORT) ? (short) this.primitiveValues[aKey]
			    : (this.types[aKey] == TYPE_INT) ? (int) this.primitiveValues[aKey] : 0;
	}
	catch (final ArrayIndexOutOfBoundsException e)
	{
	    return 0;
	}
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.ReadIntMap#getAsLong(int)
     */
    public final long getAsLong(final int aKey)
    {
	try
	{
	    return (this.types[aKey] == TYPE_BYTE) ? (byte) this.primitiveValues[aKey]
		    : (this.types[aKey] == TYPE_SHORT) ? (short) this.primitiveValues[aKey]
			    : (this.types[aKey] == TYPE_INT) ? (int) this.primitiveValues[aKey] : 0;
	}
	catch (final ArrayIndexOutOfBoundsException e)
	{
	    return 0;
	}
    }
    
    /**
     * @param aKey
     * @return
     */
    public final long getAsInteger(final int aKey)
    {
	try
	{
	    return (this.types[aKey] == TYPE_BYTE) ? (byte) this.primitiveValues[aKey]
		    : (this.types[aKey] == TYPE_SHORT) ? (short) this.primitiveValues[aKey]
			    : (this.types[aKey] == TYPE_INT) ? (int) this.primitiveValues[aKey] : 0;
	}
	catch (final ArrayIndexOutOfBoundsException e)
	{
	    return 0;
	}
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.ReadIntMap#getAsFloat(int)
     */
    public final float getAsFloat(final int aKey)
    {
	try
	{
	    return (this.types[aKey] == TYPE_FLOAT) ? Float.intBitsToFloat((int) this.primitiveValues[aKey]) : 0;
	}
	catch (final ArrayIndexOutOfBoundsException e)
	{
	    return 0;
	}
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.ReadIntMap#getAsDouble(int)
     */
    public final double getAsDouble(final int aKey)
    {
	try
	{
	    return (this.types[aKey] == TYPE_DOUBLE) ? Double.longBitsToDouble(this.primitiveValues[aKey]) : 0;
	}
	catch (final ArrayIndexOutOfBoundsException e)
	{
	    return 0;
	}
    }
    
    /**
     * @param aKey
     * @return
     */
    public final double getAsDecimal(final int aKey)
    {
	try
	{
	    switch (this.types[aKey])
	    {
//		case TYPE_3BITS_DECIMAL:
//		    return (this.primitiveValues[aKey] >> 8) / DOZENS[(int) (0xFF & this.primitiveValues[aKey])];
		case TYPE_5BITS_DECIMAL:
		    return (this.primitiveValues[aKey] >> 8) / DOZENS[(int) (0xFF & this.primitiveValues[aKey])];
		case TYPE_DOUBLE:
		    return Double.longBitsToDouble(this.primitiveValues[aKey]);
		case TYPE_FLOAT:
		    return Float.floatToIntBits(this.primitiveValues[aKey]);
		default:
		    return 0;
	    }
	}
	catch (final ArrayIndexOutOfBoundsException e)
	{
	    return 0;
	}
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.ReadIntMap#getAsString(int)
     */
    public final String getAsString(final int aKey)
    {
	// TODO Auto-generated method stub
	return null;
    }
    
    public final IntMapValue getAsIntMapValue(int aKey)
    {
	// TODO Auto-generated method stub
	return null;
    }
    
    public final ReadIntMapMsg getAsMap(int aKey)
    {
	// TODO Auto-generated method stub
	return null;
    }
    
    public final int[] getKeys()
    {
	// TODO Auto-generated method stub
	return null;
    }
    
    public final IntMapIterator<ReadIntMapMsg> getIterator()
    {
	// TODO Auto-generated method stub
	return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.WriteIntMap#set(int, byte)
     */
    public final void set(final int aKey, final byte aByte)
    {
	try
	{
	    this.primitiveValues[aKey] = aByte;
	    this.types[aKey] = TYPE_BYTE;
	}
	catch (ArrayIndexOutOfBoundsException e)
	{
	    // TODO
	    throw new IllegalArgumentException("");
	}
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.WriteIntMap#set(int, short)
     */
    public final void set(final int aKey, final short aShort)
    {
	try
	{
	    this.primitiveValues[aKey] = aShort;
	    this.types[aKey] = TYPE_SHORT;
	}
	catch (ArrayIndexOutOfBoundsException e)
	{
	    // TODO
	    throw new IllegalArgumentException("");
	}
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.WriteIntMap#set(int, int)
     */
    public final void set(final int aKey, final int aInt)
    {
	try
	{
	    this.primitiveValues[aKey] = aInt;
	    this.types[aKey] = TYPE_INT;
	}
	catch (ArrayIndexOutOfBoundsException e)
	{
	    // TODO
	    throw new IllegalArgumentException("");
	}
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.WriteIntMap#set(int, long)
     */
    public final void set(final int aKey, final long aLong)
    {
	try
	{
	    this.primitiveValues[aKey] = aLong;
	    this.types[aKey] = TYPE_LONG;
	}
	catch (ArrayIndexOutOfBoundsException e)
	{
	    // TODO
	    throw new IllegalArgumentException("");
	}
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.WriteIntMap#set(int, float)
     */
    public final void set(final int aKey, final float aFloat)
    {
	try
	{
	    this.primitiveValues[aKey] = Float.floatToIntBits(aFloat);
	    this.types[aKey] = TYPE_FLOAT;
	}
	catch (ArrayIndexOutOfBoundsException e)
	{
	    // TODO
	    throw new IllegalArgumentException("");
	}
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.WriteIntMap#set(int, double)
     */
    public final void set(final int aKey, final double aDouble)
    {
	try
	{
	    this.primitiveValues[aKey] = Double.doubleToLongBits(aDouble);
	    this.types[aKey] = TYPE_DOUBLE;
	}
	catch (ArrayIndexOutOfBoundsException e)
	{
	    // TODO
	    throw new IllegalArgumentException("");
	}
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.WriteIntMap#set(int, double, int)
     */
    // TODO check nbDigit via enum or the
    public final void set(final int aKey, final double aDouble, final int nbDigit)
    {
	try
	{
	    final double d = (aDouble * DOZENS[nbDigit]) + 0.5;
//	    if (d >= Short.MIN_VALUE && d <= Short.MAX_VALUE)
//	    {
//		this.primitiveValues[aKey] = (nbDigit) | (((short) (d)) << 8);
//		this.types[aKey] = TYPE_3BITS_DECIMAL;
//	    }
//	    else
	    {
		if (d >= Integer.MIN_VALUE && d <= Integer.MAX_VALUE)
		{
		    this.primitiveValues[aKey] = (nbDigit) | (((int) (d)) << 8);
		    this.types[aKey] = TYPE_5BITS_DECIMAL;
		}
		else
		{
		    this.set(aKey, aDouble);
		}
	    }
	}
	catch (final ArrayIndexOutOfBoundsException e)
	{
	    // TODO
	    throw new IllegalArgumentException("");
	}
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.WriteIntMap#set(int, java.lang.String)
     */
    public final void set(final int aKey, final String aString)
    {
	// TODO Auto-generated method stub
	
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.WriteIntMap#set(int,
     * org.fame.z.intmap.IntMapValue)
     */
    public final void set(final int aKey, final IntMapValue aString)
    {
	// TODO Auto-generated method stub
	
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.WriteIntMap#set(int, org.fame.z.intmap.ReadIntMap)
     */
    public final void set(final int aKey, final ReadIntMapMsg aIntMap)
    {
	// TODO Auto-generated method stub
	
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.WriteIntMap#setAll(org.fame.z.intmap.ReadIntMap)
     */
    public final void setAll(final ReadIntMapMsg aIntMap)
    {
	// TODO Auto-generated method stub
	
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.intmap.WriteIntMap#remove(int)
     */
    public final void remove(final int aKey)
    {
	try
	{
	    this.types[aKey] = TYPE_NULL_KEY;
	}
	catch (ArrayIndexOutOfBoundsException e)
	{
	    // TODO
	    throw new IllegalArgumentException("");
	}
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.ReadMsg#readFrom(byte[], int, int)
     */
    public final void readFrom(final byte[] bytes, final int offset, final int length)
    {
	int pos = offset;
	int key = 0;
	
	// find max key
	// not need for feed
	// Calculate max key value
	while (pos < offset + length)
	{
	    if ((bytes[pos] & TYPE_MASK) == TYPE_NULL_KEY)
	    {
		final int size = bytes[pos++] & SIZE_MASK;
		key += (size != SIZE_ENCODED_IN_AN_INT) ? size : (bytes[pos++] & 0xFF) | ((bytes[pos++] & 0xFF) << 8)
			| ((bytes[pos++] & 0xFF) << 16) | ((bytes[pos] & 0xFF) << 24);
		
	    }
	    else
	    {
		final int size = bytes[pos++] & SIZE_MASK;
		pos += (size != SIZE_ENCODED_IN_AN_INT) ? size : (bytes[pos++] & 0xFF) | ((bytes[pos++] & 0xFF) << 8)
			| ((bytes[pos++] & 0xFF) << 16) | ((bytes[pos] & 0xFF) << 24);
		key++;
	    }
	}
	clear(key);
	
	// Decoding
	key = 0;
	pos = offset;
	while (pos < offset + length)
	{
	    final byte type = bytes[pos++];
	    
	    // Skip null key
	    if ((type & TYPE_MASK) == TYPE_NULL_KEY)
	    {
		final int size = type & SIZE_MASK;
		key += (size != SIZE_ENCODED_IN_AN_INT) ? size : (bytes[pos++] & 0xFF) | ((bytes[pos++] & 0xFF) << 8)
			| ((bytes[pos++] & 0xFF) << 16) | ((bytes[pos] & 0xFF) << 24);
	    }
	    // Decode values
	    else
	    {
		this.types[key] = type;
		switch (type)
		{
		    case TYPE_BYTE:
			this.primitiveValues[key] = ((bytes[pos++] & 0xFF));
			break;
		    
		    case TYPE_SHORT:
			this.primitiveValues[key] = ((bytes[pos++] & 0xFF)) | ((bytes[pos++] & 0xFF) << 8);
			break;
		    
		    case TYPE_INT:
			this.primitiveValues[key] = ((bytes[pos++] & 0xFF)) | ((bytes[pos++] & 0xFF) << 8)
				| ((bytes[pos++] & 0xFF) << 16) | ((bytes[pos++] & 0xFF) << 24);
			break;
		    
		    case TYPE_DOUBLE:
			this.primitiveValues[key] = (((long) bytes[pos++] & 0xFF) | (((long) bytes[pos++] & 0xFF) << 8)
				| (((long) bytes[pos++] & 0xFF) << 16) | (((long) bytes[pos++] & 0xFF) << 24)
				| (((long) bytes[pos++] & 0xFF) << 32) | (((long) bytes[pos++] & 0xFF) << 40)
				| (((long) bytes[pos++] & 0xFF) << 48) | (((long) bytes[pos++] & 0xFF) << 56));
			break;
		    
		    case TYPE_5BITS_DECIMAL:
			this.primitiveValues[key] = ((bytes[pos++] & 0xFF) << 8) | ((bytes[pos++] & 0xFF) << 16)
				| ((bytes[pos++] & 0xFF) << 24) | ((bytes[pos++] & 0xFF) << 32) | (bytes[pos++] & 0xFF);
			break;
		    
//		    case TYPE_3BITS_DECIMAL:
//			this.primitiveValues[key] = this.primitiveValues[key] = ((bytes[pos++] & 0xFF) << 8)
//				| ((bytes[pos++] & 0xFF) << 16) | (bytes[pos++] & 0xFF);
//			break;
		    
		    case TYPE_FLOAT:
			this.primitiveValues[key] = ((bytes[pos++] & 0xFF)) | ((bytes[pos++] & 0xFF) << 8)
				| ((bytes[pos++] & 0xFF) << 16) | ((bytes[pos++] & 0xFF) << 24);
			break;
		    
		    default:
			break;
		}
		key++;
	    }
	}
	
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.WriteMsg#writeTo()
     */
    public final byte[] writeTo()
    {
	
	return null;
	
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.fame.z.WriteMsg#writeTo(byte[], int)
     */
    public final int writeTo(final byte[] bytes, final int offset)
    {
	
	// Calculate size
	final int size = getBytesSize();
	if (bytes.length - offset <= size)
	{
	    throw new IllegalArgumentException("Bytes array too small from the offset");
	}
	
	int pos = offset;
	int consecutiveNullKey = 0;
	
	// Write Type / Values
	for (int i = 0; i < this.types.length; i++)
	{
	    if (this.types[i] != TYPE_NULL_KEY)
	    {
		if (consecutiveNullKey != 0)
		{
		    if (consecutiveNullKey < SIZE_ENCODED_IN_AN_INT)
		    {
			bytes[pos++] = (byte) consecutiveNullKey;
		    }
		    else
		    {
			bytes[pos++] = SIZE_ENCODED_IN_AN_INT;
			bytes[pos++] = (byte) (consecutiveNullKey);
			bytes[pos++] = (byte) (consecutiveNullKey >> 8);
			bytes[pos++] = (byte) (consecutiveNullKey >> 16);
			bytes[pos++] = (byte) (consecutiveNullKey >> 24);
		    }
		    consecutiveNullKey = 0;
		}
		
		final byte type = this.types[i];
		bytes[pos++] = type;
		switch (type)
		{
		    case TYPE_BYTE:
			bytes[pos++] = (byte) this.primitiveValues[i];
			break;
			
		    case TYPE_SHORT:
			final int aShort = (short) this.primitiveValues[i];
			bytes[pos++] = (byte) (aShort);
			bytes[pos++] = (byte) (aShort >> 8);
			break;
		    
		    case TYPE_INT:
			final int aInt = (int) this.primitiveValues[i];
			bytes[pos++] = (byte) (aInt);
			bytes[pos++] = (byte) (aInt >> 8);
			bytes[pos++] = (byte) (aInt >> 16);
			bytes[pos++] = (byte) (aInt >> 24);
			break;
		    
		    case TYPE_DOUBLE:
			final long longBits = this.primitiveValues[i];
			bytes[pos++] = (byte) (longBits);
			bytes[pos++] = (byte) (longBits >> 8);
			bytes[pos++] = (byte) (longBits >> 16);
			bytes[pos++] = (byte) (longBits >> 24);
			bytes[pos++] = (byte) (longBits >> 32);
			bytes[pos++] = (byte) (longBits >> 40);
			bytes[pos++] = (byte) (longBits >> 48);
			bytes[pos++] = (byte) (longBits >> 56);
			break;
		    
		    case TYPE_5BITS_DECIMAL:
			final int anInt = (int) (this.primitiveValues[i] >> 8);
			bytes[pos++] = (byte) (anInt);
			bytes[pos++] = (byte) (anInt >> 8);
			bytes[pos++] = (byte) (anInt >> 16);
			bytes[pos++] = (byte) (anInt >> 24);
			bytes[pos++] = (byte) (this.primitiveValues[i]);
			break;
		    
//    		    case TYPE_3BITS_DECIMAL:
//    			final short aShort1 = (short) (this.primitiveValues[i] >> 8);
//    			bytes[pos++] = (byte) (aShort1);
//    			bytes[pos++] = (byte) (aShort1 >> 8);
//    			bytes[pos++] = (byte) (this.primitiveValues[i]);
//    			
//    			break;
		    
		    case TYPE_FLOAT:
			final int intBits = Float.floatToIntBits((int) (this.primitiveValues[i]));
			bytes[pos++] = (byte) (intBits);
			bytes[pos++] = (byte) (intBits >> 8);
			bytes[pos++] = (byte) (intBits >> 16);
			bytes[pos++] = (byte) (intBits >> 24);
			break;
		    

		    
		    case TYPE_LONG:
			final long aLong = this.primitiveValues[i];
			bytes[pos++] = (byte) (aLong);
			bytes[pos++] = (byte) (aLong >> 8);
			bytes[pos++] = (byte) (aLong >> 16);
			bytes[pos++] = (byte) (aLong >> 24);
			bytes[pos++] = (byte) (aLong >> 32);
			bytes[pos++] = (byte) (aLong >> 40);
			bytes[pos++] = (byte) (aLong >> 48);
			bytes[pos++] = (byte) (aLong >> 56);
			break;
		    
		    default:
			break;
		}
	    }
	    else
	    {
		consecutiveNullKey++;
	    }
	}
	return pos;
	
    }
    
    /**
     * @return
     */
    private final int getBytesSize()
    {
	int size = 0;
	int consecutiveNullKey = 0;
	for (int i = 0; i < this.types.length; i++)
	{
	    if (this.types[i] != TYPE_NULL_KEY)
	    {
		size += (this.types[i] & SIZE_MASK) + 1;
		// TODO Manage SIZE in a BYTE on an INT
		
		if (consecutiveNullKey != 0)
		{
		    size += (consecutiveNullKey < SIZE_ENCODED_IN_AN_INT) ? 1 : 5;
		}
	    }
	    else
	    {
		consecutiveNullKey++;
	    }
	}
	return size;
	
    }
    
}