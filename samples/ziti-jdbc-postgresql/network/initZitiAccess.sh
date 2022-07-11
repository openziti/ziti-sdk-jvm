echo Creating and enrolling identities
ziti edge -i postgresql create identity device databaseTunneler -o databaseTunneler.jwt -a databases
ziti edge -i postgresql create identity device databaseClient -o databaseClient.jwt -a clients

ziti edge enroll --jwt databaseTunneler.jwt
ziti edge enroll --jwt databaseClient.jwt

echo Creating postgres service configs
ziti edge -i postgresql create config postgres-intercept.v1 intercept.v1 '{"protocols":["tcp"],"addresses":["postgres-server.demo"], "portRanges":[{"low":5432, "high":5432}]}'
ziti edge -i postgresql create config postgres-host.v1 host.v1 '{"protocol":"tcp", "address":"postgres-db","port":5432 }'
ziti edge -i postgresql create service postgres --configs postgres-intercept.v1,postgres-host.v1 -a "postgres-services"

echo Creating postgres service access policies
ziti edge -i postgresql create service-policy postgres-dial-policy Dial --identity-roles '#clients' --service-roles '#postgres-services'
ziti edge -i postgresql create service-policy postgres-bind-policy Bind --identity-roles '#databases' --service-roles '#postgres-services'

echo Creating postgres network access policies
ziti edge -i postgresql create edge-router-policy public-router-client-access --identity-roles "#clients" --edge-router-roles "#all"
ziti edge -i postgresql create edge-router-policy public-router-database-access --identity-roles "#databases" --edge-router-roles "#all"
ziti edge -i postgresql create service-edge-router-policy public-router-access --service-roles "#postgres-services" --edge-router-roles "#all"
