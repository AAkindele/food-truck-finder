COPY food_truck(location_id,address,applicant,days_hours,food_items,latitude,longitude,schedule,zip_codes)
FROM '/tmp/food-truck-finder/data/formatted_food_trucks.csv' DELIMITER ',' CSV HEADER;

ALTER TABLE food_truck ADD COLUMN location geometry(Point, 4326);

CREATE INDEX food_truck_location_idx ON food_truck USING GIST (location);

UPDATE food_truck SET location = ST_SetSRID(ST_MakePoint(longitude, latitude), 4326);
