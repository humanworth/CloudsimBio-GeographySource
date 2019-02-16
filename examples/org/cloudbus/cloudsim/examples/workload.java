package org.cloudbus.cloudsim.examples;

import java.io.FileNotFoundException;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.util.WorkloadFileReader;

public class workload {
	public static List<Cloudlet> createCloudLets(int userId) throws FileNotFoundException{

		/** The cloudlet list. */
		List<Cloudlet> cloudletList;

		//Read Cloudlets from workload file in the swf format
		WorkloadFileReader workloadFileReader = new WorkloadFileReader("D:\\Education\\cloudsim\\Workload Dataset\\SDSC-BLUE-2000-4.2-cln.swf", 1);
workloadFileReader.setUserId(userId);
		//generate cloudlets from workload file
		cloudletList = workloadFileReader.generateWorkload();

		return cloudletList;
		}
}
