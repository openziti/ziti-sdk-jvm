# Postgres Test Network
This directory contains a few files to set up a Ziti network in a docker compose environment.

## Files
* **setup.sh:** Starts the OpenZiti network using docker compose, logs into it, and calls `initZitiAccess.sh`
* **teardown.sh:** Stops and cleans up the docker compose environment
* **initZitiAccess.sh:** Configures the OpenZiti network for the sample
* **removeZitiAccess.sh:** Removes all OpenZiti configuration added by `initZitiAccess.sh`
* **docker-compose.yaml:** The docker compose network definition
* **postgres-setup.sh:** A script that is mounted into the postgres container to create our little test database

## Docker compose network
The docker compose network has four containers for this example:
* **OpenZiti controller:** The controller orchestrates and runs the OpenZiti network.  It exposes an API on port 1280 on the local host
* **OpenZiti Edge Router:** The edge router supports connections from the edge.  In this case the tunneler and the Java application will pass data through this edge router
* **OpenZiti Tunneler:** This is the exit point of the OpenZiti network.  Its job is to bridge the gap between the OpenZiti network and the Postgresql server
* **Postgresql server:** The database server. This container is dark in that it does not expose any ports on your pc

See the [OpenZiti Documentation](https://openziti.github.io/ziti/overview.html) for more information on the Ziti components

## Postgresql Setup
The posgresql server is bootstrapped from postgres-setup.sh.

Once up and running, it will have:
* Username: postgres
* Password: postgres
* Database: simpledb
* Table: simpletable (varchar,int)
* 10 rows of data.  Varchar a-j and int 0-9
