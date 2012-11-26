/**
 * 
 */
package com.github.hermod.ser.intmap.impl;


/**
 * @author anavarro
 * 
 */
public final class Demo
{
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
	int count = 0;
	long start = System.currentTimeMillis();
	long stop = 0;
	
	final int nbTestEncode = 5000000;
	final int nbTestDecode = 5000000;
	int size = 0;
	byte[] bytes = new byte[100];
	
	//
	KeyObjectValueIntMap intMap = new KeyObjectValueIntMap(44);
	intMap.clear();
	intMap.set(0, 1.1, 1);
	intMap.set(1, 1.1, 1);
	intMap.set(2, 2.2, 1);
	intMap.set(3, 3.3, 1);
	intMap.set(4, 14.141, 4);
	intMap.set(5, 15.151, 4);
	intMap.set(6, 16.161, 4);
	intMap.set(7, 16.161, 4);
	size = intMap.writeTo(bytes, 0);
	
	double d0 = intMap.getAsDecimal(0);
	double d1 = intMap.getAsDecimal(1);
	double d2 = intMap.getAsDecimal(2);
	double d3 = intMap.getAsDecimal(3);
	double d14 = intMap.getAsDecimal(4);
	double d15 = intMap.getAsDecimal(5);
	double d16 = intMap.getAsDecimal(6);
	double d17 = intMap.getAsDecimal(7);
	
	KeyObjectValueIntMap intMap2 = new KeyObjectValueIntMap(44);
	intMap2.readFrom(bytes, 0, size);
	
	double d0a = intMap2.getAsDecimal(0);
	double d1a = intMap2.getAsDecimal(1);
	double d2a = intMap2.getAsDecimal(2);
	double d3a = intMap2.getAsDecimal(3);
	double d14a = intMap2.getAsDecimal(4);
	double d15a = intMap2.getAsDecimal(5);
	double d16a = intMap2.getAsDecimal(6);
	double d17a = intMap2.getAsDecimal(7);
	
	
	System.out.println("d0=" + d0);
	System.out.println("size=" + size);
	
	count = 0;
	start = System.currentTimeMillis();
	for (int i = nbTestEncode; i-- != 0;)
	{
	    intMap.clear();
	    intMap.set(0, 1.1);
	    intMap.set(1, 1.1);
	    intMap.set(2, 2.2);
	    intMap.set(3, 3.3);
	    intMap.set(4, 14.14);
	    intMap.set(5, 15.15);
	    intMap.set(6, 16.16);
	    intMap.set(7, 16.16);
	    
	    count += intMap.writeTo(bytes, 0);
	}
	stop = System.currentTimeMillis();
	System.out.println(intMap.getClass().getSimpleName() + " size=" + intMap.writeTo(bytes, 0));
	System.out.println("time to encode " + intMap.getClass().getSimpleName() + " random " + nbTestEncode
		+ " count=" + count + " time=" + (stop - start) + " msg/s="
		+ (((nbTestEncode / (stop - start)) * 1000)));
	
	intMap2 = new KeyObjectValueIntMap(44);
	
	start = System.currentTimeMillis();
	for (int i = nbTestEncode; i-- != 0;)
	{
	    intMap2.readFrom(bytes, 0, size);
	    count += intMap2.getAsDouble(0) + intMap2.getAsDouble(1) + intMap2.getAsDouble(2) + intMap2.getAsDouble(3)
		    + intMap2.getAsDouble(4) + intMap2.getAsDouble(5) + intMap2.getAsDouble(6)
		    + +intMap2.getAsDouble(7);
	}
	
	stop = System.currentTimeMillis();
	System.out.println("time to decode " + intMap.getClass().getSimpleName() + " " + nbTestDecode + " count="
		+ count + " time=" + (stop - start) + " msg/s=" + (((nbTestEncode / (stop - start)) * 1000)));
	
	count = 0;
	start = System.currentTimeMillis();
	for (int i = nbTestEncode; i-- != 0;)
	{
	    intMap.clear();
	    intMap.set(0, 1.1, 1);
	    intMap.set(1, 1.1, 1);
	    intMap.set(2, 2.2, 1);
	    intMap.set(3, 3.3, 1);
	    intMap.set(4, 14.141, 4);
	    intMap.set(5, 15.151, 4);
	    intMap.set(6, 16.161, 4);
	    intMap.set(7, 16.161, 4);
	    
	    count += intMap.writeTo(bytes, 0);
	}
	stop = System.currentTimeMillis();
	System.out.println(intMap.getClass().getSimpleName() + " size=" + intMap.writeTo(bytes, 0));
	System.out.println("time to encode " + intMap.getClass().getSimpleName() + " random " + nbTestEncode
		+ " count=" + count + " time=" + (stop - start) + " msg/s="
		+ (((nbTestEncode / (stop - start)) * 1000)));
	
	intMap2 = new KeyObjectValueIntMap(44);
	
	start = System.currentTimeMillis();
	for (int i = nbTestEncode; i-- != 0;)
	{
	    intMap2.readFrom(bytes, 0, size);
	    count += intMap2.getAsDecimal(0) + intMap2.getAsDecimal(1) + intMap2.getAsDecimal(2)
		    + intMap2.getAsDecimal(3) + intMap2.getAsDecimal(4) + intMap2.getAsDecimal(5)
		    + intMap2.getAsDecimal(6) + intMap2.getAsDecimal(7);
	}
	
	stop = System.currentTimeMillis();
	System.out.println("time to decode " + intMap.getClass().getSimpleName() + " " + nbTestDecode + " count="
		+ count + " time=" + (stop - start) + " msg/s=" + (((nbTestEncode / (stop - start)) * 1000)));
	
	
	
    }
    
}
