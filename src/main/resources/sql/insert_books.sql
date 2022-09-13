/*
This SQL code inserts 3 books into "Books" table.
Title or name of the book, will be stored under "title" column, while
the reference to the genre of the book is stored under "genre_code" column.
*/

INSERT INTO "Books" (title, genre_code)
VALUES ("Test Driven Development", "PROG");

INSERT INTO "Books" (title, genre_code)
VALUES ("Programming in Haskell", "PROG");

INSERT INTO "Books" (title, genre_code)
VALUES ("Scatterlings of Africa", "BIO");