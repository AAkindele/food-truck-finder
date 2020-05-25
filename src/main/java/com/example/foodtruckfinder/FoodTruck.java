package com.example.foodtruckfinder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
public class FoodTruck {
  // Notes:
  // I decided to only include a subset of the columns from the original dataset.
  // The main reason is that this class is used with the "spring.jpa.hibernate.ddl-auto=update" to create the table on startup.
  // So, using a smaller number of columns keeps this maneagable for this exercise.
  //
  // locationId is used directly from the original dataset as the primary.
  // This is also for simplicity during this exercise because there is only one data source.
  //
  // An improvement would be to use database migrations and not use this class to create the database schema.
  // Another potential improvement is to let the app and/or database manage a primary key for the data.
  // That would remove the risk of having duplicate primary keys in the case of multiple data sources.
  @Id
  private int locationId;
  private String address;
  private String applicant;
  private String daysHours;
  @Column(columnDefinition="TEXT")
  private String foodItems;
  private double latitude;
  private double longitude;
  private String schedule;
  private String zipCodes;

  public int getLocationId() { return this.locationId; }

  public String getAddress() { return this.address; }

  public String getApplicant() { return this.applicant; }

  public String getDaysHours() { return this.daysHours; }

  public String getFoodItems() { return this.foodItems; }

  public double getLatitude() { return this.latitude; }

  public double getLongitude() { return this.longitude; }

  public String getSchedule() { return this.schedule; }

  public String getZipCodes() { return this.zipCodes; }
}
