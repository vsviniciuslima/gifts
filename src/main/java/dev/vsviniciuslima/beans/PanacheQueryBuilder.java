package dev.vsviniciuslima.beans;

import dev.vsviniciuslima.gifts.model.Gift;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.util.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.UriInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
public class PanacheQueryBuilder {

    @Context
    UriInfo uriInfo;
    public Optional<PanacheQuery> buildQuery() {

        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();

        if (queryParameters.isEmpty()) return Optional.empty();

        Map<String, Object> nonNullParams = queryParameters.entrySet().stream()
                .filter( entry -> entry.getValue() != null )
                .collect( Collectors.toMap( Map.Entry::getKey, entry -> {
                    String firstValue = entry.getValue().get(0);
                    if(List.of("false", "true").contains(firstValue)) {
                        return Boolean.valueOf(firstValue);
                    }
                    return firstValue;
                }));

        String query = nonNullParams.keySet().stream()
                .map(param -> param + "=:" + param)
                .collect( Collectors.joining(" and ") );

        log.info("query: {}", query);
        log.info("params: {}", nonNullParams);
        return Optional.of(new PanacheQuery(nonNullParams, query));

    }

}
