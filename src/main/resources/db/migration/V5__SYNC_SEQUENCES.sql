SELECT setval(
               pg_get_serial_sequence('clientes', 'id'),
               COALESCE((SELECT MAX(id) FROM clientes), 1),
               true
       );

SELECT setval(
               pg_get_serial_sequence('carros', 'id'),
               COALESCE((SELECT MAX(id) FROM carros), 1),
               true
       );

SELECT setval(
               pg_get_serial_sequence('alugueis', 'id'),
               COALESCE((SELECT MAX(id) FROM alugueis), 1),
               true
       );
