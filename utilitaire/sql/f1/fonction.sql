CREATE OR REPLACE FUNCTION getResultats(idgp INT, annee integer)
RETURNS TABLE (idgrandprix INT, idpilote INT, nom_pilote VARCHAR(255), points DOUBLE PRECISION) AS
$$
DECLARE
    coeff integer[];
    r1 RECORD;
    count integer;
BEGIN
    coeff := ARRAY[25, 18, 15, 12, 10, 8, 6, 4, 2, 1];
    count := 1;
    FOR r1 IN
        SELECT gpp.idgrandprix, gpp.idpilote, p.nom_pilote, gpp.temps_course
        FROM grand_prix_pilotes gpp
        JOIN pilotes p ON gpp.idpilote = p.id_pilote
        WHERE EXTRACT(YEAR FROM gpp.date_grand_prix) = annee AND gpp.idgrandprix = idgp
        ORDER BY temps_course
    LOOP
        idgrandprix:= r1.idgrandprix;
        idpilote:= r1.idpilote;
        nom_pilote:= r1.nom_pilote;
        IF count <= 10 THEN
            points:= coeff[count];
        ELSE
            points:= 0;
        END IF;
        count:= count + 1;
        RETURN NEXT;
    END LOOP;
END;
$$
LANGUAGE plpgsql;


SELECT * FROM getResultats(1, 2023);



CREATE OR REPLACE FUNCTION getClassement(annee integer)
RETURNS TABLE (idpilote INT, nom_pilote VARCHAR(255), points DOUBLE PRECISION) AS
$$
DECLARE
    r_gp RECORD;
    resultat RECORD;
BEGIN

    CREATE TEMP TABLE classement(
        idpilote INT,
        nom_pilote VARCHAR(255),
        points DOUBLE PRECISION
    );

    FOR r_gp IN
        SELECT id_grand_prix FROM grand_prixs
    LOOP
        FOR resultat IN
            SELECT * FROM getResultats(r_gp.id_grand_prix, annee)
        LOOP
            INSERT INTO classement VALUES (resultat.idpilote, resultat.nom_pilote, resultat.points);
        END LOOP;
    END LOOP;

    RETURN QUERY SELECT c.idpilote, c.nom_pilote, SUM(c.points) FROM classement c GROUP BY c.idpilote, c.nom_pilote ORDER BY SUM(c.points) DESC;
    DROP TABLE classement;
END;    
$$
LANGUAGE plpgsql;


SELECT * FROM getClassement(2023);


CREATE OR REPLACE FUNCTION getResultatEcuries(idgp INT, annee integer)
RETURNS TABLE (idgrandprix INT, idecurie INT, nom_ecurie VARCHAR(255), points DOUBLE PRECISION) AS
$$
DECLARE
BEGIN
    RETURN QUERY (SELECT res.idgrandprix , e.id_ecurie, e.nom_ecurie, SUM(res.points) points from getResultats(idgp, annee) res
                    JOIN ecurie_pilotes ep ON res.idpilote = ep.idpilote
                    JOIN ecuries e ON e.id_ecurie= ep.idecurie
                    GROUP BY res.idgrandprix, e.id_ecurie, e.nom_ecurie);
END;
$$
LANGUAGE plpgsql;

SELECT * from getResultatEcuries(1,2023);



CREATE OR REPLACE FUNCTION getClassementsEcuries (annee integer)
RETURNS TABLE (idecurie INT, nom_ecurie VARCHAR(255), points DOUBLE PRECISION) AS
$$
DECLARE
    r1 RECORD;
    r2 RECORD;
BEGIN
    CREATE TEMP TABLE classement(
        idecurie INT,
        nom_ecurie VARCHAR(255),
        points DOUBLE PRECISION
    );
    FOR r1 IN
        SELECT id_grand_prix from grand_prixs
    LOOP
        FOR r2 IN 
            SELECT * from getResultatEcuries(r1.id_grand_prix, annee)
        LOOP
            INSERT INTO classement VALUES (r2.idecurie, r2.nom_ecurie, r2.points);
        END LOOP;
    END LOOP;
    RETURN QUERY(
        SELECT c.idecurie, c.nom_ecurie,SUM(c.points) points  FROM classement c 
        GROUP BY c.idecurie, c.nom_ecurie
        ORDER BY SUM(c.points) DESC
    );
    DROP TABLE classement;
END;
$$
LANGUAGE plpgsql;



SELECT * FROM getClassementsEcuries(2023);