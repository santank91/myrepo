package com.infosys.k8smp.model;

import java.io.Serializable;
//import java.util.Date;
import java.sql.Date;

import lombok.Data;

//Mapped to Cluster Table
@Data
public class ClusterView extends BaseCacheEntity implements Serializable {
	
	
	private static final long serialVersionUID = 1L;


	public ClusterView() {
		super(null);
	}

	public ClusterView(String clusterID) {
		super(clusterID);
	}
	
	public ClusterView(String clusterID, String status) {
		super(clusterID);
		this.createStatus = status;
	}
	
	public ClusterView(String clusterID, String clusterName, String status) {
		super(clusterID);
		this.clusterName = clusterName;
		this.createStatus = status;
	}
	
	private int id;
	
	private String createStatus;
	
	private String clusterName;
	
	private String k8sDashboardUrl;
	
	private String createUser;
	
	private Date createTime;	


	@Override
	public String toString() {
		return "Cluster [processID=" + clusterReqId + ", status=" + createStatus + "]";
	}

	
}
