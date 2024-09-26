SELECT
id AS id,
column_1 AS column_1,
(
    SELECT T2.sub_col_1 FROM table_2 T2 WHERE T2.foreign_key LIKE "match"
) AS column_2
FROM table_1
WHERE {{condition}}