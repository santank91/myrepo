package com.infosys.k8smp.vault.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Credentials implements Serializable {

	private String username;
	private String password;
	public Credentials(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	public Credentials() {
		super();
	}
	
	
}
