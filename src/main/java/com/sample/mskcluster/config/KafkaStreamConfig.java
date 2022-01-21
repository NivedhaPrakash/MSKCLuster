package com.sample.mskcluster.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.kafka.KafkaStreamsMetrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sample.mskcluster.config.properties.KafkaConfigProperties;
import com.sample.mskcluster.processor.EligibilityStartProcessor;

import java.util.Properties;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class KafkaStreamConfig {

    @Autowired
    private KafkaConfigProperties kafkaConfigProperties;

    private final MeterRegistry meterRegistry;
    
    private final EligibilityStartProcessor processor;

    @Bean
    public CommandLineRunner stream()
    {
        return args ->{
            final KafkaStreams streams = new KafkaStreams(processor.getTopology(),this.getProperties());
            final KafkaStreamsMetrics kafkaStreamsMetrics = new KafkaStreamsMetrics(streams);
            kafkaStreamsMetrics.bindTo(meterRegistry);
            log.debug("***********************Topology Description************************* {}",processor.getTopology().describe());
            streams.start();
            Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        };
    }

    private Properties getProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigProperties.getBootstrapServer());
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, kafkaConfigProperties.getApplicationId());
        props.put(ConsumerConfig.CLIENT_ID_CONFIG,kafkaConfigProperties.getApplicationId());
        return props;
    }

    @Bean
    public Producer<String, String> producer(KafkaConfigProperties kafkaConfigProperties)
    {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigProperties.getBootstrapServer());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.CLIENT_ID_CONFIG,kafkaConfigProperties.getApplicationId());
        return new KafkaProducer<>(props);
    }

}
