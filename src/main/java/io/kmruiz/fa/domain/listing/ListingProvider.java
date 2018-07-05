package io.kmruiz.fa.domain.listing;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ListingProvider {
    Mono<Listing> provide(UUID listing);
}
