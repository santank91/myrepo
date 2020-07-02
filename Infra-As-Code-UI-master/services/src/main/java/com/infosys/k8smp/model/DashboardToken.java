package com.infosys.k8smp.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class DashboardToken implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String dashboardToken;

	public DashboardToken(String dashboardToken) {
		super();
		this.dashboardToken = dashboardToken;
	}
	
	

}
