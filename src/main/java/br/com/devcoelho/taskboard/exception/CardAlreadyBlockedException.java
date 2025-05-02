package br.com.devcoelho.taskboard.exception;

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
