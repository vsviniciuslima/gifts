package dev.vsviniciuslima.whatsapp.connections.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@ToString
@Entity(name = "Connections")
public class Connection extends PanacheEntity {
    public String jid;
    public String name;
    public UUID uuid;
    public boolean active = true;

    @Column(name = "phone_number")
    public long phoneNumber;

    @Column(name = "created_at")
    public LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    public LocalDateTime updatedAt = LocalDateTime.now();


}
