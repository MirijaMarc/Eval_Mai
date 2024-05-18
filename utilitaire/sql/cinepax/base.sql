CREATE DATABASE cinepax;

\c cinepax

CREATE TABLE salle (
    id_salle SERIAL PRIMARY KEY,
    nom VARCHAR(250) NOT NULL UNIQUE,
    etat_salle INTEGER DEFAULT 0 NOT NULL,
    CHECK (etat_salle = 0 OR etat_salle=-10)
);

CREATE TABLE categorie (
    id_categorie SERIAL PRIMARY KEY,
    nom VARCHAR(250) NOT NULL UNIQUE,
    etat_categorie INTEGER DEFAULT 0 NOT NULL,
    CHECK (etat_categorie = 0 OR etat_categorie=-10)
);

CREATE TABLE film (
    id_film SERIAL PRIMARY KEY,
    nom VARCHAR(250) NOT NULL UNIQUE,
    etat_film INTEGER DEFAULT 0 NOT NULL,
    CHECK (etat_film = 0 OR etat_film=-10)
);

CREATE TABLE seance (
    id_seance SERIAL PRIMARY KEY,
    num_seance INT NOT NULL,
    id_film INT NOT NULL,
    id_categorie INT NOT NULL,
    id_salle INT NOT NULL,
    date_jour DATE NOT NULL,
    heure TIME NOT NULL,

    FOREIGN KEY (id_film) REFERENCES film(id_film),
    FOREIGN KEY (id_categorie) REFERENCES categorie(id_categorie),
    FOREIGN KEY (id_salle) REFERENCES salle(id_salle),
    
    etat_seance INTEGER DEFAULT 0 NOT NULL,
    CHECK (etat_seance = 0 OR etat_seance=-10)
);

CREATE TABLE csv (
    id SERIAL PRIMARY KEY,
    num_seance VARCHAR(100),
    film VARCHAR(250),
    categorie VARCHAR(250),
    salle VARCHAR(250),
    date_jour VARCHAR(150),
    heure VARCHAR(150)
);