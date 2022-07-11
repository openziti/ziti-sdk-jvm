#! /bin/bash

docker-compose -p pg down -v
rm -rf tunnel
rm -f *.json
rm -f *.jwt
