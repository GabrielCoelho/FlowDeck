package br.com.devcoelho.taskboard.dto;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO {
  private Long id;
  private String title;
  private String description;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
  private Long boardColumnId; // Referência à BoardColumn pai
  private String boardColumnName; // Para conveniência da UI

  @Builder.Default private List<BlockDTO> blocks = new ArrayList<>();

  // Campos calculados
  private boolean blocked; // Indica se o card está atualmente bloqueado
}
