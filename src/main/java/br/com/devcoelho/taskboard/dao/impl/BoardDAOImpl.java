package br.com.devcoelho.taskboard.dao.impl;

import br.com.devcoelho.taskboard.dao.BoardDAO;
import br.com.devcoelho.taskboard.model.Board;
import br.com.devcoelho.taskboard.repository.BoardRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * Implementação da interface BoardDAO. Utiliza EntityManager do JPA e o BoardRepository para
 * fornecer funcionalidades adicionais de acesso a dados para a entidade Board.
 */
@Repository
@RequiredArgsConstructor
public class BoardDAOImpl implements BoardDAO {

  @PersistenceContext private EntityManager entityManager;

  private final BoardRepository boardRepository;

  @Override
  public List<Board> findMostActiveBoards(int limit) {
    String jpql =
        """
        SELECT b FROM Board b
        LEFT JOIN b.columns c
        LEFT JOIN c.cards card
        GROUP BY b.id
        ORDER BY COUNT(card.id) DESC
        """;

    TypedQuery<Board> query = entityManager.createQuery(jpql, Board.class);
    query.setMaxResults(limit);
    return query.getResultList();
  }

  @Override
  public List<Board> findByNameAndMinColumns(String name, int minColumns) {
    String jpql =
        """
        SELECT DISTINCT b FROM Board b
        JOIN b.columns c
        WHERE LOWER(b.name) LIKE LOWER(:name)
        GROUP BY b.id
        HAVING COUNT(c.id) >= :minColumns
        """;

    TypedQuery<Board> query = entityManager.createQuery(jpql, Board.class);
    query.setParameter("name", "%" + name + "%");
    query.setParameter("minColumns", minColumns);
    return query.getResultList();
  }

  @Override
  public Optional<Board> findByIdWithAllRelations(Long id) {
    String jpql =
        """
        SELECT DISTINCT b FROM Board b
        LEFT JOIN FETCH b.columns c
        LEFT JOIN FETCH c.cards
        WHERE b.id = :id
        """;

    TypedQuery<Board> query = entityManager.createQuery(jpql, Board.class);
    query.setParameter("id", id);

    try {
      return Optional.of(query.getSingleResult());
    } catch (jakarta.persistence.NoResultException e) {
      return Optional.empty();
    }
  }

  @Override
  public int countTotalCards(Long boardId) {
    String jpql =
        """
        SELECT COUNT(card) FROM Card card
        JOIN card.boardColumn c
        JOIN c.board b
        WHERE b.id = :boardId
        """;

    TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
    query.setParameter("boardId", boardId);
    return query.getSingleResult().intValue();
  }
}
