package io.kmruiz.fa.domain.listing;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.Function;

@Document(indexName = "listings")
public class Listing {
    @Id
    public final UUID uuid;
    @Field(type = FieldType.Text, fielddata = true, store = true)
    public final String title;
    @Field(type = FieldType.Text, fielddata = true, store = true)
    public final List<String> labels;
    @Field(type = FieldType.Text, fielddata = true, store = true)
    public final String description;
    @Field(type = FieldType.Text, fielddata = true, store = true)
    public final String price;
    @Field(type = FieldType.Long, store = true)
    public final int likes;

    private Listing(UUID uuid, String title, List<String> labels, String description, String price, int likes) {
        this.uuid = uuid;
        this.title = title;
        this.labels = labels;
        this.description = description;
        this.price = price;
        this.likes = likes;
    }

    private Listing() {
        this(null, null, null, null, null, 0);
    }

    public static Mono<Listing> build(Map<String, Object> mm) {
        Listing listing = new Listing(
                Optional.ofNullable(mm.get("uuid")).map(Object::toString).map(UUID::fromString).orElse(UUID.randomUUID()),
                mm.get("title").toString(),
                (List<String>) mm.get("labels"),
                mm.get("description").toString(),
                mm.get("price").toString(),
                Integer.valueOf(mm.get("likes").toString())
        );

        return Mono.just(listing);
    }

    public static Function<Listing, Listing> like() {
        return listing -> new Listing(listing.uuid, listing.title, listing.labels, listing.description, listing.price, listing.likes + 1);
    }
}
