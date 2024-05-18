-- INSERT INTO users (email, password, role) VALUES ('root@gmail.com', 'root', 10);


-- Données de test pour la table finitions
INSERT INTO finitions (nom_finition, marge, etat_finition) 
VALUES 
('Standard', 0, 0),
('Gold', 5, 0),
('Premium', 7.5, 0),
('VIP', 10, 0);

-- Données de test pour la table unites
INSERT INTO unites (nom_unite, etat_unite) 
VALUES 
('m2', 0),
('m3', 0),
('fft', 0);

-- Données de test pour la table type_travaux
INSERT INTO type_travaux (code_type_travaux, nom_type_travaux, etat_type_travaux) 
VALUES 
('000', 'Travaux preparatoires', 0),
('100', 'Travaux de terrassement', 0),
('200', 'Travaux en Infrastructure', 0);

-- Données de test pour la table travauxs
INSERT INTO travauxs (idtype_travaux, code_travaux, nom_travaux, unite_travaux, prix_unitaire, etat_travaux) 
VALUES 
(1, '001', 'Mur de soutainement', 1, 190000, 0),
(2, '101', 'Decapage des terrains meubles', 2, 3072.87, 0),
(2, '102', 'Travaux 3', 3, 3736.26, 0),
(2, '103', 'Travaux 4', 3, 9390.93, 0),
(2, '104', 'Travaux 5', 3, 37563.26, 0),
(2, '105', 'Travaux 6', 3, 152656, 0),
(3, '201', 'Travaux 7', 3, 172114.40, 0),
(3, '202', 'Travaux 8', 3, 573215.80, 0),
(3, '202', 'Travaux 9', 3,  573215.80, 0),
(3, '202', 'Travaux 10', 3, 573215.80, 0),
(3, '203', 'Travaux 11', 3, 37563.26, 0),
(3, '204', 'Travaux 12', 3, 73245.40, 0),
(3, '205', 'Travaux 13', 3, 487815.80, 0),
(3, '206', 'Travaux 14', 3, 33566.54, 0);

-- Données de test pour la table maisons
INSERT INTO maisons (nom_maison, description_maison, etat_maison, duree_construction ,surface) 
VALUES 
('Maison 1', 'Description de la maison 1', 0, 320, 126),
('Maison 2', 'Description de la maison 2', 0, 460, 212),
('Maison 3', 'Description de la maison 3', 0, 660, 126);

INSERT INTO maisons_travaux (idmaison, idtrauvaux, quantite) 
VALUES 
(1, 1, 2),
(1, 2, 1),
(1, 3, 4),
(1, 4, 3),
(2, 5, 2),
(2, 6, 1),
(2, 7, 4),
(2, 8, 3),
(3, 9, 2),
(3, 10, 1),
(3, 11, 4),
(3, 12, 3),
(3, 13, 2),
(3, 14, 1);

-- Données de test pour la table deviss
-- INSERT INTO deviss (numero_devis, idmaison, idfinition, iduser, montant, finition, duree, date_debut_travaux, etat_devis) 
-- VALUES 
-- ('DEV001', 1, 2, 2, 10000.00, 5, 320, '2024-10-01', 0);


-- INSERT INTO payement_devis( iddevis, montant, date_payement)
-- VALUES
-- (1, 5300, '2024-07-10'),
-- (1, 400, '2024-07-11');


INSERT INTO maisons (nom_maison, description_maison, surface, duree_construction) 
SELECT  
distinct mt.type_maison, mt.description,
CAST (mt.surface as double PRECISION) ,
CAST (mt.duree_travaux as double PRECISION) 
FROM maisons_travaux_csv mt
LEFT JOIN maisons m ON m.nom_maison = mt.type_maison
WHERE m.id_maison is null;


INSERT INTO unites (nom_unite)
SELECT
distinct mt.unite 
from maisons_travaux_csv mt
LEFT JOIN unites u ON u.nom_unite = mt.unite
WHERE u.id_unite is null;



INSERT INTO travauxs (code_travaux, nom_travaux, unite_travaux, prix_unitaire)
SELECT 
DISTINCT code_travaux,
type_travaux,
u.id_unite,
CAST (mt.prix_unitaire as double PRECISION)
FROM maisons_travaux_csv mt
JOIN unites u ON u.nom_unite = mt.unite;

INSERT into maisons_travaux (idmaison, idtravaux, quantite)
SELECT
m.id_maison, t.id_travaux, CAST(quantite as double PRECISION)
FROM maisons_travaux_csv mt
JOIN maisons m ON m.nom_maison = mt.type_maison
JOIN travauxs t ON t.nom_travaux = mt.type_travaux
ON CONFLICT (idmaison, idtravaux) DO NOTHING;


INSERT INTO users (numero)
SELECT 
distinct client
FROM devis_csv dc
LEFT JOIN users u ON u.numero = dc.client
WHERE u.id_user is null;

INSERT INTO finitions (nom_finition, marge)
SELECT 
DISTINCT finition,
CAST(taux_finition as double PRECISION) 
FROM devis_csv 
LEFT JOIN finitions f ON f.nom_finition= finition
WHERE f.id_finition is null;


INSERT INTO deviss (reference, idmaison,idfinition, iduser, montant,finition , duree, date_devis, date_debut_travaux,lieu )
SELECT 
    ref_devis,
    m.id_maison,
    f.id_finition,
    u.id_user,
    vp.prix + (vp.prix*f.marge/100),
    f.marge,
    vp.duree,
    to_timestamp(dc.date_devis, 'DD/MM/YYYY'),
    to_date(dc.date_debut, 'DD/MM/YYYY'),
    dc.lieu
FROM devis_csv dc 
JOIN maisons m ON m.nom_maison = dc.type_maison
JOIN finitions f ON f.nom_finition = dc.finition
JOIN users u ON u.numero= dc.client
JOIN v_prix_maisons vp on vp.id= m.id_maison
LEFT JOIN deviss d ON d.reference = dc.ref_devis
WHERE d.id_devis is null;


INSERT INTO devis_travaux (iddevis, idtravaux, prix_unitaire, quantite)
SELECT
    d.id_devis,
    t.id_travaux,
    t.prix_unitaire,
    mt.quantite
FROM devis_csv dc
JOIN deviss d ON d.reference = dc.ref_devis
JOIN maisons_travaux mt ON mt.idmaison = d.idmaison
JOIN travauxs t ON t.id_travaux = mt.idtravaux
ON CONFLICT(iddevis, idtravaux) DO NOTHING;


INSERT INTO payement_devis (reference ,iddevis,montant, date_payement)
SELECT 
    pc.ref_paiement,
    d.id_devis,
    CAST(pc.montant as double precision),
    to_timestamp(date_paiement, 'DD/MM/YYYY')
FROM paiement_csv pc 
JOIN deviss d ON d.reference = pc.ref_devis
LEFT JOIN payement_devis pd ON pd.reference = pc.ref_paiement
WHERE pd.id_payement_devis is null;

