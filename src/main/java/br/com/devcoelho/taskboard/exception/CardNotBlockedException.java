package br.com.devcoelho.taskboard.exception;

/**
 * Exception thrown when an attempt is made to unblock a card that is not currently blocked.
 *
 * <p>This exception is typically thrown by the BlockService when the unblockCard method is called
 * for a card that does not have an active block. Only cards with an active block can be unblocked.
 *
 * <p>The HTTP status code associated with this exception is 400 Bad Request.
 *
 * @author Gabriel Coelho Soares
 * @version 1.0
 * @since 1.0
 * @see br.com.devcoelho.taskboard.service.BlockService#unblockCard(Long, String)
 */
public class CardNotBlockedException extends FlowDeckException {

  private Object cardId;

  public Object getCardId() {
    return cardId;
  }

  public CardNotBlockedException(Object cardId) {
    super(formatMessage(cardId));
  }

  private static String formatMessage(Object cardId) {
    return "The card (ID: " + cardId + ") isn't blocked. Operation can't be done";
  }
}
