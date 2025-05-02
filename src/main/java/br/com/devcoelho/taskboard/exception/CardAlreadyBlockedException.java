package br.com.devcoelho.taskboard.exception;

/**
 * Exception thrown when an attempt is made to block a card that is already blocked.
 *
 * <p>This exception is typically thrown by the BlockService when the blockCard method is called for
 * a card that already has an active block. A card can only have one active block at a time.
 *
 * <p>The HTTP status code associated with this exception is 409 Conflict, indicating that the
 * request conflicts with the current state of the resource.
 *
 * @author Gabriel Coelho Soares
 * @version 1.0
 * @since 1.0
 * @see br.com.devcoelho.taskboard.service.BlockService#blockCard(Long, String)
 */
public class CardAlreadyBlockedException extends FlowDeckException {

  private Object cardId;

  public Object getCardId() {
    return cardId;
  }

  public CardAlreadyBlockedException(Object cardId) {
    super(formatMessage(cardId));
  }

  private static String formatMessage(Object cardId) {
    return "The card (ID: " + cardId + ") is already blocked. Operation can't be done";
  }
}
