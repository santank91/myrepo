package com.infosys.k8smp.request;

import lombok.Data;

@Data
public class VaultRequest {
	
	private String userID;
	
	private String credentialType;	
	
	private String credentialName;
	
	private AzurePrincipal azurePrincipal;
	
	private String apiServerToken;
	
	private String dashboardToken;
	
	private String clusterReqId; 

}
