package dev.vsviniciuslima.orders;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@ToString
@Entity(name = "Orders")
// Define os campos de um Pedido na tabela Orders
public class Order extends PanacheEntity {

    // campos do front-end
    public String name;
    public String price;
    public String clientName;
    public String clientPhoneNumber;
    public String clientObservations;
    public String clientAddress;
    public String paymentMethod;
    public String status = "PENDING";

    // campos q são úteis pro back e banco de dados
    public LocalDateTime createdAt = LocalDateTime.now();
    public UUID uuid = UUID.randomUUID();
    public boolean active = true;
}
