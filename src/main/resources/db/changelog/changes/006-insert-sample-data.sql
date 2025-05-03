-- Liquibase formatted SQL
-- changeset author:gabriel-coelho:006
-- Inserir boards de exemplo
INSERT INTO
  board (id, name)
VALUES
  (1, 'Desenvolvimento de Software'),
  (2, 'Marketing de Produto'),
  (3, 'Suporte ao Cliente');

-- Inserir colunas para o board 1
INSERT INTO
  board_column (id, name, board_id, kind, `order`)
VALUES
  (1, 'Backlog', 1, 'INITIAL', 1),
  (2, 'To Do', 1, 'PENDING', 2),
  (3, 'Em Progresso', 1, 'PENDING', 3),
  (4, 'Revisão', 1, 'PENDING', 4),
  (5, 'Concluído', 1, 'FINAL', 5),
  (6, 'Cancelado', 1, 'CANCEL', 6);

-- Inserir colunas para o board 2
INSERT INTO
  board_column (id, name, board_id, kind, `order`)
VALUES
  (7, 'Ideias', 2, 'INITIAL', 1),
  (8, 'Planejamento', 2, 'PENDING', 2),
  (9, 'Em Execução', 2, 'PENDING', 3),
  (10, 'Análise', 2, 'PENDING', 4),
  (11, 'Concluído', 2, 'FINAL', 5),
  (12, 'Descartado', 2, 'CANCEL', 6);

-- Inserir colunas para o board 3
INSERT INTO
  board_column (id, name, board_id, kind, `order`)
VALUES
  (13, 'Tickets Abertos', 3, 'INITIAL', 1),
  (14, 'Em Investigação', 3, 'PENDING', 2),
  (15, 'Aguardando Cliente', 3, 'PENDING', 3),
  (16, 'Resolvido', 3, 'FINAL', 4),
  (17, 'Fechado', 3, 'CANCEL', 5);

-- Inserir alguns cards no board 1
INSERT INTO
  card (
    id,
    title,
    description,
    created_at,
    board_column_id
  )
VALUES
  (
    1,
    'Implementar Login',
    'Criar sistema de autenticação com JWT',
    DATE_SUB (NOW (), INTERVAL 10 DAY),
    1
  ),
  (
    2,
    'Corrigir Bug #123',
    'Problema na validação de formulários',
    DATE_SUB (NOW (), INTERVAL 8 DAY),
    2
  ),
  (
    3,
    'Design da Tela Principal',
    'Criar layout responsivo',
    DATE_SUB (NOW (), INTERVAL 12 DAY),
    3
  ),
  (
    4,
    'Otimizar Consultas SQL',
    'Melhorar performance do relatório mensal',
    DATE_SUB (NOW (), INTERVAL 5 DAY),
    4
  ),
  (
    5,
    'Atualizar Documentação',
    'Documentar novas APIs',
    DATE_SUB (NOW (), INTERVAL 15 DAY),
    5
  );

-- Inserir alguns bloqueios
INSERT INTO
  block (
    id,
    blocked_at,
    block_reason,
    unblocked_at,
    unblock_reason,
    card_id
  )
VALUES
  (
    1,
    DATE_SUB (NOW (), INTERVAL 7 DAY),
    'Aguardando definição do cliente',
    DATE_SUB (NOW (), INTERVAL 5 DAY),
    'Cliente enviou requisitos',
    2
  ),
  (
    2,
    DATE_SUB (NOW (), INTERVAL 9 DAY),
    'Dependência externa não disponível',
    DATE_SUB (NOW (), INTERVAL 6 DAY),
    'Dependência resolvida',
    3
  ),
  (
    3,
    DATE_SUB (NOW (), INTERVAL 4 DAY),
    'Conflito de merge',
    NULL,
    NULL,
    4
  );
