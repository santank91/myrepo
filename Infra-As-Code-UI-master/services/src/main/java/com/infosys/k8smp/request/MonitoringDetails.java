package com.infosys.k8smp.request;

import lombok.Data;

@Data
public class MonitoringDetails {
	

	private String grafanaUrl;
	
	private String grafanaUser;
	
	private String grafanaPassword;

}
