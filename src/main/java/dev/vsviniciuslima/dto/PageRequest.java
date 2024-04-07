package dev.vsviniciuslima.config;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.ws.rs.QueryParam;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class PageRequest {

    @QueryParam("sortDirection")
    private Sort.Direction sortDirection = Sort.Direction.Descending;

    @QueryParam("sortBy")
    private String sortBy = "id";

    @QueryParam("index")
    private int index = 0;

    @QueryParam("size")
    private int size = 0;

    public Sort getSort() {
        return Sort.by(sortBy, sortDirection);
    }

    public Page getPage() {
        if(size == 0) {
            return Page.of(0, Integer.MAX_VALUE);
        } else {
            return Page.of(index, size);
        }
    }


}
