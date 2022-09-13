/*
This SQL code retrieves all books with the
title of a book and the description of its genre.
*/

SELECT Books.title, Genres.description
FROM "Books"
INNER JOIN "Genres"
ON Books.genre_code = Genres.code;
