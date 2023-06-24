DROP TABLE IF EXISTS meals;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS global_seq;
DROP SEQUENCE IF EXISTS global_seq_meal;

CREATE SEQUENCE global_seq START WITH 100000;
CREATE SEQUENCE global_seq_meal START WITH 200000;

CREATE TABLE users
(
    id               INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    name             VARCHAR                           NOT NULL,
    email            VARCHAR                           NOT NULL,
    password         VARCHAR                           NOT NULL,
    registered       TIMESTAMP           DEFAULT now() NOT NULL,
    enabled          BOOL                DEFAULT TRUE  NOT NULL,
    calories_per_day INTEGER             DEFAULT 2000  NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_role
(
    user_id INTEGER NOT NULL,
    role    VARCHAR NOT NULL,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE meals
(
    id              INTEGER PRIMARY KEY DEFAULT nextval('global_seq_meal'),
    date_time       TIMESTAMP           DEFAULT now()   NOT NULL,
    description     VARCHAR                             NOT NULL,
    calories        INTEGER             DEFAULT 0       NOT NULL,
    user_id         INTEGER             DEFAULT 0       NOT NULL,
    CONSTRAINT meal_datetime_index UNIQUE (id, date_time),
    FOREIGN KEY (user_id) REFERENCES users (id)
);
CREATE UNIQUE INDEX meals_unique_users_index on meals (date_time);