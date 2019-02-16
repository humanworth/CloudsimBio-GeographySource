package org.cloudbus.cloudsim.examples;

import java.io.FileNotFoundException;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CustomDatacenterBroker2;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.power.PowerHostUtilizationHistory;
import org.cloudbus.cloudsim.power.PowerVm;
import org.cloudbus.cloudsim.util.WorkloadFileReader;


public class Output {
	

	
	
	
	
	public static void NewVmsCreated(CustomDatacenterBroker2 broker) {		/** The cloudlet list. */
		int Counter=0;
		List<PowerVm> newVmsCreated=broker.getNewVmsCreated();
		for(PowerVm vm: newVmsCreated){
				System.out.println("Vm " + vm.getId() +" Has been Created");
		}
		System.out.println("Total Created Vms: "+newVmsCreated.size());
		//Read Cloudlets from workload file in the swf format
		}
	public static void overloadedVms(CustomDatacenterBroker2 broker) {		/** The cloudlet list. */
		List<PowerVm> newVmsCreated=broker.getOverloadedVms();
		for(PowerVm vm: newVmsCreated){
				System.out.println("Vm " + vm.getId() +" Has been overloaded");
		}
		System.out.println("Total Overloaded Vms: "+newVmsCreated.size());
		//Read Cloudlets from workload file in the swf format
		}
	public static double getMaxCpuUtil(double[] UtilHistory){
		double max=-1;
		for(double util:UtilHistory){
			if(util>=max){
				max=util;
			}
				
		}
		return max;
	}
	public static void DatacenterUtilization(Datacenter datacenter1,Datacenter datacenter2,Datacenter datacenter3,Datacenter datacenter4){
		for(PowerHostUtilizationHistory h:datacenter1.getHostList()){
			double RamUtil=0,BwUtil=0,cpuUtil=0;
			cpuUtil=h.getUtilizationOfCpu();
			RamUtil= h.getUtilizationOfRam()/h.getRam();
			double util2=0;
			BwUtil= h.getUtilizationOfBw()/h.getBw();
			if(cpuUtil==0.0d){
			for(double util:h.getUtilizationHistory()){
				util2+=util;
			}
			//util2/=h.getUtilizationHistory().length;
			util2=getMaxCpuUtil(h.getUtilizationHistory());
			double power=(h.getPowerModel().getPower(util2));
			if(util2==0){
				power=(h.getPowerModel().getPower(0.000001));
			}
			System.out.println("DC"+datacenter1.getId()+": Host: "+h.getId() +"  Utilizations of  Cpu : "+util2+"     Bw: "+BwUtil+"      Ram: "+RamUtil+"    Power:"+power);
			}else{
				double power=(h.getPowerModel().getPower(cpuUtil));
				if(cpuUtil==0){
					power=(h.getPowerModel().getPower(0.000001));
				}
				System.out.println("DC"+datacenter1.getId()+": Host: "+h.getId() +"  Utilizations of  Cpu : "+cpuUtil+"     Bw: "+BwUtil+"      Ram: "+RamUtil+"    Power:"+power);
			}
		}
		
		for(PowerHostUtilizationHistory h:datacenter2.getHostList()){
			double RamUtil=0,BwUtil=0,cpuUtil=0;
			cpuUtil=h.getUtilizationOfCpu();
			RamUtil= h.getUtilizationOfRam()/h.getRam();
			double util2=0;
			BwUtil= h.getUtilizationOfBw()/h.getBw();
			if(cpuUtil==0.0d){
			for(double util:h.getUtilizationHistory()){
				util2+=util;
			}
			//util2/=h.getUtilizationHistory().length;
			util2=getMaxCpuUtil(h.getUtilizationHistory());
			double power=(h.getPowerModel().getPower(util2));
			if(util2==0){
				power=(h.getPowerModel().getPower(0.000001));
			}
			System.out.println("DC"+datacenter2.getId()+": Host: "+h.getId() +"  Utilizations of  Cpu : "+util2+"     Bw: "+BwUtil+"      Ram: "+RamUtil+"    Power:"+power);
			}else{
				double power=(h.getPowerModel().getPower(cpuUtil));
				if(cpuUtil==0){
					power=(h.getPowerModel().getPower(0.000001));
				}
				System.out.println("DC"+datacenter2.getId()+": Host: "+h.getId() +"  Utilizations of  Cpu : "+cpuUtil+"     Bw: "+BwUtil+"      Ram: "+RamUtil+"    Power:"+power);
			}
		}
		
		
		for(PowerHostUtilizationHistory h:datacenter3.getHostList()){

			double RamUtil=0,BwUtil=0,cpuUtil=0;
			cpuUtil=h.getUtilizationOfCpu();
			RamUtil= h.getUtilizationOfRam()/h.getRam();
			double util2=0;
			BwUtil= h.getUtilizationOfBw()/h.getBw();
			if(cpuUtil==0.0d){
			for(double util:h.getUtilizationHistory()){
				util2+=util;
			}
			//util2/=h.getUtilizationHistory().length;
			util2=getMaxCpuUtil(h.getUtilizationHistory());
			double power=(h.getPowerModel().getPower(util2));
			if(util2==0){
				power=(h.getPowerModel().getPower(0.000001));
			}
			System.out.println("DC"+datacenter3.getId()+": Host: "+h.getId() +"  Utilizations of  Cpu : "+util2+"     Bw: "+BwUtil+"      Ram: "+RamUtil+"    Power:"+power);
			}else{
				double power=(h.getPowerModel().getPower(cpuUtil));
				if(cpuUtil==0){
					power=(h.getPowerModel().getPower(0.000001));
				}
				System.out.println("DC"+datacenter3.getId()+": Host: "+h.getId() +"  Utilizations of  Cpu : "+cpuUtil+"     Bw: "+BwUtil+"      Ram: "+RamUtil+"    Power:"+power);
			}
			
		}
		for(PowerHostUtilizationHistory h:datacenter4.getHostList()){
			
			double RamUtil=0,BwUtil=0,cpuUtil=0;
			cpuUtil=h.getUtilizationOfCpu();
			RamUtil= h.getUtilizationOfRam()/h.getRam();
			double util2=0;
			BwUtil= h.getUtilizationOfBw()/h.getBw();
			if(cpuUtil==0.0d){
			for(double util:h.getUtilizationHistory()){
				util2+=util;
			}
			//util2/=h.getUtilizationHistory().length;
			util2=getMaxCpuUtil(h.getUtilizationHistory());
			double power=(h.getPowerModel().getPower(util2));
			if(util2==0){
				power=(h.getPowerModel().getPower(0.000001));
			}
			System.out.println("DC"+datacenter4.getId()+": Host: "+h.getId() +"  Utilizations of  Cpu : "+util2+"     Bw: "+BwUtil+"      Ram: "+RamUtil+"    Power:"+power);
			}else{
				double power=(h.getPowerModel().getPower(cpuUtil));
				if(cpuUtil==0){
					power=(h.getPowerModel().getPower(0.000001));
				}
				System.out.println("DC"+datacenter4.getId()+": Host: "+h.getId() +"  Utilizations of  Cpu : "+cpuUtil+"     Bw: "+BwUtil+"      Ram: "+RamUtil+"    Power:"+power);
			}
			
		}		
    }
	public static void TheNumberOfMigratedCloudlets(List<Cloudlet> cloudlets){
		int Counter=0;
		for(Cloudlet cloudlet:cloudlets){
			if(cloudlet.getIsMigrated()){
				Counter++;
			}
		}
		System.out.println("The number of Migrated Cloudlets are:"+Counter);
		
	}
	public static void TotalCloudletProcessingCost(List<Cloudlet> cloudlets){
		double Cost=0;
		for(Cloudlet cloudlet:cloudlets){
			Cost+=cloudlet.getProcessingCost();
		}
		System.out.println("Total Cloudlet Processing Cost is:"+Cost);
		
	}	
	
