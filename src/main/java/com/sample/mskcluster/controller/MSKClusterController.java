package com.sample.mskcluster.controller;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sample.mskcluster.config.properties.KafkaConfigProperties;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/api")
@AllArgsConstructor
public class MSKClusterController {

	private final Producer<String, String> producer;
	private final KafkaConfigProperties props;
	
	@GetMapping(value = "/v1/data")
	public String sayHelloWorld() {
		
		ProducerRecord<String, String> producerRecord = new ProducerRecord<>(props.getSource(),props.getApplicationId(), "Hi Nivedha");
        producer.send(producerRecord);
	    return "Hello, Sent Messages to the kafka topics";
	}
	
}
