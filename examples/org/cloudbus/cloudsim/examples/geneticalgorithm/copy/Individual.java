package org.cloudbus.cloudsim.examples.geneticalgorithm.copy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.power.PowerVm;

public class Individual {

    static int defaultGeneLength = 100;
    private int[] genes;
    // Cache
    private float fitness = 0;
    public int TaskSize=0;
	public static Random rn;
	public static	List<PowerVm> Vms=new ArrayList<PowerVm>();
	public static List<Cloudlet> Cloudlets=new ArrayList<Cloudlet>();
	public Individual(List<PowerVm> Vms1,List<Cloudlet> Cloudlets1,int tskSize){
		TaskSize=tskSize;
	Vms=Vms1;
	Cloudlets=Cloudlets1;
	defaultGeneLength=Vms1.size();
	genes=new int[defaultGeneLength];
}
    // Create a random individual
    public void generateIndividual(int NewProblemDimension) {
     rn=new Random();
    	int step=0;
		for(int j=0;j<NewProblemDimension;j++){
			int gene = (int) step+j;
			if(gene<(TaskSize*NewProblemDimension)){
				gene+=TaskSize*NewProblemDimension;
			}
			if(gene>=((TaskSize+1)*NewProblemDimension)){
				gene=gene%((TaskSize+1)*NewProblemDimension);
			}
			genes[j]=gene;
		}
		step= step++ %  ((TaskSize+1)*NewProblemDimension);
		for(int swaps=0;swaps<NewProblemDimension/3;swaps++){
			swap(genes, rn.nextInt(NewProblemDimension),
					rn.nextInt(NewProblemDimension));
		}
    }
	public static void swap (int[] data,int indFirst,int indSeco){
		int temp=data[indFirst];
		data[indFirst]=data[indSeco];
		data[indSeco]=temp;
	}

    /* Getters and setters */
    // Use this if you want to create individuals with different gene lengths
    public static void setDefaultGeneLength(int length) {
        defaultGeneLength = length;
    }
    /*
     * In This Procedure parameter index=virtual machine number and returns a cloudlet id 
     */
    public int getGene(int index) {
        return genes[index];
    }
/*
 *index= virtualmachine number and value=cloudlet number
 */
    public void setGene(int index, int value) {
        genes[index] = value;
        fitness = 0;
    }

    /*
     * this function represents NewProblemDimention Variable value or gene length 
     */
    public int size() {
        return genes.length;
    }
/*
 * this function represents fitness of this individual 
 */
    public float getFitness() {
        if (fitness == 0) {
            fitness = FitnessCalc2.getFitness(this, Vms, Cloudlets);
        }
        return fitness;
    }
    public int[] ToIntArray() {
        int[] geneArray =new int[Vms.size()];
        for (int i = 0; i < size(); i++) {
            geneArray[i] = getGene(i);
        }
        return geneArray;
    }
    @Override
    public String toString() {
        String geneString = "";
        for (int i = 0; i < size(); i++) {
            geneString += getGene(i)+" ";
        }
        return geneString;
    }
}