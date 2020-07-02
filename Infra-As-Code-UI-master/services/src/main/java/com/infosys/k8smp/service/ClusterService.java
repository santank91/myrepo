package com.infosys.k8smp.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosys.k8smp.config.AppConfig;
import com.infosys.k8smp.config.CacheKeysEnum;
import com.infosys.k8smp.enums.CloudSvcTypes;
import com.infosys.k8smp.enums.ClusterStatusEnum;
import com.infosys.k8smp.enums.K8SMPConstants;
import com.infosys.k8smp.enums.VaultEntityTypes;
import com.infosys.k8smp.exception.ClusterException;
import com.infosys.k8smp.model.ClusterEnv;
import com.infosys.k8smp.model.ClusterReqDetails;
import com.infosys.k8smp.model.Credential;
import com.infosys.k8smp.model.K8SCluster;
import com.infosys.k8smp.repository.ClusterRepository;
import com.infosys.k8smp.repository.RedisRepository;
import com.infosys.k8smp.request.AzurePrincipal;
import com.infosys.k8smp.request.BaseRequest;
import com.infosys.k8smp.request.CreateClusterRequest;
import com.infosys.k8smp.request.GetCredentialsRequest;
import com.infosys.k8smp.request.ProvisionDetails;
import com.infosys.k8smp.request.ProvisionRequest;
import com.infosys.k8smp.request.UpdateClusterRequest;
import com.infosys.k8smp.request.VaultRequest;
import com.infosys.k8smp.response.CredentialItem;

/**
 * @author Prasanth_D
 *
 */
@Service
public class ClusterService {

	@Autowired
	private RedisRepository redisRepo;

	@Autowired
	private ClusterRepository clusterRepository;

	@Autowired
	private AppConfig appConfig;

	@Autowired
	private VaultService vaultService;

	private static final Logger logger = LoggerFactory.getLogger(ClusterService.class);

	@Async
	public void createClusterForCloudSrvc(ProvisionRequest provisionRequest, String processID) throws ClusterException {

		try {

			// K8SCluster cluster = new K8SCluster(processID, "Started");
			// redisRepo.save(cluster, CacheKeysEnum.CLUSTER_STATUS);

			// Set cluster ID & callback url
			provisionRequest.setClusterID(processID);
			populateRequestCallback(provisionRequest);

			createDBModelAndPersist(provisionRequest);

			if (provisionRequest.getCloudSrvc().equalsIgnoreCase(CloudSvcTypes.AZURENATIVE.getKeyValue())) {
				// to create cluster fields from provision request (kubeDashboard value) is also
				// required.
				logger.info("Starting Azure cluster creation...");
				createClusterAsyncAzureNative(provisionRequest, processID);

			} else if (provisionRequest.getCloudSrvc().equalsIgnoreCase(CloudSvcTypes.AKS.getKeyValue())) {
				logger.info("Starting AKS cluster creation...");
				createClusterForAks(provisionRequest, processID);
			}

			logger.info("Inside Async process");
		} catch (Exception ex) {
			logger.error("Exception Occurred...", ex);
			throw new ClusterException("Exception occurred in createClusterForCloudSrvc", ex);
		}
		// return createdCluster;
	}

