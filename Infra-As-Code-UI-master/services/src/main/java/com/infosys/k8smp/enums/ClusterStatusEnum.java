package com.infosys.k8smp.enums;

public enum ClusterStatusEnum {
	
	IN_PROGRESS("InProgress"),
	
	PROV_COMPLETED("Provision"),
	
	READY("Ready"),
	
	FAILURE("Failure");
	
	private String cacheKeyValue;
	
	ClusterStatusEnum(String value) {
		this.cacheKeyValue  = value;
	}
	
	public String getKeyValue() {
		return this.cacheKeyValue;
	}

}
