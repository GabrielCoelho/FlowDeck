package br.com.devcoelho.taskboard.repository;

import br.com.devcoelho.taskboard.model.Card;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
  List<Card> findByBoardColumnId(Long boardColumnId);

  @Query("SELECT c FROM Card c WHERE c.boardColumn.board.id = :boardId")
  List<Card> findByBoardId(Long boardId);

  List<Card> findByCreatedAtBetween(OffsetDateTime start, OffsetDateTime end);
}
