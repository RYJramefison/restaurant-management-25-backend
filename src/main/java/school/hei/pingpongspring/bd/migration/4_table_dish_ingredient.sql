do
$$
    begin
        if not  exists(select from pg_type where typname = 'unit') then
            create  type "unit" as enum ('G', 'L', 'U');
        end if;
    end
$$;

CREATE TABLE dish_ingredient(
                                dish_id BIGINT,
                                ingredient_id BIGINT,
                                required_quantity float,
                                unit unit,
                                PRIMARY KEY(dish_id, ingredient_id),
                                CONSTRAINT fk_dish FOREIGN KEY (dish_id) REFERENCES dish(id),
                                CONSTRAINT fk_ingredient FOREIGN KEY (ingredient_id) REFERENCES ingredient(id)
);