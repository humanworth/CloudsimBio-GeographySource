package org.cloudbus.cloudsim.examples;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CustomDatacenterBroker2;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.power.PowerHostUtilizationHistory;
import org.cloudbus.cloudsim.power.PowerVm;


public class OutputData {
	
	public static int NewVmsCreated=0;
	
	public static int overloadedVms=0;
	
	public static int NumberOfMigratedCloudlets=0;
	
	public static double TotalCloudletCost=0;

	public static double TotalCloudletBandwidth=0;
	public static List<HostUtiliation>  HostsUtil=new ArrayList<HostUtiliation>();
	
	public static double Makespan=0;
	public static List<DatacenterUtilization> DCUtili=new ArrayList<DatacenterUtilization>();

	public static List<Datacenter> Dcs=new ArrayList<Datacenter>();
	
public OutputData(Datacenter Dc1,Datacenter Dc2, Datacenter Dc3, Datacenter Dc4,List<Cloudlet> FinishedList,CustomDatacenterBroker2 broker,double FinishTime){
	Dcs.add(Dc1);
	Dcs.add(Dc2);
	Dcs.add(Dc3);
	Dcs.add(Dc4);
	TheNumberOfMigratedCloudlets(FinishedList);
	TotalCloudletProcessingCost(FinishedList);
	TotalCloudletBwCost(FinishedList, FinishTime);
	getMakeSpan(FinishedList);
	NewVmsCreated(broker);
	overloadedVms(broker);
}


public static void NewVmsCreated(CustomDatacenterBroker2 broker) {
		/** The cloudlet list. */
	
	List<PowerVm> newVmsCreated=broker.getNewVmsCreated();
	for(PowerVm vm: newVmsCreated){
			System.out.println("Vm " + vm.getId() +" Has been Created");
	}
NewVmsCreated=newVmsCreated.size();
	//Read Cloudlets from workload file in the swf format
	}

public static void overloadedVms(CustomDatacenterBroker2 broker) {		/** The cloudlet list. */
	List<PowerVm> newVmsCreated=broker.getOverloadedVms();
	for(PowerVm vm: newVmsCreated){
			System.out.println("Vm " + vm.getId() +" Has been overloaded");
	}
	overloadedVms=newVmsCreated.size();
	}

public static List<DatacenterUtilization> getDatacenterUtilization(){
	for(Datacenter dc:Dcs){
		DatacenterUtilization DcUtil=new DatacenterUtilization(dc);
		HostsUtil.addAll(DcUtil.GetHostUtil());
		DCUtili.add(DcUtil);
	}
	return DCUtili;
}
public static void TheNumberOfMigratedCloudlets(List<Cloudlet> cloudlets){
	int Counter=0;
	for(Cloudlet cloudlet:cloudlets){
		if(cloudlet.getIsMigrated()){
			Counter++;
		}
	}
NumberOfMigratedCloudlets=Counter;	
}
public static void TotalCloudletProcessingCost(List<Cloudlet> cloudlets){
	double Cost=0;
	for(Cloudlet cloudlet:cloudlets){
		Cost+=cloudlet.getProcessingCost();
	}
TotalCloudletCost=Cost;

}

public static void TotalCloudletBwCost(List<Cloudlet> cloudlets,double FinishTime){
	double Cost=0;
	for(Cloudlet cloudlet:cloudlets){
		Cost+=cloudlet.getUtilizationOfBw(FinishTime);
	}
	TotalCloudletBandwidth=Cost;
}	
public static void getMakeSpan(List<Cloudlet> cloudlets){
	double makespan=0;
	for(Cloudlet cloudlet:cloudlets){
		if(makespan<cloudlet.getFinishTime()){
			makespan=cloudlet.getFinishTime();
		}
	}
Makespan=makespan;
}

public static void HostsUsed(Datacenter dc1,Datacenter dc2,Datacenter dc3,Datacenter dc4){
	List<PowerHostUtilizationHistory> hosts=dc1.getHostList();
	int used=0,unused=0,totalUsed=0,totalUnused=0;
	for(PowerHostUtilizationHistory host:hosts){
		
		if(host.getCompletedVms().size()>0)
		{
			used++;totalUsed++;
			System.out.println("Host "+host.getId()+  " Is Used");
		}else{
			unused++;totalUnused++;
			System.out.println("Host "+host.getId()+  " Is Unused");
		}
	}
	System.out.println("DC"+dc1.getId()+" has "+used+ " Used Hosts and "+unused+" Unused  Hosts");
	 used=0;unused=0;
		List<PowerHostUtilizationHistory> hosts2=dc2.getHostList();

	for(PowerHostUtilizationHistory host:hosts2){
		
		if(host.getCompletedVms().size()>0)
		{
			used++;totalUsed++;
			System.out.println("Host "+host.getId()+  " Is Used");
		}else{
			unused++;totalUnused++;
			System.out.println("Host "+host.getId()+  " Is Unused");
		}
	}
	System.out.println("DC"+dc2.getId()+" has "+used+ " Used Hosts and "+unused+" Unused  Hosts");

	List<PowerHostUtilizationHistory> hosts3=dc3.getHostList();
	used=0;unused=0;
	for(PowerHostUtilizationHistory host:hosts3){
		
		if(host.getCompletedVms().size()>0)
		{
			used++;totalUsed++;
			System.out.println("Host "+host.getId()+  " Is Used");
		}else{
			unused++;totalUnused++;
			System.out.println("Host "+host.getId()+  " Is Unused");
		}
	}
	System.out.println("DC"+dc3.getId()+" has "+used+ " Used Hosts and "+unused+" Unused  Hosts");

	List<PowerHostUtilizationHistory> hosts4=dc4.getHostList();

	used=0;unused=0;
	for(PowerHostUtilizationHistory host:hosts4){
		
		if(host.getCompletedVms().size()>0)
		{
			used++;totalUsed++;
			System.out.println("Host "+host.getId()+  " Is Used");
		}else{
			unused++;totalUnused++;
			System.out.println("Host "+host.getId()+  " Is Unused");
		}
	}
	System.out.println("DC"+dc4.getId()+" has "+used+ " Used Hosts and "+unused+" Unused  Hosts");
	
	System.out.println("   ");
	System.out.println("Total Used Hosts Are "+totalUsed+ " TotalUnused Hosts Are "+totalUnused);

}

}
