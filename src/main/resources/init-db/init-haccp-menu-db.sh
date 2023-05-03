#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE USER haccp_menu;
    CREATE DATABASE haccp_menu;
    GRANT ALL PRIVILEGES ON DATABASE haccp_menu TO haccp_menu;
EOSQL