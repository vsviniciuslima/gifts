package dev.vsviniciuslima.beans;

import java.util.Map;

public record PanacheQueryData(Map<String, Object> params, String query) {
}
