package io.kmruiz.fa.domain.listing;

import reactor.core.publisher.Flux;

import java.util.Optional;

public interface ListingFunnel {
    Flux<Listing> search(Optional<String> fullTextSearch, Optional<String> orderBy, String orderDirection);
}
