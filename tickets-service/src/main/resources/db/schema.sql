CREATE TABLE IF NOT EXISTS station
(
    id   SERIAL PRIMARY KEY,
    station VARCHAR(255) NOT NULL
);


CREATE TABLE IF NOT EXISTS orders
(
    id              SERIAL PRIMARY KEY,
    user_id         INT       NOT NULL,
    from_station_id INT       NOT NULL,
    to_station_id   INT       NOT NULL,
    status          INT       NOT NULL,
    created         TIMESTAMP NOT NULL,
    FOREIGN KEY (from_station_id) REFERENCES station (id),
    FOREIGN KEY (to_station_id) REFERENCES station (id)
);
