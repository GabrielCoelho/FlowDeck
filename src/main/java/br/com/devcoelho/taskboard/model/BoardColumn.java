package br.com.devcoelho.taskboard.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents a column in a board that contains cards. Columns represent different stages in a
 * workflow.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardColumn {

  private Long id;
  private String name;
  private int order;
  private BoardColumnKind kind;

  @Builder.Default private Board board = new Board();

  @ToString.Exclude @EqualsAndHashCode.Exclude @Builder.Default
  private List<Card> cards = new ArrayList<>();
}
