package dev.vsviniciuslima.common;

import java.util.Map;

public record ErrorResponse(String message, int code, Map<String, Object> details) { }
