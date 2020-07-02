package com.infosys.k8smp.enums;

public enum CredentialTypeEnum {
	
	AZURE_PRINCIPAL("azureServicePrincipal"),
	
	AWS_CREDENTIAL("awsCredential");
		
	private String cacheKeyValue;
	
	CredentialTypeEnum(String value) {
		this.cacheKeyValue  = value;
	}
	
	public String getKeyValue() {
		return this.cacheKeyValue;
	}

}
