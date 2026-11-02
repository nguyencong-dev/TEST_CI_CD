INSERT INTO example.greetings(name)
SELECT 'Docker' WHERE NOT EXISTS (SELECT 1 FROM example.greetings WHERE name = 'Docker');
INSERT INTO example.greetings(name)
SELECT 'Spring Boot' WHERE NOT EXISTS (SELECT 1 FROM example.greetings WHERE name = 'Spring Boot');
INSERT INTO example.greetings(name)
SELECT 'MariaDB' WHERE NOT EXISTS (SELECT 1 FROM example.greetings WHERE name = 'MariaDB');
