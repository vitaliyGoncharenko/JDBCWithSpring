CREATE FUNCTION getFirstNameByld(in_id INT)
RETURNS VARCHAR (60)
BEGIN
RETURN (SELECT first name FROM contact WHERE id = in_id);
END