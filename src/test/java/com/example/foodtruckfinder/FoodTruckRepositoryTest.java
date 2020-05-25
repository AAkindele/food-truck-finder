package com.example.foodtruckfinder;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FoodTruckRepositoryTest {
  @Autowired
  private FoodTruckRepository foodTruckRepository;

  // The test data that comes with this repo has 3 trucks in a line.
  // The truck locationIds are 1, 2, and 3.

  @Test
  public void returnsFirstTwo() {
    // this is the location of the truck with id 1
    // the results should be truck 1 and 2 when searching from truck 1
    double longitude = -122.479139;
    double latitude = 37.825009;
    int limit = 2;
    List<FoodTruck> result = foodTruckRepository.nearbyFoodTrucks(longitude, latitude, limit);
    assertEquals(result.size(), 2);
    assertEquals(result.get(0).getLocationId(), 1);
    assertEquals(result.get(1).getLocationId(), 2);
  }

  @Test
  public void returnsLastTwo() {
    // this is the location of the truck with id 3
    // the results should be truck 3 and 2 when searching from truck 3
    double longitude = -122.477613;
    double latitude = 37.811458;
    int limit = 2;
    List<FoodTruck> result = foodTruckRepository.nearbyFoodTrucks(longitude, latitude, limit);
    assertEquals(result.size(), 2);
    assertEquals(result.get(0).getLocationId(), 3);
    assertEquals(result.get(1).getLocationId(), 2);
  }

  @Test
  public void returnsZeroWhenNegativeLimit() {
    double longitude = -122.478425;
    double latitude = 37.818833;
    int limit = -1;
    List<FoodTruck> result = foodTruckRepository.nearbyFoodTrucks(longitude, latitude, limit);
    assertEquals(result.size(), 0);
  }
}
