run:
    docker run --rm -d -p 27017:27017  bitnami/mongodb:latest
run-dev:
    docker run --rm -d -p 27017:27017 -v ./mongodb_data:/bitnami --name twittContainer bitnami/mongodb:latest
stop:
    docker stop twittContainer