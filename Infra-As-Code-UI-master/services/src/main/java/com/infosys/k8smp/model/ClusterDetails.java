package com.infosys.k8smp.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class ClusterDetails extends K8SCluster implements Serializable {
	
	public ClusterDetails(String processID) {
		super(processID);
	}

	public ClusterDetails(String processID, String[] kubeMasterIps, String[] kubeNodeIps) {
		super(processID);
		this.kubeMasterIps = kubeMasterIps;
		this.kubeNodeIps = kubeNodeIps;
	}

	private String[] kubeMasterIps;

	private String[] kubeNodeIps;
	
	private String clusterName;
	
	private String k8sAdminToken;
	
	private String k8sDashboardUrl;
	
	private String inventoryPath;
	
	private String dashboardToken;

}
