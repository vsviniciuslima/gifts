package dev.vsviniciuslima.gifts.model;

import java.util.List;

public record CreateGift(String name, String imageUrl, Double price, String category, List<Recommendation> recommendations){
}
