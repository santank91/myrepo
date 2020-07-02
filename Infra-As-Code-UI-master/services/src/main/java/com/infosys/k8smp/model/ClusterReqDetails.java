package com.infosys.k8smp.model;



import java.util.Date;

import lombok.Data;

@Data
public class ClusterReqDetails {

	private String clusterReqId;
	
	private String provider;
	
	private int masterNodes;
	
	private int workerNodes;
	
	private String masterInstance;
	
	private String workerInstance;
	
	private String availZone;
	
	private String dashboardType;
	
	private boolean logEnabled;
	
	private boolean monitoringEnabled;
	
	private String credentialId;
	
	private String createUser;
	
	private String updateUser;
	
	private Date createTime;
	
	private Date updateTime;
	
	
}
