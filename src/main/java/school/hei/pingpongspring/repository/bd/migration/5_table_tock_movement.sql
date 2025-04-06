do
$$
    begin
        if not exists(select from pg_type WHERE typname= 'movement_type') then
            create type "movement_type" as enum ('IN', 'OUT');
        end if;
    end
$$;

CREATE TABLE stock_movement(
                               id BIGSERIAL PRIMARY KEY ,
                               ingredient_id BIGINT,
                               type movement_type,
                               quantity FLOAT,
                               unit unit,
                               date TIMESTAMP,
                               CONSTRAINT fk_ingredient_to_stock_movement FOREIGN KEY (ingredient_id) REFERENCES ingredient(id)
);
