package br.com.devcoelho.taskboard.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Represents a card (task) in the taskboard system. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {

  private Long id;
  private String title;
  private String description;

  @Builder.Default private BoardColumn boardColumn = new BoardColumn();

  @Builder.Default private List<Block> blocks = new ArrayList<>();

  @Builder.Default private OffsetDateTime createdAt = OffsetDateTime.now();

  private OffsetDateTime updatedAt;

  /**
   * Checks if the card is currently blocked.
   *
   * @return true if the card has an active block, false otherwise
   */
  public boolean isBlocked() {
    return blocks.stream().anyMatch(block -> block.getUnblockedAt() == null);
  }

  /**
   * Gets the current block if the card is blocked.
   *
   * @return the current block or null if not blocked
   */
  public Block getCurrentBlock() {
    return blocks.stream().filter(block -> block.getUnblockedAt() == null).findFirst().orElse(null);
  }

  /**
   * Gets the number of times this card has been blocked.
   *
   * @return the number of blocks
   */
  public int getBlocksCount() {
    return blocks.size();
  }
}
