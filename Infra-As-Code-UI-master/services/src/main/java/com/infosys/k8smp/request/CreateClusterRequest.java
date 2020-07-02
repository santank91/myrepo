package com.infosys.k8smp.request;

import lombok.Data;

@Data
public class CreateClusterRequest extends BaseRequest {
	
	private String cloudSrvc;

	private String clusterID;

	private String kubeUser;

	private String[] openPorts;

	private String[] instPkgs;

	private String[] enblSvcs;

	private String callbackUrl;
	
	private String kubeDashboard;
	
	private String clusterName;
	
	private String kubeMonitoring;

}