	public void validateAndSetDefault(ProvisionRequest provisionRequest) {

		if (provisionRequest.getMasterCount() == null
				&& provisionRequest.getCloudSrvc().equalsIgnoreCase(CloudSvcTypes.AZURENATIVE.getKeyValue())) {
			provisionRequest.setMasterCount(appConfig
					.getConfigValue("defaultMasterCount" + getPropertySuffix(provisionRequest.getCloudSrvc())));
		}

		if (provisionRequest.getMasterSize() == null
				&& provisionRequest.getCloudSrvc().equalsIgnoreCase(CloudSvcTypes.AZURENATIVE.getKeyValue())) {
			provisionRequest.setMasterSize(
					appConfig.getConfigValue("defaultMasterSize" + getPropertySuffix(provisionRequest.getCloudSrvc())));
		}

		if (provisionRequest.getNodeSize() == null) {
			provisionRequest.setNodeSize(
					appConfig.getConfigValue("defaultNodeSize" + getPropertySuffix(provisionRequest.getCloudSrvc())));
		}

		if (provisionRequest.getAvailZone() == null) {
			provisionRequest.setAvailZone(
					appConfig.getConfigValue("defaultAvailZone" + getPropertySuffix(provisionRequest.getCloudSrvc())));
		}

		// Credential name is mandatory
		// Credentials will be fetched from vault based on credential name
		if (provisionRequest.getCredentialName() != null) {

			//AzurePrincipal credentials = vaultService.getAzureCredential(K8SMPConstants.USER_ID,
			//		provisionRequest.getCredentialName());
//			credentials.setSubscriptionID(optionsConfig.getOptions().getCredentials().get("subscriptionID"));
//			credentials.setClientID(optionsConfig.getOptions().getCredentials().get("clientID"));
//			credentials.setTenant(optionsConfig.getOptions().getCredentials().get("tenant"));
//			credentials.setSecret(optionsConfig.getOptions().getCredentials().get("secret"));
			//credentials.setSubscriptionID(credentials.getSubscriptionID());
			//credentials.setClientID(credentials.getClientID());
			//credentials.setTenant(credentials.getTenant());
			//credentials.setSecret(credentials.getSecret());
			provisionRequest.setCredentialKey(K8SMPConstants.USER_ID + "_" + provisionRequest.getCredentialName());
			//provisionRequest.setCredentials(credentials);
		}

		// Image is not cloud srv specific and will be set only for native installation,
		// not for managed
		if (provisionRequest.getImageName() == null
				&& provisionRequest.getCloudSrvc().equalsIgnoreCase(CloudSvcTypes.AZURENATIVE.getKeyValue())) {
			provisionRequest.setImageName(appConfig.getConfigValue("defaultImageName"));
		}

		if (provisionRequest.getCloudSrvc() == null || provisionRequest.getNodeCount() == null
				|| provisionRequest.getClusterName() == null || provisionRequest.getCredentialName() == null) {
			throw new ClusterException("Please provide valid Cloud srvc, Node count & Cluster name...");
		}

		if (provisionRequest.getKubeDashboard() == null) {
			provisionRequest.setKubeDashboard(appConfig.getConfigValue("defaultKubeDashboard"));
		}

	}

	// @Cacheable(value = "cluster", key = "#processID")
	@Deprecated
	public K8SCluster retrieveClusterStatus(String processID) throws ClusterException {

		K8SCluster clusterStatus = null;

		try {
			logger.info("Trying to retrieve from cache for Process ID:" + processID);
			// clusterStatus = getClusterStatus(processID, null);
			clusterStatus = (K8SCluster) redisRepo.findById(processID, CacheKeysEnum.CLUSTER_STATUS);
			if (clusterStatus == null) {
				logger.info("Cluster Status value is null from cache!!");
				clusterStatus = new K8SCluster(processID, "Failure");
			}

		} catch (Exception ex) {
			logger.error("Re-thowing exception:", ex.getMessage());
			throw new ClusterException("Exception occurred in retrieveClusterStatus...", ex);
		}
		return clusterStatus;

	}

