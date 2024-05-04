CREATE TABLE "user" (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE "restaurant" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    lat VARCHAR(255),
    lng VARCHAR(255),
    address VARCHAR(255)
);

CREATE TABLE "session" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    session_identifier UUID UNIQUE,
    selected_restaurant VARCHAR(100),
    created TIMESTAMP,
    created_by VARCHAR(100)
);

CREATE TABLE "user_session" (
    session_id BIGINT,
    user_id BIGINT,
    PRIMARY KEY (session_id, user_id),
    FOREIGN KEY (session_id) REFERENCES "session"(id),
    FOREIGN KEY (user_id) REFERENCES "user"(id)
);

CREATE TABLE "session_restaurant" (
    session_id BIGINT,
    restaurant_id BIGINT,
    PRIMARY KEY (session_id, restaurant_id),
    FOREIGN KEY (session_id) REFERENCES "session"(id),
    FOREIGN KEY (restaurant_id) REFERENCES "restaurant"(id)
);
