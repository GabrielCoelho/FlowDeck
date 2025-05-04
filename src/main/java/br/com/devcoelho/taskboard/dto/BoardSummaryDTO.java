package br.com.devcoelho.taskboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardSummaryDTO {
  private Long id;
  private String name;
  private int columnCount;
  private int cardCount;
}
