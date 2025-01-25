CREATE OR REPLACE PROCEDURE public.deposit_money(
    IN p_user_id integer,
    IN p_amount double precision)
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE users
    SET balance = balance + p_amount
    WHERE id = p_user_id;

    INSERT INTO transactions (user_id, type, amount)
    VALUES (p_user_id, 'DEPOSIT', p_amount);
END;
$BODY$;

ALTER PROCEDURE public.deposit_money(integer, double precision)
    OWNER TO postgres;