	/**
	 * updates cache based on request from ansible
	 * 
	 * @param callbackRequest
	 */
	public void updateClusterDetails(UpdateClusterRequest callbackRequest) throws JsonProcessingException, IOException {
	
		ObjectMapper mapper = new ObjectMapper();
		String callbackRequestStr = mapper.writeValueAsString(callbackRequest);
		logger.info("Update Request: " + callbackRequestStr);
		ArrayList<String> masterNodes = new ArrayList<String>();
		ArrayList<String> workderNodes = new ArrayList<String>();
		// set cluster id and inventory path.
		ClusterEnv clusterEnv = new ClusterEnv(callbackRequest.getClusterID());
		clusterEnv.setInventoryPath(getInventoryPath(callbackRequest.getClusterID()));
		// if provision details are present set it. If request is posted after
		// provisioning
		K8SCluster cluster = clusterRepository.findById(callbackRequest.getClusterID());

		if (cluster == null) {
			logger.info("Cluster could not be found in Database... ");
			throw new ClusterException("Cluster could not be found in database..");
		}

		if (callbackRequest.getProvisionDetails() != null) {
			for (ProvisionDetails provDetails : callbackRequest.getProvisionDetails()) {
				if (provDetails.getNodeType().toUpperCase().equals("MASTER") && masterNodes.size() <= 1) {
					masterNodes.add(provDetails.getPublicIP());
				}
				if (provDetails.getNodeType().toUpperCase().equals("WORKER")) {
					workderNodes.add(provDetails.getPublicIP());
				}
			}
			String[] kubeMasterIps = new String[masterNodes.size()];
			String[] kubeNodeIps = new String[workderNodes.size()];
			masterNodes.toArray(kubeMasterIps);
			workderNodes.toArray(kubeNodeIps);
			clusterEnv.setKubeMasterIps(kubeMasterIps);
			clusterEnv.setKubeNodeIps(kubeNodeIps);
			clusterEnv.setClusterseqid(cluster.getId());
			clusterRepository.saveClusterEnv(clusterEnv);

			cluster.setCreateStatus(ClusterStatusEnum.PROV_COMPLETED.getKeyValue());
			clusterRepository.updateClusterStatus(cluster);
		}
		// if request is posted after create cluster
		if (callbackRequest.getCreateClusterDetails() != null) {
//			clusterDetails = (ClusterEnv) redisRepo.findById(clusterRequest.getClusterID(),
//					CacheKeysEnum.CLUSTER_DETAILS);	

			clusterEnv.setApiServerUrl(callbackRequest.getCreateClusterDetails().getKubeApiServerUrl());
			// clusterEnv.setK8sAdminToken(clusterRequest.getCreateClusterDetails().getK8sAdminToken());
			clusterEnv.setK8sDashboardUrl(callbackRequest.getCreateClusterDetails().getK8sDashboadUrl());
			// clusterEnv.setDashboardToken(clusterRequest.getCreateClusterDetails().getDashboardToken());
			clusterEnv.setClusterseqid(cluster.getId());

			// If provisioning completed, that means, Azure Native flow. Already record
			// present in clusterenv - hence update
			if (cluster.getCreateStatus().equalsIgnoreCase(ClusterStatusEnum.PROV_COMPLETED.getKeyValue())) {
				clusterRepository.updateClusterEnv(clusterEnv);
			} else {
				clusterRepository.saveClusterEnv(clusterEnv);
			}

			// SAVE Tokens to Vault
			saveTokensToVault(callbackRequest);

			cluster.setCreateStatus(ClusterStatusEnum.READY.getKeyValue());
			clusterRepository.updateClusterStatus(cluster);
			if (callbackRequest.getCreateClusterDetails().getKubeConfig() != null) {

				logger.info("Kube Cofig from callback request::"
						+ callbackRequest.getCreateClusterDetails().getKubeConfig().asText());
			} else {
				logger.info("Kube Cofig from callback request is null::");
			}
		}

		if (callbackRequest.getMonitoringDetails() != null) {
			// TODO - Save montiroting details in database
			logger.info("Monitoring details - Grafana URL- from callback request::"
					+ callbackRequest.getMonitoringDetails().getGrafanaUrl());
		}
		// redisRepo.save(clusterEnv, CacheKeysEnum.CLUSTER_DETAILS);

	}

	private void saveTokensToVault(UpdateClusterRequest callbackRequest) {
		// Save API token to vault
		VaultRequest vaultRequest = new VaultRequest();
		vaultRequest.setClusterReqId(callbackRequest.getClusterID());
		vaultRequest.setApiServerToken(callbackRequest.getCreateClusterDetails().getK8sAdminToken());
		vaultRequest.setDashboardToken(callbackRequest.getCreateClusterDetails().getDashboardToken());
		vaultService.saveCredential(vaultRequest, VaultEntityTypes.API_TOKEN.getKeyValue());
		vaultService.saveCredential(vaultRequest, VaultEntityTypes.DASHBOARD_TOKEN.getKeyValue());
	}

	public void createClusterSync(Process process, String processID) throws InterruptedException {

		StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
		Executors.newSingleThreadExecutor().submit(streamGobbler);

		saveAndGetProvisionStatus(processID, process, null);
		logger.info("Inside Async process");
		// return createdCluster;
	}

	public List<CredentialItem> getCredentials(GetCredentialsRequest credentailRequest) {
		Credential credential = new Credential();
		credential.setUserID(credentailRequest.getUserID());
		credential.setCredentialType(credentailRequest.getCredentialType());
		List<Credential> credentialNames = clusterRepository.getCredentialNamesForUser(credential);
		List<CredentialItem> credentialItemList = new ArrayList<CredentialItem>();
		for (Credential credentialName : credentialNames) {
			credentialItemList
					.add(new CredentialItem(credentialName.getCredentialName(), credentialName.getCredentialType()));
		}

		return credentialItemList;
	}

