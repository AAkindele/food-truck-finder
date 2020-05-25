package com.example.foodtruckfinder;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
public class FoodTruckController {
  @Autowired
  private FoodTruckRepository foodTruckRepository;

  // Specifies a default limit if the user does not provide one.
  public static final String DEFAULT_LIMIT = "5";

  // An improvement here is to turn this into a proper REST endpoint with reference links, pagination, etc.
  // Spring has packages that would help with that.
  @GetMapping("/nearby")
  public List<FoodTruck> nearbyFoodTrucks(
    @RequestParam(value = "longitude") double longitude,
    @RequestParam(value = "latitude") double latitude,
    @RequestParam(value = "limit", defaultValue = DEFAULT_LIMIT) int limit
  ) {
    return foodTruckRepository.nearbyFoodTrucks(longitude, latitude, limit);
  }
}
