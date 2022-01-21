package com.sample.mskcluster.processor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import com.sample.mskcluster.config.properties.KafkaConfigProperties;

@Component
@Slf4j
@AllArgsConstructor
public class EligibilityStartProcessor {

	private final Producer<String, String> producer;
	private final KafkaConfigProperties props;

    // Consumes from the source Topic
    public Topology getTopology()
    {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> stream = builder.stream(props.getSource());
        this.process(stream);
        return builder.build();
    }

    public void process(KStream<String, String> stream) {
        // update the response
        stream.peek((key, value) ->{
                    MDC.put("correlationId",value.toString());
                    log.info("Process stream starting ",value.toString());
        } );

        stream.foreach(this::processEligibilityResponse);
        log.info("Process stream completed");
    }

    public void processEligibilityResponse(String key, String value) {
    	ProducerRecord<String, String> producerRecord = new ProducerRecord<>(props.getResponse(),props.getApplicationId(), "Hi Nivedha");
        producer.send(producerRecord);
    }

}
