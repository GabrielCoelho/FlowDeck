package br.com.devcoelho.taskboard.repository;

import br.com.devcoelho.taskboard.model.Block;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {
  List<Block> findByCardId(Long cardId);

  // Busca o bloqueio ativo de um card (unblockedAt é null)
  @Query("SELECT b FROM Block b WHERE b.card.id = :cardId AND b.unblockedAt IS NULL")
  Optional<Block> findActiveBlockByCardId(Long cardId);
}
