package br.com.devcoelho.taskboard.controller;

import br.com.devcoelho.taskboard.dto.BoardColumnDTO;
import br.com.devcoelho.taskboard.dto.mappers.BoardColumnMapper;
import br.com.devcoelho.taskboard.dto.request.CreateBoardColumnRequest;
import br.com.devcoelho.taskboard.dto.request.UpdateBoardColumnRequest;
import br.com.devcoelho.taskboard.model.BoardColumn;
import br.com.devcoelho.taskboard.service.BoardColumnService;
import jakarta.validation.Valid;
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
  private final BoardColumnMapper boardColumnMapper;

  @GetMapping
  public ResponseEntity<List<BoardColumnDTO>> getColumnsByBoardId(@PathVariable Long boardId) {
    List<BoardColumn> columns = boardColumnService.findByBoardId(boardId);
    return ResponseEntity.ok(boardColumnMapper.toDtoList(columns));
  }

  @GetMapping("/{id}")
  public ResponseEntity<BoardColumnDTO> getColumnById(@PathVariable Long id) {
    BoardColumn column = boardColumnService.findById(id);
    return ResponseEntity.ok(boardColumnMapper.toDto(column));
  }

  @PostMapping
  public ResponseEntity<BoardColumnDTO> createColumn(
      @PathVariable Long boardId, @Valid @RequestBody CreateBoardColumnRequest request) {

    BoardColumn column = new BoardColumn();
    column.setName(request.getName());
    column.setKind(request.getKind());
    column.setOrder(request.getOrder());

    BoardColumn createdColumn = boardColumnService.create(boardId, column);
    return new ResponseEntity<>(boardColumnMapper.toDto(createdColumn), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<BoardColumnDTO> updateColumn(
      @PathVariable Long id, @Valid @RequestBody UpdateBoardColumnRequest request) {

    BoardColumn existingColumn = boardColumnService.findById(id);
    existingColumn.setName(request.getName());

    BoardColumn updatedColumn = boardColumnService.update(id, existingColumn);
    return ResponseEntity.ok(boardColumnMapper.toDto(updatedColumn));
  }

  @PostMapping("/reorder")
  public ResponseEntity<List<BoardColumnDTO>> reorderColumns(
      @PathVariable Long boardId, @RequestBody List<Long> columnIds) {

    List<BoardColumn> reordered = boardColumnService.reorderColumns(boardId, columnIds);
    return ResponseEntity.ok(boardColumnMapper.toDtoList(reordered));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteColumn(@PathVariable Long id) {
    boardColumnService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
