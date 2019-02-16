package org.cloudbus.cloudsim.examples;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;

public class BBOAlgorithmScheduling {
	static int GenerationLimit = 50; // generation count limit 
	static int PopulationSize = 50; // population size
	static int ProblemDimension = 16; // number of variables in each solution (i.e., problem dimension or number of Vistual Machines)
 	static double MutationProbability = 0.05; // mutation probability per solution per independent variable
	static int NumberOfElites = 2; // how many of the best solutions to keep from one generation to the next
	static double MinDomain = 0; // lower bound of each element of the function domain
	static double MaxDomain = 16; // upper bound of each element of the function domain (Number Of Cloudlets)
	static Random rn = new Random();
	static List<float[]> IndisvisualPermutations;
	static List<Cloudlet> Cloudlets;
	static List<Vm> Vms;
	public static int NewProblemDimension;
	public BBOAlgorithmScheduling(int MinimumDomain,int MaximumDomain,List<Cloudlet> Cldlets,List<Vm> VirtualMachines,int problemDimention){
		MinDomain=MinimumDomain;
		MaxDomain=MaximumDomain;
		Cloudlets=Cldlets;
		Vms=VirtualMachines;
		ProblemDimension=problemDimention;
	}
	//System.out.println(dateFormat.format(date)); //2014/08/06 15:59:48
	public float[][] MapVmByBBOAlgorithm(double[] output){
		float [][] VmMaps=new float [Cloudlets.size()/ProblemDimension][Vms.size()];
		java.util.Date dt1=new java.util.Date();

for(int TaskSize=0;TaskSize*ProblemDimension<MaxDomain;TaskSize++){
	NewProblemDimension=ProblemDimension;
	if((TaskSize+1)*ProblemDimension>MaxDomain){
		NewProblemDimension=(int) (MaxDomain % ProblemDimension);
		
	}
		float [][] X = new float[PopulationSize][NewProblemDimension]; // allocate memory for the population
		int step=0;
			for(int i=0;i<PopulationSize;i++){
			
				for(int j=0;j<NewProblemDimension;j++){
					X[i][j]=(step+j);
			
					if(X[i][j]<(TaskSize*NewProblemDimension)){
						X[i][j]+=TaskSize*NewProblemDimension;
					}
					if(X[i][j]>=((TaskSize+1)*NewProblemDimension)){
						X[i][j]=X[i][j]%((TaskSize+1)*NewProblemDimension);
					}
				}
				step= step++ %  ((TaskSize+1)*NewProblemDimension);
				for(int swaps=0;swaps<NewProblemDimension/3;swaps++){
					swap(X[i], rn.nextInt(NewProblemDimension),
							rn.nextInt(NewProblemDimension));
				}
			}
			
		float [][] TempPopulation = new float[PopulationSize][NewProblemDimension];// allocate memory for the temporary population

		float [] Cost = IndivisualCosts(X); // compute the cost of each individual 
		PopulationSort(X, Cost,NewProblemDimension);
		float[]	MinimumCost =new float[GenerationLimit]; // allocate memory
		MinimumCost[0] = Cost[0]; // save the best cost at each generation in the MinimumCost array
		System.out.println("Generation 0 min cost = "+ MinimumCost[0]);
		// Compute migration rates, assuming the population is sorted from most fit to least fit
	 // emigration rate
		float []mu=new float[PopulationSize];
		float []lambda=new float[PopulationSize];
		for(int i=0;i<mu.length;i++){
			mu[i]=(float)(PopulationSize-i)/(PopulationSize + 1);
			lambda[i]=1-mu[i];
		}
		System.arraycopy(X, 0, TempPopulation, 0,X.length);

		for(int GenIndex=1;GenIndex<GenerationLimit;GenIndex++){

				float[][] EliteSolutions=new float[NumberOfElites][NewProblemDimension];
				float[] EliteCosts=new float[NumberOfElites];
				System.arraycopy(X[0], 0, EliteSolutions[0], 0,X[0].length);
				System.arraycopy(X[1], 0, EliteSolutions[1], 0,X[1].length);
				EliteCosts[0]=Cost[0];
				EliteCosts[1]=Cost[1];
		//Use migration rates to decide how much information to share between solutions
	
			for(int k=0;k<PopulationSize;k++){
				//Probabilistic migration to the k-th solution
				for(int j=0;j<NewProblemDimension;j++){ 
					if(rn.nextFloat()<lambda[k]){ //Should we immigrate?
						//Yes - Pick a solution from which to emigrate (roulette wheel selection)
						float sum=0,Select=mu[0];
						int SelectIndex=0;
						for(int l=0;l<mu.length;l++)
							sum+=mu[l];
						float RandomNumber=(float)(rn.nextFloat()*sum);
						 while ((RandomNumber > Select) && (SelectIndex < PopulationSize-1)){ // by this We find the solution with maximum Mu rate (SelectIndex)
							 SelectIndex++; 
							 Select += mu[SelectIndex];
						 }
						 if(TempPopulation[k][j]!=X[SelectIndex][j]){
						 TempPopulation[k][j]=X[SelectIndex][j];//this is the migration step
						 /////// Find Repeated number and replace
						TempPopulation[k]=FindRepeatedAndReplace(TempPopulation[k], j, NewProblemDimension,TaskSize);
						 }
					}else{
						TempPopulation[k][j]=X[k][j]; //no migration for this independent variable
					//	TempPopulation[k]=FindRepeatedAndReplace(TempPopulation[k], j, NewProblemDimension,TaskSize);

					}
				}
			}		
		

	    // Mutation
	    for(int k = 0; k<PopulationSize;k++){
	    	for(int Index=0;Index<NewProblemDimension;Index++){
	    		
	    		if (rn.nextFloat() < MutationProbability){
                float randnum=rn.nextInt(NewProblemDimension)+(TaskSize*NewProblemDimension);
                if(TempPopulation[k][Index]!=randnum){
	    			TempPopulation[k][Index] = randnum;
				TempPopulation[k]=FindRepeatedAndReplace(TempPopulation[k], Index, NewProblemDimension,TaskSize);                	
                }
	    		}
	    	}
	    }
	    System.arraycopy(TempPopulation, 0, X, 0, TempPopulation.length);// replace the solutions with their new migrated and mutated versions
	//    System.arraycopy(X, 0, TempPopulation, 0, X.length);
	   Cost = IndivisualCosts(X); // calculate cost
	    PopulationSort(X, Cost,NewProblemDimension); // sort the population and costs from best to worst
	    for(int k = 0;k<NumberOfElites;k++){// replace the worst individuals with the previous generation's elites
	    	System.arraycopy(EliteSolutions[k], 0, X[PopulationSize-k-1], 0, EliteSolutions[k].length);
	    	//X[PopulationSize-k-1]=EliteSolutions[k];
	    //	System.arraycopy(EliteCosts[k], 0, Cost[PopulationSize-k-1], 0,1);
	    	Cost[PopulationSize-k-1]=EliteCosts[k];
	    }
	    PopulationSort(X, Cost,NewProblemDimension); // sort the population and costs from best to worst
	    MinimumCost[GenIndex] = Cost[0];
	    System.out.println("Generation "+ (GenIndex)+ "  min cost = "+ MinimumCost[GenIndex]);
	   
		} // end for genindex
		


System.out.println("Best solution found = ");
for(int k=0;k<NewProblemDimension;k++)
	System.out.print(X[0][k]+"   ");
System.out.print("And Cost Is:"+Cost[0]);
System.arraycopy(X[0], 0, VmMaps[TaskSize] , 0, X[0].length);
} //end for task size

java.util.Date dt2=new java.util.Date();
System.out.println("Convergence Time:"+String.valueOf(dt2.getTime()-dt1.getTime()));
output[0]=dt2.getTime()-dt1.getTime();
return  VmMaps;
	}

	
	public static float[] FindRepeatedAndReplace(float [] OriginalSolution,int SelectIndex,int MaximumIndividual,int bulkSize){
		int total=((bulkSize+1)*MaximumIndividual)*(((bulkSize+1)*MaximumIndividual)-1)/2;
		total-=(bulkSize*MaximumIndividual)*((bulkSize)*MaximumIndividual-1)/2;
		int arrayTotal=0;
		int index=-1;
		for(int i=0;i<OriginalSolution.length;i++){
			if(OriginalSolution[SelectIndex]==OriginalSolution[i] && i!=SelectIndex){
				index=i;
			}else{
				arrayTotal+=OriginalSolution[i];
			}
		}
		try{
if(total-arrayTotal<0){
	System.out.println(total-arrayTotal);
}
			OriginalSolution[index]=total-arrayTotal;

		}catch(Exception e){
			
		}
		return OriginalSolution;
		
		
	}
					
