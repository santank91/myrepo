package com.infosys.k8smp.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
//AWS Credentials
public class AzurePrincipal {


	@JsonProperty( "subscriptionID")
	private String subscriptionID;
	
	@JsonProperty("clientID")
	private String clientID;
	
	@JsonProperty("tenant")
	private String tenant;
	
	@JsonProperty("secret")
	private String secret;

}
