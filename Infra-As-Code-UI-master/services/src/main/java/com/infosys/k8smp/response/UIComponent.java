package com.infosys.k8smp.response;

import lombok.Data;

@Data
public class UIComponent {
	private String name;

	private String value;

	public UIComponent(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	
	
}