	public void saveCredentialName(VaultRequest credentailRequest) {
		clusterRepository.saveCredentialName(credentailRequest);
	}

	public void deleteCredential(VaultRequest credentailRequest) {
		clusterRepository.deleteCredential(credentailRequest);
	}

	/**
	 * Invokes provision playbook followed by create cluster
	 * 
	 * @param provisionRequest
	 * @param processID
	 * @throws ClusterException
	 */
	private void createClusterAsyncAzureNative(ProvisionRequest provisionRequest, String processID)
			throws ClusterException {

		try {

			// K8SCluster cluster = new K8SCluster(processID, "Started");
			// redisRepo.save(cluster, CacheKeysEnum.CLUSTER_STATUS);

			Process process = startProvisionTask(provisionRequest);

			K8SCluster cluster = saveAndGetProvisionStatus(processID, process,
					ClusterStatusEnum.PROV_COMPLETED.getKeyValue());

			Thread.sleep(10000);

			if (cluster.getCreateStatus().equals(ClusterStatusEnum.PROV_COMPLETED.getKeyValue())) {
				// to create cluster fields from provision request (kubeDashboard value) is also
				// required.
				createClusterForAzureNative(provisionRequest, processID);

			} else {
				// Retry once
			}

			logger.info("Inside Async process");
		} catch (Exception ex) {
			logger.error("Exception Occurred...", ex);
			throw new ClusterException("Exception occurred in createClusterAsyncAzure..", ex);
		}
	}

	private void createDBModelAndPersist(ProvisionRequest provisionRequest) {
		K8SCluster k8sCluster = new K8SCluster(provisionRequest.getClusterID(),
				ClusterStatusEnum.IN_PROGRESS.getKeyValue());
		k8sCluster.setClusterName(provisionRequest.getClusterName());
		k8sCluster.setCreateUser(K8SMPConstants.USER_ID);
		k8sCluster.setCreateTime(new Date(System.currentTimeMillis()));
		clusterRepository.saveCluster(k8sCluster);

		ClusterReqDetails clusterDetails = new ClusterReqDetails();
		clusterDetails.setClusterReqId(provisionRequest.getClusterID());
		clusterDetails.setProvider(provisionRequest.getCloudSrvc());
		if (provisionRequest.getMasterCount() != null) {
			clusterDetails.setMasterNodes(Integer.parseInt(provisionRequest.getMasterCount()));
		}
		clusterDetails.setWorkerNodes(Integer.parseInt(provisionRequest.getNodeCount()));
		clusterDetails.setMasterInstance(provisionRequest.getMasterSize());
		clusterDetails.setWorkerInstance(provisionRequest.getNodeSize());
		clusterDetails.setAvailZone(provisionRequest.getAvailZone());
		clusterDetails.setDashboardType(provisionRequest.getKubeDashboard());
		if (provisionRequest.getLogEnabled() != null) {
			clusterDetails.setLogEnabled(provisionRequest.getLogEnabled().equals("Y") ? true : false);
		}
		if (provisionRequest.getMonitoringEnabled() != null) {
			clusterDetails.setMonitoringEnabled(provisionRequest.getMonitoringEnabled().equals("Y") ? true : false);
		}
		clusterDetails.setCredentialId(provisionRequest.getCredentialName());
		clusterDetails.setCreateUser(K8SMPConstants.USER_ID);
		clusterDetails.setCreateTime(new Date(System.currentTimeMillis()));
		clusterRepository.saveClusterReqDetails(clusterDetails);
	}

	/**
	 * Invoke playbook and Create AKS cluster
	 * 
	 * @param provisionRequest
	 * @param processID
	 */
	private void createClusterForAks(ProvisionRequest provisionRequest, String processID) {
		if ("Y".equals(provisionRequest.getMonitoringEnabled())) {
			provisionRequest.setKubeMonitoring("Grafana");
		}
		startCreateClusterTaskForAks(provisionRequest);
	}

	private void populateRequestCallback(BaseRequest baseRequest) {
		baseRequest.setCallbackUrl(appConfig.getConfigValue("callbackUrl"));
		logger.info("Read Config Value callbackUrl:" + appConfig.getConfigValue("callbackUrl"));

	}

