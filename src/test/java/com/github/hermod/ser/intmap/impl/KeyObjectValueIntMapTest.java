package com.github.hermod.ser.intmap.impl;

import com.github.hermod.ser.intmap.ReadWriteIntMapMsg;

/**
 * KeyObjectValueIntMapTest.
 * 
 * @author anavarro - Dec 8, 2012
 * 
 */
public final class KeyObjectValueIntMapTest
    extends AbstractIntMapTest
{
    
    /**
     * (non-Javadoc)
     * 
     * @see com.github.hermod.ser.intmap.impl.AbstractIntMapTest#create()
     */
    @Override
    public ReadWriteIntMapMsg create()
    {
        return new KeyObjectValueIntMap(16);
    }
    
    
    
    
}
