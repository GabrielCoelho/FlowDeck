package br.com.devcoelho.taskboard.dao;

import br.com.devcoelho.taskboard.model.BoardColumn;
import java.util.List;
import java.util.Optional;

/**
 * Interface DAO para operações de acesso a dados relacionadas à entidade BoardColumn. Fornece
 * métodos específicos não cobertos pela interface JpaRepository.
 */
public interface BoardColumnDAO {

  /**
   * Busca colunas vazias (sem cards) de um determinado board.
   *
   * @param boardId identificador do board
   * @return lista de colunas sem cards
   */
  List<BoardColumn> findEmptyColumns(Long boardId);

  /**
   * Encontra a coluna mais populosa (com mais cards) de um board.
   *
   * @param boardId identificador do board
   * @return a coluna com mais cards, se existir
   */
  Optional<BoardColumn> findMostPopulatedColumn(Long boardId);

  /**
   * Encontra colunas com cards bloqueados em um board.
   *
   * @param boardId identificador do board
   * @return lista de colunas que contêm cards bloqueados
   */
  List<BoardColumn> findColumnsWithBlockedCards(Long boardId);

  /**
   * Reorganiza as ordens das colunas em um board para eliminar lacunas. Por exemplo, se as ordens
   * são 1, 3, 6, serão reorganizadas para 1, 2, 3.
   *
   * @param boardId identificador do board
   * @return lista de colunas com ordens atualizadas
   */
  List<BoardColumn> normalizeColumnOrders(Long boardId);
}
