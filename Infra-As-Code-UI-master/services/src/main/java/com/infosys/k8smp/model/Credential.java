package com.infosys.k8smp.model;

import lombok.Data;

@Data
public class Credential {
	
	private String credentialName;
	
	private String credentialType;
	
	private String userID;
	
	private String status;
	
	

	public Credential(String credentialName, String k8smpuser,String credentialType, String status) {
		super();
		this.credentialName = credentialName;
		this.userID = k8smpuser;
		this.credentialType = credentialType;
		this.status = status;
	}



	public Credential() {
		super();
	}
	
	
	

}
