package br.com.devcoelho.taskboard.exception;

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
