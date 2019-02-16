package org.cloudbus.cloudsim.examples.geneticalgorithm.copy;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.examples.LoadBalancingGA;
import org.cloudbus.cloudsim.power.PowerVm;

public class GAScheduling {
    public static	List<PowerVm> Vms=new ArrayList<PowerVm>();
    public static List<Cloudlet> Cloudlets=new ArrayList<Cloudlet>();
    public static int ProblemDimension=LoadBalancingGA.VMNo;
    public static int MaxGeneration=200;
    public static int PopulationSize=100;
    public static int MaxDomain=ProblemDimension;
    public static int NewProblemDimension=ProblemDimension;
    public GAScheduling(int MinimumDomain,int MaximumDomain,List<Cloudlet> Cloudlets1,List<PowerVm> Vms1,int problemDimention1){
    	Vms=Vms1;
    	Cloudlets=Cloudlets1;
    	ProblemDimension=problemDimention1;
    	MaxDomain=MaximumDomain;
    	NewProblemDimension=problemDimention1;
    }
    @SuppressWarnings("static-access")
	public static int[] MapCloudletsToVms(double [] output, int TaskSize) {
    	int[]solution=new int[ProblemDimension];
		int [] VmMaps=new int [Vms.size()];
		java.util.Date dt1=new java.util.Date();

        // Set a candidate solution
    	//for(int TaskSize2=0;TaskSize2*ProblemDimension<MaxDomain;TaskSize2++){
    		NewProblemDimension=ProblemDimension;
    		if((TaskSize+1)*ProblemDimension>MaxDomain){
    			NewProblemDimension=(int) (MaxDomain % ProblemDimension);   			
    		}
    		int data=TaskSize*ProblemDimension;
        	for(int i=TaskSize*ProblemDimension;i<(TaskSize+1)*ProblemDimension;i++){
        	//	data+=i;
        		solution[i%ProblemDimension]=i;
        	}
            FitnessCalc2.setSolution(solution);

            // Create an initial population
            Population myPop = new Population(PopulationSize, true,Vms,Cloudlets,TaskSize);
            
            // Evolve our population until we reach an optimum solution
            int generationCount = 0;
            Algorithm alg=new Algorithm(Vms, Cloudlets,TaskSize,ProblemDimension);
            while (generationCount<MaxGeneration) {
                generationCount++;
                System.out.println("Generation: " + generationCount + " Fittest: " + myPop.getFittest().getFitness());
                myPop = alg.evolvePopulation(myPop);
            }
            System.out.println("Solution found!");
            System.out.println("Generation: " + generationCount);
            System.out.println("Genes:");
            System.out.println(myPop.getFittest());   
            System.arraycopy(myPop.getFittest().ToIntArray(), 0, VmMaps , 0, myPop.getFittest().ToIntArray().length);

        return VmMaps;
    }
}