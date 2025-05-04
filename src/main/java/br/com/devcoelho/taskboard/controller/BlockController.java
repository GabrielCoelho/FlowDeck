package br.com.devcoelho.taskboard.controller;

import br.com.devcoelho.taskboard.dto.BlockDTO;
import br.com.devcoelho.taskboard.dto.mappers.BlockMapper;
import br.com.devcoelho.taskboard.dto.request.BlockCardRequest;
import br.com.devcoelho.taskboard.dto.request.UnblockCardRequest;
import br.com.devcoelho.taskboard.model.Block;
import br.com.devcoelho.taskboard.service.BlockService;
import jakarta.validation.Valid;
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
  private final BlockMapper blockMapper;

  @GetMapping
  public ResponseEntity<List<BlockDTO>> getBlocksByCardId(@PathVariable Long cardId) {
    List<Block> blocks = blockService.findByCardId(cardId);
    return ResponseEntity.ok(blockMapper.toDtoList(blocks));
  }

  @GetMapping("/status")
  public ResponseEntity<Map<String, Boolean>> isCardBlocked(@PathVariable Long cardId) {
    boolean blocked = blockService.isCardBlocked(cardId);
    return ResponseEntity.ok(Map.of("blocked", blocked));
  }

  @PostMapping
  public ResponseEntity<BlockDTO> blockCard(
      @PathVariable Long cardId, @Valid @RequestBody BlockCardRequest request) {

    Block block = blockService.blockCard(cardId, request.getReason());
    return new ResponseEntity<>(blockMapper.toDto(block), HttpStatus.CREATED);
  }

  @PostMapping("/unblock")
  public ResponseEntity<BlockDTO> unblockCard(
      @PathVariable Long cardId, @Valid @RequestBody UnblockCardRequest request) {

    Block block = blockService.unblockCard(cardId, request.getReason());
    return ResponseEntity.ok(blockMapper.toDto(block));
  }
}
