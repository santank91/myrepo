package com.infosys.k8smp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.infosys.k8smp.enums.VaultEntityTypes;
import com.infosys.k8smp.request.AzurePrincipal;
import com.infosys.k8smp.request.GetCredentialsRequest;
import com.infosys.k8smp.request.VaultRequest;
import com.infosys.k8smp.response.CredentialItem;
import com.infosys.k8smp.response.GetCredentialsResponse;
import com.infosys.k8smp.service.ClusterService;
import com.infosys.k8smp.service.VaultService;

@RestController
@RequestMapping(path = "/api/v1")
@CrossOrigin(origins = "*", maxAge = 3600)
public class VaultController {

	@Autowired
	private VaultService vaultService;

	@Autowired
	private ClusterService clusterService;

	private static final Logger logger = LoggerFactory.getLogger(VaultController.class);

	@PostMapping("/fetchCredentialList")
	public ResponseEntity<GetCredentialsResponse> getCredentialNamesForUser(
			@RequestBody GetCredentialsRequest credentialsRequest) {

		List<CredentialItem> credentialItems = clusterService.getCredentials(credentialsRequest);
		return new ResponseEntity<GetCredentialsResponse>(new GetCredentialsResponse(credentialItems), HttpStatus.OK);

	}

	@PostMapping("/saveCredential")
	public ResponseEntity saveCredential(@RequestBody VaultRequest credentialRequest) throws Exception {
		try {
			logger.debug("Invoked saveCredentials....");

			vaultService.saveCredential(credentialRequest, VaultEntityTypes.AZURE_PRINCIPAL.getKeyValue());

			// TODO - prasanth_D need update this to database as well
			clusterService.saveCredentialName(credentialRequest);

		} catch (Exception ex) {
			logger.error("Exception in updateCluster::: ", ex);
		}
		return ResponseEntity.ok().build();

	}
	
	@DeleteMapping("/deleteCredential")
	public ResponseEntity deleteCredential(@RequestBody VaultRequest credentialRequest) throws Exception {
		try {
			logger.debug("Invoked saveCredentials....");

			vaultService.deleteAzureCredential(credentialRequest);

			// TODO - prasanth_D need update this to database as well
			clusterService.deleteCredential(credentialRequest);

		} catch (Exception ex) {
			logger.error("Exception in updateCluster::: ", ex);
		}
		return ResponseEntity.ok().build();

	}


	//For testing purpose Only will be disabled in Prod
	@Deprecated
	@GetMapping("/getCredentialFromVault")
	public AzurePrincipal getCredential(@RequestParam String userID, @RequestParam String credentialName) {
		return vaultService.getAzureCredential(userID, credentialName);
	}

}
