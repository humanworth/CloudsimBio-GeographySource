package org.cloudbus.cloudsim.examples;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.power.PowerHostUtilizationHistory;
import org.cloudbus.cloudsim.util.WorkloadFileReader;

public class DatacenterUtilization {
	public static List<HostUtiliation> HostUtilization=new ArrayList<HostUtiliation>();
	
	public int UsedHosts=0;
	
	public static List<PowerHostUtilizationHistory> Hosts=new ArrayList<PowerHostUtilizationHistory>();
	
	public DatacenterUtilization(Datacenter Dc){
		for(PowerHostUtilizationHistory host:Dc.getHostList()){
			Hosts.add(host);
		}
		GetHostUtil();
		getHostsUsed();
	}
	public  List<HostUtiliation>  GetHostUtil(){
		for(PowerHostUtilizationHistory host:Hosts){
			HostUtiliation Util=new HostUtiliation();
			Util.BwUtilization=host.getUtilizationOfBw()/host.getBw();
			Util.CputUtilization= host.getUtilizationOfCpu();
			Util.RamUtilization=host.getUtilizationOfRam()/host.getRam();
			Util.HostId=host.getId();
			double power=(host.getPowerModel().getPower(Util.CputUtilization));
			if(Util.CputUtilization==0){
				power=(host.getPowerModel().getPower(0.0000001));
			}
			Util.WastedPower=power;
			HostUtilization.add(Util);
		}
		return HostUtilization;
	}
	public int getHostsUsed(){
		int counter=0;
		for(PowerHostUtilizationHistory host:Hosts){
			
			if(host.getCompletedVms().size()>0)
			{
				counter++;
				System.out.println("Host "+host.getId()+  " Is Used");
			}
		}
		UsedHosts=counter;
		return counter;
	}
	
}
