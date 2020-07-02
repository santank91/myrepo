package com.infosys.k8smp.request;

import java.util.ArrayList;

import lombok.Data;

@Data
public class UpdateClusterRequest {
	
	private String clusterID; 
	
	//private String inventoryPath;
	
	private ArrayList<ProvisionDetails> provisionDetails;
	
	private CreateClusterDetails createClusterDetails;
	
	private MonitoringDetails monitoringDetails;
	

}
