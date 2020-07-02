package com.infosys.k8smp.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.infosys.k8smp.model.K8SCluster;
import com.infosys.k8smp.service.ClusterService;

@RestController
@Deprecated
public class EmitterController {

	@Autowired
	private ClusterService clusterService;

	//@GetMapping("/clusterStatus")
	@Deprecated
	public SseEmitter fetchClusterData(@RequestParam String processID) {
		
		SseEmitter emitter = new SseEmitter();

		ExecutorService executor = Executors.newSingleThreadExecutor();

		executor.execute(() -> {
			
			try {
				//clusterService.getClusterStatus(processID, null);
				K8SCluster clusterStatus = new K8SCluster(processID, "In-Progress");
				emitter.send(clusterStatus);
				
				//randomDelay();
				
				
				clusterStatus = clusterService.retrieveClusterStatus(processID);
				// randomDelay();
				
				emitter.send(clusterStatus);

				emitter.complete();

			} catch (Exception e) {
				emitter.completeWithError(e);
			}
		});
		executor.shutdown();
		return emitter;
	}
	

}