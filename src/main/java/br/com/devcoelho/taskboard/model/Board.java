package br.com.devcoelho.taskboard.model;

import static br.com.devcoelho.taskboard.model.BoardColumnKind.CANCEL;
import static br.com.devcoelho.taskboard.model.BoardColumnKind.INITIAL;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "board")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @Builder.Default
  @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
  @JsonManagedReference("board-columns")
  private List<BoardColumn> columns = new ArrayList<>();

  /**
   * Gets the initial column of the board.
   *
   * @return the initial column
   * @throws IllegalStateException if no initial column is found
   */
  public BoardColumn getInitialColumn() {
    return getFilteredColumn(bc -> bc.getKind().equals(INITIAL));
  }

  /**
   * Gets the cancel column of the board.
   *
   * @return the cancel column
   * @throws IllegalStateException if no cancel column is found
   */
  public BoardColumn getCancelColumn() {
    return getFilteredColumn(bc -> bc.getKind().equals(CANCEL));
  }

  /**
   * Gets a column based on the provided filter.
   *
   * @param filter predicate to filter columns
   * @return the first column that matches the filter
   * @throws IllegalStateException if no column matches the filter
   */
  private BoardColumn getFilteredColumn(Predicate<BoardColumn> filter) {
    return columns.stream()
        .filter(filter)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("No matching column found"));
  }
}
