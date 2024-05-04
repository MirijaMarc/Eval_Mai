CREATE VIEW v_classement AS
SELECT gpp.*, gp.nom_grand_prix, FROM grand_prix_pilote gpp
JOIN grand_prix gp ON gpp.idgrandprix = gp.id_grand_prix
ORDER BY gpp.temps_course ASC;