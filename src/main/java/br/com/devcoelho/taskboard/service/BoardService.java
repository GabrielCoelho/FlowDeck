package br.com.devcoelho.taskboard.service;

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
}
