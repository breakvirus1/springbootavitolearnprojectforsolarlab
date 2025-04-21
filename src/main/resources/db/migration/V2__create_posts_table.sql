CREATE TABLE posts (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2),
    date TIMESTAMP,
    category VARCHAR(50),
    author_id BIGINT REFERENCES authors(id)
); 