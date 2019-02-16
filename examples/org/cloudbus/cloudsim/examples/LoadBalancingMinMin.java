package org.cloudbus.cloudsim.examples;

/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation
 *               of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009, The University of Melbourne, Australia
 */

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerDynamicWorkload;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.HostDynamicWorkload;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.MaxMinDatacenterBroker;
import org.cloudbus.cloudsim.MinMinDatacenterBroker;
import org.cloudbus.cloudsim.MyPolicyNew;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.UtilizationModelStochastic;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicyCustom;
import org.cloudbus.cloudsim.VmAllocationPolicyMaxMin;
import org.cloudbus.cloudsim.VmAllocationPolicyMinMin;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerHostUtilizationHistory;
import org.cloudbus.cloudsim.power.PowerVm;
import org.cloudbus.cloudsim.power.models.PowerModelSqrt;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import cern.jet.random.Normal;
import cern.jet.random.Poisson;
import cern.jet.random.engine.DRand;
import cern.jet.random.engine.RandomEngine;

/**
 * A simple example showing how to create a datacenter with one host and run one
 * cloudlet on it.
 */
public class LoadBalancingMinMin {

	public static Random rn;
	/** The cloudlet list. */
	private static List<Cloudlet> cloudletList;

	/** The vmlist. */
	private static List<PowerVm> vmlist;
	private static List<Cloudlet> dataset;

