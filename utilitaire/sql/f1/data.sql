-- Données CSV

-- Insertion des données dans la table "pilote"
INSERT INTO pilotes (nom_pilote, date_naissance)
VALUES
( 'Lewis Hamilton', '1985-01-07'),
( 'Max Verstappen', '1997-09-30'),
( 'Valtteri Bottas', '1989-08-28'),
( 'Charles Leclerc', '1997-10-16'),
( 'Fernando Alonso', '1981-07-29'),
( 'Daniel Ricciardo', '1989-07-01'),
( 'Lando Norris', '1999-11-13'),
( 'Carlos Sainz', '1994-09-01'),
( 'Sergio Perez', '1990-01-26'),
( 'Pierre Gasly', '1996-02-07'),
( 'Esteban Ocon', '1996-09-17'),
( 'Sebastian Vettel', '1987-07-03'),
( 'Lance Stroll', '1998-10-29'),
( 'Yuki Tsunoda', '2000-05-11'),
( 'Kimi Raikkonén', '1979-10-17'),
( 'Antonio Giovinazzi', '1993-12-14'),
( 'Mick Schumacher', '1999-03-22'),
( 'Nikita Mazepin', '1999-03-02'),
( 'Nicholas Latifi', '1995-06-29'),
( 'George Russell', '1998-02-15');


-- Insertion des données dans la table "ecurie"
INSERT INTO ecuries (nom_ecurie)
VALUES
( 'Mercedes-AMG Petronas Formula One Team'),
( 'Red Bull Racing Honda'),
( 'Scuderia Ferrari'),
( 'Alpine F1 Team'),
( 'McLaren F1 Team'),
( 'Scuderia AlphaTauri'),
( 'Aston Martin Cognizant Formula One Team'),
( 'Alfa Romeo Racing'),
( 'Uralkali Haas F1 Team'),
( 'Williams Racing');

-- Insertion des données dans la table "ecurie_pilote"
INSERT INTO ecurie_pilotes (idecurie, idpilote)
VALUES
-- Mercedes
(1, 1),
(1, 3),
-- Red Bull
(2, 2),
(2, 10),
-- Ferrari
(3, 4),
(3, 8),
-- Alpine
(4, 12),
(4, 11),
-- McLaren
(5, 7),
( 5, 6),
-- AlphaTauri
( 6, 9),
( 6, 14),
-- Aston Martin
( 7, 13),
( 7, 16),
-- Alfa Romeo
( 8, 15),
( 8, 17),
-- Haas
( 9, 18),
( 9, 19),
-- Williams
( 10, 20),
( 10, 5);


-- Insertion des données dans la table "grand_prix"
INSERT INTO grand_prixs (nom_grand_prix)
VALUES
('Australian Grand Prix'),
('Bahrain Grand Prix'),
('Chinese Grand Prix'),
('Spanish Grand Prix'),
('Monaco Grand Prix');

-- Insertion des données dans la table "grand_prix_pilote"
INSERT INTO grand_prix_pilotes (idgrandprix, idpilote, temps_course, date_grand_prix)
VALUES
(1, 1, '01:30:25', '2023-03-20'),
(1, 2, '01:31:10' , '2023-03-20'),
(1, 3, '01:29:45','2023-03-20' ),
(1, 4, '01:30:05','2023-03-20' ),
(1, 5, '01:33:20','2023-03-20' ),
(1, 6, '01:34:15','2023-03-20' ),
(1, 7, '01:32:50','2023-03-20' ),
(1, 8, '01:32:30','2023-03-20' ),
(1, 9, '01:35:05','2023-03-20' ),
( 1, 10, '01:31:55','2023-03-20' ),
( 1, 11, '01:32:40','2023-03-20' ),
( 1, 12, '01:33:10','2023-03-20' ),
( 1, 13, '01:35:30','2023-03-20' ),
( 1, 14, '01:33:55','2023-03-20' ),
( 1, 15, '01:36:20','2023-03-20' ),
( 1, 16, '01:35:40','2023-03-20' ),
( 1, 17, '01:37:10','2023-03-20' ),
( 1, 18, '01:38:05','2023-03-20' ),
( 1, 19, '01:36:45','2023-03-20' ),
( 1, 20, '01:33:35','2023-03-20' ),

(2, 1, '01:28:45', '2023-03-27'),
( 2, 2, '01:29:20', '2023-03-27'),
( 2, 3, '01:29:05', '2023-03-27'),
( 2, 4, '01:30:10', '2023-03-27'),
( 2, 5, '01:32:30', '2023-03-27'),
( 2, 6, '01:33:15', '2023-03-27'),
( 2, 7, '01:31:50', '2023-03-27'),
( 2, 8, '01:31:30', '2023-03-27'),
( 2, 9, '01:34:00', '2023-03-27'),
( 2, 10, '01:30:55', '2023-03-27'),
( 2, 11, '01:31:40', '2023-03-27'),
( 2, 12, '01:32:10', '2023-03-27'),
( 2, 13, '01:34:30', '2023-03-27'),
( 2, 14, '01:32:55', '2023-03-27'),
( 2, 15, '01:35:20', '2023-03-27'),
( 2, 16, '01:34:40', '2023-03-27'),
( 2, 17, '01:36:10', '2023-03-27'),
( 2, 18, '01:37:05', '2023-03-27'),
( 2, 19, '01:35:40', '2023-03-27'),
( 2, 20, '01:32:35', '2023-03-27'),



