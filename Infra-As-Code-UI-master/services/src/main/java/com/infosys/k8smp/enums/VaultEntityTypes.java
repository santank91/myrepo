package com.infosys.k8smp.enums;

public enum VaultEntityTypes {

	AZURE_PRINCIPAL("azureServicePrincipal"),

	API_TOKEN("kubeApiToken"),
	
	DASHBOARD_TOKEN("dashboardToken");

	private String vaultEntityType;

	VaultEntityTypes(String value) {
		this.vaultEntityType = value;
	}

	public String getKeyValue() {
		return this.vaultEntityType;
	}

}
