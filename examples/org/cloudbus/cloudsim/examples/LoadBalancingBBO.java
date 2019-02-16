package org.cloudbus.cloudsim.examples;

import java.io.OutputStreamWriter;

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
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.CustomDatacenterBroker;
import org.cloudbus.cloudsim.CustomDatacenterBroker2;
import org.cloudbus.cloudsim.CustomDatacenterBroker3;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.HostDynamicWorkload;
import org.cloudbus.cloudsim.HostStateHistoryEntry;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.MyPolicyNew;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.ResCloudlet;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.UtilizationModelNull;
import org.cloudbus.cloudsim.UtilizationModelStochastic;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.VmAllocationPolicyCustom;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.VmSchedulerTimeSharedOverSubscription;
import org.cloudbus.cloudsim.VmStateHistoryEntry;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.lists.CloudletList;
import org.cloudbus.cloudsim.power.PowerHostUtilizationHistory;
import org.cloudbus.cloudsim.power.PowerVm;
import org.cloudbus.cloudsim.power.models.PowerModel;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPower;
import org.cloudbus.cloudsim.power.models.PowerModelSqrt;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import cern.jet.math.Constants;
import cern.jet.random.Normal;
import cern.jet.random.Poisson;
import cern.jet.random.engine.DRand;
import cern.jet.random.engine.RandomEngine;
/**
 * A simple example showing how to create a datacenter with one host and run one
 * cloudlet on it.
 */
public class LoadBalancingBBO {

	public static int VMNo=10;
	
	public static Random rn;
	/** The cloudlet list. */
	//private static List<Cloudlet> cloudletList;
	private static List<Cloudlet> cloudletList;
	private static List<Cloudlet> dataset;

	/** The vmlist. */
	private static List<PowerVm> vmlist;
	/**
	 * Creates main() to run this example.
	 *
	 * @param args the args
	 */
	
	public static int hostNo=0;
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		List<OutputData> Dcu=new ArrayList<OutputData>();
		//for(int p=0;p<50;p++){
			
			int pesNumber = 1; // number of cpus
			rn=new Random();
			Log.printLine("Starting CloudSimExample1...");
			RandomEngine engine = new DRand();
			Poisson poisson = new Poisson(100, engine);
			Normal normal=new Normal(40000, 10000, engine);
				int num_user = 100; // number of cloud users
				Calendar calendar = Calendar.getInstance();
				boolean trace_flag = false; // mean trace events
				CloudSim.init(num_user, calendar, trace_flag);
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
				CustomDatacenterBroker2 brokerBBO = createBroker(1);
				int brokerIdBBO = brokerBBO.getId();
				List<Cloudlet> newList=null;
				 double  finishTime=0;
				try {		
				vmlist = new ArrayList<PowerVm>();
				for(int i=0;i<VMNo;i+=2){
					int vmid = i;
					int mips = ((i+1)*5 % 301)+100;//300
					int mips1=((i+1)*15 % 501)+200;//500
					long size = 2200; // image size (MB)
					int ram = 512; // 16gig vm memory (MB)
					int ram1=256; //32gig
					long bw = 100;
					String vmm = "Xen"; // VMM name
					PowerVm vm = new PowerVm(i,brokerIdBBO, mips, pesNumber, ram, bw, size, 0, vmm, new CloudletSchedulerDynamicWorkload(mips, pesNumber),1); //new CloudletSchedulerDynamicWorkload(mips, pesNumber));
					vmlist.add(vm);
					PowerVm vm1 = new PowerVm(i+1, brokerIdBBO, mips1, pesNumber, ram1, bw, size,0 ,vmm, new CloudletSchedulerDynamicWorkload(mips, pesNumber),1); //new CloudletSchedulerDynamicWorkload(mips, pesNumber));
					vmlist.add(vm1);
				}
				brokerBBO.submitVmList(vmlist);
				UtilizationModel utilizationModelCPU=new UtilizationModelStochastic(1);
				UtilizationModel utilizationModelRam=new UtilizationModelStochastic(1);
				UtilizationModel utilizationModelBw=new UtilizationModelStochastic(1);
				cloudletList = new ArrayList<Cloudlet>();
				dataset=workload.createCloudLets(brokerIdBBO);
				for(int i=0;i<1000;i++){
					dataset.get(i).SetCloudletId(i);
					dataset.get(i).setUserId(brokerIdBBO);
					cloudletList.add(dataset.get(i));
				}
	brokerBBO.submitCloudletList(cloudletList);

		
	List<Datacenter> dcs= GetDatacenters();
			 finishTime = CloudSim.startSimulation();
			 newList = brokerBBO.getCloudletReceivedList();
				printCloudletList(newList);
		CloudSim.stopSimulation();
	
	System.out.println("Convergene Time is: "+ brokerBBO.convergenceTime);
//CloudSim.terminateSimulation();

			} catch (Exception e) {
				e.printStackTrace();
				Log.printLine("Unwanted errors happen");
			

			
			
		}
				
Output.DatacenterUtilization(datacenter1, datacenter2, datacenter3, datacenter4);
//Output.HostsUsed(datacenter1, datacenter2, datacenter3, datacenter4);
//Output.MakeSpan(newList);
//Output.NewVmsCreated(brokerBBO);
//Output.overloadedVms(brokerBBO);
//Output.TheNumberOfMigratedCloudlets(cloudletList);
//Output.TotalCloudletBwCost(newList, finishTime);
//Output.TotalCloudletProcessingCost(newList);
Output.WorkloadDistribution(cloudletList, brokerBBO.getVmList(),brokerBBO,VMNo);

}
	
