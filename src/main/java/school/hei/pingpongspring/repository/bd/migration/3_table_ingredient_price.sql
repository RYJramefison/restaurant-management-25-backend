CREATE TABLE ingredient_price (
                                  id BIGSERIAL PRIMARY KEY ,
                                  ingredient_id BIGINT,
                                  price FLOAT,
                                  date TIMESTAMP,
                                  FOREIGN KEY (ingredient_id) REFERENCES ingredient(id)
);