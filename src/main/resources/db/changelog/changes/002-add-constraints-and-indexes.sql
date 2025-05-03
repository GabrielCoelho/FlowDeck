-- Liquibase formatted SQL
-- changeset author:gabriel-coelho:002
-- Adiciona um índice composto para otimizar consultas de busca de cards por data e board
CREATE INDEX idx_card_date_board ON card (created_at, board_column_id);

-- Adiciona um índice para otimizar consultas de bloqueios por estado
CREATE INDEX idx_block_status ON block (card_id, unblocked_at);

-- Adiciona um índice para otimizar busca por nome de board
CREATE INDEX idx_board_name ON board (name);

-- Adiciona restrição para garantir que um card não possa ser movido se estiver bloqueado
ALTER TABLE card ADD CONSTRAINT ck_card_blocked CHECK (
  NOT EXISTS (
    SELECT
      1
    FROM
      block
    WHERE
      block.card_id = card.id
      AND block.unblocked_at IS NULL
  )
);
