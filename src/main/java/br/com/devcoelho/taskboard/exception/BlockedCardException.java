package br.com.devcoelho.taskboard.exception;

/**
 * Exception thrown when an operation is attempted on a card that is currently blocked.
 *
 * <p>This exception is typically thrown when trying to move a card between columns while it has an
 * active block. Cards with active blocks cannot be moved until they are unblocked.
 *
 * <p>The HTTP status code associated with this exception is 400 Bad Request.
 *
 * @author Gabriel Coelho Soares
 * @version 1.0
 * @since 1.0
 * @see br.com.devcoelho.taskboard.model.Card#isBlocked()
 * @see br.com.devcoelho.taskboard.service.CardService#moveCard(Long, Long)
 */
public class BlockedCardException extends FlowDeckException {

  private Object cardId;

  public Object getCardId() {
    return cardId;
  }

  public BlockedCardException(Object cardId) {
    super(formatMessage(cardId));
  }

  private static String formatMessage(Object cardId) {
    return "The card (ID: " + cardId + ") is blocked. Operation can't be done";
  }
}
