package io.kmruiz.fa.infrastructure;

import io.kmruiz.fa.domain.listing.ListingMaterializer;
import io.kmruiz.fa.domain.listing.ListingProvider;
import io.kmruiz.fa.infrastructure.handlers.ListingHandler;
import io.kmruiz.fa.infrastructure.handlers.SearchFunnelHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.HttpHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RouterFunctions.toHttpHandler;

@Configuration
public class ServerConfiguration {
    @Bean
    public SearchFunnelHandler searchFunnel() {
        return new SearchFunnelHandler();
    }

    @Bean
    public ListingHandler listingCreation(ListingMaterializer materializer, ListingProvider provider) {
        return new ListingHandler(materializer, provider);
    }

    @Bean
    public HttpHandler httpHandler(SearchFunnelHandler funnel, ListingHandler listing) {
        return toHttpHandler(
                route(
                        GET("/listings"), funnel::listings)
                .and(
                        route(POST("/listing"), listing::create))
                .and(
                        route(PUT("/listing/{uuid}/like"), listing::like)
                )
        );
    }
}
