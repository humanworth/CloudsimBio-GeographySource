package org.cloudbus.cloudsim.examples.geneticalgorithm.copy;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.power.PowerVm;

public class Population {

    Individual[] individuals;
    public static int TaskSize=0;
    public static	List<PowerVm> Vms=new ArrayList<PowerVm>();
    public static List<Cloudlet> Cloudlets=new ArrayList<Cloudlet>();
    /*
     * Constructors
     */
    // Create a population
    public Population(int populationSize, boolean initialise,List<PowerVm> Vms1,List<Cloudlet> Cloudlets1,int tskSize){
    	Vms=Vms1;
    	TaskSize=tskSize;
    	Cloudlets=Cloudlets1;
        individuals = new Individual[populationSize];
        // Initialise population
        if (initialise) {
            // Loop and create individuals
        	
            for (int i = 0; i < size(); i++) {
                Individual newIndividual = new Individual(Vms1,Cloudlets1,TaskSize);
                newIndividual.generateIndividual(Vms1.size());
                saveIndividual(i, newIndividual);
            }
        }
    }

    /* Getters */
    public Individual getIndividual(int index) {
        return individuals[index];
    }

    public Individual getFittest() {
        Individual fittest = individuals[0];
        // Loop through individuals to find fittest
        for (int i = 0; i < size(); i++) {
            if (fittest.getFitness() >= getIndividual(i).getFitness()) {
                fittest = getIndividual(i);
            }
        }
        return fittest;
    }

    /* Public methods */
    // Get population size
    public int size() {
        return individuals.length;
    }

    // Save individual
    public void saveIndividual(int index, Individual indiv) {
        individuals[index] = indiv;
    }
}