/*
Creates a table named "Genres" for storing book genres.
"code" column is unique short code to uniquely identify a genre (TEXT, NOT NULL, PRIMARY KEY).
"description" column - full description of the genre(TEXT, NOT NULL).
*/

CREATE TABLE "Genres" (
    "code" TEXT NOT NULL PRIMARY KEY,
    "description" TEXT NOT NULL
);