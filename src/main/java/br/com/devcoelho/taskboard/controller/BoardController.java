package br.com.devcoelho.taskboard.controller;

import br.com.devcoelho.taskboard.model.Board;
import br.com.devcoelho.taskboard.service.BoardService;
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

  @GetMapping
  public ResponseEntity<List<Board>> getAllBoards() {
    return ResponseEntity.ok(boardService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Board> getBoardById(@PathVariable Long id) {
    return ResponseEntity.ok(boardService.findById(id));
  }

  @PostMapping
  public ResponseEntity<Board> createBoard(@RequestBody Board board) {
    return new ResponseEntity<>(boardService.create(board), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Board> updateBoard(@PathVariable Long id, @RequestBody Board boardDetails) {
    return ResponseEntity.ok(boardService.update(id, boardDetails));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
    boardService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
