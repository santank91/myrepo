package com.infosys.k8smp.job;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.infosys.k8smp.enums.K8SMPConstants;
import com.infosys.k8smp.model.BaseCacheEntity;
import com.infosys.k8smp.model.ClusterView;
import com.infosys.k8smp.model.K8SCluster;
import com.infosys.k8smp.repository.ClusterRepository;

@Service
public class CacheObserverJob {

	public final ApplicationEventPublisher eventPublisher;
	
	private static final Logger logger = LoggerFactory.getLogger(CacheObserverJob.class);	
	
	@Autowired
	private ClusterRepository clusterRepository;

	public CacheObserverJob(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@Scheduled(fixedRate = 30000)
	public void pollRedis() {

		K8SCluster searchCluster = new K8SCluster();
		searchCluster.setCreateUser(K8SMPConstants.USER_ID);
		List<K8SCluster> clusterList = clusterRepository.getAllClustersForUser(searchCluster);
		ClusterView clusterView = null;

		for (K8SCluster cluster : clusterList) {
			// System.out.println("Publising Event...." + cluster);
			clusterView = new ClusterView();
			clusterView.setClusterName(cluster.getClusterName());
			clusterView.setClusterReqId(cluster.getClusterReqId());			
			clusterView.setCreateStatus(cluster.getCreateStatus());
			clusterView.setK8sDashboardUrl(cluster.getClusterEnv().getK8sDashboardUrl());
			this.eventPublisher.publishEvent(clusterView);
			
		}

	}
}