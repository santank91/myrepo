package com.infosys.k8smp.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class ClusterEnv extends BaseCacheEntity implements Serializable {
	
	public ClusterEnv(String processID) {
		super(processID);
	}

	public ClusterEnv(String processID, String[] kubeMasterIps, String[] kubeNodeIps) {
		super(processID);
		this.kubeMasterIps = kubeMasterIps;
		this.kubeNodeIps = kubeNodeIps;
	}

	private int clusterseqid;
	
	//mapped to masternodes
	private String[] kubeMasterIps;

	//mapped to workernodes
	private String[] kubeNodeIps;
	
	//mapped to apiserverurl 
	private String apiServerUrl;
	
	//will not be stored in DB, but vault
	private String k8sAdminToken;
	
	//mapped to dashboardurl
	private String k8sDashboardUrl;
	
	private String inventoryPath;
	
	//will not be stored in DB, but vault
	private String dashboardToken;

}
