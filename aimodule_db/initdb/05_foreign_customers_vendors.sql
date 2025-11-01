DROP SERVER backend_srv CASCADE;

CREATE EXTENSION IF NOT EXISTS postgres_fdw;

CREATE SERVER backend_srv
FOREIGN DATA WRAPPER postgres_fdw
OPTIONS (host 'backend_db', dbname 'backend_db', port '5432');

CREATE USER MAPPING FOR admin
SERVER backend_srv
OPTIONS (user 'admin', password 'admin123');

IMPORT FOREIGN SCHEMA public
LIMIT TO (accounts, customers, vendors)
FROM SERVER backend_srv
INTO public;