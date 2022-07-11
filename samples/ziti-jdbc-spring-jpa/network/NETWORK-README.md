# Overview
This document describes the steps necessary to configure an OpenZiti (https://github.com/openziti) network for the code samples contained in this project.

# Requirements
* docker-compose
* a bash shell*

*The startup and teardown scripts use bash. The docker-compose commands could be run individually from a different shell

# Network directory file inventory
* **docker-compose.yml** Compose file with the dark postgres server and an edge router to access it
* **startup.sh:** Starts the docker-compose environment
* **teardown.sh:** Stops and cleans up the docker-compose environment
* **network-setup.sh** Bootstrap file for the OpenZiti network.  Creates identities and access policies for the sample
* **postgres-bootstrap.sh:** Bootstrap file for the postgres server. It creates the database used by the sample
* **NETWORK-README.md:** This file

# Start the OpenZiti network
## Get the OpenZiti quickstart simplified docker-compose and environment files
OpenZiti provides a quickstart docker-compose file that will run a network that has one controller and one edge router.

Open a terminal window to the `spring-jpa/network` folder.  Leave this terminal open, you'll need it to manage the 
network too.

```shell
# Pull the docker compose file
curl -o simplified-docker-compose.yml https://raw.githubusercontent.com/openziti/ziti/release-next/quickstart/docker/simplified-docker-compose.yml

#Pull the docker environment file
curl -o .env https://raw.githubusercontent.com/openziti/ziti/release-next/quickstart/docker/.env
```

## Launch the network
The startup script will launch the OpenZiti quickstart network then add in the postgresql database and all 
OpenZiti network configuration for the demo.

Run the startup script from the `samples/spring-jpa/network` directory:

```shell
./startup.sh
```

This will start the OpenZiti network in the background.  It will take some time to start the network.  Everything is
ready when the identity files appear in the network directory.

# Stop the OpenZiti network
The `teardown.sh` script will stop the docker environment and clean it up by removing volumes and networks created for 
the demo.

To run the teardown script from the `samples/spring-jpa/network` directory:

```shell
./teardown.sh
```

# What's going on in network-setup.sh
This section is for folks that want to know what's required to set up a network for this sample.

This demo makes use of a few OpenZiti features:
* **Identities:** An OpenZiti identity grants access to the network. Each person or application connecting to the 
OpenZiti network requires a unique identity
* **Services:** Services are endpoints that can be invoked over the OpenZiti network. This example includes two 
services - the HTTP server and Postgresql database
* **Service Access Policies:** The identity grants connection rights. Service Access Policies grant service rights
* **Identity Edge Router Policies:** An OpenZiti edge router policy lets an identity connect to one or more edge 
routers. Edge routers allow data in and out of the OpenZiti network overlay
* **Service Edge Router Policies:** These policies tell OpenZiti which edge routers can be used to handle data for a 
service. Restricting edge routers via this policy can restrict data for a service to a sub-section of the OpenZiti overlay mesh

## Create and enroll identities
This example uses three OpenZiti identities:
* **private-service:** Hosts the web application. In Ziti terms this identity will bind the HTTP service and 
dial the database service
* **client** Used by the client to make web requests to the http server
* **database** Hosts the postgres database service.

```shell
ziti edge create identity device private-service -o /openziti/network-setup/private-service.jwt -a "services"
ziti edge create identity device client -o /openziti/network-setup/client.jwt -a "clients"
ziti edge create identity device database -o /openziti/network-setup/database.jwt -a "databases"
```

The identities need to be enrolled before they can be used. Enrolling the identities creates files that contain
private encryption keys required to connect to the OpenZiti network overlay.

```shell
ziti edge enroll -j /openziti/network-setup/private-service.jwt
ziti edge enroll -j /openziti/network-setup/client.jwt
ziti edge enroll -j /openziti/network-setup/database.jwt
```

The network directory will contain three identity files after enrollment is complete:
* private-service.json
* client.json
* database.json

## Create services for the database and http server
The jpa demo uses two services; one for the HTTP server and one for the Postgresql database.

```shell
ziti edge create config demo-service-config ziti-tunneler-client.v1 '{"hostname": "example.web","port": 8080}'
ziti edge create service demo-service --configs demo-service-config -a "demo-service"

ziti edge create config private-postgres-intercept.v1 intercept.v1 '{"protocols":["tcp"],"addresses":["private-postgres-server.demo"], "portRanges":[{"low":5432, "high":5432}]}'
ziti edge create config private-postgres-host.v1 host.v1 '{"protocol":"tcp", "address":"postgres-db","port":5432 }'
ziti edge create service private-postgres --configs private-postgres-intercept.v1,private-postgres-host.v1 -a "private-postgres-services"
```

## Create service access policies
Access required:

|identity|policy|service|
|--------|------|------|
|client|Dial|demo-service|
|private-service|Dial|private-postgres|
|private-service|Bind|demo-service|
|database|Bind|private-postgres|

The network setup script uses roles to grant permissions. Roles allow identities to be logically grouped together to 
make management easier.

```shell
ziti edge create service-policy service-bind-policy Bind --identity-roles "#services" --service-roles "#demo-service"
ziti edge create service-policy service-dial-policy Dial --identity-roles "#clients" --service-roles "#demo-service"
ziti edge create service-policy database-bind-policy Bind --identity-roles "#databases" --service-roles "#private-postgres-services"
ziti edge create service-policy database-dial-policy Dial --identity-roles "#services" --service-roles "#private-postgres-services"
```

## Create edge router access policies
The quickstart network includes edge router access policies that give all identities access to `public` edge routers.

```shell
ziti edge create edge-router-policy all-endpoints-public-routers --edge-router-roles "#public" --identity-roles "#all"
```

## Create service edge router access policies
The quickstart network includes service edge router access policies that grants access to all `public` edge routers.

```shell
ziti edge create service-edge-router-policy all-routers-all-services --edge-router-roles "#all" --service-roles "#all"
```

# The End
At this point your network should be running and correctly configured for use with the jpa example.