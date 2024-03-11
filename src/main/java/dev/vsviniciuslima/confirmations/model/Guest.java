package dev.vsviniciuslima.confirmations.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Entity(name = "Guest")
public class Guest extends PanacheEntity {
    public String name;
    public String mainGuest;
    public LocalDateTime createdAt;
}
