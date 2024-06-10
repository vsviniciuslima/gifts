package dev.vsviniciuslima.orders;

import dev.vsviniciuslima.dto.PageRequest;
import dev.vsviniciuslima.dto.PaginatedResponse;
import dev.vsviniciuslima.gifts.model.BuyGift;
import dev.vsviniciuslima.gifts.model.CreateGift;
import dev.vsviniciuslima.gifts.model.Gift;
import dev.vsviniciuslima.orders.Order;
import dev.vsviniciuslima.orders.OrderService;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

@Slf4j
@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
public class OrderController {

    @Inject
    OrderService service;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Criar pedidos")
    @APIResponse(description = "Lista de pedidos criados",
            responseCode = "200",
            content = { @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            implementation = Order.class,
                            type = SchemaType.ARRAY
                    ))
            })
    @Tag(name = "Pedidos", description = "Adicionar, remover e atualizar pedidos")
    @Transactional
    public Response createOrders(
            @RequestBody(description = "Lista de pedidos a serem criados") List<CreateOrder> orders
    ) {
        List<Order> createdOrders = orders.stream()
                .map(request -> service.create(request))
                .toList();

        return Response.ok(createdOrders).build();
    }

    @PATCH
    @Transactional
    @Path("/{id}/accept")
    @Consumes(MediaType.APPLICATION_JSON)
    @Tag(name = "Pedidos", description = "Aceitar pedido")
    public Response acceptOrder(@PathParam("id") Long id) {

        Order order = Order.findById(id);
        order.status = "ACCEPTED";
        order.persist();

        return Response.ok().build();
    }


    @GET
    @Operation(summary = "Listar pedidos",
            description = "Listar todos os pedidos cadastrados")
    @Tag(name = "Pedidos", description = "Listar pedidos")
    public Response getOrders() {
        return Response.ok(service.listOrders()).build();
    }
}
