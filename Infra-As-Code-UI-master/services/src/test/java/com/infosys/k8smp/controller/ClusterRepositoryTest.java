package com.infosys.k8smp.controller;

import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.infosys.k8smp.enums.ClusterStatusEnum;
import com.infosys.k8smp.model.ClusterReqDetails;
import com.infosys.k8smp.model.K8SCluster;
import com.infosys.k8smp.repository.ClusterRepository;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootTest
public class ClusterRepositoryTest {

  @Autowired
  private ClusterRepository clusterRepository;

  @Test
  public void testFindByStaffId() {
    
    K8SCluster cluster = new K8SCluster(UUID.randomUUID().toString());
    
   //cluster.setClusterReqId();
    
    KeyHolder keyHolder = clusterRepository.saveCluster(cluster);
    assertNotNull(keyHolder);
  }

}