package br.com.devcoelho.taskboard.dao;

import br.com.devcoelho.taskboard.model.Card;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * Interface DAO para operações de acesso a dados relacionadas à entidade Card. Fornece métodos
 * específicos não cobertos pela interface JpaRepository.
 */
public interface CardDAO {

  /**
   * Busca cards por período e status de bloqueio.
   *
   * @param startDate data inicial do período
   * @param endDate data final do período
   * @param blocked status de bloqueio (true para bloqueados, false para desbloqueados)
   * @return lista de cards que atendem aos critérios
   */
  List<Card> findByDateRangeAndBlockStatus(
      OffsetDateTime startDate, OffsetDateTime endDate, boolean blocked);

  /**
   * Obtém estatísticas de cards por coluna em um board específico.
   *
   * @param boardId identificador do board
   * @return mapa com o nome da coluna como chave e quantidade de cards como valor
   */
  Map<String, Integer> getCardStatsByColumn(Long boardId);

  /**
   * Busca cards com histórico de bloqueios ordenados por quantidade de bloqueios.
   *
   * @param limit número máximo de cards a retornar
   * @return lista de cards ordenados pela quantidade de bloqueios
   */
  List<Card> findMostBlockedCards(int limit);

  /**
   * Busca cards que estão há mais tempo em uma determinada coluna.
   *
   * @param columnId identificador da coluna
   * @param limit número máximo de cards a retornar
   * @return lista de cards ordenados pelo tempo na coluna
   */
  List<Card> findOldestCardsInColumn(Long columnId, int limit);
}
