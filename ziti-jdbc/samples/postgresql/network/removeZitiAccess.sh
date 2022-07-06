echo Deleting identities
ziti edge delete identity databaseTunneler
ziti edge delete identity databaseClient


echo Deleting postgres service configs
ziti edge delete config postgres-intercept.v1
ziti edge delete config postgres-host.v1
ziti edge delete service postgres

echo Deleting postgres service access policies
ziti edge delete service-policy postgres-dial-policy
ziti edge delete service-policy postgres-bind-policy

echo Deleting postgres network access policies
ziti edge delete edge-router-policy public-router-client-access
ziti edge delete edge-router-policy public-router-database-access
ziti edge delete service-edge-router-policy public-router-access
