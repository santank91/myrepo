package com.infosys.k8smp.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.infosys.k8smp.config.CacheKeysEnum;
import com.infosys.k8smp.model.BaseCacheEntity;
import com.infosys.k8smp.model.ClusterEnv;
import com.infosys.k8smp.model.K8SCluster;

@Repository
public class RedisRepository {

	@Autowired
	private RedisTemplate redisTemplate;

	private static final Logger logger = LoggerFactory.getLogger(RedisRepository.class);

	public void save(BaseCacheEntity cacheEntity, CacheKeysEnum cacheKeyEnum) {
		if (cacheKeyEnum.getKeyValue().equals(CacheKeysEnum.CLUSTER_STATUS.getKeyValue())) {
			logger.info("Saving Cache entity for key CLUSTER_STATUS");
			this.redisTemplate.opsForHash().put(cacheKeyEnum.getKeyValue(), ((K8SCluster) cacheEntity).getClusterReqId(),
					cacheEntity);
		} else if (cacheKeyEnum.getKeyValue().equals(CacheKeysEnum.CLUSTER_DETAILS.getKeyValue())) {
			logger.info("Saving Cache entity for key PROVISION_RESULT");
			this.redisTemplate.opsForHash().put(cacheKeyEnum.getKeyValue(),
					((ClusterEnv) cacheEntity).getClusterReqId(), cacheEntity);
		} else {
			logger.info("Trying to saveg invalid cache entity!!");
		}
	}

	public List findAll(CacheKeysEnum cacheKeyEnum) {
		return this.redisTemplate.opsForHash().values(cacheKeyEnum.getKeyValue());
	}

	public BaseCacheEntity findById(String id, CacheKeysEnum cacheKeyEnum) {
		BaseCacheEntity cacheEntity = null;
		try {

			if (cacheKeyEnum.getKeyValue().equals(CacheKeysEnum.CLUSTER_STATUS.getKeyValue())) {
				logger.info("Reading STATUS from Cache...");
				cacheEntity = (K8SCluster) this.redisTemplate.opsForHash().get(cacheKeyEnum.getKeyValue(), id);
			} else if (cacheKeyEnum.getKeyValue().equals(CacheKeysEnum.CLUSTER_DETAILS.getKeyValue())) {
				logger.info("Reading ClusterDetails from Cache...");
				cacheEntity = (ClusterEnv) this.redisTemplate.opsForHash().get(cacheKeyEnum.getKeyValue(), id);
			}
		} catch (Exception ex) {
			logger.info("Exception occurred in findById:", ex);
		}
		return cacheEntity;
	}

	public void update(K8SCluster Cluster, CacheKeysEnum cacheKeyEnum) {
		save(Cluster, cacheKeyEnum);
	}

	public void delete(String id, CacheKeysEnum cacheKeyEnum) {
		this.redisTemplate.opsForHash().delete(cacheKeyEnum.getKeyValue(), id);
	}

}