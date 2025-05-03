-- Liquibase formatted SQL

-- changeset author:gabriel-coelho:004

-- Procedure para normalizar ordens das colunas
DELIMITER //
CREATE PROCEDURE sp_normalize_column_orders(IN p_board_id BIGINT)
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE col_id BIGINT;
    DECLARE new_order INT DEFAULT 1;

    DECLARE cur CURSOR FOR
        SELECT id FROM board_column
        WHERE board_id = p_board_id
        ORDER BY `order`;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    START TRANSACTION;

    OPEN cur;

    read_loop: LOOP
        FETCH cur INTO col_id;
        IF done THEN
            LEAVE read_loop;
        END IF;

        UPDATE board_column SET `order` = new_order WHERE id = col_id;
        SET new_order = new_order + 1;
    END LOOP;

    CLOSE cur;

    COMMIT;
END //
DELIMITER ;

-- Function para verificar se um card está bloqueado
DELIMITER //
CREATE FUNCTION fn_is_card_blocked(p_card_id BIGINT) RETURNS BOOLEAN
DETERMINISTIC
BEGIN
    DECLARE is_blocked BOOLEAN;

    SELECT EXISTS (
        SELECT 1 FROM block
        WHERE card_id = p_card_id
        AND unblocked_at IS NULL
    ) INTO is_blocked;

    RETURN is_blocked;
END //
DELIMITER ;

-- Procedure para mover um card para outra coluna
DELIMITER //
CREATE PROCEDURE sp_move_card(
    IN p_card_id BIGINT,
    IN p_target_column_id BIGINT,
    OUT p_success BOOLEAN,
    OUT p_message VARCHAR(255)
)
BEGIN
    DECLARE is_blocked BOOLEAN;

    SET is_blocked = fn_is_card_blocked(p_card_id);

    IF is_blocked THEN
        SET p_success = FALSE;
        SET p_message = 'Card is blocked and cannot be moved';
    ELSE
        UPDATE card
        SET board_column_id = p_target_column_id,
            updated_at = CURRENT_TIMESTAMP
        WHERE id = p_card_id;

        SET p_success = TRUE;
        SET p_message = 'Card moved successfully';
    END IF;
END //
DELIMITER ;
