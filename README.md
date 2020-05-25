## Food truck finder
Web API for finding nearby foodtrucks around a geo location.
The dataset for this app is a formatted version of the San Francisco food truck data.
The original can be found here. https://data.sfgov.org/Economy-and-Community/Mobile-Food-Facility-Permit/rqzj-sfat/data

The primary purpose of this excercise is to try and build something with tools that I rarely or have never used.
For this app, those tools are Java, Spring, Gradle, and PostGIS.

### Setup:
Assumes these tools are installed.
- Docker (https://docs.docker.com/get-docker/). I picked docker for managing the database because of problems I ran into getting postgis to work with my existing postgres setup.
- JDK 1.8 or higher (https://www.oracle.com/java/technologies/javase-downloads.html)
- Gradle 4+ (https://gradle.org/install/)

Clone the repo
```
git clone git@github.com:AAkindele/food-truck-finder.git
cd food-truck-finder
```

#### Setup: database:

Get the postgis docker image.
```
docker pull postgis/postgis:9.6-3.0-alpine
```

Create a directory for the postgres container to use.
```
mkdir -p $HOME/food_truck_finder_data
```

Start the database container.
Replace [FULL PATH TO FORMATTED CSV FILE IN THIS REPO] with the path of the "data" directory in this repo.
This will make the "formatted_food_trucks.csv" file available inside the container in the "/tmp" directory.
```
docker run -it --rm \
  --name food_truck_finder_database \
  -v $HOME/food_truck_finder_data:/var/lib/postgresql/data \
  -v [FULL PATH TO FORMATTED CSV FILE IN THIS REPO]:/tmp/food-truck-finder/data \
  -e POSTGRES_USER=foodtruckfinder \
  -e POSTGRES_PASSWORD=mysecretpassword \
  -e POSTGRES_DB=foodtruckfinder_dev \
  -p 5432:5432 \
  postgis/postgis:9.6-3.0-alpine
```

For example, if the location of this repository is located at `/my-code/food-truck-finder`, then change
```
-v [FULL PATH TO FORMATTED CSV FILE IN THIS REPO]:/tmp/food-truck-finder/data \
```
to
```
-v /my-code/food-truck-finder/data:/tmp/food-truck-finder/data \
```

#### Setup: web server:

In a new tab, start the web server. This will create the database table.  Ideally, migrations would be used to setup the database.
```
./gradlew bootRun
```

Another option is to build the applciation without running tests.
```
./gradlew build -x test
```

Then start the server.
```
java -jar build/libs/foodtruckfinder-0.0.1-SNAPSHOT.jar
```

#### Setup: load sample data:

In a new tab, import the sample data into the database.
In a production environment, a separate ETL process for fetching the latest data, and keeping the database updated would be ideal.
These steps are okay for setting up a development environment.

Login to the database container.
```
docker exec -it food_truck_finder_database /bin/sh
```

Load the development data.
```
psql -h localhost -U foodtruckfinder -d foodtruckfinder_dev -f /tmp/food-truck-finder/data/dev_data.sql
```

### Using the app:

Open a web browser and go to `http://localhost:8080/nearby`.
Query the data by passing in latitude, longitude, and an optional limit param.
ex: `http://localhost:8080/nearby?longitude=-122.399617948655&latitude=37.7992601135023&limit=5`


### Testing:

#### Testing: database:

Setup the testing database. Make sure to stop the development database first and web server first.

Create a directory for the postgres testing database container to use.
```
mkdir -p $HOME/food_truck_finder_test_data
```

Start a new database container with some modifications to the command for starting the development database.

Make these changes:
- Change `--name food_truck_finder_database \` to `--name food_truck_finder_test_database \`.
- Change `-v $HOME/food_truck_finder_data:/var/lib/postgresql/data \` to `-v $HOME/food_truck_finder_test_data:/var/lib/postgresql/data \`.
- Change `-e POSTGRES_DB=foodtruckfinder_dev \` to `-e POSTGRES_DB=foodtruckfinder_test \`.

The command should look like
```
docker run -it --rm \
  --name food_truck_finder_test_database \
  -v $HOME/food_truck_finder_test_data:/var/lib/postgresql/data \
  -v [FULL PATH TO FORMATTED CSV FILE IN THIS REPO]:/tmp/food-truck-finder/data \
  -e POSTGRES_USER=foodtruckfinder \
  -e POSTGRES_PASSWORD=mysecretpassword \
  -e POSTGRES_DB=foodtruckfinder_test \
  -p 5432:5432 \
  postgis/postgis:9.6-3.0-alpine
```

#### Testing: web server:

In a new tab, start the web server and point it to the test database.
This will create the database table in the test database. Ideally, migrations would be used to setup the test database as well.
```
DB_NAME=foodtruckfinder_test ./gradlew bootRun
```

#### Testing: load sample data:

In a new tab, import the sample test data into the test database.
Login to the database container
```
docker exec -it food_truck_finder_test_database /bin/sh
```

Load the development data
```
psql -h localhost -U foodtruckfinder -d foodtruckfinder_test -f /tmp/food-truck-finder/data/test_data.sql
```

#### Testing: run tests:

In a new tab, run the tests
```
DB_NAME=foodtruckfinder_test gradle clean build
```
