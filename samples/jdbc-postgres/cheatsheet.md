# Cheetsheet

This is the list of commands run to get a ziti environment setup running with docker compose for this sample.

## SETUP work
* clone the ziti repo
    git clone git@github.com:openziti/ziti.git

* modify docker compose file

      postgres-db:
        image: postgres
        #ports:
        #  - 5432:5432
        networks:
          - ziticontrol
          - zitiblue
        volumes:
          - ./data/db:/var/lib/postgresql/data
        environment:
          - POSTGRES_DB=postgres
          - POSTGRES_USER=postgres
          - POSTGRES_PASSWORD=postgres
    
* launch the docker environment

    docker-compose down -v
    clear; docker-compose -f /home/cd/git/github/openziti/ziti/quickstart/docker/docker-compose.yml up
    
* bootstrap postgres

    docker exec -it -u root docker_ziti-private-blue_1 /bin/bash
    apt update && apt install postgresql-client -y --fix-missing
    psql -h postgres-db -U postgres

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

* install the latest ziti cli and add it to your path by passing 'yes' to the getLatestZiti function

    source <(wget -qO- https://raw.githubusercontent.com/openziti/ziti/release-next/quickstart/docker/image/ziti-cli-functions.sh); getLatestZiti yes

* login to the local ziti instance (NOTE: you'll need/want to add a hosts entry for ziti-edge-controller or figure out some other way to make `ziti-edge-controller` and `ziti-edge-router` addressible)

    ziti edge login ziti-edge-controller:1280 -u admin -p admin

### CLEANUP COMMANDS:

Not needed unless you want to try again without recreating docker

    ziti edge delete service private-postgres
    ziti edge delete config private-postgres-cfg
    ziti edge delete service-policy postgres-dial-policy
    ziti edge delete edge-router-policy public-router-access
    ziti edge delete identity tunneler-id 
    ziti edge delete identity java-identity
    
### CREATE/UPDATE COMMANDS:

    ziti edge login ziti-edge-controller:1280 -u admin -p admin

    ziti edge create config private-postgres-cfg ziti-tunneler-client.v1 '{ "hostname" : "zitified-postgres", "port" : 5432 }'
    ziti edge create service private-postgres --configs private-postgres-cfg -a "private-postgres-services"
    ziti edge create terminator "private-postgres" "ziti-private-blue" tcp:postgres-db:5432

    ziti edge create service-policy postgres-dial-policy Dial --identity-roles '#postgres-clients' --service-roles '#private-postgres-services'

    ziti edge create edge-router-policy public-router-access --identity-roles "#postgres-clients" --edge-router-roles "#public-edge-routers"
    ziti edge create service-edge-router-policy blue-router-service-access --edge-router-roles "#public-edge-routers" --service-roles "#private-postgres-services"

    ziti edge update edge-router ziti-edge-router -a "public-edge-routers"
    ziti edge update edge-router ziti-fabric-router-br -a "bluerouters"
    ziti edge update edge-router ziti-private-blue -a "bluerouters"

### CREATE TUNNELER ID for testing

(replace the path accordingly)

    ziti edge create identity user tunneler-id -o tunneler-id.jwt -a "postgres-clients"
    ziti-tunnel enroll tunneler-id.jwt -o /mnt/v/temp/tunneler-id.json
        
### TEST IDENTITY HAS ACCESS TO POSTGRES

(replace the path accordingly)

    ziti-tunnel proxy -i /mnt/v/temp/tunneler-id.json private-postgres:5432 -v

### CREATE JAVA ID for SDK demo

(replace the path accordingly)

    ziti edge create identity user java-identity -o java-identity.jwt -a "postgres-clients" 
    ziti-tunnel enroll java-identity.jwt -o /mnt/v/temp/java-identity.json

### Easy way of adding ziti-edge-controller/ziti-edge-router to you hosts file if you wish

(don't forget to remove them afterwards) :)

    echo "127.0.0.1       ziti-edge-controller" | sudo tee -a /etc/hosts
    echo "127.0.0.1       ziti-edge-router" | sudo tee -a /etc/hosts