

-- Eliminar todas las reservas
DELETE FROM reservation;

-- Eliminar todos los asientos
DELETE FROM seat;

-- Eliminar todos los vuelos
DELETE FROM flight;

-- Insertar un vuelo
INSERT INTO flight (id, flight_number)
VALUES (1, 'ABC123'),
       (2, 'XYZ456');

-- Insertar asientos para el vuelo 1
INSERT INTO seat (id, flight_id, seat_number, reserved, version, pending)
VALUES (1, 1, '1A', false, 1, false),
       (2, 1, '1B', false, 1, false),
       (3, 1, '2A', false, 1, false),
       (4, 1, '2B', false, 1, false);

-- Insertar asientos para el vuelo 2
INSERT INTO seat (id, flight_id, seat_number, reserved, version, pending)
VALUES (5, 2, '1A', false, 1, false),
       (6, 2, '1B', false, 1, false),
       (7, 2, '2A', false, 1, false),
       (8, 2, '2B', false, 1, false);

-- Insertar una reserva para el vuelo 1, asiento 1A
INSERT INTO reservation (id, seat_id, email, created_at, status)
VALUES (1, 1, 'john.doe@example.com', '2025-02-07 10:00:00', 'CONFIRMED');

-- Insertar una reserva para el vuelo 1, asiento 2A
INSERT INTO reservation (id, seat_id, email, created_at, status)
VALUES (2, 3, 'jane.doe@example.com', '2025-02-07 11:00:00', 'PENDING');