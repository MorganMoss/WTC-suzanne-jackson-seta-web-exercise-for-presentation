CREATE TABLE Books (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    title Text NOT NULL,
    genre_code Text NOT NULL,
    CONSTRAINT fk_code
        FOREIGN KEY (genre_code)
        REFERENCES Genres(code)
);