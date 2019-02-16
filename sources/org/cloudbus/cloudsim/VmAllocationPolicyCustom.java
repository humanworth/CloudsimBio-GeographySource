/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerHostUtilizationHistory;

/**
 * VmAllocationPolicySimple is an VmAllocationPolicy that chooses, as the host for a VM, the host
 * with less PEs in use.
 * 
 * @author Rodrigo N. Calheiros
 * @author Anton Beloglazov
 * @since CloudSim Toolkit 1.0
 */
public class VmAllocationPolicyCustom extends VmAllocationPolicy {

	/** The vm table. */
	private Map<String, Host> vmTable;

	public List<? extends Host> totalHosts;
	/** The used pes. */
	private Map<String, Integer> usedPes;

	/** The free pes. */
	private List<Integer> freePes;

	public static int tryto=0;
	public static double PesCounter=0;
	/**
	 * Creates the new VmAllocationPolicySimple object.
	 * 
	 * @param list the list
	 * @pre $none
	 * @post $none
	 */
	public VmAllocationPolicyCustom(List<? extends Host> list) {
		super(list);

		setFreePes(new ArrayList<Integer>());
		for (Host host : getHostList()) {
			getFreePes().add(host.getNumberOfPes());

		}

		setVmTable(new HashMap<String, Host>());
		setUsedPes(new HashMap<String, Integer>());
	}
	
	
	public static List<PowerHostUtilizationHistory> GetAllHosts() {
		List<PowerHostUtilizationHistory> hostList2=new ArrayList<PowerHostUtilizationHistory>();
		List<PowerHostUtilizationHistory> DCHost=new ArrayList<PowerHostUtilizationHistory>();
		for(int i=1;i<5;i++){
			Datacenter datacenter=(Datacenter) CloudSim.getEntity("Datacenter_"+i);
			DCHost=datacenter.getHostList();
			for(PowerHostUtilizationHistory hos:DCHost){
				hostList2.add(hos);
			}
		}
		return hostList2;
	}
	/**
	 * Allocates a host for a given VM.
	 * 
	 * @param vm VM specification
	 * @return $true if the host could be allocated; $false otherwise
	 * @pre $none
	 * @post $none
	 */
	@Override
	public boolean allocateHostForVm(Vm vm) {
		int requiredPes = vm.getNumberOfPes();
		
		long requiredRam=vm.getRam();
		long totalRam=0;
		long totalBw=0;
		long totalcpupower=0;
		boolean result = false;
		int tries = 0;
		totalHosts=GetAllHosts();
		this.setHostList(totalHosts);
		List<Integer> freePesTmp = new ArrayList<Integer>();
		for (Integer freePes : getFreePes()) {
			freePesTmp.add(freePes);
		}
		if(this.tryto==0){
			getFreePes().clear();

			for (Host host : getHostList()) {
				getFreePes().add(host.getNumberOfFreePes());
			}
		}
		
for(Host h:totalHosts){
	totalRam=h.getRamProvisioner().getAvailableRam();
	totalcpupower=(long) (h.getAvailableMips());
	totalBw=h.getBwProvisioner().getAvailableBw();
	if((totalRam-requiredRam)>(totalRam/5)){
		if(totalcpupower-vm.getMips()>totalcpupower/5){
			if(totalBw-vm.getBw()>totalBw/5){
				if (!getVmTable().containsKey(vm.getUid())) {
					Host host;
					if(getFreePes().get(h.getId())<=0){
						host=getHostList().get((int) ((PesCounter+1)%getHostList().size()));
						PesCounter++;
						if(getFreePes().get((int) ((PesCounter+1)%getHostList().size()))>0)
							continue;
					}else{
						host=h;
					}
					result = host.vmCreate(vm);
					
					if (result) { // if vm were succesfully created in the host
						
						getVmTable().put(vm.getUid(), host);
						getUsedPes().put(vm.getUid(), requiredPes);
						getFreePes().set(host.getId(), freePesTmp.get(host.getId()) - requiredPes);
						tryto++;
					//	result = true;
						this.setFreePes(getFreePes());
					//	vm.setHost(host);
						break;
				}
				//	else{
				//		result=false;
					continue;	
				//	}
			}
		}
	}
 }

} //breaks from this point

		return result;
	}

	/**
	 * Releases the host used by a VM.
	 * 
	 * @param vm the vm
	 * @pre $none
	 * @post none
	 */
	@Override
	public void deallocateHostForVm(Vm vm) {
		Host host = getVmTable().remove(vm.getUid());
		if(host!=null){
			int idx = getHostList().indexOf(host);
			int pes = getUsedPes().remove(vm.getUid());
			if (host != null) {
				host.vmDestroy(vm);
				getFreePes().set(idx, getFreePes().get(idx) + pes);
			}			
		}

	}

	/**
	 * Gets the host that is executing the given VM belonging to the given user.
	 * 
	 * @param vm the vm
	 * @return the Host with the given vmID and userID; $null if not found
	 * @pre $none
	 * @post $none
	 */
	@Override
	public Host getHost(Vm vm) {
		return getVmTable().get(vm.getUid());
	}

	/**
	 * Gets the host that is executing the given VM belonging to the given user.
	 * 
	 * @param vmId the vm id
	 * @param userId the user id
	 * @return the Host with the given vmID and userID; $null if not found
	 * @pre $none
	 * @post $none
	 */
	@Override
	public Host getHost(int vmId, int userId) {
		return getVmTable().get(Vm.getUid(userId, vmId));
	}

	/**
	 * Gets the vm table.
	 * 
	 * @return the vm table
	 */
	public Map<String, Host> getVmTable() {
		return vmTable;
	}

	/**
	 * Sets the vm table.
	 * 
	 * @param vmTable the vm table
	 */
	protected void setVmTable(Map<String, Host> vmTable) {
		this.vmTable = vmTable;
	}

	/**
	 * Gets the used pes.
	 * 
	 * @return the used pes
	 */
	protected Map<String, Integer> getUsedPes() {
		return usedPes;
	}

	/**
	 * Sets the used pes.
	 * 
	 * @param usedPes the used pes
	 */
	protected void setUsedPes(Map<String, Integer> usedPes) {
		this.usedPes = usedPes;
	}

	/**
	 * Gets the free pes.
	 * 
	 * @return the free pes
	 */
	protected List<Integer> getFreePes() {
		return freePes;
	}

	/**
	 * Sets the free pes.
	 * 
	 * @param freePes the new free pes
	 */
	protected void setFreePes(List<Integer> freePes) {
		this.freePes = freePes;
	}

	/*
	 * (non-Javadoc)
	 * @see cloudsim.VmAllocationPolicy#optimizeAllocation(double, cloudsim.VmList, double)
	 */
	@Override
	public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> vmList) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.cloudbus.cloudsim.VmAllocationPolicy#allocateHostForVm(org.cloudbus.cloudsim.Vm,
	 * org.cloudbus.cloudsim.Host)
	 */
	@Override
	public boolean allocateHostForVm(Vm vm, Host host) {
		if (host.vmCreate(vm)) { // if vm has been succesfully created in the host
			getVmTable().put(vm.getUid(), host);

			int requiredPes = vm.getNumberOfPes();
			int idx = getHostList().indexOf(host);
			getUsedPes().put(vm.getUid(), requiredPes);
			getFreePes().set(idx, getFreePes().get(idx) - requiredPes);

			Log.formatLine(
					"%.2f: VM #" + vm.getId() + " has been allocated to the host #" + host.getId(),
					CloudSim.clock());
			return true;
		}

		return false;
	}
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}
}
