package br.com.devcoelho.taskboard.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "block")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Block {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private OffsetDateTime blockedAt;

  private String blockReason;

  private OffsetDateTime unblockedAt;

  private String unblockReason;

  @ManyToOne
  @JoinColumn(name = "card_id")
  @JsonBackReference("card-blocks")
  private Card card;
}
