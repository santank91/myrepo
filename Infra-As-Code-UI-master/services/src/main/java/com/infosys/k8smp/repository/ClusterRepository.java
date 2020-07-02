package com.infosys.k8smp.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.infosys.k8smp.model.ClusterEnv;
import com.infosys.k8smp.model.ClusterReqDetails;
import com.infosys.k8smp.model.Credential;
import com.infosys.k8smp.model.K8SCluster;
import com.infosys.k8smp.request.VaultRequest;

@Repository
@Transactional
public class ClusterRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Credential> getCredentialNamesForUser(Credential credential) {
		List<Credential> credentials = (List<Credential>) jdbcTemplate.query(
				"Select * FROM ik8smp.credential WHERE k8smpuser = ? ", new Object[] { credential.getUserID() },
				(rs, rowNum) -> new Credential(rs.getString("credentialname"), rs.getString("k8smpuser"),
						rs.getString("credentialtype"), rs.getString("status")));

		return credentials;
	}
	
	public List<K8SCluster> getAllClustersForUser(K8SCluster cluster) {
		List<K8SCluster> clusters = (List<K8SCluster>) jdbcTemplate.query(
				"Select * FROM ik8smp.cluster c left join ik8smp.clusterenv ce on c.id=ce.clusterseqid AND c.createUser = ? ", new Object[] { cluster.getCreateUser() },
				(rs, rowNum) -> new K8SCluster(rs.getString("clusterreqId"), rs.getString("clustername"),
						rs.getString("createstatus"), rs.getString("dashboardurl")));

		return clusters;
	}

	public KeyHolder saveCredentialName(VaultRequest credentialRequest) {

		KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update((connection) -> {
			PreparedStatement ps = connection.prepareStatement(
					"INSERT INTO ik8smp.credential(id, credentialname, k8smpuser, credentialtype, status) VALUES(nextval('ik8smp.credential_sequence'),?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, credentialRequest.getCredentialName());
			ps.setString(2, credentialRequest.getUserID());
			ps.setString(3, credentialRequest.getCredentialType());
			ps.setString(4, "ACTIVE");
			return ps;
		}, holder);

		return holder;

	}

	public KeyHolder saveCluster(K8SCluster cluster) {

		KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update((connection) -> {
			PreparedStatement ps = connection.prepareStatement(
					"INSERT INTO ik8smp.Cluster(id, clusterreqid, createstatus, clustername, createuser, createtime) VALUES(nextval('ik8smp.cluster_sequence'),?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, cluster.getClusterReqId());
			ps.setString(2, cluster.getCreateStatus());
			ps.setString(3, cluster.getClusterName());
			ps.setString(4, cluster.getCreateUser());
			ps.setDate(5, cluster.getCreateTime());
			return ps;
		}, holder);

		return holder;

	}

	public KeyHolder saveClusterReqDetails(ClusterReqDetails clusterDetails) {

		KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update((connection) -> {
			PreparedStatement ps = connection.prepareStatement(
					"INSERT INTO ik8smp.ClusterReqDetail(id, clusterreqid, provider, "
							+ "masternodes, workernodes, masterinstance, workerinstance,availzone,dashboard,logenabled,monitoringenabled,credentialid) "
							+ "VALUES(nextval('ik8smp.clusterreqdetails_sequence'),?,?,?,?,?,?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, clusterDetails.getClusterReqId());
			ps.setString(2, clusterDetails.getProvider());
			ps.setInt(3, clusterDetails.getMasterNodes());
			ps.setInt(4, clusterDetails.getWorkerNodes());
			ps.setString(5, clusterDetails.getMasterInstance());
			ps.setString(6, clusterDetails.getWorkerInstance());
			ps.setString(7, clusterDetails.getAvailZone());
			ps.setString(8, clusterDetails.getDashboardType());
			ps.setBoolean(9, clusterDetails.isLogEnabled());
			ps.setBoolean(10, clusterDetails.isMonitoringEnabled());
			ps.setString(11, clusterDetails.getCredentialId());
			return ps;
		}, holder);

		return holder;

	}

	public KeyHolder updateClusterEnv(ClusterEnv clusterEnv) {

		KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update((connection) -> {
			PreparedStatement ps = connection.prepareStatement(
					"UPDATE ik8smp.ClusterEnv SET apiserverurl=?, dashboardurl =? WHERE clusterreqid = ? ",
					Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, clusterEnv.getApiServerUrl());
			ps.setString(2, clusterEnv.getK8sDashboardUrl());
			ps.setString(3, clusterEnv.getClusterReqId());

			return ps;
		}, holder);

		return holder;

	}

	public KeyHolder saveClusterEnv(ClusterEnv clusterEnv) {

		KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update((connection) -> {
			PreparedStatement ps = connection.prepareStatement(
					"INSERT INTO ik8smp.ClusterEnv(id, clusterreqid, apiserverurl, dashboardurl, masternodes, workernodes, clusterseqid) VALUES(nextval('ik8smp.clusterenv_sequence'),?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, clusterEnv.getClusterReqId());
			ps.setString(2, clusterEnv.getApiServerUrl());
			ps.setString(3, clusterEnv.getK8sDashboardUrl());
			ps.setString(4, Arrays.toString(clusterEnv.getKubeMasterIps()));
			ps.setString(5, Arrays.toString(clusterEnv.getKubeNodeIps()));
			ps.setInt(6, clusterEnv.getClusterseqid());
			return ps;
		}, holder);

		return holder;

	}

	public KeyHolder updateClusterStatus(K8SCluster cluster) {

		KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update((connection) -> {
			PreparedStatement ps = connection.prepareStatement(
					"UPDATE ik8smp.Cluster SET createstatus = ? WHERE clusterreqid = ?",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, cluster.getCreateStatus());
			ps.setString(2, cluster.getClusterReqId());
			return ps;
		}, holder);

		return holder;

	}

	public K8SCluster findById(String clusterReqId) {

		K8SCluster cluster = (K8SCluster) jdbcTemplate.queryForObject(
				"Select * FROM ik8smp.cluster WHERE clusterreqid = ?", new Object[] { clusterReqId },
				new BeanPropertyRowMapper<K8SCluster>(K8SCluster.class));
		return cluster;
	}

	public void deleteCredential(VaultRequest credentialRequest) {
		jdbcTemplate.update((connection) -> {
			PreparedStatement ps = connection
					.prepareStatement("DELETE FROM ik8smp.credential WHERE credentialname = ? ");
			ps.setString(1, credentialRequest.getCredentialName());
			return ps;
		});

	}

}
