package br.com.devcoelho.taskboard.model;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a block (impediment) for a card in the taskboard system. A block prevents a card from
 * being moved until it's unblocked.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Block {

  private Long id;
  private OffsetDateTime blockedAt;
  private String blockReason;
  private OffsetDateTime unblockedAt;
  private String unblockReason;
  private Card card;
}
