package org.cloudbus.coudsim.examples.MaxMin;

import java.util.Comparator;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;

public class ArrayCloudletComparatorMinMin implements Comparator<Cloudlet>
{
    private static List<Cloudlet> array;

    public ArrayCloudletComparatorMinMin(List<Cloudlet> array2)
    {
    	this.array=array2;
    //	int i=0;
    //	for(Cloudlet f:array2){
    //		this.array.get(arg0) =f!=null?f:Float.NaN;
    //	}
        
    }

    public Integer[] createIndexArray()
    {
        Integer[] indexes = new Integer[array.size()];
        for (int i = 0; i < array.size(); i++)
        {
            indexes[i] = i; // Autoboxing
        }
        return indexes;
    }
public int compare(Cloudlet index1, Cloudlet index2)
    {
         // Autounbox from Integer to int to use as array indexes
	if(index2.getCloudletLength()<index1.getCloudletLength())
		return 1;
	if(index2.getCloudletLength()>index1.getCloudletLength())
		return -1;

	return 0;
   //     return array.get(index1.getCloudletId()).compareTo(array[index2]);
    }
}
