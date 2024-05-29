package dev.vsviniciuslima.beans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.UriInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
public class PanacheQueryBuilder {

    @Context
    UriInfo uriInfo;
    public PanacheQuery buildQuery() {

        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();

        if (queryParameters.isEmpty())
            return new PanacheQuery(Map.of(), "");

        Map<String, Object> acceptedParams = queryParameters.entrySet().stream()
                .filter(this::filterAcceptedParams)
                .collect(Collectors.toMap(Map.Entry::getKey, this::getEntryValue));

        String query = acceptedParams.keySet().stream()
                .map(param -> param + "=:" + param)
                .collect( Collectors.joining(" and ") );

        return new PanacheQuery(acceptedParams, query);

    }

    private boolean filterAcceptedParams(Map.Entry<String, List<String>> entry) {
        boolean paramIsAllowed = !List.of("sortDirection", "sortBy", "index", "size").contains(entry.getKey());
        return paramIsAllowed && entry.getValue() != null;
    }

    private Object getEntryValue(Map.Entry<String, List<String>> entry) {
        String firstValue = entry.getValue().get(0);
        if (List.of("false", "true").contains(firstValue)) {
            return Boolean.valueOf(firstValue);
        }
        return firstValue;
    }



}
