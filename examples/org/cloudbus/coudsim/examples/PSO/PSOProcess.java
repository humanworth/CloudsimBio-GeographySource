package org.cloudbus.coudsim.examples.PSO;

import java.util.ArrayList;
import java.util.List;

/* author: gandhi - gandhi.mtm [at] gmail [dot] com - Depok, Indonesia */

// this is the heart of the PSO program
// the code is for 2-dimensional space problem
// but you can easily modify it to solve higher dimensional space problem

import java.util.Random;
import java.util.Vector;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.power.PowerVm;

public class PSOProcess implements PSOConstants {
	private Vector<Particle> swarm = new Vector<Particle>();
	private double[] pBest = new double[SWARM_SIZE];
	private Vector<Location> pBestLocation = new Vector<Location>();
	private double gBest;
	private Location gBestLocation;
	private double[] fitnessValueList = new double[SWARM_SIZE];
    public static	List<PowerVm> Vms=new ArrayList<PowerVm>();
    public static List<Cloudlet> Cloudlets=new ArrayList<Cloudlet>();
    private int MinDomain=0;
    private int MaxDomain=PROBLEM_DIMENSION;
	Random generator = new Random();
	public PSOProcess(List<Cloudlet> Cloudlets1,List<PowerVm> Vms1,int min,int max){
		Vms=Vms1;
		Cloudlets=Cloudlets1;
		MinDomain=min;
		MaxDomain=max;
	}
	public Location execute() {
		initializeSwarm();
		updateFitnessList();
		
		for(int i=0; i<SWARM_SIZE; i++) {
			pBest[i] = fitnessValueList[i];
			pBestLocation.add(swarm.get(i).getLocation());
		}
		
		int t = 0;
		double w;
		double err = 9999;
		
		while(t < MAX_ITERATION && err > ProblemSet.ERR_TOLERANCE) {
			// step 1 - update pBest
			for(int i=0; i<SWARM_SIZE; i++) {
				if(fitnessValueList[i] < pBest[i]) {
					pBest[i] = fitnessValueList[i];
					pBestLocation.set(i, swarm.get(i).getLocation());
				}
			}
				
			// step 2 - update gBest
			int bestParticleIndex = PSOUtility.getMinPos(fitnessValueList);
			if(t == 0 || fitnessValueList[bestParticleIndex] < gBest) {
				gBest = fitnessValueList[bestParticleIndex];
				gBestLocation = swarm.get(bestParticleIndex).getLocation();
			}
			
			w = W_UPPERBOUND - (((double) t) / MAX_ITERATION) * (W_UPPERBOUND - W_LOWERBOUND);
			
			for(int i=0; i<SWARM_SIZE; i++) {
				double r1 = generator.nextDouble();
				double r2 = generator.nextDouble();
				
				Particle p = swarm.get(i);
				
				// step 3 - update velocity
				double[] newVel = new double[PROBLEM_DIMENSION];
				for(int size=0;size<PROBLEM_DIMENSION;size++){
					newVel[size] = (w * p.getVelocity().getPos()[size]) + 
							(r1 * C1) * Math.abs((pBestLocation.get(i).getLoc()[size] - p.getLocation().getLoc()[size])) +
							(r2 * C2) * Math.abs((gBestLocation.getLoc()[size] - p.getLocation().getLoc()[size]));
					
				}
				
				Velocity vel = new Velocity(newVel);
				p.setVelocity(vel);
				
				// step 4 - update location
				double[] newLoc = new double[PROBLEM_DIMENSION];

				for(int size=0;size<PROBLEM_DIMENSION;size++){
					newLoc[size] =Math.ceil(p.getLocation().getLoc()[size] + newVel[size]);
					if(newLoc[size]<0){
						newLoc[size]=newLoc[size]*(-1);
					}
					
					while(newLoc[size]<MinDomain || newLoc[size]>=MaxDomain){
						if(newLoc[size]<(MinDomain)){
							newLoc[size]+=MinDomain;
						}
						if(newLoc[size]>=(MaxDomain)){
							newLoc[size]=newLoc[size]%(MaxDomain);
						}
					}
					
				}
				
				Location loc = new Location(newLoc);
				loc.NormalizeLocation(MinDomain, MaxDomain);
				p.setLocation(loc);
			}
			
			err = ProblemSet.evaluate(gBestLocation,Vms,Cloudlets) - 0; // minimizing the functions means it's getting closer to 0
			
			
	//		System.out.println("ITERATION " + t + ": ");
	//		System.out.println("     Best X: " + gBestLocation.getLoc()[0]);
	//		System.out.println("     Best Y: " + gBestLocation.getLoc()[1]);
			System.out.println("     Value: " + ProblemSet.evaluate(gBestLocation,Vms,Cloudlets));
			t++;
			updateFitnessList();
		}
		
		System.out.println("\nSolution found at iteration " + (t - 1) + ", the solutions is:");
		System.out.println("     Best X: ");
		gBestLocation.printLoc();
		
		return gBestLocation;
		//System.out.println("     Best Y: " + gBestLocation.getLoc()[1]);
	}
	
	public void initializeSwarm() {
		Particle p;
		for(int i=0; i<SWARM_SIZE; i++) {
			p = new Particle(Cloudlets,Vms);
			
			// randomize location inside a space defined in Problem Set
			double[] loc = new double[PROBLEM_DIMENSION];
		//	for(int size=0;size<PROBLEM_DIMENSION;size++){
		//		loc[size] = MinDomain + generator.nextInt(PROBLEM_DIMENSION) * (MaxDomain - MinDomain);
		//	}
			
   //     	for(int j=MinDomain;i<MaxDomain;j++){
      
        	//	loc[j%PROBLEM_DIMENSION]=j;
     //  	}
			loc=generateParticle(PROBLEM_DIMENSION);
			Location location = new Location(loc);
			
			// randomize velocity in the range defined in Problem Set
			double[] vel = new double[PROBLEM_DIMENSION];
			for(int size=0;size<PROBLEM_DIMENSION;size++){
				vel[size] = ProblemSet.VEL_LOW + generator.nextInt(2) * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			}
			Velocity velocity = new Velocity(vel);
			p.setLocation(location);
			p.setVelocity(velocity);
			swarm.add(p);
		}
	}
    // Create a random Particle Location
    public double[] generateParticle(int NewProblemDimension) {
    	double[] locations=new double [NewProblemDimension];
     Random rn=new Random();
    	int step=0;
		for(int j=0;j<NewProblemDimension;j++){
			int gene = (int) step+j;
			if(gene<(MinDomain)){
				gene+=MinDomain;
			}
			if(gene>=(MaxDomain)){
				gene=gene%(MaxDomain);
			}
			locations[j]=gene;
		}
		step= step++ %  (MaxDomain);
		for(int swaps=0;swaps<NewProblemDimension/3;swaps++){
			swap(locations, rn.nextInt(NewProblemDimension),
					rn.nextInt(NewProblemDimension));
		}
		return locations;
    }
	public static void swap (double[] data,int indFirst,int indSeco){
		double temp=data[indFirst];
		data[indFirst]=data[indSeco];
		data[indSeco]=temp;
	}
	
	public void updateFitnessList() {
		for(int i=0; i<SWARM_SIZE; i++) {
			fitnessValueList[i] = swarm.get(i).getFitnessValue();
		}
	}
}
