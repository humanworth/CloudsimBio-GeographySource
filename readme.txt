Table of Contents
-----------------

1. Directory structure of the CloudSim Toolkit
2. Software requirements: Java version 1.6 or newer 
3. Installation and running the CloudSim Toolkit
4. Important Notes.

1. Directory structure of the CloudSim Toolkit
----------------------------------------------

cloudsim/                -- top level CloudSim directory
	docs/            -- CloudSim API Documentation
	examples/        -- CloudSim examples
	jars/            -- CloudSim jar archives
	sources/         -- CloudSim source code
	tests/           -- CloudSim unit tests


2. Software requirements: Java version 1.6 or newer
---------------------------------------------------

The provided toolkit is an extended version of Cloudsim, in which we introduced a range of different scheduling algorithms through different classes. 
CloudSim-BBO has been tested and ran on Sun's Java version 1.6.0 or newer.
Older versions of Java are not compatible.
If you have non-Sun Java version, such as gcj or J++, they may not be compatible.


3. Installation and running the CloudSim Toolkit
------------------------------------------------

There are 4 steps to run our codes properly:
          1- You should import the code in IDE having cloned the codes from GitHub.
          2- Make sure the required packages in our code like  "Colt" are included in the solution.
          3- Run the intended class to see outputs from our examples, like "BBOLoadBalancing".
          4- See and note the outputs in order to perform statistical analysis.

The Directory in which our codes are provided: 
I.     BBO algorithm: ./CloudsimBio-GeographySource/examples/org/cloudbus/cloudsim/examples/LoadBalancingBBO.java
II.    Genetic Algorithm: ./CloudsimBio-GeographySource/examples/org/cloudbus/cloudsim/examples/LoadBalancingGA.java
III.   PSO Algorithm: ./CloudsimBio-GeographySource/examples/org/cloudbus/cloudsim/examples/LoadBalancingPSO.java
IV.    Round Robin: ./CloudsimBio-GeographySource/examples/org/cloudbus/cloudsim/examples/LoadBalancingRoundRobin2.java
V.     Max-Min Algorithm: ./CloudsimBio-GeographySource/examples/org/cloudbus/cloudsim/examples/LoadBalancingMaxMin.java
VI.    Min-Min Algorithm: ./CloudsimBio-GeographySource/examples/org/cloudbus/cloudsim/examples/LoadBalancingMinMin.java


4. Important Notes:

  *  We highly recommend you to use Eclipse IDE to run our codes.
  *  You can change the number of VMs, PMs, and Workloads according to your research targets.
  *  In evolutionary algorithms, you can also change the maximum number of Generations, the number of problem dimensions and the mutation probablity (Default is 0.05).
  
  

