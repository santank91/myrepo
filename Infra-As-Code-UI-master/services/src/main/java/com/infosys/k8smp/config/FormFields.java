package com.infosys.k8smp.config;

import java.util.Map;

import lombok.Data;

@Data
public class FormFields {

	private Map<String, String> azureServicePrincipal;

	private Map<String, String> awsCredential;

}
