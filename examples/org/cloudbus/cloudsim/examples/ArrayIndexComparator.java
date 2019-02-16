package org.cloudbus.cloudsim.examples;

import java.util.Comparator;

public class ArrayIndexComparator implements Comparator<Integer>
{
    private static Float[] array;

    public ArrayIndexComparator(float[] array2)
    {
    	this.array=new Float[array2.length];
    	int i=0;
    	for(Float f:array2){
    		this.array[i++] =f!=null?f:Float.NaN;
    	}
        
    }

    public Integer[] createIndexArray()
    {
        Integer[] indexes = new Integer[array.length];
        for (int i = 0; i < array.length; i++)
        {
            indexes[i] = i; // Autoboxing
        }
        return indexes;
    }
public int compare(Integer index1, Integer index2)
    {
         // Autounbox from Integer to int to use as array indexes
        return array[index1].compareTo(array[index2]);
    }
}
