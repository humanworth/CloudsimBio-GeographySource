package org.cloudbus.cloudsim.examples.geneticalgorithm.copy;

import java.awt.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerVm;
public class FitnessCalc2 {

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
    static float getFitness(Individual individual,java.util.List<PowerVm> Vms,java.util.List<Cloudlet> Cloudlets) {
        // Loop through our individuals genes and compare them to our cadidates
			//float totalCost=0;
			double w1=0.8,w2=0.2;
			double alpha=0.2;
			double L=1;
			double etha=0;
			double totCost=0;
			double [] costVector=new double[individual.size()];
			for(int i=0;i<individual.size();i++){
				PowerVm VirtualMachine=Vms.get(i);
				Cloudlet Cldlet=Cloudlets.get(individual.getGene(i));
				double seconds=Cldlet.getCloudletLength()/VirtualMachine.getMips();
				
			L=Cldlet.getEstimatedProcessingCost();
			alpha=Cldlet.getCloudletLength()*0.2/seconds; //   Cost/Second
			etha=w1*alpha*(Cldlet.getCloudletLength()/VirtualMachine.getMips())+w2*L;
				costVector[i]=etha;
				totCost+=costVector[i];
			}
			return (float)totCost/individual.size();
    }
    
    // Get optimum fitness
    static int getMaxFitness() {
        int maxFitness = solution.length;
        return maxFitness;
    }

}