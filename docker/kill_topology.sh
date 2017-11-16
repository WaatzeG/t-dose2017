#!/usr/bin/env bash


docker run --network docker_default -it \
    storm storm kill -w 10 "word_counter"
