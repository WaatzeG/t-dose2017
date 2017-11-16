#!/usr/bin/env bash

gradle build -p $(pwd)/..

docker run --network docker_default -it -v $(pwd)/../build/libs/shakespeareCounter-with-dependencies.jar:/topology.jar \
    storm storm jar /topology.jar \
    Main