	private void populateCreateClusterRequest(CreateClusterRequest createClusterRequest) {
		createClusterRequest.setKubeUser(appConfig.getConfigValue("defaultKubeUser"));
		createClusterRequest.setInstPkgs(new String[] { "kubeadm", "kubectl" });
		createClusterRequest.setOpenPorts(new String[] { "6443/tcp", "10250/tcp" });
		createClusterRequest.setEnblSvcs(new String[] { "docker", "kubelet" });
		// populate callback API url
		populateRequestCallback(createClusterRequest);

	}

	private Process startProvisionTask(ProvisionRequest provisionRequest) throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String provisionRequestStr = mapper.writeValueAsString(provisionRequest);
		String provisionRequestStrWithQt = "'" + provisionRequestStr + "'";
		ProcessBuilder builder = new ProcessBuilder(new String[0]);

		String command = "ansible-playbook "
				+ appConfig.getConfigValue("provisionPlayBook" + getPropertySuffix(provisionRequest.getCloudSrvc()))
				+ " --e " + provisionRequestStrWithQt;
// Process p = Runtime.getRuntime().exec(ansible_run, null);
		builder.command(new String[] { "sh", "-c", command });

		logger.info("Provision Command: " + command);
		String directory = appConfig
				.getConfigValue("provisionFolder" + getPropertySuffix(provisionRequest.getCloudSrvc()));
		builder.directory(new File(directory));

		logger.info("Provisioning command directory::" + directory);

		Process process = builder.start();

		logger.info("Running Provision task Asynchronously...");

		StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
		Executors.newSingleThreadExecutor().submit(streamGobbler);
		return process;
	}

	// @Cacheable(value = "student", key = "#processID")
	private K8SCluster saveAndGetProvisionStatus(String processID, Process process, String status)
			throws InterruptedException {
		// TODO Auto-generated method stub
		int exitCode = 1;
		if (process != null) {

			exitCode = process.waitFor();
			logger.info("Exit Code from create cluster..." + exitCode);
		}
		logger.info("Storing data for processID: " + processID + " Return after create...");
		K8SCluster cluster = new K8SCluster(processID,
				(exitCode == 0) ? status : ClusterStatusEnum.FAILURE.getKeyValue());
		clusterRepository.updateClusterStatus(cluster);
		// redisRepo.save(cluster, CacheKeysEnum.CLUSTER_STATUS);
		logger.info("Cluster:" + cluster);

		return cluster;
	}

	// @Cacheable(value = "student", key = "#processID")
	private K8SCluster saveAndGetCreateClusterStatus(String processID, Process process) throws InterruptedException {
		// TODO Auto-generated method stub
		int exitCode = 1;
		if (process != null) {

			exitCode = process.waitFor();
			logger.info("Exit Code..." + exitCode);
		}
		logger.info("Storing data for processID: " + processID + " Return after create...");
		K8SCluster cluster = new K8SCluster(processID, (exitCode == 0) ? "Completed" : "Failure");
		redisRepo.save(cluster, CacheKeysEnum.CLUSTER_STATUS);
		logger.info("Cluster:" + cluster);

		return cluster;
	}

	private CreateClusterRequest createClusterForAzureNative(ProvisionRequest provisionRequest, String processID) {

		// Read IP from file
		// CreateClusterRequest clusterRequest = readFileAndGetIPs();

		// Read IP from Cache - disabling as IPs need not be passed for azure. (replaced
		// by inventory)
		// CreateClusterRequest clusterRequest = buildClusterRequestWithIPs(processID);
		CreateClusterRequest clusterRequest = new CreateClusterRequest();
		// Populate other fields
		clusterRequest.setClusterID(processID);
		clusterRequest.setKubeDashboard(provisionRequest.getKubeDashboard());
		clusterRequest.setCloudSrvc(provisionRequest.getCloudSrvc());
		// set cluster after replacing - with _
		clusterRequest.setClusterName(provisionRequest.getClusterName().replace('-', '_'));
		logger.info("provisionRequest.getMonitoringEnabled()::" + provisionRequest.getMonitoringEnabled());
		if ("Y".equals(provisionRequest.getMonitoringEnabled())) {
			clusterRequest.setKubeMonitoring("Grafana");
			logger.info("setKubeMonitoring::Grafana");
		}
		// Populate other constant params
		populateCreateClusterRequest(clusterRequest);

		startCreateClusterTaskAzure(clusterRequest);

		return clusterRequest;

	}

	private String getInventoryPath(String processID) {
		logger.info("getInventoryPath for cluserReqId:" + processID);
		// ClusterDetails clusterDetails = (ClusterDetails)
		// redisRepo.findById(processID, CacheKeysEnum.CLUSTER_DETAILS);
		return appConfig.getConfigValue("inventoryFile");
	}

	// Create cluster command structure is different for Azure and AKS - this method
	// is for Azure
	private Process startCreateClusterTaskAzure(CreateClusterRequest clusterRequest) throws ClusterException {
		Process process = null;

		try {
			ObjectMapper mapper = new ObjectMapper();
			String clusterRequestStr = mapper.writeValueAsString(clusterRequest);
			String provisionRequestStrWithQt = "'" + clusterRequestStr + "'";
			ProcessBuilder builder = new ProcessBuilder(new String[0]);

			String command = "ansible-playbook -i " + "\"" + getInventoryPath(clusterRequest.getClusterID()) + "\" "
					+ appConfig
							.getConfigValue("createClusterPlayBook" + getPropertySuffix(clusterRequest.getCloudSrvc()))
					+ " --e " + provisionRequestStrWithQt;
// Process p = Runtime.getRuntime().exec(ansible_run, null);
			logger.info("Create Cluster Command:: " + command);
			builder.command(new String[] { "sh", "-c", command });
			logger.info("Config param for directory:" + "createClusterFolder"
					+ getPropertySuffix(clusterRequest.getCloudSrvc()));
			String directory = appConfig
					.getConfigValue("createClusterFolder" + getPropertySuffix(clusterRequest.getCloudSrvc()));

			logger.info("Create cluster directory::" + directory);
			builder.directory(new File(directory));

			process = builder.start();

			logger.info("Running create azure cluster playbook ...");

			StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
			Executors.newSingleThreadExecutor().submit(streamGobbler);
		} catch (Exception ex) {
			// ex.printStackTrace();
			throw new ClusterException("Exception occurred in startCreateClusterTaskAzure", ex);
		}
		return process;
	}

	// Create cluster command structure is different for Azure and AKS
	private Process startCreateClusterTaskForAks(ProvisionRequest provisionRequest) throws ClusterException {
		Process process = null;

		try {
			ObjectMapper mapper = new ObjectMapper();
			String clusterRequestStr = mapper.writeValueAsString(provisionRequest);
			String provisionRequestStrWithQt = "'" + clusterRequestStr + "'";
			ProcessBuilder builder = new ProcessBuilder(new String[0]);

			String command = "ansible-playbook "
					+ appConfig.getConfigValue(
							"createClusterPlayBook" + getPropertySuffix(provisionRequest.getCloudSrvc()))
					+ " --e " + provisionRequestStrWithQt;
// Process p = Runtime.getRuntime().exec(ansible_run, null);
			logger.info("Create Cluster Command:: " + command);
			builder.command(new String[] { "sh", "-c", command });
			String directory = appConfig
					.getConfigValue("createClusterFolder" + getPropertySuffix(provisionRequest.getCloudSrvc()));
			builder.directory(new File(directory));

			process = builder.start();

			logger.info("Running create aks playbook ...");

			StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
			Executors.newSingleThreadExecutor().submit(streamGobbler);
		} catch (Exception ex) {
			// ex.printStackTrace();
			throw new ClusterException("Exception occurred in startCreateClusterTaskForAks...", ex);
		}
		return process;
	}

	private static class StreamGobbler implements Runnable {
		private InputStream inputStream;
		private Consumer<String> consumer;

		public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
			this.inputStream = inputStream;
			this.consumer = consumer;
		}

		@Override
		public void run() {
			new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(consumer);
		}
	}

	private String getPropertySuffix(String cloudSrvc) {
		String suffix = null;
		if (cloudSrvc == null || cloudSrvc.equalsIgnoreCase(CloudSvcTypes.AZURENATIVE.getKeyValue())) {
			suffix = CloudSvcTypes.AZURENATIVE.getKeyValue();
		}

		if (cloudSrvc == null || cloudSrvc.equalsIgnoreCase("aks")) {
			suffix = CloudSvcTypes.AKS.getKeyValue();
		}
		return suffix;
	}

}
