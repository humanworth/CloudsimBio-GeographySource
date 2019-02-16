package org.cloudbus.coudsim.examples.PSO;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.examples.geneticalgorithm.copy.Individual;
import org.cloudbus.cloudsim.power.PowerVm;

public class PSOScheduling {
    public static	List<PowerVm> Vms=new ArrayList<PowerVm>();
    public static List<Cloudlet> Cloudlets=new ArrayList<Cloudlet>();
    public static int ProblemDimension=	PSOConstants.PROBLEM_DIMENSION; //Particle Dimension
    public static int MaxGeneration=200;
    public static int PopulationSize=100; //Swarm Size
    public static int MaxDomain=PSOConstants.PROBLEM_DIMENSION;
    public static int NewProblemDimension=PSOConstants.PROBLEM_DIMENSION;
    public PSOScheduling(int MinimumDomain,int MaximumDomain,List<Cloudlet> Cloudlets1,List<PowerVm> Vms1,int problemDimention1){
    	Vms=Vms1;
    	Cloudlets=Cloudlets1;
    	ProblemDimension=problemDimention1;
    	MaxDomain=MaximumDomain;
    }
    @SuppressWarnings("static-access")
	public static double [] MapCloudletsToVms(double [] output,int TaskSize) {
    	int[]solution=new int[ProblemDimension];
		double [] VmMaps=new double [Vms.size()];
    		NewProblemDimension=ProblemDimension;
    		if((TaskSize+1)*ProblemDimension>MaxDomain){
    			NewProblemDimension=(int) (MaxDomain % ProblemDimension);
    			
    		}
    		int data=TaskSize*ProblemDimension;

    	PSOProcess ps=new PSOProcess(Cloudlets, Vms,data,data+ProblemDimension); 
    	Location loc=ps.execute();
    	loc.printLoc();
    	VmMaps=loc.getLoc(); //this line should be changed
        return VmMaps;
    }
    public static double[] FindRepeatedAndReplace(double[] OriginalSolution,int SelectIndex,int MaximumIndividual,int bulkSize){
		int total=((bulkSize+1)*MaximumIndividual)*(((bulkSize+1)*MaximumIndividual)-1)/2;
		total-=(bulkSize*MaximumIndividual)*((bulkSize)*MaximumIndividual-1)/2;
		int arrayTotal=0;
		int index=-1;
		for(int i=0;i<OriginalSolution.length;i++){
			if(OriginalSolution[i]==OriginalSolution[i] && i!=SelectIndex){
				index=i;
			}else{
				arrayTotal+=OriginalSolution[i];
			}
		}
		try{
			if(total-arrayTotal<0){
				System.out.println(total-arrayTotal);
			}
			OriginalSolution[index]= total-arrayTotal;

		}catch(Exception e){
			
		}
		return OriginalSolution;
    }
}