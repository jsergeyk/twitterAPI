1 Download image mongo and rename to mongodb:

    docker pull mongo
    docker tag mongo mongodb

2 Run creating container "mongodb":

    docker run -v mongodata:/data/db -d -p 27018:27017 --name mongodb mongodb

3 Build project with running integration tests:

    ./gradlew clean build

4 ./gradlew docker - creating image "kalchenkoserhii/twitterapp" from Dockerfile

    ./gradlew docker

or pull from hub.docker:

    docker pull kalchenkoserhii/twitterapp

5 Run creating container "twitterapp":
    
    docker run -d -p 8080:8080 --link mongodb --name twitterapp kalchenkoserhii/twitterapp
