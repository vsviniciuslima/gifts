package dev.vsviniciuslima.dto;

import dev.vsviniciuslima.beans.PageablePanacheEntity;
import io.quarkus.panache.common.Sort;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class PageResponse {
    private int currentPage;
    private int pageSize;
    private long totalPages;
    private long totalItems;
    private Sort.Direction sortDirection;
    private String sortBy;
    private List<? extends PageablePanacheEntity> data;

    public PageResponse(PageRequest params, List<? extends PageablePanacheEntity> data) {
        this.currentPage = params.getPage().index;
        this.pageSize = params.getPage().size;
        this.sortDirection = params.getSortDirection();
        this.sortBy = params.getSortBy();
        this.data = data;
    }

    public int getTotalItems() {
        return this.data.size();
    }

    public long getTotalPages() {
        return (this.data.size() / this.pageSize -1) / this.pageSize;
    }
}
