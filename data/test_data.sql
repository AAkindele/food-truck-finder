INSERT INTO food_truck(location_id, latitude, longitude) VALUES(1, 37.825009, -122.479139);
INSERT INTO food_truck(location_id, latitude, longitude) VALUES(2, 37.819112, -122.478515);
INSERT INTO food_truck(location_id, latitude, longitude) VALUES(3, 37.811458, -122.477613);

ALTER TABLE food_truck ADD COLUMN location geometry(Point, 4326);
CREATE INDEX food_truck_location_idx ON food_truck USING GIST (location);
UPDATE food_truck SET location = ST_SetSRID(ST_MakePoint(longitude, latitude), 4326);
