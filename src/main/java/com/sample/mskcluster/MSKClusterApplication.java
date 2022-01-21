package com.sample.mskcluster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.schema.registry.client.EnableSchemaRegistryClient;

@SpringBootApplication
@EnableSchemaRegistryClient
public class MSKClusterApplication {

	public static void main(String[] args)
    {
        SpringApplication.run(MSKClusterApplication.class);
    }
	
}
