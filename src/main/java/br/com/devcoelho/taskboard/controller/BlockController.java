package br.com.devcoelho.taskboard.controller;

import br.com.devcoelho.taskboard.model.Block;
import br.com.devcoelho.taskboard.service.BlockService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards/{cardId}/blocks")
@RequiredArgsConstructor
public class BlockController {

  private final BlockService blockService;

  @GetMapping
  public ResponseEntity<List<Block>> getBlocksByCardId(@PathVariable Long cardId) {
    return ResponseEntity.ok(blockService.findByCardId(cardId));
  }

  @GetMapping("/status")
  public ResponseEntity<Map<String, Boolean>> isCardBlocked(@PathVariable Long cardId) {
    boolean blocked = blockService.isCardBlocked(cardId);
    return ResponseEntity.ok(Map.of("blocked", blocked));
  }

  @PostMapping
  public ResponseEntity<Block> blockCard(
      @PathVariable Long cardId, @RequestBody Map<String, String> payload) {
    String reason = payload.getOrDefault("reason", "No reason provided");
    return new ResponseEntity<>(blockService.blockCard(cardId, reason), HttpStatus.CREATED);
  }

  @PostMapping("/unblock")
  public ResponseEntity<Block> unblockCard(
      @PathVariable Long cardId, @RequestBody Map<String, String> payload) {
    String reason = payload.getOrDefault("reason", "No reason provided");
    return ResponseEntity.ok(blockService.unblockCard(cardId, reason));
  }
}
