
DROP KEYSPACE IF EXISTS universe;

CREATE KEYSPACE IF NOT EXISTS universe WITH REPLICATION = {
    'class' : 'SimpleStrategy',
    'replication_factor' : 1
};

CREATE TABLE IF NOT EXISTS universe.persons(
    id      int     PRIMARY KEY,
    name    text
);
