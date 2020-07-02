package com.infosys.k8smp.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Cluster implements Serializable {
	
	public Cluster(String processID, String status) {
		super();
		this.processID = processID;
		this.status = status;
	}

	String processID;
	
	String status;

	@Override
	public String toString() {
		return "Cluster [processID=" + processID + ", status=" + status + "]";
	}

	
}
