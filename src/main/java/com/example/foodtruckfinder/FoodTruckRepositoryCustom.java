package com.example.foodtruckfinder;

import java.util.List;

public interface FoodTruckRepositoryCustom {
  public List<FoodTruck> nearbyFoodTrucks(double longitude, double latitude, int limit);
}
