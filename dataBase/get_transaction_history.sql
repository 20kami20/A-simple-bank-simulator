CREATE OR REPLACE FUNCTION public.get_transaction_history(
    p_user_id integer)
    RETURNS TABLE(transaction_type character varying, amount numeric, transaction_date timestamp without time zone) 
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT t.type, t.amount, t.transaction_date
    FROM transactions t
    WHERE t.user_id = p_user_id
    ORDER BY t.transaction_date DESC;
END;
$BODY$;

ALTER FUNCTION public.get_transaction_history(integer)
    OWNER TO postgres;