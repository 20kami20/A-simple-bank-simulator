CREATE OR REPLACE PROCEDURE public.register_user(
    IN p_username character varying,
    IN p_password character varying,
    IN p_pin_code character)
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    INSERT INTO users (username, password, pin_code, balance)
    VALUES (p_username, p_password, p_pin_code, 0.00);
END;
$BODY$;

ALTER PROCEDURE public.register_user(character varying, character varying, character)
    OWNER TO postgres;