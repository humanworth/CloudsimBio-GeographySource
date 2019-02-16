package org.cloudbus.coudsim.examples.MaxMin;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;

public class MaxMinScheduling {
    public static	List<Vm> Vms=new ArrayList<Vm>();
    public static List<Cloudlet> Cloudlets=new ArrayList<Cloudlet>();
    public static int ProblemDimension=100;
    public static int MaxGeneration=50;
    public static int PopulationSize=50;
    public static int MaxDomain=100;
    public static int NewProblemDimension=100;
    public MaxMinScheduling(List<Cloudlet> Cloudlets1,List<Vm> Vms1,int problemDimention1){
    	Vms=Vms1;
    	Cloudlets=Cloudlets1;
    	ProblemDimension=problemDimention1;
    }
    @SuppressWarnings("static-access")
	public static int[][] MapCloudletsToVms() {
    	int[]solution=new int[ProblemDimension];
		int [][] VmMaps=new int [Cloudlets.size()/ProblemDimension][Vms.size()];

        // Set a candidate solution

            System.arraycopy(myPop.getFittest().ToIntArray(), 0, VmMaps[TaskSize] , 0, myPop.getFittest().ToIntArray().length);
    	
        return VmMaps;
    }
}