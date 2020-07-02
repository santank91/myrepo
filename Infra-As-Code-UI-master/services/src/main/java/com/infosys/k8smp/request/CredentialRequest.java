package com.infosys.k8smp.request;

import lombok.Data;

@Data
public class CredentialRequest {
	
	private String userID;
	
	private String credentialType;	
	
	private String credentialName;
	
	private AzurePrincipal azurePrincipal;

}
