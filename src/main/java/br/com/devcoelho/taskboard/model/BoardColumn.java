package br.com.devcoelho.taskboard.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "board_column")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardColumn {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Column(name = "column_order")
  private int order;

  @Enumerated(EnumType.STRING)
  private BoardColumnKind kind;

  @ManyToOne
  @JoinColumn(name = "board_id")
  @Builder.Default
  @JsonBackReference("board-columns")
  private Board board = new Board();

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @Builder.Default
  @OneToMany(mappedBy = "boardColumn", cascade = CascadeType.ALL)
  @JsonManagedReference("column-cards")
  private List<Card> cards = new ArrayList<>();
}
