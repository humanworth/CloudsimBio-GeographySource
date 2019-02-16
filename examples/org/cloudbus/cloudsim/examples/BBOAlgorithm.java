package org.cloudbus.cloudsim.examples;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Random;

import javax.naming.LimitExceededException;

public class BBOAlgorithm {
	static int GenerationLimit = 50; // generation count limit 
	static int PopulationSize = 50; // population size
	static int ProblemDimension = 50; // number of variables in each solution (i.e., problem dimension)
 	static double MutationProbability = 0.04; // mutation probability per solution per independent variable
	static int NumberOfElites = 2; // how many of the best solutions to keep from one generation to the next
	static double MinDomain = -2.048; // lower bound of each element of the function domain
	static double MaxDomain = +2.048; // upper bound of each element of the function domain
	static Random rn = new Random();
	//static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	//static Date date = new Date();
	//static double Seed=1234123344;
 
	//System.out.println(dateFormat.format(date)); //2014/08/06 15:59:48
	/**
	 * @param args
	 */
	public static void main(String[] args){
		float [][] X = new float[PopulationSize][ProblemDimension]; // allocate memory for the population
		//rn.setSeed(date.getTime());
		for(int i=0;i<PopulationSize;i++){ //randomly initialize the population
			for(int j=0;j<ProblemDimension;j++){
				X[i][j]= (float) (MinDomain + ((MaxDomain - MinDomain) * rn.nextFloat()));
		//		System.out.print(X[i][j]+"   ");
			}
		//	System.out.println();
			
		}
		float [][] TempPopulation = new float[PopulationSize][ProblemDimension];// allocate memory for the temporary population

		float [] Cost = RosenbrockCost(X); // compute the cost of each individual 
	//	System.out.println();
		
		PopulationSort(X, Cost);
		//System.out.println();
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
for(int GenIndex=1;GenIndex<GenerationLimit;GenIndex++){

		float[][] EliteSolutions=new float[NumberOfElites][ProblemDimension];
		float[] EliteCosts=new float[NumberOfElites];
		EliteSolutions[0]=X[0];
		EliteSolutions[1]=X[1];
		EliteCosts[0]=Cost[0];
		EliteCosts[1]=Cost[1];
		//Use migration rates to decide how much information to share between solutions
		for(int k=0;k<PopulationSize;k++){
			//Probabilistic migration to the k-th solution
			for(int j=0;j<ProblemDimension;j++){ 
				if(rn.nextFloat()<lambda[k]){ //Should we immigrate?
					//Yes - Pick a solution from which to emigrate (roulette wheel selection)
					float sum=0,Select=mu[0];
					int SelectIndex=0;
					for(int l=0;l<mu.length;l++)
						sum+=mu[l];
					float RandomNumber=(float)(rn.nextFloat()*sum);
					 while ((RandomNumber > Select) && (SelectIndex < PopulationSize-1)){
						 SelectIndex++;
						 Select += mu[SelectIndex];
					 }
					 TempPopulation[k][j]=X[SelectIndex][j];//this is the migration step
	           
				}else{
					TempPopulation[k][j]=X[k][j]; //no migration for this independent variable
				}
			}
		}
	    // Mutation
	    for(int k = 0; k<PopulationSize;k++){
	    	for(int Index=0;Index<ProblemDimension;Index++){
	    		float f=(float) rn.nextFloat();
	    		if (rn.nextFloat() < MutationProbability)
                TempPopulation[k][Index] = (float) (MinDomain + (MaxDomain - MinDomain) * rn.nextFloat());
           
	    	}
	    }
	    System.arraycopy(TempPopulation, 0, X, 0, TempPopulation.length);
	  
	 //    X=TempPopulation;// replace the solutions with their new migrated and mutated versions
	   Cost = RosenbrockCost(X); // calculate cost
	    PopulationSort(X, Cost); // sort the population and costs from best to worst
	    for(int k = 0;k<NumberOfElites;k++){// replace the worst individuals with the previous generation's elites
	    X[PopulationSize-k-1]=EliteSolutions[k];
	    Cost[PopulationSize-k-1]=EliteCosts[k];
	    
	    }
	    PopulationSort(X, Cost); // sort the population and costs from best to worst
	    MinimumCost[GenIndex] = Cost[0];
	    System.out.println("Generation "+ (GenIndex)+ "  min cost = "+ MinimumCost[GenIndex]);
}
System.out.println("Best solution found = ");
for(int k=0;k<ProblemDimension;k++)
	System.out.print(X[0][k]+"   ");

	}

					
	public static boolean PopulationSort(float [][]XData, float []CostVector){
				ArrayIndexComparator comparator = new ArrayIndexComparator(CostVector);
				Integer[] indexes = comparator.createIndexArray();
			Arrays.sort(indexes, comparator);
			Arrays.sort(CostVector);
			//	int i=0;
				float [][] CopyXData=new float[PopulationSize][ProblemDimension];
				System.arraycopy(XData, 0, CopyXData, 0, XData.length);
				
				for(int k=0;k<PopulationSize;k++){
					float[] temp=new float[ProblemDimension];
					System.arraycopy(XData[indexes[k]], 0,temp , 0, ProblemDimension);
					System.arraycopy(XData[indexes[k]], 0, XData[k], 0, ProblemDimension);
					//XData[k]=XData[indexes[k]];
					System.arraycopy(temp, 0, XData[indexes[k]], 0, ProblemDimension);
				//	XData[indexes[k]]=temp;
					//for(int j=0;j<ProblemDimension;j++){
					//}
				}
			//	for(float[] item :XData){
			//		float[] temp=item;
			//		item=XData[indexes[i]];
			//		XData[indexes[i]]=temp;
			//		i++;
			//	}
			//	System.out.println();
			//	for(int k=0;k<50;k++){
			//		System.out.print(CostVector[k]/1000+"  ");
			//		for(int j=0;j<20;j++){
					//	System.out.print(XData[k][j]+"  ");
				//	}
				//	System.out.println();
			//	}
				return true;
			}
	
	public static  float[] RosenbrockCost(float[][] XData){
		int NumberOfDimensions = XData[0].length;
		float []Cost =new float[XData.length] ; // allocate memory for the Cost array
		for(int i=0;i<XData.length;i++){
			Cost[i]=0;
			for(int j=0;j<NumberOfDimensions-1;j++){
				float temp1=XData[i][j];
				float temp2=XData[i][j+1];
				//If We Want to normalize this vector we should divide it by 1000
			//	System.out.println((Cost[i]+(100*(Math.pow(temp2 - Math.pow(temp1, 2), 2))) + Math.pow(temp1-1, 2))/10000);
			//	Cost[i]=Cost[i] + 100 * (temp2^2 - (temp1^2))^2 + (temp1 - 1)^2;
				Cost[i]=(float) (Cost[i]+(100 * (Math.pow(temp2 - Math.pow(temp1, 2), 2))) + Math.pow(temp1 - 1, 2));
			}
		}
		return Cost;
	}
}

