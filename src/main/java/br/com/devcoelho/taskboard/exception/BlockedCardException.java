package br.com.devcoelho.taskboard.exception;

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
