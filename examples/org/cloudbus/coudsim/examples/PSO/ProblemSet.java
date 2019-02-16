package org.cloudbus.coudsim.examples.PSO;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.lists.VmList;
import org.cloudbus.cloudsim.power.PowerVm;

/* author: gandhi - gandhi.mtm [at] gmail [dot] com - Depok, Indonesia */

// this is the problem to be solved
// to find an x and a y that minimize the function below:
// f(x, y) = (2.8125 - x + x * y^4)^2 + (2.25 - x + x * y^2)^2 + (1.5 - x + x*y)^2
// where 1 <= x <= 4, and -1 <= y <= 1

// you can modify the function depends on your needs
// if your problem space is greater than 2-dimensional space
// you need to introduce a new variable (other than x and y)

public class ProblemSet {
	public static final double LOC_X_LOW = 1;
	public static final double LOC_X_HIGH = 4;
	public static final double LOC_Y_LOW = -1;
	public static final double LOC_Y_HIGH = 1;
	public static final double VEL_LOW = -1;
	public static final double VEL_HIGH = 1;
	
	public static final double ERR_TOLERANCE = 1E-20; // the smaller the tolerance, the more accurate the result, 
	                                                  // but the number of iteration is increased
	
	public static double evaluate(Location location,java.util.List<PowerVm> Vms,java.util.List<Cloudlet> Cloudlets) {
		double totCost=0;
		double [] costVector=new double[location.Size()];
		PowerVm prevVm;
		double DEi=0,VMm=0,VMc=0,Bi=0,DTz=0,TExeK=0,TExe=0,Ttrans=0;
		for(int i=0;i<location.Size();i++){
			if(location.getSwarm(i)<0){
				location.printLoc();
			}
			PowerVm VirtualMachine=Vms.get(i);
			Cloudlet Cldlet=Cloudlets.get(location.getSwarm(i));
			prevVm=Cldlet.getOverloadedVm();
				DEi=Cldlet.getCloudletTotalLength();
				VMm=VirtualMachine.getRam();
				VMc=VirtualMachine.getNumberOfPes();
				Bi=VirtualMachine.getBw();
				DTz=Cldlet.getCloudletFileSize();
				TExeK=DEi/(VMm*VMc);
				Ttrans=DTz/VirtualMachine.getBw()+ DTz/prevVm.getBw();
				costVector[i]=Ttrans;
				totCost+=costVector[i];
		}
		return totCost;
	}
}
