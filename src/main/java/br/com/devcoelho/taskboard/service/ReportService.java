package br.com.devcoelho.taskboard.service;

import br.com.devcoelho.taskboard.dao.BlockDAO;
import br.com.devcoelho.taskboard.dao.BoardDAO;
import br.com.devcoelho.taskboard.dao.CardDAO;
import br.com.devcoelho.taskboard.model.Block;
import br.com.devcoelho.taskboard.model.Board;
import br.com.devcoelho.taskboard.model.BoardColumn;
import br.com.devcoelho.taskboard.model.Card;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Serviço para geração de relatórios e análises do sistema FlowDeck. Combina dados de diferentes
 * DAOs para fornecer insights sobre o funcionamento do sistema.
 */
@Service
@RequiredArgsConstructor
public class ReportService {

  private final BoardDAO boardDAO;
  private final CardDAO cardDAO;
  private final BlockDAO blockDAO;

  /**
   * Gera um relatório de atividade do sistema em um determinado período.
   *
   * @param startDate data inicial do período
   * @param endDate data final do período
   * @return mapa com estatísticas do período
   */
  public Map<String, Object> generateActivityReport(
      OffsetDateTime startDate, OffsetDateTime endDate) {
    Map<String, Object> report = new HashMap<>();

    // Estatísticas de cards
    List<Card> blockedCards = cardDAO.findByDateRangeAndBlockStatus(startDate, endDate, true);
    List<Card> activeCards = cardDAO.findByDateRangeAndBlockStatus(startDate, endDate, false);

    // Estatísticas de bloqueios
    List<Block> blocks = blockDAO.findByDateRange(startDate, endDate, false);
    Map<String, Long> blockReasons = blockDAO.findMostCommonBlockReasons(5);

    // Boards mais ativos
    List<Board> activeBoards = boardDAO.findMostActiveBoards(5);

    // Compilação do relatório
    report.put("period", Map.of("start", startDate, "end", endDate));
    report.put("cardsCreated", blockedCards.size() + activeCards.size());
    report.put("blockedCards", blockedCards.size());
    report.put("activeCards", activeCards.size());
    report.put("totalBlocks", blocks.size());
    report.put("commonBlockReasons", blockReasons);
    report.put("activeBoards", activeBoards);

    return report;
  }

  /**
   * Gera um relatório de eficiência de workflow por board.
   *
   * @param boardId identificador do board
   * @return mapa com métricas de eficiência
   */
  public Map<String, Object> generateWorkflowEfficiencyReport(Long boardId) {
    Map<String, Object> report = new HashMap<>();

    // Estatísticas de cards por coluna
    Map<String, Integer> cardStats = cardDAO.getCardStatsByColumn(boardId);

    // Colunas com cards bloqueados
    List<String> columnsWithBlockedCards =
        boardDAO.findByIdWithAllRelations(boardId).get().getColumns().stream()
            .filter(column -> column.getCards().stream().anyMatch(Card::isBlocked))
            .map(BoardColumn::getName)
            .toList();

    // Duração média de bloqueios
    Map<Long, Double> blockDurations = blockDAO.calculateAverageBlockDurationByBoard();
    Double avgBlockDuration = blockDurations.getOrDefault(boardId, 0.0);

    // Compilação do relatório
    report.put("boardId", boardId);
    report.put("cardDistribution", cardStats);
    report.put("columnsWithBlockedCards", columnsWithBlockedCards);
    report.put("averageBlockDuration", avgBlockDuration);
    report.put("totalCards", boardDAO.countTotalCards(boardId));

    return report;
  }
}
