package dev.vsviniciuslima.confirmations.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@ToString
@Entity(name = "Guest")
public class Guest extends PanacheEntity {
    public String name;
    public String mainGuest;
    @Getter public LocalDateTime createdAt;
}
