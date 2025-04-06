INSERT  INTO  stock_movement (id ,ingredient_id, type, quantity, unit, date)
VALUES (1, 3,'IN',100,'U','2025-02-01T08:00'),
       (2, 4,'IN',50,'U','2025-02-01T08:00'),
       (3, 1,'IN',10000,'G','2025-02-01T08:00'),
       (4, 2,'IN',20,'L','2025-02-01T08:00');


-- inserer apres avoir fini la methode pour verifier l'etat des stock dans l'entite ingredient
INSERT  INTO  stock_movement (id, ingredient_id, type, quantity, unit, date)
VALUES
(5, 3,'OUT',10,'U','2025-02-02T10:00'),
(6, 3,'OUT',10,'U','2025-02-03T15:00'),
(7, 4,'OUT',20,'U','2025-02-05T16:00');
