package com.infosys.k8smp.controller;

import java.io.File;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosys.k8smp.request.CreateClusterRequest;
import com.infosys.k8smp.request.ProvisionRequest;
import com.infosys.k8smp.request.UpdateClusterRequest;
import com.infosys.k8smp.response.CreateClusterResponse;
import com.infosys.k8smp.service.ClusterService;

@RestController
@RequestMapping(path = "/api/v1")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ClusterController {

	@Autowired
	private ClusterService clusterService;

	private static final Logger logger = LoggerFactory.getLogger(ClusterController.class);


	@PostMapping("/createClusterAsync")
	public ResponseEntity createClusterAsync(@RequestBody ProvisionRequest provisionRequest) throws Exception {

		try {

			logger.debug("Started createClusterAsync...");
			String processID = UUID.randomUUID().toString();

			clusterService.validateAndSetDefault(provisionRequest);
			clusterService.createClusterForCloudSrvc(provisionRequest, processID);

			CreateClusterResponse clusterResponse = new CreateClusterResponse(processID);
			logger.debug("Returning from main...");

			return new ResponseEntity<CreateClusterResponse>(clusterResponse, HttpStatus.OK);

			// return ResponseEntity.ok().build();
		} catch (Exception ex) {
			logger.error("Exception in createClusterAsync::: ", ex);
			throw new Exception(ex);
		}

	}

	@PostMapping("/clusterDetailsCallback")
	public ResponseEntity updateCluster(@RequestBody UpdateClusterRequest clusterRequest) throws Exception {
		try {
			logger.debug("Invoked updateCluster....");
			clusterService.updateClusterDetails(clusterRequest);
			
		} catch (Exception ex) {
			logger.error("Exception in updateCluster::: ", ex);
		}
		return ResponseEntity.ok().build();

	}
	

	@PostMapping("/createCluster")
	public ResponseEntity createCluster(@RequestBody CreateClusterRequest clusterRequest) throws Exception {

		try {

			ObjectMapper mapper = new ObjectMapper();
			String clusterRequestStr = mapper.writeValueAsString(clusterRequest);
			String clusterRequestStrWithQt = "'" + clusterRequestStr + "'";
//			String[] ansible_run = {"sh", "-c",  "ansible-playbook", "/home/engdop/sample/main.yml", "-e",
//					clusterRequestStrWithQt };

			// ProcessBuilder builder = new ProcessBuilder("ansible-playbook",
			// "/home/engdop/sample/main.yml", "-e", clusterRequestStrWithQt);
			ProcessBuilder builder = new ProcessBuilder(new String[0]);

			String command = "ansible-playbook main.yml --e " + clusterRequestStrWithQt;
			// Process p = Runtime.getRuntime().exec(ansible_run, null);
			builder.command(new String[] { "sh", "-c", command });
			String directory = "/home/engdop/sample";
			builder.directory(new File(directory));

			Process process = builder.start();

			String processID = UUID.randomUUID().toString();

			logger.debug("Running create process Synchronously...");
			clusterService.createClusterSync(process, processID);

			CreateClusterResponse clusterResponse = new CreateClusterResponse(processID);
			logger.debug("Returning from main...");

			return new ResponseEntity<CreateClusterResponse>(clusterResponse, HttpStatus.OK);

			// return ResponseEntity.ok().build();
		} catch (Exception ex) {
			logger.error("Exception in createCluster::: ", ex);
			throw new Exception(ex);
		}

	}

}
