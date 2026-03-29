INSERT INTO ROOM (name, capacity) VALUES ('Sala A', 10);
INSERT INTO ROOM (name, capacity) VALUES ('Sala B', 10);
INSERT INTO ROOM (name, capacity) VALUES ('Sala C', 10);

INSERT INTO users (name, email, password, role)
VALUES ('Admin', 'admin@test.com', '$2a$10$8bwbuqBKx/K5MioCxQZh3eTeqNU/xxZEySJHNnxDumVt4S2SI49TS', 'ADMIN');

INSERT INTO users (name, email, password, role)
VALUES ('Empleado', 'emp@test.com', '1234', 'EMPLOYEE');