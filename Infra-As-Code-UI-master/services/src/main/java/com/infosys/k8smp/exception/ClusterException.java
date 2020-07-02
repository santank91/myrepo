package com.infosys.k8smp.exception;

public class ClusterException extends RuntimeException {
	
	private String customMessage;
	
	public ClusterException(String message) {
		this.customMessage = message;
	}
	
	public ClusterException(String message, Exception exception) {
		super(exception);
		this.customMessage = message;
	}

}
