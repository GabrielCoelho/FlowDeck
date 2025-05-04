package br.com.devcoelho.taskboard.dto;

import br.com.devcoelho.taskboard.model.BoardColumnKind;
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
public class BoardColumnDTO {
  private Long id;
  private String name;
  private int order;
  private BoardColumnKind kind;
  private Long boardId; // Referência ao Board pai

  @Builder.Default private List<CardDTO> cards = new ArrayList<>();

  // Campos adicionais para conveniência da UI
  private int cardCount; // Número de cards na coluna
}
