#! /bin/bash

function waitForController {
  while [[ "$(curl -w "%{http_code}" -m 1 -s -k -o /dev/null https://127.0.0.1:1280/version)" != "200" ]]; do
    echo "waiting for https://127.0.0.1:1280"
    sleep 3
  done
}


# Start everything except the tunneler.  The tunneler needs the identity file created during 
# the OpenZiti initialization, so needs to be started later
docker-compose -p pg up -d ziti-controller ziti-edge-router postgres-db

waitForController

# Log into the ziti controller
ziti edge login -y -i postgresql 127.0.0.1:1280 -u admin -p admin

# Create the database service and grant permissions
./initZitiAccess.sh

mkdir -p tunnel
mv databaseTunneler.json tunnel

# Finally, start the tunneler. 
docker-compose -p pg up -d database-tunneler
