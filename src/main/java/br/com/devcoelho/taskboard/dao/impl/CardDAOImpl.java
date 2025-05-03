package br.com.devcoelho.taskboard.dao.impl;

import br.com.devcoelho.taskboard.dao.CardDAO;
import br.com.devcoelho.taskboard.model.Card;
import br.com.devcoelho.taskboard.repository.CardRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * Implementação da interface CardDAO. Utiliza EntityManager do JPA e o CardRepository para fornecer
 * funcionalidades adicionais de acesso a dados para a entidade Card.
 */
@Repository
@RequiredArgsConstructor
public class CardDAOImpl implements CardDAO {

  @PersistenceContext private EntityManager entityManager;

  private final CardRepository cardRepository;

  @Override
  public List<Card> findByDateRangeAndBlockStatus(
      OffsetDateTime startDate, OffsetDateTime endDate, boolean blocked) {

    String jpql =
        """
        SELECT DISTINCT c FROM Card c
        LEFT JOIN FETCH c.blocks b
        WHERE c.createdAt BETWEEN :startDate AND :endDate
        AND ((:blocked = true AND EXISTS (SELECT b FROM Block b WHERE b.card = c AND b.unblockedAt IS NULL))
            OR (:blocked = false AND NOT EXISTS (SELECT b FROM Block b WHERE b.card = c AND b.unblockedAt IS NULL)))
        """;

    TypedQuery<Card> query = entityManager.createQuery(jpql, Card.class);
    query.setParameter("startDate", startDate);
    query.setParameter("endDate", endDate);
    query.setParameter("blocked", blocked);

    return query.getResultList();
  }

  @Override
  public Map<String, Integer> getCardStatsByColumn(Long boardId) {
    String jpql =
        """
        SELECT col.name as columnName, COUNT(card.id) as cardCount
        FROM Card card
        JOIN card.boardColumn col
        JOIN col.board b
        WHERE b.id = :boardId
        GROUP BY col.name
        ORDER BY col.order
        """;

    List<Tuple> results =
        entityManager
            .createQuery(jpql, Tuple.class)
            .setParameter("boardId", boardId)
            .getResultList();

    Map<String, Integer> statsByColumn = new HashMap<>();
    for (Tuple tuple : results) {
      String columnName = tuple.get("columnName", String.class);
      Integer cardCount = ((Long) tuple.get("cardCount")).intValue();
      statsByColumn.put(columnName, cardCount);
    }

    return statsByColumn;
  }

  @Override
  public List<Card> findMostBlockedCards(int limit) {
    String jpql =
        """
        SELECT c FROM Card c
        LEFT JOIN FETCH c.blocks b
        GROUP BY c.id
        ORDER BY COUNT(b.id) DESC
        """;

    return entityManager.createQuery(jpql, Card.class).setMaxResults(limit).getResultList();
  }

  @Override
  public List<Card> findOldestCardsInColumn(Long columnId, int limit) {
    String jpql =
        """
        SELECT c FROM Card c
        WHERE c.boardColumn.id = :columnId
        ORDER BY
            CASE WHEN c.updatedAt IS NULL THEN c.createdAt ELSE c.updatedAt END ASC
        """;

    return entityManager
        .createQuery(jpql, Card.class)
        .setParameter("columnId", columnId)
        .setMaxResults(limit)
        .getResultList();
  }
}