	public static void WorkloadDistribution(List<Cloudlet> cloudlets, List<PowerVm> list,CustomDatacenterBroker2 broker, int ProblemDomain){
		double Cost=0;
		List<PowerVm> overloadedVms=broker.getOverloadedVms();

		for(Vm vm:list){
			//System.out.println("VM Id: "+vm.getId());
			for(Cloudlet cloudlet:cloudlets){
			if(vm.getId()==cloudlet.getVmId()){
				System.out.print("  "+cloudlet.getCloudletId()+"  ");
			}
			
			}
			for(PowerVm overload:overloadedVms){
				if(overload.getId()==vm.getId())
					System.out.print("OF_REDANDANT_VM:"+(int)(overload.getId()+ProblemDomain));
			}
			System.out.println("----");

		}

		
	}	
	
	
	
	public static void TotalCloudletBwCost(List<Cloudlet> cloudlets,double FinishTime){
		double Cost=0;
		for(Cloudlet cloudlet:cloudlets){
			Cost+=cloudlet.getUtilizationOfBw(FinishTime);
		}
		System.out.println("Total Cloudlet Bandwidth Utilization is:"+Cost);
	}	
	public static void MakeSpan(List<Cloudlet> cloudlets){
		double makespan=0;
		for(Cloudlet cloudlet:cloudlets){
			if(makespan<cloudlet.getFinishTime()){
				makespan=cloudlet.getFinishTime();
			}
		}
		System.out.println("Makespan is:"+makespan);
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
