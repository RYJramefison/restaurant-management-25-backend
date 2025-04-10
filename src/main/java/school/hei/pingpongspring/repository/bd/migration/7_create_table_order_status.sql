do
$$
    begin
        if not exists(select from pg_type where typname = 'status_order') then
            create type status_order as enum ('CREATE', 'CONFIRMED', 'IN_PROGRESS', 'FINISHED', 'DELIVER');
        end if;
    end
$$;

create table if not exists order_status
(
    id                bigserial primary key,
    date              timestamp without time zone,
    status            status_order,
    order_id          bigint,
    foreign key (order_id) references "order"(id)
);