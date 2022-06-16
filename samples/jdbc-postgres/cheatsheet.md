# Cheatsheet

This is the list of commands run to get a ziti environment setup running with docker compose for this sample.

## SETUP work
* Java 11+ installed and on the path (run: java -version and confirm)
* This example uses the docker-compose environment. To replicate you need the compose file and docker/docker-compose installed

    # clone the ziti repo
    git clone git@github.com:openziti/ziti.git
    
    # -- OR --
    # download the docker-compose file 
    curl https://raw.githubusercontent.com/openziti/ziti/release-next/quickstart/docker/docker-compose.yml > /tmp/docker-compose.yml
    curl https://raw.githubusercontent.com/openziti/ziti/release-next/quickstart/docker/.env > /tmp/.env
    
* modify docker compose file

      postgres-db:
        image: postgres
        #ports:
        #  - 5432:5432
        networks:
          - zitiblue
        volumes:
          - ./data/db:/var/lib/postgresql/data
        environment:
          - POSTGRES_DB=postgres
          - POSTGRES_USER=postgres
          - POSTGRES_PASSWORD=postgres
    
* launch the docker environment

    # use the project name of 'pg' (for postgres) when starting docker-compose
    docker-compose -f /tmp/docker-compose.yml -p pg down -v
    clear; docker-compose -f /tmp/docker-compose.yml -p pg up
    
* bootstrap postgres

    docker exec -it -u root pg_ziti-private-blue_1 /bin/bash
    apt update && apt install postgresql-client -y --fix-missing
    PGPASSWORD=postgres psql -h postgres-db -U postgres 

* issue these commands once connected to postgres

    DROP DATABASE simpledb;
    CREATE DATABASE simpledb;
    ALTER DATABASE simpledb OWNER TO postgres;
    \connect simpledb;
    
    CREATE TABLE simpletable(chardata varchar(100), somenumber int);

    INSERT INTO simpletable VALUES('a', 1);
    INSERT INTO simpletable VALUES('b', 2);
    INSERT INTO simpletable VALUES('c', 3);
    INSERT INTO simpletable VALUES('d', 4);
    INSERT INTO simpletable VALUES('e', 5);
    INSERT INTO simpletable VALUES('f', 6);
    INSERT INTO simpletable VALUES('g', 7);
    INSERT INTO simpletable VALUES('h', 8);
    INSERT INTO simpletable VALUES('i', 9);
    INSERT INTO simpletable VALUES('j', 0);

    select * from simpletable;

* SHOW POSTGRES not exposed:

    docker ps (show 5432 not exposed)

## ZITI BOOTSTRAPPING

* exec into the controller using docker

    docker exec -it pg_ziti-controller_1 bash

* login to the local ziti instance

    ziti edge login ziti-edge-controller:1280 -u admin -p admin
    -- or --
    zitiLogin

### CLEANUP COMMANDS:

Not needed unless you want to try again without recreating docker

    ziti edge delete service private-postgres
    ziti edge delete config private-postgres-intercept.v1
    ziti edge delete config private-postgres-host.v1
    ziti edge delete service-policy postgres-dial-policy
    ziti edge delete service-policy postgres-bind-policy

    ziti edge delete edge-router-policy public-router-access
    ziti edge delete identity tunneler-id 
    ziti edge delete identity java-identity
    
### CREATE/UPDATE COMMANDS:

    ziti edge create config private-postgres-intercept.v1 intercept.v1 '{"protocols":["tcp"],"addresses":["zitified-postgres"], "portRanges":[{"low":5432, "high":5432}]}'
    ziti edge create config private-postgres-host.v1 host.v1 '{"protocol":"tcp", "address":"postgres-db","port":5432 }'
    ziti edge create service private-postgres --configs private-postgres-intercept.v1,private-postgres-host.v1 -a "private-postgres-services"
    ziti edge create service-policy postgres-dial-policy Dial --identity-roles '#postgres-clients' --service-roles '#private-postgres-services'
    ziti edge create service-policy postgres-bind-policy Bind --identity-roles '@ziti-private-blue' --service-roles '#private-postgres-services'

### CREATE JAVA ID for SDK demo

(replace the path accordingly)

    ziti edge create identity user java-identity -o java-identity.jwt -a "postgres-clients" 
    ziti edge enroll java-identity.jwt -o java-identity.json
    
    # exit docker
    docker cp pg_ziti-controller_1:/openziti/java-identity.json .

### Easy way of adding ziti-edge-controller/ziti-edge-router to you hosts file if you wish

(don't forget to remove them afterwards) :)

    echo "127.0.0.1       ziti-edge-controller" | sudo tee -a /etc/hosts
    echo "127.0.0.1       ziti-edge-router" | sudo tee -a /etc/hosts