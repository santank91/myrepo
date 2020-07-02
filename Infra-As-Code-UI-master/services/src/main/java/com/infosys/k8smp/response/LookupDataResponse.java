package com.infosys.k8smp.response;

import java.util.List;

import com.infosys.k8smp.config.Options;

import lombok.Data;

@Data
public class LookupDataResponse {
	
	private List<ComponentsForCredentialType> credComponentsList;
	
	private Options options;

}
