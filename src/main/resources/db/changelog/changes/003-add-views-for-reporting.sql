-- Liquibase formatted SQL
-- changeset author:gabriel-coelho:003
-- View para estatísticas de cards por coluna
CREATE
OR REPLACE VIEW vw_card_stats_by_column AS
SELECT
  bc.board_id,
  bc.id AS column_id,
  bc.name AS column_name,
  bc.kind AS column_kind,
  COUNT(c.id) AS card_count
FROM
  board_column bc
  LEFT JOIN card c ON bc.id = c.board_column_id
GROUP BY
  bc.board_id,
  bc.id,
  bc.name,
  bc.kind
ORDER BY
  bc.board_id,
  bc.`order`;

-- View para estatísticas de bloqueios
CREATE
OR REPLACE VIEW vw_block_stats AS
SELECT
  b.id AS board_id,
  b.name AS board_name,
  COUNT(DISTINCT bl.id) AS total_blocks,
  COUNT(
    DISTINCT CASE
      WHEN bl.unblocked_at IS NULL THEN bl.id
      ELSE NULL
    END
  ) AS active_blocks,
  AVG(
    TIMESTAMPDIFF (
      HOUR,
      bl.blocked_at,
      CASE
        WHEN bl.unblocked_at IS NULL THEN CURRENT_TIMESTAMP
        ELSE bl.unblocked_at
      END
    )
  ) AS avg_block_duration_hours
FROM
  board b
  JOIN board_column bc ON b.id = bc.board_id
  JOIN card c ON bc.id = c.board_column_id
  LEFT JOIN block bl ON c.id = bl.card_id
GROUP BY
  b.id,
  b.name;

-- View para razões comuns de bloqueio
CREATE
OR REPLACE VIEW vw_common_block_reasons AS
SELECT
  block_reason,
  COUNT(*) AS count
FROM
  block
GROUP BY
  block_reason
ORDER BY
  count DESC;
