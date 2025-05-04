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
public class CardSummaryDTO {
  private Long id;
  private String title;
  private OffsetDateTime createdAt;
  private boolean blocked;
  private String columnName;
}
