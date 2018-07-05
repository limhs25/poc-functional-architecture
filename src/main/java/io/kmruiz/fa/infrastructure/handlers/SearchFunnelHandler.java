package io.kmruiz.fa.infrastructure.handlers;

import io.kmruiz.fa.domain.listing.Listing;
import io.kmruiz.fa.domain.listing.ListingFunnel;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class SearchFunnelHandler {
    private final ListingFunnel funnel;

    public SearchFunnelHandler(ListingFunnel funnel) {
        this.funnel = funnel;
    }

    public Mono<ServerResponse> listings(ServerRequest request) {
        Flux<Listing> listings = funnel.search(request.queryParam("fti"), request.queryParam("o"), request.queryParam("od").orElse("desc"));
        return ServerResponse.ok().body(listings, Listing.class);
    }
}
