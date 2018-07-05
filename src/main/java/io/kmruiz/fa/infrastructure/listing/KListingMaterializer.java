package io.kmruiz.fa.infrastructure.listing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.kmruiz.fa.domain.listing.Listing;
import io.kmruiz.fa.domain.listing.ListingMaterializer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import reactor.core.publisher.Mono;

public class KListingMaterializer implements ListingMaterializer {
    private final KafkaTemplate<String, String> kafka;
    private final ObjectMapper objectMapper;

    public KListingMaterializer(KafkaTemplate<String, String> kafka, ObjectMapper objectMapper) {
        this.kafka = kafka;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Listing> materialize(Listing listing) {
        try {
            String listingJson = objectMapper.writeValueAsString(listing);
            ListenableFuture<SendResult<String, String>> completion = kafka.send("listings", listing.uuid.toString(), listingJson);

            return Mono.fromFuture(completion.completable())
                    .map(r -> listing);
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }
}
