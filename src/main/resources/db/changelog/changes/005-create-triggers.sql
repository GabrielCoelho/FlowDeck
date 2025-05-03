-- Liquibase formatted SQL
-- changeset author:gabriel-coelho:005
-- Trigger para atualizar a data de modificação de um card
DELIMITER / / CREATE TRIGGER trg_card_update_timestamp BEFORE
UPDATE ON card FOR EACH ROW BEGIN
SET
  NEW.updated_at = CURRENT_TIMESTAMP;

END / / DELIMITER;

-- Trigger para impedir a movimentação de cards bloqueados
DELIMITER / / CREATE TRIGGER trg_prevent_blocked_card_move BEFORE
UPDATE ON card FOR EACH ROW BEGIN DECLARE is_blocked BOOLEAN;

-- Verificamos se estamos tentando mover o card para outra coluna
IF OLD.board_column_id <> NEW.board_column_id THEN
-- Verificamos se o card está bloqueado
SELECT
  EXISTS (
    SELECT
      1
    FROM
      block
    WHERE
      card_id = NEW.id
      AND unblocked_at IS NULL
  ) INTO is_blocked;

IF is_blocked THEN SIGNAL SQLSTATE '45000'
SET
  MESSAGE_TEXT = 'Cannot move a blocked card';

END IF;

END IF;

END / / DELIMITER;

-- Trigger para garantir que colunas especiais sejam únicas por board
DELIMITER / / CREATE TRIGGER trg_unique_special_columns BEFORE INSERT ON board_column FOR EACH ROW BEGIN DECLARE count_special INT;

-- Só verificamos para colunas especiais (não PENDING)
IF NEW.kind <> 'PENDING' THEN
SELECT
  COUNT(*) INTO count_special
FROM
  board_column
WHERE
  board_id = NEW.board_id
  AND kind = NEW.kind;

IF count_special > 0 THEN SIGNAL SQLSTATE '45000'
SET
  MESSAGE_TEXT = CONCAT (
    'Board already has a column with kind: ',
    NEW.kind
  );

END IF;

END IF;

END / / DELIMITER;