@Override
public void finalize() throws Throwable {
	// TODO Auto-generated method stub
	
	super.finalize();
}


    private static void showCpuUtilizationForAllHosts(final double simulationFinishTime, Datacenter datacenter0) {
        Log.printLine("\nHosts CPU utilization history for the entire simulation period");
        Log.printLine("Simulation finish time: "+ simulationFinishTime);
        int numberOfUsageHistoryEntries = 0;
        final double interval = 40;
        for (Host host : datacenter0.getHostList()) {
            for(int clock = 0; clock <= simulationFinishTime; clock+=interval){
                final double hostCpuUsage
                        = getHostTotalUtilizationOfCpuInMips(host, simulationFinishTime);
                if(hostCpuUsage > 0){
                    numberOfUsageHistoryEntries++;
                    Log.printLine(
                            " Time: "+ clock+ "     "+
                            " Host: "+ host.getId() + "     "+
                            " CPU Utilization (MIPS): "+ hostCpuUsage);
                }
            }
            Log.printLine("--------------------------------------------------");
        }
        if(numberOfUsageHistoryEntries == 0)
            Log.printLine(" No CPU usage history was found");
    }
    
    /**
     * Gets the total CPU utilization of host at a given time.
     * 
     * @param host
     * @param time
     * @return The total host CPU utilization in MIPS for the requested time
     */
    public static double getHostTotalUtilizationOfCpuInMips(Host host, double time) {
        double totalHostUtilization = 0;
        for (Vm vm : host.getDatacenter().getVmList()) {
            if(vm.getHost().equals(host)){
                totalHostUtilization += vm.getTotalUtilizationOfCpuMips(time);
            }
        }

        return totalHostUtilization;
    }
    
	/**
	 * Creates the datacenter.
	 *
	 * @param name the name
	 *
	 * @return the datacenter
	 */
	private static Datacenter createDatacenter(String name,int hosts,int pes,double timezone,int MIPS,double ProcessingCost) {


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
			//int hosttype=hostNo%org.cloudbus.cloudsim.examples.power.Constants.HOST_TYPES;
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
		double cost = ProcessingCost; // the cost of using processing in this resource
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
			datacenter = new Datacenter(name, characteristics, new VmAllocationPolicyCustom(hostList), storageList, 0);
		//	datacenter = new Datacenter(name, characteristics, new VmAllocationPolicy(hostList), storageList, 0);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;
	}

	// We strongly encourage users to develop their own broker policies, to
	// submit vms and cloudlets according
	// to the specific rules of the simulated scenario
	/**
	 * Creates the broker.
	 *
	 * @return the datacenter broker
	 */
	private static CustomDatacenterBroker2 createBroker(int id) {
		CustomDatacenterBroker2 broker = null;
		try {
			broker = new CustomDatacenterBroker2("Broker"+id);
		
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
		Cloudlet cloudlet,cloudlet2;

		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent
				+ "Data center ID" + indent + "VM ID" + indent + "Time" + indent
				+ "Start Time" + indent + "Finish Time"+ indent +"wallclock"+ indent + "#");
		DecimalFormat dft = new DecimalFormat("###.##");
		Log.enable();
		int id=0;
		for (int i = 0; i < size; i++) {
			boolean isRepeated=false;
			cloudlet = list.get(i);
			for(int j=i+1;j<size;j++){
				cloudlet2 = list.get(j);
				if(cloudlet2.getCloudletId()==cloudlet.getCloudletId()){
					isRepeated=true;
					break;
				}
			}
			if(!isRepeated){
				Log.print(cloudlet.getCloudletId() + indent + indent);
				if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
					Log.print("SUCCESS");
					Log.printLine(indent+indent+indent+ cloudlet.getResourceId()
							+ indent + indent + indent + cloudlet.getVmId()
							+ indent + indent
							+ dft.format(cloudlet.getActualCPUTime()) + indent
							+ indent + dft.format(cloudlet.getExecStartTime())
							+ indent + indent
							+ dft.format(cloudlet.getFinishTime())+ indent + cloudlet.getWallClockTime()+
							indent+ id++ +"");
				}			
			}
		}
	}
	public static List<Datacenter> GetDatacenters(){
	List<Datacenter> dcs=new ArrayList<Datacenter>();
			for(int i=0;i<5;i++){
				Datacenter datacenter=(Datacenter) CloudSim.getEntity("Datacenter_"+i);
				dcs.add(datacenter);
			}
			return dcs;
		}
	
	public static void Calculate(List<OutputData> outputs){
		double MeanMakespan=0,processCost=0,Bwutil=0,MigratedCld=0,newVms=0,overloaded=0;
		for(OutputData output:outputs){
			MeanMakespan+=output.Makespan;
			processCost+=output.TotalCloudletCost;
			Bwutil+=output.TotalCloudletBandwidth;
			MigratedCld+=output.NumberOfMigratedCloudlets;
			newVms+=output.NewVmsCreated;
			overloaded+=output.overloadedVms;
		}
		MeanMakespan=MeanMakespan/50;
		processCost=processCost/50;
		Bwutil=Bwutil/50;
		MigratedCld=MigratedCld/50;
		newVms=newVms/50;
		overloaded=overloaded/50;
		
	}
}
