#!/bin/sh

start_mongo() {
    mongod --config mongod.conf
}

activate_rs() {
    echo "Waiting for Mongo to start..."
    sleep 5
    echo "INITIATE REPLICA SET"
    mongosh --host mongo1 config-replica.js
    sleep 10
    echo "MAKE ADMIN USER FOR REPLICA SET"
    mongosh --host mongo1 config-data.js admin
}

activate_rs & start_mongo