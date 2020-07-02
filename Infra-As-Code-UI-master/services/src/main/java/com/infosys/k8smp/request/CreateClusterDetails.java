package com.infosys.k8smp.request;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
public class CreateClusterDetails {

	//kuber api server url
	private String kubeApiServerUrl;

	private String k8sAdminToken;
	
	private String k8sDashboadUrl;
	
	private String dashboardToken;
	
	private JsonNode kubeConfig;
}
