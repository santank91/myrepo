package com.infosys.k8smp.response;

import lombok.Data;

@Data
public class CreateClusterResponse {

	private String clusterID;
	
	public CreateClusterResponse(String clusterID) {
		this.clusterID = clusterID;
	}
}
