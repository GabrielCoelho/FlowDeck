package br.com.devcoelho.taskboard.service;

import br.com.devcoelho.taskboard.exception.BlockedCardException;
import br.com.devcoelho.taskboard.exception.ResourceNotFoundException;
import br.com.devcoelho.taskboard.model.Board;
import br.com.devcoelho.taskboard.model.BoardColumn;
import br.com.devcoelho.taskboard.model.Card;
import br.com.devcoelho.taskboard.repository.BoardColumnRepository;
import br.com.devcoelho.taskboard.repository.CardRepository;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardService {

  private final CardRepository cardRepository;
  private final BoardColumnRepository boardColumnRepository;
  private final BlockService blockService;

  /** Busca todos os cards de uma coluna */
  public List<Card> findByBoardColumnId(Long boardColumnId) {
    return cardRepository.findByBoardColumnId(boardColumnId);
  }

  /** Busca todos os cards de um board */
  public List<Card> findByBoardId(Long boardId) {
    return cardRepository.findByBoardId(boardId);
  }

  /** Busca um card pelo ID */
  public Card findById(Long id) {
    return cardRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Card", id));
  }

  /** Cria um novo card na coluna inicial do board */
  @Transactional
  public Card create(Long boardId, Card card) {
    // Busca o board e a coluna inicial
    Board board =
        boardColumnRepository
            .findById(boardId)
            .orElseThrow(() -> new ResourceNotFoundException("Board", boardId))
            .getBoard();

    BoardColumn initialColumn = board.getInitialColumn();

    // Define a coluna inicial e data de criação
    card.setBoardColumn(initialColumn);
    card.setCreatedAt(OffsetDateTime.now());

    return cardRepository.save(card);
  }

  /** Atualiza um card existente */
  @Transactional
  public Card update(Long id, Card cardDetails) {
    Card card = findById(id);

    // Atualiza os campos permitidos
    card.setTitle(cardDetails.getTitle());
    card.setDescription(cardDetails.getDescription());
    card.setUpdatedAt(OffsetDateTime.now());

    return cardRepository.save(card);
  }

  /** Move um card para outra coluna */
  @Transactional
  public Card moveCard(Long cardId, Long targetColumnId) {
    Card card = findById(cardId);
    BoardColumn targetColumn =
        boardColumnRepository
            .findById(targetColumnId)
            .orElseThrow(() -> new ResourceNotFoundException("Target column", targetColumnId));

    // Verifica se o card está bloqueado
    if (card.isBlocked()) {
      throw new BlockedCardException(card.getId());
    }

    // Move o card para a nova coluna
    card.setBoardColumn(targetColumn);
    card.setUpdatedAt(OffsetDateTime.now());

    return cardRepository.save(card);
  }

  /** Remove um card */
  @Transactional
  public void delete(Long id) {
    cardRepository.deleteById(id);
  }

  /** Cancela um card (move para a coluna de cancelamento) */
  @Transactional
  public Card cancelCard(Long cardId) {
    Card card = findById(cardId);
    BoardColumn cancelColumn = card.getBoardColumn().getBoard().getCancelColumn();

    // Desbloqueia o card se estiver bloqueado
    if (card.isBlocked()) {
      blockService.unblockCard(cardId, "Card canceled");
    }

    // Move o card para a coluna de cancelamento
    card.setBoardColumn(cancelColumn);
    card.setUpdatedAt(OffsetDateTime.now());

    return cardRepository.save(card);
  }
}
