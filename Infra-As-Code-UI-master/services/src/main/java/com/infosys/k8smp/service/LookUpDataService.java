package com.infosys.k8smp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infosys.k8smp.config.FormFieldConfig;
import com.infosys.k8smp.config.Options;
import com.infosys.k8smp.config.OptionsConfig;
import com.infosys.k8smp.enums.CredentialTypeEnum;
import com.infosys.k8smp.enums.K8SMPConstants;
import com.infosys.k8smp.request.GetCredentialsRequest;
import com.infosys.k8smp.response.ComponentsForCredentialType;
import com.infosys.k8smp.response.CredentialItem;
import com.infosys.k8smp.response.LookupDataResponse;
import com.infosys.k8smp.response.UIComponent;

@Service
public class LookUpDataService {

	@Autowired
	private FormFieldConfig formFieldConfig;

	@Autowired
	private OptionsConfig optionsConfig;

	@Autowired
	private ClusterService clusterService;

	public LookupDataResponse constructLookUpData() {
		LookupDataResponse lookupDataResponse = new LookupDataResponse();
		lookupDataResponse.setOptions(new Options());
		lookupDataResponse.getOptions().setProvider(optionsConfig.getOptions().getProvider());
		lookupDataResponse.getOptions().setMasterInstTypes(optionsConfig.getOptions().getMasterInstTypes());
		lookupDataResponse.getOptions().setProvider(optionsConfig.getOptions().getProvider());
		lookupDataResponse.getOptions().setWorkerInstTypes(optionsConfig.getOptions().getWorkerInstTypes());
		lookupDataResponse.getOptions().setDashboard(optionsConfig.getOptions().getDashboard());
		lookupDataResponse.getOptions().setAvailZone(optionsConfig.getOptions().getAvailZone());
		lookupDataResponse.getOptions().setImageName(optionsConfig.getOptions().getImageName());
		lookupDataResponse.getOptions().setCredentialType(optionsConfig.getOptions().getCredentialType());

		// look up credential data from database
		GetCredentialsRequest credRequest = new GetCredentialsRequest();
		credRequest.setUserID(K8SMPConstants.USER_ID);
		List<CredentialItem> credentialNameList = clusterService.getCredentials(credRequest);
		Map<String, String> credentialMap = new HashMap<String, String>();
		for (CredentialItem credentialItem : credentialNameList) {
			credentialMap.put(credentialItem.getCredentialName(), credentialItem.getCredentialName());
		}

		lookupDataResponse.getOptions().setCredentials(credentialMap);

		// Data to be populated in credential components
		ComponentsForCredentialType ComponentsForCredentialType = null;
		List<UIComponent> uiComponentList = new ArrayList<UIComponent>();
		List<ComponentsForCredentialType> componentsForCredentialTypeList = new ArrayList<ComponentsForCredentialType>();

		for (Entry<String, String> formField : formFieldConfig.getFormFields().getAzureServicePrincipal().entrySet()) {
			uiComponentList.add(new UIComponent(formField.getValue(), formField.getKey()));
		}
		ComponentsForCredentialType = new ComponentsForCredentialType();
		ComponentsForCredentialType.setCredentialType(CredentialTypeEnum.AZURE_PRINCIPAL.getKeyValue());
		ComponentsForCredentialType.setComponents(uiComponentList);
		componentsForCredentialTypeList.add(ComponentsForCredentialType);

		uiComponentList = new ArrayList<UIComponent>();
		for (Entry<String, String> formField : formFieldConfig.getFormFields().getAwsCredential().entrySet()) {
			uiComponentList.add(new UIComponent(formField.getValue(), formField.getKey()));
		}
		ComponentsForCredentialType = new ComponentsForCredentialType();
		ComponentsForCredentialType.setCredentialType(CredentialTypeEnum.AWS_CREDENTIAL.getKeyValue());
		ComponentsForCredentialType.setComponents(uiComponentList);
		componentsForCredentialTypeList.add(ComponentsForCredentialType);
		lookupDataResponse.setCredComponentsList(componentsForCredentialTypeList);

		return lookupDataResponse;
	}

}
