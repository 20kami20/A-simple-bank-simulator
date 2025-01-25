CREATE OR REPLACE FUNCTION public.login_user(
    p_username character varying,
    p_password character varying)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
    user_id INT;
BEGIN
    SELECT id INTO user_id
    FROM users
    WHERE username = p_username AND password = p_password;

    IF user_id IS NULL THEN
        RETURN NULL;
    END IF;

    RETURN user_id;
END;
$BODY$;

ALTER FUNCTION public.login_user(character varying, character varying)
    OWNER TO postgres;