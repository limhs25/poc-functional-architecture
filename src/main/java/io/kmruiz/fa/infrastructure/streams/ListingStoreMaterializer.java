package io.kmruiz.fa.infrastructure.streams;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kmruiz.fa.domain.listing.Listing;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;

import java.io.IOException;

public class ListingStoreMaterializer {
    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ObjectMapper objectMapper;

    public ListingStoreMaterializer(ElasticsearchTemplate elasticsearchTemplate, ObjectMapper objectMapper) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.objectMapper = objectMapper;
    }

    public void apply(StreamsBuilder streamsBuilder) {
        streamsBuilder.stream("listings", Consumed.with(Serdes.String(), Serdes.String()))
                .peek((key, listingJson) -> {
                    try {
                        Listing listing = objectMapper.readValue(listingJson, Listing.class);
                        elasticsearchTemplate.index(new IndexQueryBuilder().withId(key).withObject(listing).build());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .to("listings-gt");

        streamsBuilder.globalTable("listings-gt", Materialized.as("listings"));
    }
}
