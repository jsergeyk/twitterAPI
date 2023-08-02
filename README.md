Download image mongo and rename to mongodb:

    docker pull mongo
    docker tag mongo mongodb

Run creating container "mongodb":

    docker run -v mongodata:/data/db -d -p 27018:27017 --name mongodb mongodb

Build project with running integration tests:

    ./gradlew clean build

./gradlew docker - creating image "twitterapp" from Dockerfile

    ./gradlew docker

Run creating container "twitterapp":
    
    docker run -d -p 8080:8080 --link mongodb --name twitterapp twitterapp
