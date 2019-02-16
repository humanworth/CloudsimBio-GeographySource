package org.cloudbus.coudsim.examples.PSO;

import java.util.List;

/* author: gandhi - gandhi.mtm [at] gmail [dot] com - Depok, Indonesia */

// bean class to represent location

public class Location {
	// store the Location in an array to accommodate multi-dimensional problem space
	private double[] loc;

	public Location(double[] loc) {
		super();
		this.loc = loc;
	}
	public void NormalizeLocation(int MinDomain,int MaxDomain) {
		double[] repeated=new double[(MaxDomain-MinDomain)*(MaxDomain-MinDomain+1)/2];int index=-1;
		for(int i=0;i<repeated.length;i++)repeated[i]=-1;
	//	for(int i=0;i<MaxDomain-MinDomain;i++)repeated[i]=-1;
		for(int i=0;i<loc.length-1;i++){
			for(int j=i+1;j<loc.length;j++){
				if(loc[i]==loc[j]) {
					index++;
					
					repeated[index]=j;
				}
			}
		}
		for(int i=0;i<repeated.length && repeated[i]>=0;i++){	
			for(int number=MinDomain;number<MaxDomain;number++){
				boolean exists=false;
				for(int k=0;k<loc.length;k++){
					if(loc[k]==number){
						exists=true;
						break;
					}
				}
				if(!exists){
					loc[(int)repeated[i]]=number;
				}
				
			}	
		}

		
	}
	public double[] getLoc() {
		return loc;
	}
	public void printLoc(){
for(double i:loc){
	System.out.print("  "+i+"  ");
}
	}

	public void setLoc(double[] loc) {
		this.loc = loc;
	}
	public int Size() {
		return loc.length;
	}
	public int getSwarm(int position) {
		return (int)loc[position];
	}
}
