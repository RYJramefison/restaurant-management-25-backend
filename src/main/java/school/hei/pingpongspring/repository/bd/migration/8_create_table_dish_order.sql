create table if not exists dish_order
(
    id bigserial PRIMARY KEY,
    dish_id     bigint,
    order_id    bigint,
    quantity    int,
    foreign key (dish_id) references dish(id),
    foreign key (order_id) references "order"(id)
);