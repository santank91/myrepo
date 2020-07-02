package com.infosys.k8smp.config;

public enum CacheKeysEnum {
	
	CLUSTER_STATUS("ClusterStatus"),
	
	CLUSTER_DETAILS("ClusterDetails");	
	
	private String cacheKeyValue;
	
	CacheKeysEnum(String value) {
		this.cacheKeyValue  = value;
	}
	
	public String getKeyValue() {
		return this.cacheKeyValue;
	}

}
