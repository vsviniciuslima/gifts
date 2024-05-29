package dev.vsviniciuslima.beans;

import java.util.Map;

public record PanacheQuery(Map<String, Object> params, String query) {
}
