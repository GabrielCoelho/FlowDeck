package br.com.devcoelho.taskboard.dao.impl;

import br.com.devcoelho.taskboard.dao.BoardColumnDAO;
import br.com.devcoelho.taskboard.model.BoardColumn;
import br.com.devcoelho.taskboard.repository.BoardColumnRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementação da interface BoardColumnDAO. Utiliza EntityManager do JPA e o BoardColumnRepository
 * para fornecer funcionalidades adicionais de acesso a dados para a entidade BoardColumn.
 */
@Repository
@RequiredArgsConstructor
public class BoardColumnDAOImpl implements BoardColumnDAO {

  @PersistenceContext private EntityManager entityManager;

  private final BoardColumnRepository boardColumnRepository;

  @Override
  public List<BoardColumn> findEmptyColumns(Long boardId) {
    String jpql =
        """
        SELECT DISTINCT bc FROM BoardColumn bc
        LEFT JOIN bc.cards c
        WHERE bc.board.id = :boardId
        GROUP BY bc.id
        HAVING COUNT(c.id) = 0
        ORDER BY bc.order
        """;

    return entityManager
        .createQuery(jpql, BoardColumn.class)
        .setParameter("boardId", boardId)
        .getResultList();
  }

  @Override
  public Optional<BoardColumn> findMostPopulatedColumn(Long boardId) {
    String jpql =
        """
        SELECT bc FROM BoardColumn bc
        LEFT JOIN bc.cards c
        WHERE bc.board.id = :boardId
        GROUP BY bc.id
        ORDER BY COUNT(c.id) DESC
        """;

    TypedQuery<BoardColumn> query =
        entityManager
            .createQuery(jpql, BoardColumn.class)
            .setParameter("boardId", boardId)
            .setMaxResults(1);

    try {
      return Optional.of(query.getSingleResult());
    } catch (jakarta.persistence.NoResultException e) {
      return Optional.empty();
    }
  }

  @Override
  public List<BoardColumn> findColumnsWithBlockedCards(Long boardId) {
    String jpql =
        """
        SELECT DISTINCT bc FROM BoardColumn bc
        JOIN bc.cards c
        JOIN c.blocks b
        WHERE bc.board.id = :boardId
        AND b.unblockedAt IS NULL
        ORDER BY bc.order
        """;

    return entityManager
        .createQuery(jpql, BoardColumn.class)
        .setParameter("boardId", boardId)
        .getResultList();
  }

  @Override
  @Transactional
  public List<BoardColumn> normalizeColumnOrders(Long boardId) {
    // Primeiro, buscamos todas as colunas ordenadas
    List<BoardColumn> columns = boardColumnRepository.findByBoardIdOrderByOrder(boardId);

    // Atualizamos as ordens para serem sequenciais
    for (int i = 0; i < columns.size(); i++) {
      BoardColumn column = columns.get(i);
      // Só atualizamos se a ordem for diferente
      if (column.getOrder() != i + 1) {
        column.setOrder(i + 1);
        boardColumnRepository.save(column);
      }
    }

    // Retornamos as colunas atualizadas
    return boardColumnRepository.findByBoardIdOrderByOrder(boardId);
  }
}
