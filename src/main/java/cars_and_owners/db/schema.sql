CREATE TABLE engine (
    id SERIAL PRIMARY KEY,
    name VARCHAR(2000) NOT NULL UNIQUE
);

--many-to-one
CREATE TABLE car (
    id SERIAL PRIMARY KEY,
    name VARCHAR(2000) NOT NULL UNIQUE,
    engine_id INT NOT NULL REFERENCES engine(id)
);

--many-to-many
CREATE TABLE driver (
    id SERIAL PRIMARY KEY,
    name VARCHAR(2000) NOT NULL
);

CREATE TABLE history_owner (
    id SERIAL PRIMARY KEY,
    driver_id INT NOT NULL REFERENCES driver(id),
    car_id INT NOT NULL REFERENCES car(id)
);