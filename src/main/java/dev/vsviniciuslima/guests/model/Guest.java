package dev.vsviniciuslima.guests.model;

import dev.vsviniciuslima.beans.PageablePanacheEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Entity(name = "Guest")
public class Guest extends PageablePanacheEntity {
    public String name;
    public String mainGuest;
    public long mainGuestId;
    public boolean active;
    @Getter public LocalDateTime createdAt;
    @Getter public LocalDateTime updatedAt;

}
