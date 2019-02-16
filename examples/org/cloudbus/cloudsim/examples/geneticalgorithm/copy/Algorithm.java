package org.cloudbus.cloudsim.examples.geneticalgorithm.copy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.power.PowerVm;

public class Algorithm {

    /* GA parameters */
    private static final double uniformRate = 0.5;
    private static final double mutationRate = 0.05;
    private static final int tournamentSize = 2;
    private static final boolean elitism = true;
    public static int TaskSize=0;
    public static int ProblemDimension=1000;
    
    public static Random rn=new Random();
    /* Public methods */
    public static	List<PowerVm> Vms=new ArrayList<PowerVm>();
    public static List<Cloudlet> Cloudlets=new ArrayList<Cloudlet>();
    public Algorithm(List<PowerVm> Vms1,List<Cloudlet> Cloudlets1,int TskSize,int problemDimension){
    	Vms=Vms1;
    	Cloudlets=Cloudlets1;
    	TaskSize=TskSize;
    	ProblemDimension=problemDimension;
    }
    // Evolve a population
    public static Population evolvePopulation(Population pop) {
        Population newPopulation = new Population(pop.size(), false,Vms,Cloudlets,TaskSize);

        // Keep our best individual
        if (elitism) {
            newPopulation.saveIndividual(0, pop.getFittest());
        }

        // Crossover population
        int elitismOffset;
        if (elitism) {
            elitismOffset = 1;
        } else {
            elitismOffset = 0;
        }
        // Loop over the population size and create new individuals with
        // crossover
        for (int i = elitismOffset; i < pop.size(); i++) {
            Individual indiv1 = tournamentSelection(pop);
            Individual indiv2 = tournamentSelection(pop);
            Individual newIndiv = crossover(indiv1, indiv2);
            newPopulation.saveIndividual(i, newIndiv);
        }

        // Mutate population
        for (int i = elitismOffset; i < newPopulation.size(); i++) {
            mutate(newPopulation.getIndividual(i));
        }

        return newPopulation;
    }

    // Crossover individuals
    private static Individual crossover(Individual indiv1, Individual indiv2) {
        Individual newSol = new Individual(Vms,Cloudlets,TaskSize);
        for(int i=0;i<indiv1.size();newSol.setGene(i, (TaskSize*ProblemDimension)+i),i++);
        // Loop through genes
        for (int i = 0; i < indiv1.size(); i++) {
            // Crossover
            if (Math.random() <= uniformRate) {
                newSol.setGene(i, indiv1.getGene(i));
                FindRepeatedAndReplace(newSol, i, newSol.size(), TaskSize);
            } else {
                newSol.setGene(i, indiv2.getGene(i));
                FindRepeatedAndReplace(newSol, i, newSol.size(), TaskSize);
            }
        }
        return newSol;
    }

    // Mutate an individual
    private static void mutate(Individual indiv) {
        // Loop through genes
        for (int i = 0; i < indiv.size(); i++) {
            if (Math.random() <= mutationRate) {
                // Create random gene
                int gene = (int)Math.round(rn.nextInt((TaskSize+1)*ProblemDimension))+(TaskSize)*ProblemDimension;
               
                if(gene>(TaskSize+1)*ProblemDimension)
                	gene%=(TaskSize+1)*ProblemDimension;
                if(gene<(TaskSize)*ProblemDimension)
                	gene+=(TaskSize)*ProblemDimension;
                if(gene==(TaskSize+1)*ProblemDimension)
                	gene=(TaskSize)*ProblemDimension;	
                if(TaskSize>0){
                	while((TaskSize>0)&&(gene>(TaskSize+1)*ProblemDimension || 
                			gene<(TaskSize)*ProblemDimension || 
                			gene==(TaskSize+1)*ProblemDimension))
                	{
                	     if(gene>(TaskSize+1)*ProblemDimension)
                         	gene%=(TaskSize+1)*ProblemDimension;
                         if(gene<(TaskSize)*ProblemDimension)
                         	gene+=(TaskSize)*ProblemDimension;
                         if(gene==(TaskSize+1)*ProblemDimension)
                         	gene=(TaskSize)*ProblemDimension;
                	}             	
                }

                
                indiv.setGene(i, gene);
                FindRepeatedAndReplace(indiv, i, indiv.size(), TaskSize);
            }
        }
    }

    // Select individuals for crossover
    private static Individual tournamentSelection(Population pop) {
        // Create a tournament population
        Population tournament = new Population(tournamentSize, false,Vms,Cloudlets,TaskSize);
        // For each place in the tournament get a random individual
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.size());
            tournament.saveIndividual(i, pop.getIndividual(randomId));
        }
        // Get the fittest
        Individual fittest = tournament.getFittest();
        return fittest;
    }
    
    public static int[] FindRepeatedAndReplace(Individual OriginalSolution,int SelectIndex,int MaximumIndividual,int bulkSize){
		int total=((bulkSize+1)*MaximumIndividual)*(((bulkSize+1)*MaximumIndividual)-1)/2;
		total-=(bulkSize*MaximumIndividual)*((bulkSize)*MaximumIndividual-1)/2;
		int arrayTotal=0;
		int index=-1;
		for(int i=0;i<OriginalSolution.size();i++){
			if(OriginalSolution.getGene(SelectIndex)==OriginalSolution.getGene(i) && i!=SelectIndex){
				index=i;
			}else{
				arrayTotal+=OriginalSolution.getGene(i);
			}
		}
		try{
			if(total-arrayTotal<0){
				System.out.println(total-arrayTotal);
			}
			OriginalSolution.setGene(index, total-arrayTotal);

		}catch(Exception e){
			
		}
		return OriginalSolution.ToIntArray();
		
		
	}
    
}
