package com.github.hermod.ser.intmap.impl;

import com.github.hermod.ser.intmap.IntMapIterator;
import com.github.hermod.ser.intmap.IntMapValue;
import com.github.hermod.ser.intmap.ReadIntMapMsg;
import com.github.hermod.ser.intmap.ReadWriteIntMapMsg;



/**
 * NoIndexIntMapMsg.
 * 
 * @author anavarro - Nov 12, 2011
 * 
 */
public class NoIndexIntMapMsg implements ReadWriteIntMapMsg
{
    
    private byte[] bytes;
    private int limit;
    private int lastKeyPut;
    
    /**
     * Constructor.
     * 
     * @param size
     */
    public NoIndexIntMapMsg(final int size)
    {
	this.bytes = new byte[size];
	this.lastKeyPut = -1;
    }

    
    public final boolean contains(final int aKey)
    {
	// TODO Auto-generated method stub
	return false;
    }

    public final Object get(final int aKey)
    {
	// TODO Auto-generated method stub
	return null;
    }

    public final byte getAsByte(final int aKey)
    {
	// TODO Auto-generated method stub
	return 0;
    }

    public final int getAsInt(final int aKey)
    {
	// TODO Auto-generated method stub
	return 0;
    }

    public final short getAsShort(final int aKey)
    {
	// TODO Auto-generated method stub
	return 0;
    }

    public final long getAsLong(final int aKey)
    {
	// TODO Auto-generated method stub
	return 0;
    }

    public final float getAsFloat(final int aKey)
    {
	// TODO Auto-generated method stub
	return 0;
    }

    public final double getAsDouble(final int aKey)
    {
	// TODO Auto-generated method stub
	return 0;
    }

    public final String getAsString(final int aKey)
    {
	// TODO Auto-generated method stub
	return null;
    }

    public final IntMapValue getAsIntMapValue(final int aKey)
    {
	// TODO Auto-generated method stub
	return null;
    }

    public final ReadIntMapMsg getAsMap(final int aKey)
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

    public final byte[] writeTo()
    {
	// TODO Auto-generated method stub
	return null;
    }

    public final int writeTo(final byte[] bytes, final int offset)
    {
	// TODO Auto-generated method stub
	return 0;
    }

    public final void clear()
    {
	// TODO Auto-generated method stub
	
    }

    public final void set(final int aKey, final byte aByte)
    {
	// TODO Auto-generated method stub
	
    }

    public final void set(final int aKey, final short aShort)
    {
	// TODO Auto-generated method stub
	
    }

    public final void set(final int aKey, final int aInt)
    {
	// TODO Auto-generated method stub
	
    }

    public final void set(final int aKey, final long aLong)
    {
	// TODO Auto-generated method stub
	
    }

    public final void set(final int aKey, final float aFloat)
    {
	// TODO Auto-generated method stub
	
    }

    public final void set(final int aKey, final double aDouble)
    {
	// TODO Auto-generated method stub
	
    }

    public final void set(final int aKey, final double aDouble, final int nbDigit)
    {
	// TODO Auto-generated method stub
	
    }

    public final void set(final int aKey, final String aString)
    {
	// TODO Auto-generated method stub
	
    }

    public final void set(final int aKey, final IntMapValue aString)
    {
	// TODO Auto-generated method stub
	
    }

    public void set(int aKey, ReadIntMapMsg aIntMap)
    {
	// TODO Auto-generated method stub
	
    }

    public final void setAll(final ReadIntMapMsg aIntMap)
    {
	// TODO Auto-generated method stub
	
    }

    public final void remove(final int aKey)
    {
	// TODO Auto-generated method stub
	
    }
    
    
    
    
    public final void readFrom(final byte[] bytes, final int offset, final int length)
    {
	// TODO Auto-generated method stub
	
    }


}
