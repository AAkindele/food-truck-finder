package com.example.foodtruckfinder;

import org.springframework.data.repository.CrudRepository;

public interface FoodTruckRepository extends CrudRepository<FoodTruck, Long>, FoodTruckRepositoryCustom {
}
