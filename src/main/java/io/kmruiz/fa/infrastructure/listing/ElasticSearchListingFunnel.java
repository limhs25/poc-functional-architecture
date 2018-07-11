package io.kmruiz.fa.infrastructure.listing;

import io.kmruiz.fa.domain.listing.Listing;
import io.kmruiz.fa.domain.listing.ListingFunnel;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.querydsl.QPageRequest;
import reactor.core.publisher.Flux;

import java.util.Optional;

public class ElasticSearchListingFunnel implements ListingFunnel {
    private final ElasticsearchTemplate template;

    public ElasticSearchListingFunnel(ElasticsearchTemplate template) {
        this.template = template;

        template.createIndex(Listing.class);
        template.putMapping(Listing.class);
    }

    @Override
    public Flux<Listing> search(Optional<String> fullTextSearch, Optional<String> orderBy, String orderDirection) {
        Criteria criteria = new Criteria();
        if (fullTextSearch.isPresent()) {
            criteria = criteria.and("title").contains(fullTextSearch.get());
        }

        CriteriaQuery query = new CriteriaQuery(criteria,
                PageRequest.of(0, 100, Sort.by(Sort.Direction.fromString(orderDirection), orderBy.orElse("title")))
        );

        return Flux.fromIterable(template.queryForList(query, Listing.class));
    }
}
