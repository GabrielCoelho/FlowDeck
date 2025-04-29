package br.com.devcoelho.taskboard.controller;

import br.com.devcoelho.taskboard.model.BoardColumn;
import br.com.devcoelho.taskboard.service.BoardColumnService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards/{boardId}/columns")
@RequiredArgsConstructor
public class BoardColumnController {

  private final BoardColumnService boardColumnService;

  @GetMapping
  public ResponseEntity<List<BoardColumn>> getColumnsByBoardId(@PathVariable Long boardId) {
    return ResponseEntity.ok(boardColumnService.findByBoardId(boardId));
  }

  @GetMapping("/{id}")
  public ResponseEntity<BoardColumn> getColumnById(@PathVariable Long id) {
    return ResponseEntity.ok(boardColumnService.findById(id));
  }

  @PostMapping
  public ResponseEntity<BoardColumn> createColumn(
      @PathVariable Long boardId, @RequestBody BoardColumn boardColumn) {

    return new ResponseEntity<>(
        boardColumnService.create(boardId, boardColumn), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<BoardColumn> updateColumn(
      @PathVariable Long id, @RequestBody BoardColumn columnDetails) {
    return ResponseEntity.ok(boardColumnService.update(id, columnDetails));
  }

  @PostMapping("/reorder")
  public ResponseEntity<List<BoardColumn>> reorderColumns(
      @PathVariable Long boardId, @RequestBody List<Long> columnIds) {
    return ResponseEntity.ok(boardColumnService.reorderColumns(boardId, columnIds));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteColumn(@PathVariable Long id) {
    boardColumnService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