	public static boolean PopulationSort(float [][]XData, float []CostVector,int ProblemDimensions){
				ArrayIndexComparator comparator = new ArrayIndexComparator(CostVector);
				Integer[] indexes = comparator.createIndexArray();
			Arrays.sort(indexes, comparator);
			Arrays.sort(CostVector);
				float [][] CopyXData=new float[PopulationSize][ProblemDimensions];
				System.arraycopy(XData, 0, CopyXData, 0, XData.length);
				for(int k=0;k<PopulationSize;k++){
					float[] temp=new float[ProblemDimensions];
					System.arraycopy(XData[indexes[k]], 0,temp , 0, ProblemDimensions);
					System.arraycopy(XData[indexes[k]], 0, XData[k], 0, ProblemDimensions);
					System.arraycopy(temp, 0, XData[indexes[k]], 0, ProblemDimensions);
				}
				return true;
			}
	
	public static void swap (float[] data,int indFirst,int indSeco){
		float temp=data[indFirst];
		data[indFirst]=data[indSeco];
		data[indSeco]=temp;
	}
	
	
	public static  float[] IndivisualCosts(float[][] XData){

		int NumberOfDimensions = XData[0].length;
		int NumberOfSolutions = XData.length;
		float []Cost =new float[XData.length] ; // allocate memory for the Cost array
		for(int i=0;i<NumberOfSolutions;i++){
			Cost[i]=0;
			float totalCost=0;
			int totcost=0;
			for(int j=0;j<NumberOfDimensions;j++){
				Vm VirtualMachine=Vms.get(j);
			//	Vm VirtualMachine2=Vms.get(j+1);
				Cloudlet Cldlet=Cloudlets.get((int) XData[i][j]);
				float alpha=0.1f;
				float beta=0.9f;
				
		//	float meanPower=(float) ((VirtualMachine.getCurrentAllocatedBw()/VirtualMachine.getBw())+ 
		//			(VirtualMachine.getCurrentAllocatedRam()/VirtualMachine.getRam()));
			long ram=VirtualMachine.getFreeRam();
			long bw=VirtualMachine.getFreeBW();
			//long size=VirtualMachine.getFreeSize();
			double mips=VirtualMachine.getMips();
double CpuUtil=Cldlet.getCloudletLength()/ mips;
double avRam=1;
double avBw=1;
float avSize=1;
//avSize=(float)Cldlet.getCloudletFileSize()/size;
avRam=(float)Cldlet.getCloudletFileSize()/(Math.max(1,ram)*Math.pow(2, 20));  
//(((float)Cldlet.getCloudletFileSize()/(VirtualMachine.getRam()*Math.pow(2, 20)))/(Math.max(1,ram)*Math.pow(2, 20)));
avBw=((float)((Cldlet.getCloudletFileSize()+Cldlet.getCloudletOutputSize())*8)/bw);
float a=0.4f;
float b=0.3f;
float c=0.2f;
float d=0.1f;
				totalCost+=(float) (a*CpuUtil + d*avRam+d*avBw);
			//	float totalCost2=(float) (Cldlet.getCloudletLength()/ VirtualMachine2.getMips());
			//	Cost[i]=(float) (Cost[i]+(100 * (Math.pow(totalCost2 - Math.pow(totalCost, 2), 2))) + Math.pow(totalCost - 1, 2));
			
			}
			Cost[i]=(float) (totalCost/NumberOfDimensions);
		}
		return Cost;
	}
}

