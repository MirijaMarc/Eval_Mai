CREATE OR REPLACE VIEW v_ecurie_pilotes as
SELECT p.id_pilote idpilote, p.nom_pilote, p.date_naissance, e.nom_ecurie from ecurie_pilotes ep
JOIN ecuries e ON e.id_ecurie = ep.idecurie 
JOIN pilotes p ON p.id_pilote = ep.idpilote;