package dev.vsviniciuslima.dto;

import io.quarkus.panache.common.Sort;

public record PaginatedResponse(int currentPage,
                                int pageSize,
                                long totalPages,
                                long totalItems,
                                Sort.Direction sortDirection,
                                String sortBy,
                                Object data
) { }
