package dev.vsviniciuslima.beans;

import dev.vsviniciuslima.dto.PageRequest;
import dev.vsviniciuslima.dto.PageResponse;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.impl.GenerateBridge;

import java.util.Collections;
import java.util.List;

public abstract class PageablePanacheEntity extends PanacheEntity {

    @GenerateBridge(
            targetReturnTypeErased = true
    )
    public static <T extends PanacheEntityBase> List<T> page(PageRequest params, PanacheQuery query) {
        Page page = params.getPage();
        return find(query.query(), params.getSort(), query.params())
                .page(page)
                .list();
    }

    @GenerateBridge(
            targetReturnTypeErased = true
    )
    public static PageResponse pageResponse(PageRequest params, PanacheQuery query) {

        List<? extends PageablePanacheEntity>
                records = find(query.query(), params.getSort(), query.params())
                .page(params.getPage())
                .list();

        return new PageResponse(params, records);
    }
}
