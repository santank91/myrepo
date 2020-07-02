package com.infosys.k8smp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix="config.k8smp")
@PropertySource(value={"file:///${spring.config.location}/config/k8smp-config.properties" })
public class OptionsConfig {

	public Options getOptions() {
		return options;
	}

	
	 private final Options options = new Options();	
	
	
}
