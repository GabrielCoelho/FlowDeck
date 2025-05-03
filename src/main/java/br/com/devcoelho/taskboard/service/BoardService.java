package br.com.devcoelho.taskboard.service;

import br.com.devcoelho.taskboard.dao.BoardDAO;
import br.com.devcoelho.taskboard.exception.ResourceNotFoundException;
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
public class BoardService {

  private final BoardRepository boardRepository;
  private final BoardColumnRepository boardColumnRepository;
  private final BoardDAO boardDAO;

  public List<Board> findAll() {
    return boardRepository.findAll();
  }

  public Board findById(Long id) {
    return boardRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Board", id));
  }

  @Transactional
  public Board create(Board board) {
    Board saved = boardRepository.save(board);
    createDefaultColumns(saved);
    return findById(saved.getId());
  }

  @Transactional
  public Board update(Long id, Board boardDetails) {
    Board board = findById(id);
    board.setName(boardDetails.getName());
    return boardRepository.save(board);
  }

  @Transactional
  public void delete(Long id) {
    boardRepository.deleteById(id);
  }

  private void createDefaultColumns(Board board) {
    BoardColumn initialColumn =
        BoardColumn.builder()
            .name("Backlog")
            .board(board)
            .order(1)
            .kind(BoardColumnKind.INITIAL)
            .build();

    BoardColumn todoColumn =
        BoardColumn.builder()
            .name("To Do")
            .board(board)
            .order(2)
            .kind(BoardColumnKind.PENDING)
            .build();

    BoardColumn inProgressColumn =
        BoardColumn.builder()
            .name("In Progress")
            .board(board)
            .order(3)
            .kind(BoardColumnKind.PENDING)
            .build();

    BoardColumn doneColumn =
        BoardColumn.builder()
            .name("Done")
            .board(board)
            .order(4)
            .kind(BoardColumnKind.FINAL)
            .build();

    BoardColumn canceledColumn =
        BoardColumn.builder()
            .name("Canceled")
            .board(board)
            .order(5)
            .kind(BoardColumnKind.CANCEL)
            .build();

    boardColumnRepository.saveAll(
        List.of(initialColumn, todoColumn, inProgressColumn, doneColumn, canceledColumn));
  }

  /**
   * Busca board com todas as relações carregadas. Utiliza o DAO para executar a consulta otimizada.
   *
   * @param id identificador do board
   * @return o board com todas as relações
   * @throws ResourceNotFoundException se o board não for encontrado
   */
  public Board findByIdWithAllRelations(Long id) {
    return boardDAO
        .findByIdWithAllRelations(id)
        .orElseThrow(() -> new ResourceNotFoundException("Board", id));
  }

  /**
   * Busca os boards mais ativos com base na quantidade de cards.
   *
   * @param limit número máximo de boards a retornar
   * @return lista de boards mais ativos
   */
  public List<Board> findMostActiveBoards(int limit) {
    return boardDAO.findMostActiveBoards(limit);
  }

  /**
   * Busca boards pelo nome contendo o texto e pela quantidade mínima de colunas.
   *
   * @param name parte do nome a ser buscada
   * @param minColumns número mínimo de colunas que o board deve ter
   * @return lista de boards que atendem aos critérios
   */
  public List<Board> findByNameAndMinColumns(String name, int minColumns) {
    return boardDAO.findByNameAndMinColumns(name, minColumns);
  }

  /**
   * Conta o número total de cards em um board.
   *
   * @param boardId identificador do board
   * @return número total de cards
   */
  public int countTotalCards(Long boardId) {
    return boardDAO.countTotalCards(boardId);
  }
}
