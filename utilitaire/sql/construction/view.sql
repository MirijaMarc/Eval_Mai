CREATE OR REPLACE VIEW v_prix_maisons AS
SELECT m.id_maison id, m.nom_maison nom, m.description_maison description, m.duree_construction duree, SUM( t.prix_unitaire * mt.quantite) prix from maisons m 
JOIN maisons_travaux mt ON mt.idmaison = m.id_maison
JOIN travauxs t ON mt.idtravaux = t.id_travaux
GROUP by m.id_maison, m.description_maison, m.duree_construction;

CREATE OR REPLACE VIEW v_devis AS 
SELECT d.id_devis id, d.reference,d.lieu, d.iduser,d.duree, d.idmaison, d.idfinition, d.montant, d.date_debut_travaux , d.etat_devis statut, COALESCE(d.montant - SUM(pd.montant), d.montant) reste, COALESCE(SUM(pd.montant), 0) payement_effectue, COALESCE(SUM(pd.montant)*100/d.montant, 0)pourcentage_paye   FROM deviss d 
LEFT JOIN payement_devis pd ON pd.iddevis = d.id_devis
GROUP BY d.id_devis, d.idmaison, d.iduser, d.duree, d.idfinition, d.montant, d.date_debut_travaux , d.etat_devis;




WITH mois_annee AS (
  SELECT *
  FROM generate_series(1,12) as mois
),
devis_par_mois AS (
  SELECT EXTRACT(YEAR FROM date_devis) AS annee,
         EXTRACT(MONTH FROM date_devis) AS mois,
         COUNT(*) AS nombre_de_devis,
         SUM(montant) AS total_montant
  FROM deviss
  GROUP BY EXTRACT(YEAR FROM date_devis), EXTRACT(MONTH FROM date_devis)
)
SELECT ma.mois,
       COALESCE(dm.nombre_de_devis, 0) AS nombre_de_devis,
       COALESCE(dm.total_montant, 0) AS total_montant
FROM mois_annee ma
LEFT JOIN devis_par_mois dm ON ma.mois = dm.mois
                             
ORDER BY ma.mois AND dm.annee = 2024;

