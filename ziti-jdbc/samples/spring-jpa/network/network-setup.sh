#!/bin/bash

echo Creating identities
ziti edge create identity device private-service -o /openziti/network-setup/private-service.jwt -a "services"
ziti edge create identity device client -o /openziti/network-setup/client.jwt -a "clients"
#ziti edge create identity device database -o /openziti/network-setup/database.jwt -a "databases"

echo Enrolling identities
ziti edge enroll -j /openziti/network-setup/private-service.jwt
ziti edge enroll -j /openziti/network-setup/client.jwt
#ziti edge enroll -j /openziti/network-setup/database.jwt

echo Adding databases attribute to identity for postgres router
ziti edge update identity ziti-database-router -a databases

echo Creating demo-service
ziti edge create config demo-service-config ziti-tunneler-client.v1 '{"hostname": "example.web","port": 8080}'
ziti edge create service demo-service --configs demo-service-config -a "demo-service"

echo Creating database service
ziti edge create config private-postgres-intercept.v1 intercept.v1 '{"protocols":["tcp"],"addresses":["private-postgres-server.demo"], "portRanges":[{"low":5432, "high":5432}]}'
ziti edge create config private-postgres-host.v1 host.v1 '{"protocol":"tcp", "address":"postgres-db","port":5432 }'
ziti edge create service private-postgres --configs private-postgres-intercept.v1,private-postgres-host.v1 -a "private-postgres-services"

echo Creating identity service policies
ziti edge create service-policy service-bind-policy Bind --identity-roles "#services" --service-roles "#demo-service"
ziti edge create service-policy service-dial-policy Dial --identity-roles "#clients" --service-roles "#demo-service"
ziti edge create service-policy database-bind-policy Bind --identity-roles "#databases" --service-roles "#private-postgres-services"
ziti edge create service-policy database-dial-policy Dial --identity-roles "#services" --service-roles "#private-postgres-services"
