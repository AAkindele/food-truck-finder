package com.example.foodtruckfinder;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;

public class FoodTruckRepositoryImpl implements FoodTruckRepositoryCustom {
  @PersistenceContext
  EntityManager entityManager;

  // Limit how much a user can request at once.
  // Would not want to give someone the ability to dump the entire table through this distance query.
  // If there is a use case for getting a larger number of results at once,
  // then would look into a different setup specifically for that feature.
  public static final int MAX_LIMIT = 50;
  public static final int MIN_LIMIT = 0;

  // Use postgis' nearest neighbor search.
  // It would be useful to return the distance for each location to the user.
  public static final String NEARBY_QUERY = "SELECT * FROM food_truck ORDER BY location <-> ST_SetSRID(ST_MakePoint( :longitude , :latitude ), 4326) ASC LIMIT :limit ;";

  public List<FoodTruck> nearbyFoodTrucks(double longitude, double latitude, int limit) {
    Query query = entityManager.createNativeQuery(NEARBY_QUERY, FoodTruck.class);
    query.setParameter("longitude", longitude).
      setParameter("latitude", latitude).
      setParameter("limit", sanitizedLimit(limit));

    return query.getResultList();
  }

  public int sanitizedLimit(int limit) {
    if (limit > MAX_LIMIT) { return MAX_LIMIT; }
    if (limit < MIN_LIMIT) { return MIN_LIMIT; }

    return limit;
  }
}
