package io.kmruiz.fa.infrastructure.handlers;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class SearchFunnelHandler {
    public Mono<ServerResponse> listings(ServerRequest request) {
        return ServerResponse.ok().body(Mono.just("xx Hi! xx"), String.class);
    }
}
