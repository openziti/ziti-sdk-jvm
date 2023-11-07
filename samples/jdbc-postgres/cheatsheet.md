# Cheatsheet

This is the list of commands run to get a ziti environment setup running with docker compose for this sample.

## SETUP work
* Java 11+ installed and on the path (run: java -version and confirm)
* cd to the location of this `cheatsheet.md`
* This example uses the docker-compose environment. To replicate you need the compose file and docker/docker-compose 
  installed. Then, curl down the simplified compose file along with the default .env file

      curl -s https://get.openziti.io/dock/simplified-docker-compose.yml > docker-compose.yml
      curl -s https://get.openziti.io/dock/.env > .env
    
* modify docker compose file and add postgres with a known user/password

        postgres-db:
          image: postgres
          #ports:
          #  - 5432:5432
          networks:
            - ziti
          volumes:
            - ./data/db:/var/lib/postgresql/data
          environment:
            - POSTGRES_DB=postgres
            - POSTGRES_USER=postgres
            - POSTGRES_PASSWORD=postgres
    
* launch the docker environment

      # use the project name of 'pg' (for postgres) when starting docker-compose
      docker compose -p pg up
    
* bootstrap postgres

      docker cp prep-db.sql pg-postgres-db-1:prep-db.sql
      docker exec -e PGPASSWORD=postgres -it pg-postgres-db-1 psql -U postgres -d postgres

* issue these commands, once connected to postgres db

      DROP DATABASE simpledb;
      CREATE DATABASE simpledb;
      ALTER DATABASE simpledb OWNER TO postgres;
      \connect simpledb;
    
* issue these commands, after connecting to the 'simpledb' database

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

* exit postgres by typing `exit`

* SHOW POSTGRES not exposed:

      docker ps (show 5432 not exposed)

## ZITI BOOTSTRAPPING

* get ziti onto your path however you like.
  * Linux/MacOS users can source a script (as always, we recommend you read the script first):
  
        source /dev/stdin <<< "$(wget -qO- https://get.openziti.io/quick/ziti-cli-functions.sh)"; getZiti "yes"
  
  * Windows users can run this powershell command (as always, we recommend you read the script first):
        
        iex(iwr -Uri https://get.openziti.io/quick/getZiti.ps1)
  
* login to the local ziti instance

      ziti edge login localhost:1280 -u admin -p admin -y

### CLEANUP COMMANDS

Not needed unless you want to try again without recreating docker

    ziti edge delete service private-postgres
    ziti edge delete config private-postgres-intercept.v1
    ziti edge delete config private-postgres-host.v1
    ziti edge delete service-policy postgres-dial-policy
    ziti edge delete service-policy postgres-bind-policy
    ziti edge delete identity pg-client
    
### CREATE/UPDATE COMMANDS

    # create and enroll an identity for the client
    ziti edge create identity pg-client -o pg-client.jwt -a postgres-clients
    ziti edge enroll pg-client.jwt
      
    # authorize the router to offload traffic towards postgres
    ziti edge update identity ziti-edge-router -a postgres-servers
    # configure the OpenZiti overlay
    # create two configs, one for dialing/intercepting and one for binding
    ziti edge create config private-postgres-intercept.v1 intercept.v1 '{"protocols":["tcp"],"addresses":["zitified-postgres"], "portRanges":[{"low":5432, "high":5432}]}'
    ziti edge create config private-postgres-host.v1 host.v1 '{"protocol":"tcp", "address":"postgres-db","port":5432 }'
    
    # add the two configs to a service
    ziti edge create service private-postgres --configs private-postgres-intercept.v1,private-postgres-host.v1 -a "private-postgres-services"
    # authorize the identities to dial and bind the service
    ziti edge create service-policy postgres-dial-policy Dial --identity-roles '#postgres-clients' --service-roles '#private-postgres-services'
    ziti edge create service-policy postgres-bind-policy Bind --identity-roles '#postgres-servers' --service-roles '#private-postgres-services'

### Easy way of adding ziti-edge-controller/ziti-edge-router to you hosts file if you wish

(don't forget to remove them afterwards) :)

    echo "127.0.0.1       ziti-edge-controller" | sudo tee -a /etc/hosts
    echo "127.0.0.1       ziti-edge-router" | sudo tee -a /etc/hosts

## Run the Sample

* cd here: `cd samples/jdbc-postgres`
* run gradlew/gradlew.bat: `./gradlew :run --args="./pg-client.json"`

### Sample Output

    > Task :run
    SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
    SLF4J: Defaulting to no-operation (NOP) logger implementation
    SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
    Result from database is: a:1
    Result from database is: b:2
    Result from database is: c:3
    Result from database is: d:4
    Result from database is: e:5
    Result from database is: f:6
    Result from database is: g:7
    Result from database is: h:8
    Result from database is: i:9
    Result from database is: j:0
    
    Deprecated Gradle features were used in this build, making it incompatible with Gradle 9.0.
    
    You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.
    
    For more on this, please refer to https://docs.gradle.org/8.4/userguide/command_line_interface.html#sec:command_line_warnings in the Gradle documentation.
    
    BUILD SUCCESSFUL in 6s
    3 actionable tasks: 3 executed
    [1]+  Done                    clear