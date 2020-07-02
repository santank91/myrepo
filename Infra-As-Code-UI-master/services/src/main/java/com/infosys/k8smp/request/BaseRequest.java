package com.infosys.k8smp.request;

import lombok.Data;

@Data
public class BaseRequest {

	
	private String clusterID;
	
	
	private String callbackUrl;
}
