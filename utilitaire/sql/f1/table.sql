-- CREATE DATABASE f1_v2;


-- \c f1_v2;



CREATE TABLE pilotes (
  id_pilote SERIAL PRIMARY KEY,
  nom_pilote VARCHAR(255) NOT NULL,
  date_naissance DATE NOT NULL,
  etat_pilote INT DEFAULT 0 NOT NULL,
  CHECK (etat_pilote = 0 OR etat_pilote=-10)
);


CREATE TABLE ecuries (
  id_ecurie SERIAL PRIMARY KEY,
  nom_ecurie VARCHAR(255) NOT NULL UNIQUE,
  etat_ecurie INT DEFAULT 0 NOT NULL,
  CHECK (etat_ecurie = 0 OR etat_ecurie=-10)
);


CREATE TABLE ecurie_pilotes (
  id_ecurie_pilote SERIAL PRIMARY KEY,
  idecurie INT NOT NULL,
  idpilote INT NOT NULL,
  FOREIGN KEY (idecurie) REFERENCES ecuries(id_ecurie),
  FOREIGN KEY (idpilote) REFERENCES pilotes(id_pilote),
  etat_ecurie_pilote INT DEFAULT 0 NOT NULL,
   UNIQUE(idecurie,idpilote),
  CHECK (etat_ecurie_pilote = 0 OR etat_ecurie_pilote=-10)

);



CREATE TABLE grand_prixs (
  id_grand_prix SERIAL PRIMARY KEY,
  nom_grand_prix VARCHAR(255) NOT NULL,
  etat_grand_prix INT DEFAULT 0 NOT NULL,
  CHECK (etat_grand_prix = 0 OR etat_grand_prix=-10)

);


CREATE TABLE grand_prix_pilotes (
  id_grand_prix_pilote SERIAL PRIMARY KEY,
  idgrandprix INT,
  idpilote INT,
  temps_course TIME NOT NULL,
  FOREIGN KEY (idgrandprix) REFERENCES grand_prixs(id_grand_prix),
  FOREIGN KEY (idpilote) REFERENCES pilotes(id_pilote),
  date_grand_prix DATE NOT NULL,
  etat_grand_prix_pilote INT DEFAULT 0 NOT NULL,
  UNIQUE(idgrandprix, idpilote, date_grand_prix),
  CHECK (etat_grand_prix_pilote = 0 OR etat_grand_prix_pilote=-10)
);

CREATE TABLE users(
  id_user SERIAL PRIMARY KEY,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  role INTEGER DEFAULT 1 NOT NULL,
  etat_user INT DEFAULT 0 NOT NULL,
  CHECK (etat_user = 0 OR etat_user = 10)
);

CREATE TABLE csv(
    id SERIAL PRIMARY KEY NOT NULL,
    nom VARCHAR(255) NOT NULL,
    datenaissance VARCHAR(255) NOT NULL,
    ecurie VARCHAR(255) NOT NULL,
    grandprix VARCHAR(255) NOT NULL,
    dateGrandPrix VARCHAR(255) NOT NULL,
    temps VARCHAR(255) NOT NULL
);



