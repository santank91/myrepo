package com.infosys.k8smp.request;

import lombok.Data;

//Shared Model for ansible, UI view
@Data
public class ProvisionRequest extends BaseRequest {

	//NOT for ansible
	private String cloudSrvc;
	
	private String masterCount;
	
	private String nodeCount;
	
	private String masterSize;
	
	private String nodeSize;
	
	private String availZone;
	
	private String clusterName;
	
	//removed in sprint4
	//private AzurePrincipal credentials;
	
	private String credentialKey;
	
	private String imageName;
	
	private String kubeDashboard;
	
	//TODO NOT REQUIRED- to remove
	private String logEnabled;
	
	//NOT for ansible
	private String monitoringEnabled;	
	
	//NOT for ansible
	private String credentialName;
	
	private String kubeMonitoring;

}