(3, 1, '01:32:15', '2023-04-10'),
(3, 2, '01:33:00', '2023-04-10'),
(3, 3, '01:31:45', '2023-04-10'),
(3, 4, '01:33:20', '2023-04-10'),
(3, 5, '01:35:30', '2023-04-10'),
(3, 6, '01:36:15', '2023-04-10'),
(3, 7, '01:34:50', '2023-04-10'),
(3, 8, '01:34:30', '2023-04-10'),
(3, 9, '01:37:00', '2023-04-10'),
(3, 10, '01:33:55', '2023-04-10'),
(3, 11, '01:34:40', '2023-04-10'),
(3, 12, '01:35:10', '2023-04-10'),
(3, 13, '01:37:30', '2023-04-10'),
(3, 14, '01:36:55', '2023-04-10'),
(3, 15, '01:39:20', '2023-04-10'),
(3, 16, '01:38:40', '2023-04-10'),
(3, 17, '01:40:10', '2023-04-10'),
(3, 18, '01:41:05', '2023-04-10'),
(3, 19, '01:39:40', '2023-04-10'),
(3, 20, '01:36:35', '2023-04-10'),


(4, 1, '01:28:50', '2023-05-08'),
(4, 2, '01:29:35', '2023-05-08'),
(4, 3, '01:28:20', '2023-05-08'),
(4, 4, '01:29:45', '2023-05-08'),
(4, 5, '01:31:10', '2023-05-08'),
(4, 6, '01:32:05', '2023-05-08'),
(4, 7, '01:30:40', '2023-05-08'),
(4, 8, '01:30:20', '2023-05-08'),
(4, 9, '01:32:50', '2023-05-08'),
(4, 10, '01:29:45', '2023-05-08'),
(4, 11, '01:30:30', '2023-05-08'),
(4, 12, '01:31:00', '2023-05-08'),
(4, 13, '01:33:20', '2023-05-08'),
(4, 14, '01:32:45', '2023-05-08'),
(4, 15, '01:35:10', '2023-05-08'),
(4, 16, '01:34:30', '2023-05-08'),
(4, 17, '01:36:00', '2023-05-08'),
(4, 18, '01:36:55', '2023-05-08'),
(4, 19, '01:35:30', '2023-05-08'),
(4, 20, '01:32:25', '2023-05-08'),


(5, 1, '01:35:20', '2023-05-22'),
(5, 2, '01:36:05', '2023-05-22'),
(5, 3, '01:34:50', '2023-05-22'),
(5, 4, '01:36:15', '2023-05-22'),
(5, 5, '01:37:40', '2023-05-22'),
(5, 6, '01:38:35', '2023-05-22'),
(5, 7, '01:37:10', '2023-05-22'),
(5, 8, '01:36:50', '2023-05-22'),
(5, 9, '01:39:20', '2023-05-22'),
(5, 10, '01:36:15', '2023-05-22'),
(5, 11, '01:37:00', '2023-05-22'),
(5, 12, '01:37:30', '2023-05-22'),
(5, 13, '01:39:50', '2023-05-22'),
(5, 14, '01:39:15', '2023-05-22'),
(5, 15, '01:41:40', '2023-05-22'),
(5, 16, '01:41:00', '2023-05-22'),
(5, 17, '01:42:30', '2023-05-22'),
(5, 18, '01:43:25', '2023-05-22'),
(5, 19, '01:42:00', '2023-05-22'),
(5, 20, '01:38:55', '2023-05-22');



INSERT INTO users (email, password, role) VALUES ('mimi@gmail.com', 'mirija', 1);



INSERT into pilotes (nom_pilote, date_naissance) FROM 
SELECT nom , CAST(datenaissance as DATE) FROM csv
GROUP BY nom, datenaissance;



INSERT INTO grand_prixs (nom_grand_prix) FROM
SELECT DISTINCT grandprix from csv;


INSERT INTO ecurie_pilotes (idpilote, idecurie) FROM
SELECT p.id_pilote, e.id_ecurie from csv c
JOIN pilotes p ON p.nom_pilote = c.nom AND p.date_naissance = CAST(c.datenaissance as date)
JOIN ecuries e ON e.nom_ecurie = c.ecurie;




INSERT INTO grand_prix_pilotes (idgrandprix,idpilote, temps_course,date_grand_prix) FROM
SELECT gp.id_grand_prix, p.id_pilote, CAST(temps as TIME ), CAST(dategrandprix as DATE) FROM csv c 
JOIN pilotes p ON p.nom_pilote = c.nom AND p.date_naissance = CAST(c.datenaissance as date)
JOIN grand_prixs gp ON gp.nom_grand_prix = c.grandprix;


SELECT * FROM pilotes WHERE  CAST(id_pilote as VARCHAR) ILIKE '%Lewis%' AND CAST(id_pilote as VARCHAR) ILIKE '%Hamilton%'
                         OR CAST(nom_pilote as VARCHAR) ILIKE '%Lewis%' AND CAST(nom_pilote as VARCHAR) ILIKE '%Hamilton%'
                          OR CAST(date_naissance as VARCHAR) ILIKE '%Lewis%' AND CAST(date_naissance as VARCHAR) ILIKE '%Hamilton%'
UNION
SELECT * from pilotes WHERE  CAST(id_pilote as VARCHAR) ILIKE '%Lewis%' 
                            OR CAST(nom_pilote as VARCHAR) ILIKE '%Lewis%' 
                            OR CAST(date_naissance as VARCHAR) ILIKE '%Lewis%'  
UNION 
SELECT * from pilotes WHERE  CAST(id_pilote as VARCHAR) ILIKE '%Hamilton%' 
OR CAST(nom_pilote as VARCHAR) ILIKE '%Hamilton%' 
OR CAST(date_naissance as VARCHAR) ILIKE '%Hamilton%';
