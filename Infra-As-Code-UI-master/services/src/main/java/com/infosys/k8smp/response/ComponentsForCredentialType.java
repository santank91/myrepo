package com.infosys.k8smp.response;

import java.util.List;

import lombok.Data;

@Data
public class ComponentsForCredentialType {
	
	private String credentialType;
	
	List<UIComponent> components;

}
