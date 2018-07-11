package io.kmruiz.fa.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kmruiz.fa.infrastructure.streams.ListingStoreMaterializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {
    public static class KafkaConfig {
        private Map<String, Object> kafka;

        public KafkaConfig(Map<String, Object> kafka) {
            this.kafka = kafka;
        }

        public Map<String, Object> getKafka() {
            return kafka;
        }

        public void setKafka(Map<String, Object> kafka) {
            this.kafka = kafka;
        }
    }

    @Bean
    @ConfigurationProperties(prefix = "spring")
    public KafkaConfig kafkaConfig() {
        return new KafkaConfig(Collections.emptyMap());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(KafkaConfig kafkaConfig) {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(kafkaConfig.kafka, new StringSerializer(), new StringSerializer()));
    }

    @Bean
    public StreamsBuilder streamsBuilder(ElasticsearchTemplate template, ObjectMapper objectMapper) {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        new ListingStoreMaterializer(template, objectMapper).apply(streamsBuilder);
        return streamsBuilder;
    }

    @Bean
    public StreamsConfig streamsConfig(KafkaConfig kafkaConfig) {
        Map<String, Object> baseCopy = new HashMap<>(kafkaConfig.kafka);
        baseCopy.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        baseCopy.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);

        return new StreamsConfig(baseCopy);
    }

    @Bean
    public KafkaStreams kafkaStreams(StreamsBuilder builder, StreamsConfig configuration) {
        KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), configuration);
        kafkaStreams.start();
        return kafkaStreams;
    }
}
