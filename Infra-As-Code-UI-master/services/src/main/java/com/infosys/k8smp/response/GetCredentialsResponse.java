package com.infosys.k8smp.response;

import java.util.List;

import lombok.Data;

@Data
public class GetCredentialsResponse {
	
	List<CredentialItem> credentialNames;

	public GetCredentialsResponse(List<CredentialItem> credentialNames) {
		super();
		this.credentialNames = credentialNames;
	}
	
	

}
