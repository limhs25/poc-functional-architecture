package io.kmruiz.fa.infrastructure.streams;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Materialized;

public class ListingStoreMaterializer {
    public static void apply(StreamsBuilder streamsBuilder) {
        streamsBuilder.stream("listings")
                .peek((k, v) -> System.out.println("-------> " + v))
                .to("listings-gt");

        streamsBuilder.globalTable("listings-gt", Materialized.as("listings"));
    }
}
