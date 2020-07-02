package com.infosys.k8smp.request;

import lombok.Data;

@Data
public class GetCredentialsRequest {
	
	private String userID;
	
	private String credentialType;

}
