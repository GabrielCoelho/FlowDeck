package br.com.devcoelho.taskboard.dto;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockDTO {
  private Long id;
  private OffsetDateTime blockedAt;
  private String blockReason;
  private OffsetDateTime unblockedAt;
  private String unblockReason;
  private Long cardId; // Referência ao Card pai
}
