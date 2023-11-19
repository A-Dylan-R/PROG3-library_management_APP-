-- create_table_author.sql
CREATE TABLE IF NOT EXISTS author (
    author_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    sex VARCHAR(1) CHECK (sex IN ('M', 'F'))
);

-- Ajout de trois lignes de données par défaut
INSERT INTO author (name, sex) VALUES
    ('Author 1', 'M'),
    ('Author 2', 'F'),
    ('Author 3', 'M');
