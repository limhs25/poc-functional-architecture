package io.kmruiz.fa.infrastructure;

import io.kmruiz.fa.domain.listing.ListingFunnel;
import io.kmruiz.fa.domain.listing.ListingMaterializer;
import io.kmruiz.fa.domain.listing.ListingProvider;
import io.kmruiz.fa.infrastructure.handlers.ListingHandler;
import io.kmruiz.fa.infrastructure.handlers.SearchFunnelHandler;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.http.server.reactive.HttpHandler;

import java.net.InetAddress;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RouterFunctions.toHttpHandler;

@Configuration
public class ServerConfiguration {
    @Bean
    public SearchFunnelHandler searchFunnel(ListingFunnel funnel) {
        return new SearchFunnelHandler(funnel);
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

    @Bean
    public Client client() throws Exception {
        return new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
    }

    @Bean
    public ElasticsearchTemplate template(Client client) {
        return new ElasticsearchTemplate(client);
    }
}
