package br.com.devcoelho.taskboard.service;

import br.com.devcoelho.taskboard.exception.ColumnContainsCardException;
import br.com.devcoelho.taskboard.exception.ResourceNotFoundException;
import br.com.devcoelho.taskboard.exception.SpecialColumnDeletionException;
import br.com.devcoelho.taskboard.exception.SpecialColumnException;
import br.com.devcoelho.taskboard.model.Board;
import br.com.devcoelho.taskboard.model.BoardColumn;
import br.com.devcoelho.taskboard.model.BoardColumnKind;
import br.com.devcoelho.taskboard.repository.BoardColumnRepository;
import br.com.devcoelho.taskboard.repository.BoardRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardColumnService {

  private final BoardColumnRepository boardColumnRepository;
  private final BoardRepository boardRepository;

  /** Busca todas as colunas de um board ordenadas */
  public List<BoardColumn> findByBoardId(Long boardId) {
    return boardColumnRepository.findByBoardIdOrderByOrder(boardId);
  }

  /** Busca uma coluna pelo ID */
  public BoardColumn findById(Long id) {
    return boardColumnRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Board Column", id));
  }

  /** Cria uma nova coluna em um board */
  @Transactional
  public BoardColumn create(Long boardId, BoardColumn boardColumn) {
    Board board =
        boardRepository
            .findById(boardId)
            .orElseThrow(() -> new ResourceNotFoundException("Board", boardId));

    boardColumn.setBoard(board);

    // Verifica se é permitido criar mais de uma coluna com o mesmo tipo especial
    if (boardColumn.getKind() != BoardColumnKind.PENDING) {
      boardColumnRepository
          .findByBoardIdAndKind(boardColumn.getBoard().getId(), boardColumn.getKind())
          .ifPresent(
              existingColumn -> {
                throw new SpecialColumnException(boardColumn.getKind(), boardColumn.getId());
              });
    }

    // Define a ordem como a última se não foi especificada
    if (boardColumn.getOrder() <= 0) {
      List<BoardColumn> columns = findByBoardId(boardColumn.getBoard().getId());
      boardColumn.setOrder(columns.size() + 1);
    }

    return boardColumnRepository.save(boardColumn);
  }

  /** Atualiza uma coluna existente */
  @Transactional
  public BoardColumn update(Long id, BoardColumn columnDetails) {
    BoardColumn column = findById(id);
    column.setName(columnDetails.getName());

    // Se estiver mudando o tipo, verifica se já existe outro do mesmo tipo
    if (columnDetails.getKind() != null
        && !column.getKind().equals(columnDetails.getKind())
        && columnDetails.getKind() != BoardColumnKind.PENDING) {

      boardColumnRepository
          .findByBoardIdAndKind(column.getBoard().getId(), columnDetails.getKind())
          .ifPresent(
              existingColumn -> {
                throw new SpecialColumnException(columnDetails.getKind(), columnDetails.getId());
              });

      column.setKind(columnDetails.getKind());
    }

    return boardColumnRepository.save(column);
  }

  /** Reorganiza a ordem das colunas */
  @Transactional
  public List<BoardColumn> reorderColumns(Long boardId, List<Long> columnIds) {
    List<BoardColumn> columns = findByBoardId(boardId);

    // Verifica se todas as colunas pertencem ao board
    if (columnIds.size() != columns.size()
        || !columns.stream().map(BoardColumn::getId).allMatch(columnIds::contains)) {
      throw new RuntimeException("Invalid column list for reordering");
    }

    // Aplica a nova ordem
    for (int i = 0; i < columnIds.size(); i++) {
      Long columnId = columnIds.get(i);
      BoardColumn column =
          columns.stream().filter(c -> c.getId().equals(columnId)).findFirst().orElseThrow();
      column.setOrder(i + 1);
      boardColumnRepository.save(column);
    }

    return findByBoardId(boardId);
  }

  /** Remove uma coluna */
  @Transactional
  public void delete(Long id) {
    BoardColumn column = findById(id);

    // Não permite remover colunas especiais (INITIAL, FINAL, CANCEL)
    if (column.getKind() != BoardColumnKind.PENDING) {
      throw new SpecialColumnDeletionException(column.getKind(), column.getId());
    }

    // Verifica se a coluna não tem cards
    if (!column.getCards().isEmpty()) {
      throw new ColumnContainsCardException(column.getId());
    }

    boardColumnRepository.deleteById(id);
  }
}
