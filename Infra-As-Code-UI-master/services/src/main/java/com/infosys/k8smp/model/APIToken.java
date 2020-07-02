package com.infosys.k8smp.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class APIToken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String k8sAdminToken;

	public APIToken(String k8sAdminToken) {
		super();
		this.k8sAdminToken = k8sAdminToken;
	}
	
	

}
