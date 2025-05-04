package br.com.devcoelho.taskboard.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "card")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private String description;

  @ManyToOne
  @JoinColumn(name = "board_column_id")
  @Builder.Default
  @JsonBackReference("column-cards")
  private BoardColumn boardColumn = new BoardColumn();

  @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
  @Builder.Default
  @JsonManagedReference("card-blocks")
  private List<Block> blocks = new ArrayList<>();

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
