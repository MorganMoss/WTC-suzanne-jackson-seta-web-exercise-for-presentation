/*
This SQL code finds the number of history books.
*/

SELECT COUNT(*)
FROM "Genres" g, "Books" b
WHERE g.code = b.id AND b.genre_code = 'BIO';