	/**
	 * Creates main() to run this example.
	 *
	 * @param args the args
	 */
	public static int hostNo=0;
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		int pesNumber = 1; // number of cpus
		rn=new Random();
		Log.printLine("Starting CloudSimExample1...");
		RandomEngine engine = new DRand();
		Poisson poisson = new Poisson(100, engine);
		Normal normal=new Normal(40000, 10000, engine);
		try {
			// First step: Initialize the CloudSim package. It should be called
			// before creating any entities.
			int num_user = 100; // number of cloud users
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false; // mean trace events

			// Initialize the CloudSim library
			CloudSim.init(num_user, calendar, trace_flag);

			// Second step: Create Datacenters
			// Datacenters are the resource providers in CloudSim. We need at
			// list one of them to run a CloudSim simulation
			@SuppressWarnings("unused")
			Datacenter datacenter1 = createDatacenter("Datacenter_1",1,3,-4,30000,3);
			@SuppressWarnings("unused")
			Datacenter datacenter2 = createDatacenter("Datacenter_2",1,12,3.5,22000,4);
			List<Datacenter> listdc=new ArrayList<Datacenter>();
			listdc.add(datacenter1);
			listdc.add(datacenter2);
			@SuppressWarnings("unused")
			Datacenter datacenter3 = createDatacenter("Datacenter_3",4,2,9,8000,5);
			listdc.add(datacenter3);
			Datacenter datacenter4 = createDatacenter("Datacenter_4",2,4,7,10000,2);
			listdc.add(datacenter4);
			// Third step: Create Broker
			MinMinDatacenterBroker broker = createBroker();
			int brokerId = broker.getId();
			
			// Fourth step: Create one virtual machine
			vmlist = new ArrayList<PowerVm>();
			
			for(int i=0;i<200;i+=2){
				// 	VM description
					int vmid = i;
					int mips = ((i+1)*5 % 301)+10;//300
					int mips1=((i+1)*15 % 501) +20;//500
					long size = 2200; // image size (MB)
					int ram = 512; // 16gig vm memory (MB)
					int ram1=256; //32gig
					long bw = 100;
		
					String vmm = "Xen"; // VMM name

		
					// create VM
					PowerVm vm = new PowerVm(i,brokerId, mips, pesNumber, ram, bw, size, 0, vmm, new CloudletSchedulerDynamicWorkload(mips, pesNumber),1); //new CloudletSchedulerDynamicWorkload(mips, pesNumber));
				//	Vm vm = new Vm(i,brokerIdBBO, mips, pesNumber, ram, bw, size, vmm,new  CloudletSchedulerSpaceShared()); //new CloudletSchedulerDynamicWorkload(mips, pesNumber));

					// 	add the VM to the vmList
		
					vmlist.add(vm);
		
					PowerVm vm1 = new PowerVm(i+1, brokerId, mips1, pesNumber, ram1, bw, size,0 ,vmm, new CloudletSchedulerDynamicWorkload(mips, pesNumber),1); //new CloudletSchedulerDynamicWorkload(mips, pesNumber));
				//	Vm vm1 = new Vm(i+1, brokerIdBBO, mips1, pesNumber, ram1, bw, size ,vmm, new  CloudletSchedulerSpaceShared()); //new CloudletSchedulerDynamicWorkload(mips, pesNumber));

					// 	add the VM to the vmList
					vmlist.add(vm1);
				}
			// submit vm list to the broker
broker.submitVmList(vmlist);
			
			//UtilizationModel utilizationModel = new UtilizationModelStochastic(100);	
			UtilizationModel utilizationModelCPU=new UtilizationModelStochastic(1);
			UtilizationModel utilizationModelRam=new UtilizationModelStochastic(1);
			UtilizationModel utilizationModelBw=new UtilizationModelStochastic(1);
			// Fifth step: Resource Cloudlet
			cloudletList = new ArrayList<Cloudlet>();
/*for(int i=0;i<1000;i++){
//	double poissonObs =  1;//
	//poisson.setMean(i);
	int aa=poisson.nextInt();
	
	long no=normal.nextInt();
	// Cloudlet properties
	int id = 0;
	long length =no;//25000 the length or size (in MI) of this cloudlet to be executed in a PowerDatacenter
	long fileSize =3000; //300*(i+1); // the file size (in byte) of this cloudlet BEFORE submitting to a PowerDatacenter
	long outputSize = 3000; //the file size (in byte) of this cloudlet AFTER submitting to a PowerDatacenter

	Cloudlet cloudlet0 = new  Cloudlet(i, length,pesNumber , fileSize, outputSize, 
			utilizationModelCPU, utilizationModelRam, utilizationModelBw);
	
	cloudlet0.setUserId(brokerId);
	cloudlet0.setSubmissionTime(aa);
	cloudletList.add(cloudlet0);// add the cloudlet to the list
}*/
			dataset=workload.createCloudLets(brokerId);
			for(int i=0;i<1000;i++){
				dataset.get(i).SetCloudletId(i);
				dataset.get(i).setUserId(brokerId);
				cloudletList.add(dataset.get(i));
			}
broker.submitCloudletList(cloudletList);
			// Sixth step: Starts the simulation
		final double finishTime = CloudSim.startSimulation();
		List<Cloudlet> newList = broker.getCloudletReceivedList();
			CloudSim.stopSimulation();
			//Final step: Print results when simulation is over
	
			printCloudletList(newList);

			Output.DatacenterUtilization(datacenter1, datacenter2, datacenter3, datacenter4);
			Output.HostsUsed(datacenter1, datacenter2, datacenter3, datacenter4);
			Output.TheNumberOfMigratedCloudlets(newList);
			Output.TotalCloudletProcessingCost(newList);
			Output.TotalCloudletBwCost(newList, finishTime);
			Output.MakeSpan(newList);
			
			//	System.out.println(broker.convergenceTime);

			Log.printLine("CloudSimExample1 finished!");
		} catch (Exception e) {
			e.printStackTrace();
			Log.printLine("Unwanted errors happen");
		}

	}

	/**
	 * Creates the datacenter.
	 *
	 * @param name the name
	 *
	 * @return the datacenter
	 */

	// We strongly encourage users to develop their own broker policies, to
	// submit vms and cloudlets according
	// to the specific rules of the simulated scenario
	/**
	 * Creates the broker.
	 *
	 * @return the datacenter broker
	 */
	private static MinMinDatacenterBroker createBroker() {
		MinMinDatacenterBroker broker = null;
		try {
			broker = new MinMinDatacenterBroker("Broker");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}

	/**
	 * Prints the Cloudlet objects.
	 *
	 * @param list list of Cloudlets
	 */
	private static void printCloudletList(List<Cloudlet> list) {
		int size = list.size();
		Cloudlet cloudlet;

		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent
				+ "Data center ID" + indent + "VM ID" + indent + "Time" + indent
				+ "Start Time" + indent + "Finish Time");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
				Log.print("SUCCESS");

				Log.printLine(indent + indent + cloudlet.getResourceId()
						+ indent + indent + indent + cloudlet.getVmId()
						+ indent + indent
						+ dft.format(cloudlet.getActualCPUTime()) + indent
						+ indent + dft.format(cloudlet.getExecStartTime())
						+ indent + indent
						+ dft.format(cloudlet.getFinishTime()));
			}
		}
	}
	private static Datacenter createDatacenter(String name,int hosts,int pes,double timezone,int MIPS,double CostOfProcessing) {


		// Here are the steps needed to create a PowerDatacenter:
		// 1. We need to create a list to store
		// our machine
		List<PowerHostUtilizationHistory> hostList = new ArrayList<PowerHostUtilizationHistory>();

		// 2. A Machine contains one or more PEs or CPUs/Cores.
		// In this example, it will have only one core.
		List<Pe> peList = new ArrayList<Pe>();

		int mips = MIPS;
for(int i=0;i<pes;i++){
	// 3. Create PEs and add these into a list.
	peList.add(new Pe(i, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
}
		// 4. Create Host with its id and list of PEs and add them to the list
		// of machines
		int hostId = 0;
		int ram = 131072; // =128GB host memory (MB)
		long storage = 10000000; // host storage
		int bw = 10000;
int i=0;
		for(i=0;i<hosts;i++){
	hostList.add(
			new PowerHostUtilizationHistory(
				hostNo++,
				new RamProvisionerSimple(ram),
				new BwProvisionerSimple(bw),
				storage,
				peList,
				new VmSchedulerTimeShared(peList),
				new PowerModelSqrt(800,80)
			)
		); // This is our machine	
		}
		
		String arch = "x86"; // system architecture
		String os = "Linux"; // operating system
		String vmm = "Xen";
		double time_zone = timezone; // time zone this resource located
		double cost = CostOfProcessing; // 3.0; // the cost of using processing in this resource
		double costPerMem = 0.05; // the cost of using memory in this resource
		double costPerStorage = 0.001; // the cost of using storage in this
										// resource
		double costPerBw = 0.2; // the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>(); // we are not adding SAN
													// devices by now

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
				arch, os, vmm, hostList, time_zone, cost, costPerMem,
				costPerStorage, costPerBw);

		// 6. Finally, we need to create a PowerDatacenter object.
		Datacenter datacenter = null;
		try {
			//datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
			datacenter = new Datacenter(name, characteristics, new VmAllocationPolicyMinMin(hostList), storageList, 0);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;

	}
}


	

