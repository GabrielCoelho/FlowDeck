package br.com.devcoelho.taskboard.controller;

import br.com.devcoelho.taskboard.dto.CardDTO;
import br.com.devcoelho.taskboard.dto.CardSummaryDTO;
import br.com.devcoelho.taskboard.dto.mappers.CardMapper;
import br.com.devcoelho.taskboard.dto.request.CreateCardRequest;
import br.com.devcoelho.taskboard.dto.request.UpdateCardRequest;
import br.com.devcoelho.taskboard.model.Card;
import br.com.devcoelho.taskboard.service.CardService;
import jakarta.validation.Valid;
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
  private final CardMapper cardMapper;

  @GetMapping("/board/{boardId}")
  public ResponseEntity<List<CardSummaryDTO>> getCardsByBoardId(@PathVariable Long boardId) {
    List<Card> cards = cardService.findByBoardId(boardId);
    return ResponseEntity.ok(cardMapper.toSummaryDtoList(cards));
  }

  @GetMapping("/column/{columnId}")
  public ResponseEntity<List<CardDTO>> getCardsByColumnId(@PathVariable Long columnId) {
    List<Card> cards = cardService.findByBoardColumnId(columnId);
    return ResponseEntity.ok(cardMapper.toDtoList(cards));
  }

  @GetMapping("/{id}")
  public ResponseEntity<CardDTO> getCardById(@PathVariable Long id) {
    Card card = cardService.findById(id);
    return ResponseEntity.ok(cardMapper.toDto(card));
  }

  @PostMapping("/board/{boardId}")
  public ResponseEntity<CardDTO> createCard(
      @PathVariable Long boardId, @Valid @RequestBody CreateCardRequest request) {

    Card card = new Card();
    card.setTitle(request.getTitle());
    card.setDescription(request.getDescription());

    Card createdCard = cardService.create(boardId, card);
    return new ResponseEntity<>(cardMapper.toDto(createdCard), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CardDTO> updateCard(
      @PathVariable Long id, @Valid @RequestBody UpdateCardRequest request) {

    Card existingCard = cardService.findById(id);
    existingCard.setTitle(request.getTitle());
    existingCard.setDescription(request.getDescription());

    Card updatedCard = cardService.update(id, existingCard);
    return ResponseEntity.ok(cardMapper.toDto(updatedCard));
  }

  @PostMapping("/{id}/move/{columnId}")
  public ResponseEntity<CardDTO> moveCard(@PathVariable Long id, @PathVariable Long columnId) {

    Card movedCard = cardService.moveCard(id, columnId);
    return ResponseEntity.ok(cardMapper.toDto(movedCard));
  }

  @PostMapping("/{id}/cancel")
  public ResponseEntity<CardDTO> cancelCard(@PathVariable Long id) {
    Card canceledCard = cardService.cancelCard(id);
    return ResponseEntity.ok(cardMapper.toDto(canceledCard));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
    cardService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
