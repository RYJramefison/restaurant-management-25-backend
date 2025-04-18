INSERT INTO order_status (id, date, status, order_id) VALUES
    (1,'2025-02-01T09:00:00', 'CREATE', 1),
    (2,'2025-02-01T09:33:00', 'CONFIRMED', 1);

INSERT INTO order_status (id, date, status, order_id) VALUES
  (3,'2025-02-01T10:00:00', 'IN_PROGRESS', 1);

update order_status set
