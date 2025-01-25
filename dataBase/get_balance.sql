CREATE OR REPLACE FUNCTION public.get_balance(
    p_user_id integer)
    RETURNS numeric
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
    user_balance NUMERIC; -- Avoid conflict by renaming variable
BEGIN
    SELECT u.balance INTO user_balance
    FROM users u
    WHERE u.id = p_user_id;

    RETURN user_balance;
END;
$BODY$;

ALTER FUNCTION public.get_balance(integer)
    OWNER TO postgres;