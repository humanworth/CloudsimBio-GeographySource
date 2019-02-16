package org.cloudbus.coudsim.examples.PSO;

import org.cloudbus.cloudsim.examples.LoadBalancingPSO;

/* author: gandhi - gandhi.mtm [at] gmail [dot] com - Depok, Indonesia */

// this is an interface to keep the configuration for the PSO
// you can modify the value depends on your needs

public interface PSOConstants {
	int SWARM_SIZE = 200;
	int MAX_ITERATION = 100;
	int PROBLEM_DIMENSION = LoadBalancingPSO.VMNo;
	double C1 = 2.0;
	double C2 = 2.0;
	double W_UPPERBOUND = 1.0;
	double W_LOWERBOUND = 0.0;
}
