package br.com.devcoelho.taskboard.controller;

import br.com.devcoelho.taskboard.dto.BoardDTO;
import br.com.devcoelho.taskboard.dto.BoardSummaryDTO;
import br.com.devcoelho.taskboard.dto.mappers.BoardMapper;
import br.com.devcoelho.taskboard.dto.request.CreateBoardRequest;
import br.com.devcoelho.taskboard.dto.request.UpdateBoardRequest;
import br.com.devcoelho.taskboard.model.Board;
import br.com.devcoelho.taskboard.service.BoardService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

  private final BoardService boardService;
  private final BoardMapper boardMapper;

  @GetMapping
  public ResponseEntity<List<BoardSummaryDTO>> getAllBoards() {
    List<Board> boards = boardService.findAll();
    return ResponseEntity.ok(boardMapper.toSummaryDtoList(boards));
  }

  @GetMapping("/{id}")
  public ResponseEntity<BoardDTO> getBoardById(@PathVariable Long id) {
    Board board = boardService.findById(id);
    return ResponseEntity.ok(boardMapper.toDto(board));
  }

  @PostMapping
  public ResponseEntity<BoardDTO> createBoard(@Valid @RequestBody CreateBoardRequest request) {
    Board board = new Board();
    board.setName(request.getName());
    Board createdBoard = boardService.create(board);
    return new ResponseEntity<>(boardMapper.toDto(createdBoard), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<BoardDTO> updateBoard(
      @PathVariable Long id, @Valid @RequestBody UpdateBoardRequest request) {

    Board existingBoard = boardService.findById(id);
    existingBoard.setName(request.getName());
    Board updatedBoard = boardService.update(id, existingBoard);
    return ResponseEntity.ok(boardMapper.toDto(updatedBoard));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
    boardService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
