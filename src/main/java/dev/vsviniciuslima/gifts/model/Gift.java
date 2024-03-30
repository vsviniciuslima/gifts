package dev.vsviniciuslima.gifts.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

import java.util.List;

@Entity(name = "Gift")
public class Gift extends PanacheEntity {

    public String name;
    public String imageUrl;
    public Double price;
    public String category;

    @ElementCollection
    public List<String> recommendationUrls;

    public boolean bought;
    public String buyer;
    public String buyerEmail;
    public String buyerMessage;

    public static List<Gift> listBoughtGifts() {
        return list("bought", true);
    }
}
