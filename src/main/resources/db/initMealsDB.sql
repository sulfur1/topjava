DROP TABLE IF EXISTS meals;
DROP SEQUENCE IF EXISTS global_seq_meal;
CREATE SEQUENCE global_seq_meal START WITH 200000;

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
CREATE UNIQUE INDEX meals_unique_users_index on meals (id);

