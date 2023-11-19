CREATE TABLE IF NOT EXISTS book (
    book_id SERIAL PRIMARY KEY,
    book_name VARCHAR(255) NOT NULL,
    page_numbers INTEGER,
    topic VARCHAR(50) CHECK (topic IN ('ROMANCE', 'COMEDY', 'OTHER')),
    release_date DATE,
    status VARCHAR(50) DEFAULT 'AVAILABLE' CHECK (status IN ('AVAILABLE', 'BORROWED')),
    author_id INTEGER REFERENCES author(author_id) ON DELETE CASCADE
);

-- Ajout de trois lignes de données par défaut
INSERT INTO book (book_name, page_numbers, topic, release_date, author_id) VALUES
    ('Book 1', 300, 'COMEDY', '2023-01-01', 1),
    ('Book 2', 250, 'ROMANCE', '2023-02-01', 2),
    ('Book 3', 200, 'OTHER', '2023-03-01', 3);