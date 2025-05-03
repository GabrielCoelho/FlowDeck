package br.com.devcoelho.taskboard.dao.impl;

import br.com.devcoelho.taskboard.dao.BlockDAO;
import br.com.devcoelho.taskboard.model.Block;
import br.com.devcoelho.taskboard.repository.BlockRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * Implementação da interface BlockDAO. Utiliza EntityManager do JPA e o BlockRepository para
 * fornecer funcionalidades adicionais de acesso a dados para a entidade Block.
 */
@Repository
@RequiredArgsConstructor
public class BlockDAOImpl implements BlockDAO {

  @PersistenceContext private EntityManager entityManager;

  private final BlockRepository blockRepository;

  @Override
  public List<Block> findByDateRange(
      OffsetDateTime startDate, OffsetDateTime endDate, boolean activeOnly) {

    StringBuilder jpql =
        new StringBuilder(
            """
            SELECT b FROM Block b
            WHERE b.blockedAt BETWEEN :startDate AND :endDate
            """);

    if (activeOnly) {
      jpql.append(" AND b.unblockedAt IS NULL");
    }

    TypedQuery<Block> query = entityManager.createQuery(jpql.toString(), Block.class);
    query.setParameter("startDate", startDate);
    query.setParameter("endDate", endDate);

    return query.getResultList();
  }

  @Override
  public Map<String, Long> findMostCommonBlockReasons(int limit) {
    String jpql =
        """
        SELECT b.blockReason as reason, COUNT(b.id) as count
        FROM Block b
        GROUP BY b.blockReason
        ORDER BY COUNT(b.id) DESC
        """;

    List<Tuple> results =
        entityManager.createQuery(jpql, Tuple.class).setMaxResults(limit).getResultList();

    Map<String, Long> reasonCounts = new HashMap<>();
    for (Tuple tuple : results) {
      String reason = tuple.get("reason", String.class);
      Long count = tuple.get("count", Long.class);
      reasonCounts.put(reason, count);
    }

    return reasonCounts;
  }

  @Override
  public Map<Long, Double> calculateAverageBlockDurationByBoard() {
    String jpql =
        """
        SELECT c.boardColumn.board.id as boardId,
               AVG(FUNCTION('TIMESTAMPDIFF', HOUR, b.blockedAt,
                  CASE WHEN b.unblockedAt IS NULL THEN CURRENT_TIMESTAMP ELSE b.unblockedAt END)) as avgDuration
        FROM Block b
        JOIN b.card c
        WHERE b.unblockedAt IS NOT NULL
        GROUP BY c.boardColumn.board.id
        """;

    List<Tuple> results = entityManager.createQuery(jpql, Tuple.class).getResultList();

    Map<Long, Double> averageDurations = new HashMap<>();
    for (Tuple tuple : results) {
      Long boardId = tuple.get("boardId", Long.class);
      Double avgDuration = tuple.get("avgDuration", Double.class);
      averageDurations.put(boardId, avgDuration);
    }

    return averageDurations;
  }

  @Override
  public List<Block> findLongDurationBlocks(int hours) {
    OffsetDateTime threshold = OffsetDateTime.now().minus(hours, ChronoUnit.HOURS);

    String jpql =
        """
        SELECT b FROM Block b
        WHERE (b.unblockedAt IS NULL AND b.blockedAt < :threshold)
           OR (b.unblockedAt IS NOT NULL AND
               FUNCTION('TIMESTAMPDIFF', HOUR, b.blockedAt, b.unblockedAt) >= :hours)
        ORDER BY
            CASE WHEN b.unblockedAt IS NULL
                 THEN FUNCTION('TIMESTAMPDIFF', HOUR, b.blockedAt, CURRENT_TIMESTAMP)
                 ELSE FUNCTION('TIMESTAMPDIFF', HOUR, b.blockedAt, b.unblockedAt)
            END DESC
        """;

    return entityManager
        .createQuery(jpql, Block.class)
        .setParameter("threshold", threshold)
        .setParameter("hours", hours)
        .getResultList();
  }
}
