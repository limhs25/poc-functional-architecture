package io.kmruiz.fa.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kmruiz.fa.domain.listing.ListingMaterializer;
import io.kmruiz.fa.domain.listing.ListingProvider;
import io.kmruiz.fa.infrastructure.listing.KListingMaterializer;
import io.kmruiz.fa.infrastructure.listing.KListingProvider;
import org.apache.kafka.streams.KafkaStreams;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class ListingInfrastructureConfiguration {
    @Bean
    public ListingProvider listingProvider(KafkaStreams kafkaStreams, ObjectMapper objectMapper) {
        return new KListingProvider(kafkaStreams, objectMapper);
    }

    @Bean
    public ListingMaterializer listingMaterializer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        return new KListingMaterializer(kafkaTemplate, objectMapper);
    }
}
