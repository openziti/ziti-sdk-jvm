#!/bin/bash

#
# Removes all of the OpenZiti network artifacts created by network-setup.sh
#
echo Deleting identities
ziti edge delete identity private-service
ziti edge delete identity client
#ziti edge delete identity database

echo Deleting demo-service
ziti edge delete service demo-service
ziti edge delete config demo-service-config

echo Deleting database service
ziti edge delete service private-postgres
ziti edge delete config private-postgres-intercept.v1
ziti edge delete config private-postgres-host.v1

echo Deleting identity service policies
ziti edge delete service-policy service-bind-policy
ziti edge delete service-policy service-dial-policy
ziti edge delete service-policy database-bind-policy
ziti edge delete service-policy database-dial-policy
