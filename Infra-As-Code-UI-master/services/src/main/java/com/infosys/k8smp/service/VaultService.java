package com.infosys.k8smp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponseSupport;

import com.infosys.k8smp.enums.K8SMPConstants;
import com.infosys.k8smp.enums.VaultEntityTypes;
import com.infosys.k8smp.model.APIToken;
import com.infosys.k8smp.model.DashboardToken;
import com.infosys.k8smp.request.AzurePrincipal;
import com.infosys.k8smp.request.VaultRequest;

@Service
public class VaultService {

	@Autowired
	private VaultTemplate vaultTemplate;

	private static final Logger logger = LoggerFactory.getLogger(VaultService.class);

	public void saveCredential(VaultRequest vaultRequest, String type) {
		try {
			String credPath = null;
			if (type.equals(VaultEntityTypes.AZURE_PRINCIPAL.getKeyValue())) {
				credPath = "pg/" + K8SMPConstants.USER_ID + "_" + vaultRequest.getCredentialName();
				vaultTemplate.write(credPath, vaultRequest.getAzurePrincipal());
			} else if (type.equals(VaultEntityTypes.API_TOKEN.getKeyValue())) {
				logger.info("API TOKEN:::::" + vaultRequest.getApiServerToken());
				credPath = "pg/" + vaultRequest.getClusterReqId() + "_" + VaultEntityTypes.API_TOKEN.getKeyValue();
				vaultTemplate.write(credPath, new APIToken(vaultRequest.getApiServerToken()));
			} else if (type.equals(VaultEntityTypes.DASHBOARD_TOKEN.getKeyValue())) {
				logger.info("Dashboard TOKEN:::::" + vaultRequest.getDashboardToken());
				credPath = "pg/" + vaultRequest.getClusterReqId() + "_"
						+ VaultEntityTypes.DASHBOARD_TOKEN.getKeyValue();
				vaultTemplate.write(credPath, new DashboardToken(vaultRequest.getDashboardToken()));
			}
		} catch (Exception ex) {
			logger.error("Exception while saving credentials to vault:", ex);

		}
	}

	public void writeToVault(String key, Object value) {
		vaultTemplate.write(key, value);
	}

	public void deleteAzureCredential(VaultRequest clusterRequest) {
		String credPath = "pg/" + clusterRequest.getUserID() + "_" + clusterRequest.getCredentialName();
		vaultTemplate.delete(credPath);
	}

	public AzurePrincipal getAzureCredential(String userID, String credentialName) {
		String credPath = "pg/" + userID + "_" + credentialName;
		VaultResponseSupport<AzurePrincipal> vaultResponse = vaultTemplate.read(credPath, AzurePrincipal.class);
		return vaultResponse.getData();
	}

}
