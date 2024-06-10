package dev.vsviniciuslima.whatsapp.messages.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "Users")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends PanacheEntity {
    private String name;
    private String email;
    private Stage stage;
    private boolean isCompany;

    private boolean active;

    @Column(name = "phone_number")
    public long phoneNumber;

    @Column(name = "created_at")
    public LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    public LocalDateTime updatedAt = LocalDateTime.now();
}
