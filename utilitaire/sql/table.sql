CREATE TABLE csv(
    id SERIAL PRIMARY KEY NOT NULL,
    nom VARCHAR(255) NOT NULL,
    datenaissance VARCHAR(255) NOT NULL,
    ecurie VARCHAR(255) NOT NULL,
    grandprix VARCHAR(255) NOT NULL,
    dateGrandPrix VARCHAR(255) NOT NULL,
    temps VARCHAR(255) NOT NULL
);