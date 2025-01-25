CREATE OR REPLACE PROCEDURE public.update_pin(
    IN p_user_id integer,
    IN p_new_pin character varying)
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE users
    SET pin_code = p_new_pin
    WHERE id = p_user_id;
END;
$BODY$;

ALTER PROCEDURE public.update_pin(integer, character varying)
    OWNER TO postgres;