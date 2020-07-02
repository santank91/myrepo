package com.infosys.k8smp.enums;

public enum CloudSvcTypes {
	
	AZURENATIVE("AzureNative"),
	
	AKS("Aks");
	
	private String cloudSvcType;
	
	CloudSvcTypes(String value) {
		this.cloudSvcType  = value;
	}
	
	public String getKeyValue() {
		return this.cloudSvcType;
	}


}
