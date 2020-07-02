package com.infosys.k8smp.request;

import lombok.Data;

@Data
public class ProvisionDetails {
	
	private String clusterName;
	private String nodeName;
	private String nodeType;
	private String privateIP;
	private String publicIP;
	private String iK8MPClusterName;
	private String iK8MPNodeName;
	private String iK8MPNodeType;
	

}
