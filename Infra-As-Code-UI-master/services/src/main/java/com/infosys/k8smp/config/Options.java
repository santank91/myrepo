package com.infosys.k8smp.config;

import java.util.Map;

import lombok.Data;

@Data
public class Options {

	private Map<String, String> provider;

	private Map<String, String> masterInstTypes;

	private Map<String, String> workerInstTypes;
	
	private Map<String, String> dashboard;
	
	private Map<String, String> availZone;
	
	private Map<String, String> imageName;
	
	private Map<String, String> credentials;
	
	private Map<String, String> credentialType;	
	
}
