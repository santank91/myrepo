package com.infosys.k8smp.model;

import java.io.Serializable;
//import java.util.Date;
import java.sql.Date;

import lombok.Data;

//Mapped to Cluster Table
@Data
public class K8SCluster extends BaseCacheEntity implements Serializable {
	
	
	
	public K8SCluster() {
		super(null);
	}

	public K8SCluster(String clusterID) {
		super(clusterID);
		clusterEnv = new ClusterEnv(clusterID);
	}
	
	public K8SCluster(String clusterID, String status) {
		super(clusterID);
		this.createStatus = status;
		clusterEnv = new ClusterEnv(clusterID);
	}
	
	public K8SCluster(String clusterID, String clusterName, String status) {
		super(clusterID);
		this.clusterName = clusterName;
		this.createStatus = status;
		clusterEnv = new ClusterEnv(clusterID);
	}
	
	public K8SCluster(String clusterID, String clusterName, String status, String dashboardUrl) {
		super(clusterID);
		this.clusterName = clusterName;
		this.createStatus = status;
		clusterEnv = new ClusterEnv(clusterID);
		clusterEnv.setK8sDashboardUrl(dashboardUrl);
	}
	
	private int id;
	
	private String createStatus;
	
	private String clusterName;
	
	private String createUser;
	
	private String updateUser;
	
	private Date createTime;
	
	private Date updateTime;
	
	private ClusterEnv clusterEnv;

	@Override
	public String toString() {
		return "Cluster [processID=" + clusterReqId + ", status=" + createStatus + "]";
	}

	
}
