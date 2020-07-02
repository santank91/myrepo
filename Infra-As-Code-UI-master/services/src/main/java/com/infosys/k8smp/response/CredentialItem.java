package com.infosys.k8smp.response;

import lombok.Data;

@Data
public class CredentialItem {
	
	private String credentialName;
	
	private String credentialType;
	
	private String credentialId;

	public CredentialItem(String credentialName, String credentialType) {
		super();
		this.credentialName = credentialName;
		this.credentialType = credentialType;
	}

}
