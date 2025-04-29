package br.com.devcoelho.taskboard.controller;

import br.com.devcoelho.taskboard.model.Card;
import br.com.devcoelho.taskboard.service.CardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

  private final CardService cardService;

  @GetMapping("/board/{boardId}")
  public ResponseEntity<List<Card>> getCardsByBoardId(@PathVariable Long boardId) {
    return ResponseEntity.ok(cardService.findByBoardId(boardId));
  }

  @GetMapping("/column/{columnId}")
  public ResponseEntity<List<Card>> getCardsByColumnId(@PathVariable Long columnId) {
    return ResponseEntity.ok(cardService.findByBoardColumnId(columnId));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Card> getCardById(@PathVariable Long id) {
    return ResponseEntity.ok(cardService.findById(id));
  }

  @PostMapping("/board/{boardId}")
  public ResponseEntity<Card> createCard(@PathVariable Long boardId, @RequestBody Card card) {
    return new ResponseEntity<>(cardService.create(boardId, card), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Card> updateCard(@PathVariable Long id, @RequestBody Card cardDetails) {
    return ResponseEntity.ok(cardService.update(id, cardDetails));
  }

  @PostMapping("/{id}/move/{columnId}")
  public ResponseEntity<Card> moveCard(@PathVariable Long id, @PathVariable Long columnId) {
    return ResponseEntity.ok(cardService.moveCard(id, columnId));
  }

  @PostMapping("/{id}/cancel")
  public ResponseEntity<Card> cancelCard(@PathVariable Long id) {
    return ResponseEntity.ok(cardService.cancelCard(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
    cardService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
