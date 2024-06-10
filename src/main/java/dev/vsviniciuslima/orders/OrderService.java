package dev.vsviniciuslima.orders;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class OrderService {
    @Transactional
    public Order create(CreateOrder request) {

        // cria um pedido e preenche com os dados enviados pelo front-end
        Order order = new Order();

        order.name = request.name;
        order.price = request.price;
        order.clientName = request.clientName;
        order.clientPhoneNumber = request.clientPhoneNumber;
        order.clientObservations = request.clientObservations;
        order.clientAddress = request.clientAddress;
        order.paymentMethod = request.paymentMethod;

        // guarda o pedido no banco de dados
        order.persist();

        return order;
    }

    public List<Order> listOrders() {
        if(Order.count() == 0) {
            return List.of();
        }
        return Order.listAll();
    }
}
