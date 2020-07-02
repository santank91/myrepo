package com.infosys.k8smp.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix="config")
@PropertySource(value={"file:///${spring.config.location}/config/k8smp-config.properties" })
public class AppConfig {

	private Map<String, String> k8smp = new HashMap<> ();
	
	
	public Map<String, String> getK8smp() {
		return k8smp;
	}
	
	public String getConfigValue(String configKey) {
		return k8smp.get(configKey);
	}
	
	
	
}
