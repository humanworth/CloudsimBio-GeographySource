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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.lists.CloudletList;
import org.cloudbus.cloudsim.lists.VmList;

/**
 * DatacentreBroker represents a broker acting on behalf of a user. It hides VM management, as vm
 * creation, sumbission of cloudlets to this VMs and destruction of VMs.
 * 
 * @author Rodrigo N. Calheiros
 * @author Anton Beloglazov
 * @since CloudSim Toolkit 1.0
 */
public class TestBroker extends DatacenterBroker {

    public TestBroker(String name) throws Exception {
        super(name);
    }

    @Override
    public void submitCloudlets() {
        int vmIndex = 0;
        int delay = 10;
        for (Cloudlet cloudlet : getCloudletList()) {
            Vm vm;

            if (cloudlet.getVmId() == -1) {
                vm = getVmsCreatedList().get(vmIndex);
            } else {
                vm = VmList.getById(getVmsCreatedList(), cloudlet.getVmId());

                if (vm == null) {
                    Log.printLine(CloudSim.clock() + ": " + getName() + ": Postponing execution of cloudlet "
                            + cloudlet.getCloudletId() + ": bount VM not available");
                    continue;
                }
            }

            Log.printLine(CloudSim.clock() + ": " + getName() + ": Sending cloudlet "
                    + cloudlet.getCloudletId() + " to VM #" + vm.getId());
            cloudlet.setVmId(vm.getId());
            schedule(getVmsToDatacentersMap().get(vm.getId()), 1, CloudSimTags.CLOUDLET_SUBMIT, cloudlet );
            //sendNow(getVmsToDatacentersMap().get(vm.getId()), CloudSimTags.CLOUDLET_SUBMIT, cloudlet);
            cloudletsSubmitted++;
            vmIndex = (vmIndex + 1) % getVmsCreatedList().size();
            getCloudletSubmittedList().add(cloudlet);
            //Cloudlet was submitted...checking VM Status 
            
            if (vm!=null){
                vm.updateVmProcessing(CloudSim.clock(), null);
                double currentCPU = vm.getCloudletScheduler().getTotalUtilizationOfCpu(CloudSim.clock());//.getTotalUtilizationOfCpu(CloudSim.clock());
                //TO-DO -> Use currentCPU to your business rules...
                //This will be done after you send each cloudlet
                System.out.println("Cloudlet: " + cloudlet.getCloudletId() + " - VM: " + vm.getId()
                        + " - Current CPU Usage Percent: " + currentCPU*100);
            }
            this.pause(delay);
            CloudSim.runClockTick();
        }
    }
}
