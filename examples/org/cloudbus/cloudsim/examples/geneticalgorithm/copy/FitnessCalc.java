package org.cloudbus.cloudsim.examples.geneticalgorithm.copy;

import java.awt.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;
public class FitnessCalc {

    static int[] solution = new int[20];

    /* Public methods */
    // Set a candidate solution as a int array
    public static void setSolution(int[] newSolution) {
        solution = newSolution;
    }

    // To make it easier we can use this method to set our candidate solution
    // with integer array
   static void setSolution(java.util.List<Integer> newSolution) {
        solution = new int[newSolution.size()];
        // Loop through each character of our string and save it in our byte
        // array
        for (int i = 0; i < newSolution.size(); i++,solution[i]=newSolution.get(i));

        }

    // Calculate inidividuals fittness by comparing it to our candidate solution
    static float getFitness(Individual individual,java.util.List<Vm> Vms,java.util.List<Cloudlet> Cloudlets) {
        // Loop through our individuals genes and compare them to our cadidates
			//float totalCost=0;
			float retCost=0;
			float totCost=0;
			float [] costVector=new float[individual.size()];
			for(int i=0;i<individual.size();i++){
				Vm VirtualMachine=Vms.get(i);
				Cloudlet Cldlet=Cloudlets.get(individual.getGene(i));
				costVector[i]=(float) ((Cldlet.getCloudletLength()/ VirtualMachine.getMips()));
				totCost+=costVector[i];
			}
			for(int j=0;j<individual.size()-1;j++){
				float totalCost2=costVector[j+1];
				float totalCost=costVector[j];
				
				retCost+=(float) ((100 * (Math.pow(totalCost2 - Math.pow(totalCost, 2), 2))) + Math.pow(totalCost - 1, 2));
			}				
		
return totCost/individual.size();
    //    return retCost;

    }
    
    // Get optimum fitness
    static int getMaxFitness() {
        int maxFitness = solution.length;
        return maxFitness;
    }

}