package com.infosys.k8smp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.infosys.k8smp.model.ClusterView;

@RestController
@RequestMapping(path = "/api/v1")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StatusController {

	private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
	
	private static final Logger logger = LoggerFactory.getLogger(StatusController.class);	

	@GetMapping("/clusterStatus")
	public SseEmitter getAllClusterStatus(HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-store");

		// SseEmitter emitter = new SseEmitter();
		SseEmitter emitter = new SseEmitter(600_000L);

		this.emitters.add(emitter);

		emitter.onCompletion(() -> this.emitters.remove(emitter));
		emitter.onTimeout(() -> this.emitters.remove(emitter));

		return emitter;
		// ExecutorService executor = Executors.newSingleThreadExecutor();
		/*
		 * executor.execute(() -> {
		 * 
		 * try { // clusterService.getClusterStatus(processID, null); // Cluster
		 * clusterStatus = new Cluster(processID, "In-Progress"); //
		 * emitter.send(clusterStatus);
		 * 
		 * // randomDelay();
		 * 
		 * // clusterStatus = clusterService.retrieveClusterStatus(processID); //
		 * randomDelay();
		 * 
		 * // emitter.send(clusterStatus);
		 * 
		 * this.emitters.add(emitter);
		 * 
		 * emitter.onCompletion(() -> this.emitters.remove(emitter));
		 * emitter.onTimeout(() -> this.emitters.remove(emitter));
		 * 
		 * //emitter.complete();
		 * 
		 * } catch (Exception e) { emitter.completeWithError(e); } });
		 * executor.shutdown();
		 */
		// return emitter;
	}

	@EventListener
	public void onClustStatuEvent(ClusterView clusterView) {
		List<SseEmitter> deadEmitters = new ArrayList<>();
		//System.out.println("Received Event:::" + cluster);
		this.emitters.forEach(emitter -> {
			try {
				emitter.send(clusterView);

				// close connnection, browser automatically reconnects
				// emitter.complete();

				// SseEventBuilder builder = SseEmitter.event().name("second").data("1");
				// SseEventBuilder builder =
				// SseEmitter.event().reconnectTime(10_000L).data(memoryInfo).id("1");
				// emitter.send(builder);
			} catch (Exception e) {				
				logger.info("Removing emitter.cluster.cluster.");
				deadEmitters.add(emitter);
			}
		});

		this.emitters.removeAll(deadEmitters);
	}
}