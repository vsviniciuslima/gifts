package dev.vsviniciuslima.dto;

public record PaginatedResponse(int currentPage, int pageSize, long totalPages, long totalItems, Object data) {
}
