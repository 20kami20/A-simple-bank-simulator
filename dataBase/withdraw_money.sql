CREATE OR REPLACE PROCEDURE public.withdraw_money(
    IN p_user_id integer,
    IN p_amount double precision)
LANGUAGE 'plpgsql'
AS $BODY$
DECLARE
    current_balance DOUBLE PRECISION;
BEGIN
    -- Check current balance
    SELECT balance INTO current_balance
    FROM users
    WHERE id = p_user_id;

    IF current_balance >= p_amount THEN
        -- Deduct balance
        UPDATE users
        SET balance = balance - p_amount
        WHERE id = p_user_id;

        -- Record transaction
        INSERT INTO transactions (user_id, type, amount)
        VALUES (p_user_id, 'WITHDRAWAL', p_amount);
    ELSE
        RAISE EXCEPTION 'Insufficient balance';
    END IF;
END;
$BODY$;

ALTER PROCEDURE public.withdraw_money(integer, double precision)
    OWNER TO postgres;