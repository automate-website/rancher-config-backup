#!/usr/bin/env bash

if [ "$TRAVIS_BRANCH" == "master" ]; then
    docker login -u="$DOCKER_USER" -p="$DOCKER_PASSWORD";
    docker push automatewebsite/rancher-config-backup;
fi

if [ ! -z "$TRAVIS_TAG" ]; then
    docker login -u="$DOCKER_USER" -p="$DOCKER_PASSWORD";
    docker tag automatewebsite/rancher-config-backup automatewebsite/rancher-config-backup:$TRAVIS_TAG
    docker push automatewebsite/rancher-config-backup:$TRAVIS_TAG;
fi
