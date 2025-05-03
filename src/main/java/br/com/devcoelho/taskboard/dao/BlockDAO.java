package br.com.devcoelho.taskboard.dao;

import br.com.devcoelho.taskboard.model.Block;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * Interface DAO para operações de acesso a dados relacionadas à entidade Block. Fornece métodos
 * específicos não cobertos pela interface JpaRepository.
 */
public interface BlockDAO {

  /**
   * Busca bloqueios por período.
   *
   * @param startDate data inicial
   * @param endDate data final
   * @param activeOnly indica se deve buscar apenas bloqueios ativos
   * @return lista de bloqueios no período especificado
   */
  List<Block> findByDateRange(OffsetDateTime startDate, OffsetDateTime endDate, boolean activeOnly);

  /**
   * Busca motivos mais comuns de bloqueio.
   *
   * @param limit número máximo de motivos a retornar
   * @return mapa com o motivo como chave e a contagem como valor
   */
  Map<String, Long> findMostCommonBlockReasons(int limit);

  /**
   * Calcula a duração média de bloqueios por board.
   *
   * @return mapa com o ID do board como chave e a duração média (em horas) como valor
   */
  Map<Long, Double> calculateAverageBlockDurationByBoard();

  /**
   * Busca bloqueios de longa duração (maiores que o parâmetro especificado).
   *
   * @param hours duração mínima em horas
   * @return lista de bloqueios de longa duração
   */
  List<Block> findLongDurationBlocks(int hours);
}
