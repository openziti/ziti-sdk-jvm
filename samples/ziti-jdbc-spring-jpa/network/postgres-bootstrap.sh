#!/bin/bash
set -e

psql -v --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	DROP DATABASE simpledb;
	CREATE DATABASE simpledb;
	ALTER DATABASE simpledb OWNER TO postgres;
	\connect simpledb;

	CREATE TABLE simpletable(chardata varchar(100), somenumber int);
	
	INSERT INTO simpletable VALUES('a', 0);
	INSERT INTO simpletable VALUES('b', 1);
	INSERT INTO simpletable VALUES('c', 2);
	INSERT INTO simpletable VALUES('d', 3);
	INSERT INTO simpletable VALUES('e', 4);
	INSERT INTO simpletable VALUES('f', 5);
	INSERT INTO simpletable VALUES('g', 6);
	INSERT INTO simpletable VALUES('h', 7);
	INSERT INTO simpletable VALUES('i', 8);
	INSERT INTO simpletable VALUES('j', 9);
EOSQL

