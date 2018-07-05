package io.kmruiz.fa.domain.listing;

import reactor.core.publisher.Mono;

public interface ListingMaterializer {
    Mono<Listing> materialize(Listing listing);
}
