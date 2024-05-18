-- CREATE DATABASE construction;


-- \c construction



-- CREATE TABLE users(
--   id_user SERIAL PRIMARY KEY,
--   email VARCHAR(255),
--   numero VARCHAR(255),
--   password VARCHAR(255),
--   role INTEGER DEFAULT 1 NOT NULL,
--   etat_user INT DEFAULT 0 NOT NULL
-- );


CREATE TABLE finitions(
  id_finition SERIAL PRIMARY KEY,
  nom_finition VARCHAR(255) NOT NULL,
  marge DOUBLE PRECISION NOT NULL,
  etat_finition INT DEFAULT 0 NOT NULL
);


CREATE TABLE unites(
  id_unite SERIAL PRIMARY KEY,
  nom_unite VARCHAR(255) NOT NULL,
  etat_unite INT DEFAULT 0 NOT NULL
);


CREATE TABLE type_travaux(
  id_type_travaux SERIAL PRIMARY KEY,
  code_type_travaux VARCHAR(255) NOT NULL,
  nom_type_travaux VARCHAR(255) NOT NULL,
  etat_type_travaux INT DEFAULT 0 NOT NULL
);

CREATE TABLE travauxs(
  id_travaux SERIAL PRIMARY KEY,
  idtype_travaux INT REFERENCES type_travaux(id_type_travaux),
  code_travaux VARCHAR(255) NOT NULL,
  nom_travaux VARCHAR(255) NOT NULL,
  unite_travaux INTEGER REFERENCES unites(id_unite),
  prix_unitaire DOUBLE PRECISION,
  etat_travaux INT DEFAULT 0 NOT NULL
);

CREATE TABLE maisons(
  id_maison SERIAL PRIMARY KEY,
  nom_maison VARCHAR(255) NOT NULL,
  description_maison TEXT NOT NULL,
  etat_maison INT DEFAULT 0 NOT NULL,
  surface DOUBLE PRECISION,
  duree_construction DOUBLE PRECISION not null
);

CREATE TABLE maisons_travaux(
  id_maison_travaux SERIAL PRIMARY KEY,
  idmaison INTEGER REFERENCES maisons(id_maison),
  idtravaux INTEGER REFERENCES travauxs(id_travaux),
  quantite DOUBLE PRECISION,
  unique(idmaison, idtravaux)
);


CREATE TABLE deviss(
  id_devis SERIAL PRIMARY KEY,
  reference VARCHAR(255) NOT NULL,
  idmaison INTEGER REFERENCES maisons(id_maison),
  idfinition INTEGER REFERENCES finitions(id_finition),
  iduser INTEGER REFERENCES users(id_user),
  montant DOUBLE PRECISION NOT NULL,
  finition DOUBLE PRECISION NOT NULL,
  duree DOUBLE PRECISION NOT NULL,
  date_devis TIMESTAMP DEFAULT now(),
  date_debut_travaux DATE,
  lieu VARCHAR(255),
  etat_devis INTEGER DEFAULT 0 NOT NULL
);

CREATE TABLE payement_devis(
  id_payement_devis SERIAL PRIMARY KEY,
  reference VARCHAR(255) NOT NULL,
  iddevis INTEGER REFERENCES deviss(id_devis),
  montant DOUBLE PRECISION not null,
  date_payement TIMESTAMP NOT NULL,
  etat_payement_devis INTEGER DEFAULT 0 NOT NULL
);


CREATE TABLE devis_travaux(
  id_devis_travaux SERIAL PRIMARY KEY,
  iddevis INTEGER REFERENCES deviss(id_devis),
  idtravaux INTEGER REFERENCES travauxs(id_travaux),
  prix_unitaire DOUBLE PRECISION NOT NULL,
  quantite DOUBLE PRECISION NOT NULL,
  unique(iddevis, idtravaux)
);


CREATE TABLE maisons_travaux_csv(
  id SERIAL PRIMARY KEY,
  type_maison VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL,
  surface VARCHAR(255) NOT null,
  code_travaux VARCHAR(255) NOT NULL,
  type_travaux VARCHAR(255) NOT NULL,
  unite VARCHAR(255) NOT NULL,
  prix_unitaire VARCHAR(255) NOT NULL,
  quantite VARCHAR (255) NOT NULL,
  duree_travaux VARCHAR (255) not NULL
);

CREATE TABLE devis_csv(
  id SERIAL PRIMARY KEY,
  client VARCHAR (255) NOT NULL,
  ref_devis VARCHAR (255) NOT NULL,
  type_maison VARCHAR (255) NOT NULL,
  finition VARCHAR (255) NOT NULL,
  taux_finition VARCHAR (255) NOT NULL,
  date_devis VARCHAR (255) NOT NULL,
  date_debut VARCHAR (255) NOT NULL,
  lieu VARCHAR (255) NOT NULL
);

CREATE TABLE paiement_csv(
  id SERIAL PRIMARY KEY,
  ref_devis VARCHAR (255) NOT NULL,
  ref_paiement VARCHAR (255) NOT NULL,
  date_paiement VARCHAR (255) NOT NULL,
  montant VARCHAR (255) NOT NULL
);





