do
$$
    begin
        if not  exists(select from pg_type where typname = 'unit') then
            create  type "unit" as enum ('G', 'L', 'U');
        end if;
    end
$$;

CREATE TABLE if not exists ingredient (
                                          id BIGSERIAL PRIMARY KEY,
                                          name VARCHAR(150),
                                          dateTime TIMESTAMP,
                                          unit unit
);
