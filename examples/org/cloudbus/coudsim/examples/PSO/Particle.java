package org.cloudbus.coudsim.examples.PSO;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.power.PowerVm;

public class Particle {
	private double fitnessValue;
	private Velocity velocity;
	private Location location;
    public static	List<PowerVm> Vms=new ArrayList<PowerVm>();
    public static List<Cloudlet> Cloudlets=new ArrayList<Cloudlet>();
    
	public Particle(List<Cloudlet> Cloudlets1,List<PowerVm> Vms1) {
		
		super();
    	Vms=Vms1;
    	Cloudlets=Cloudlets1;
	}

	public Particle(double fitnessValue, Velocity velocity, Location location) {
		super();
		this.fitnessValue = fitnessValue;
		this.velocity = velocity;
		this.location = location;
	}

	public Velocity getVelocity() {
		return velocity;
	}

	public void setVelocity(Velocity velocity) {
		this.velocity = velocity;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public double getFitnessValue() {
		fitnessValue = ProblemSet.evaluate(location,Vms,Cloudlets);
		return fitnessValue;
	}
}
