-- Liquibase formatted SQL
-- changeset author:gabriel-coelho:001
-- Tabela para boards
CREATE TABLE board (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL
) ENGINE = InnoDB;

-- Tabela para colunas do board
CREATE TABLE board_column (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  board_id BIGINT NOT NULL,
  kind VARCHAR(20) NOT NULL,
  `order` INT NOT NULL,
  CONSTRAINT fk_column_board FOREIGN KEY (board_id) REFERENCES board (id) ON DELETE CASCADE,
  CONSTRAINT uk_board_column_order UNIQUE (board_id, `order`)
) ENGINE = InnoDB;

-- Tabela para cards
CREATE TABLE card (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NULL,
  board_column_id BIGINT NOT NULL,
  CONSTRAINT fk_card_column FOREIGN KEY (board_column_id) REFERENCES board_column (id)
) ENGINE = InnoDB;

-- Tabela para bloqueios
CREATE TABLE block (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  blocked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  block_reason VARCHAR(255) NOT NULL,
  unblocked_at TIMESTAMP NULL,
  unblock_reason VARCHAR(255) NULL,
  card_id BIGINT NOT NULL,
  CONSTRAINT fk_block_card FOREIGN KEY (card_id) REFERENCES card (id) ON DELETE CASCADE
) ENGINE = InnoDB;

-- Índices para otimizar consultas
CREATE INDEX idx_card_created_at ON card (created_at);

CREATE INDEX idx_card_column_id ON card (board_column_id);

CREATE INDEX idx_block_card_id ON block (card_id);

CREATE INDEX idx_block_blocked_at ON block (blocked_at);

CREATE INDEX idx_block_unblocked_at ON block (unblocked_at);

CREATE INDEX idx_column_board_id ON board_column (board_id);

CREATE INDEX idx_column_kind ON board_column (kind);
