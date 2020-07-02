package com.infosys.k8smp.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Credentials {
	
	@JsonProperty( "subscriptionID")
	private String subscriptionID;
	
	@JsonProperty("clientID")
	private String clientID;
	
	@JsonProperty("tenant")
	private String tenant;
	
	@JsonProperty("secret")
	private String secret;

}
