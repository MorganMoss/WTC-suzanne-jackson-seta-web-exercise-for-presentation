/*
Creates a table named "Books" for storing data for books.
"id" column that uniquely identify each book/row(INTEGER, NOT NULL, UNIQUE, PRIMARY KEY, AUTOINCREMENT)
"title" column   to store book title/name(NOT NULL)
"genre_code" column that stores a reference to the genre of the book and
it references the code column on the Genres table. Hence a FOREIGN KEY and NOT NULL
*/

CREATE TABLE "Books" (
    "id" INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT,
    "title" TEXT NOT NULL ,
    "genre_code" TEXT NOT NULL,
    FOREIGN KEY ("genre_code") REFERENCES "Genres"("code")
);