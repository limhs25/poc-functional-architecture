package io.kmruiz.fa.infrastructure.listing;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kmruiz.fa.domain.listing.Listing;
import io.kmruiz.fa.domain.listing.ListingProvider;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.UUID;

public class KListingProvider implements ListingProvider {
    private final KafkaStreams kafka;
    private final ObjectMapper objectMapper;

    public KListingProvider(KafkaStreams kafka, ObjectMapper objectMapper) {
        this.kafka = kafka;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Listing> provide(UUID listing) {
        return Mono.just(queryableStore().get(listing.toString()))
                .flatMap(this::parseOrError);
    }

    private Mono<Listing> parseOrError(String json) {
        try {
            return Mono.just(objectMapper.readValue(json, Listing.class));
        } catch (IOException e) {
            return Mono.error(e);
        }
    }

    private ReadOnlyKeyValueStore<String, String> queryableStore() {
        return kafka.store("listings", QueryableStoreTypes.keyValueStore());
    }
}
