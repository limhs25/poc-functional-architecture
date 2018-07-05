package io.kmruiz.fa.infrastructure.handlers;

import io.kmruiz.fa.domain.listing.Listing;
import io.kmruiz.fa.domain.listing.ListingMaterializer;
import io.kmruiz.fa.domain.listing.ListingProvider;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

public class ListingHandler {
    private final ListingMaterializer materializer;
    private final ListingProvider provider;

    public ListingHandler(ListingMaterializer materializer, ListingProvider provider) {
        this.materializer = materializer;
        this.provider = provider;
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(Map.class)
                .map(e -> (Map<String, Object>) e)
                .flatMap(Listing::build)
                .flatMap(materializer::materialize)
                .flatMap(ServerResponse.ok()::syncBody);
    }

    public Mono<ServerResponse> like(ServerRequest request) {
        String uuid = request.pathVariable("uuid");
        return provider.provide(UUID.fromString(uuid))
                .map(Listing.like())
                .flatMap(materializer::materialize)
                .flatMap(ServerResponse.ok()::syncBody);
    }
}
