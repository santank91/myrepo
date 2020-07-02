package com.infosys.k8smp.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class BaseCacheEntity implements Serializable {
	
	protected String clusterReqId;
	
	protected BaseCacheEntity(String processID) {
		this.clusterReqId = processID;		
	}

}
