package br.com.devcoelho.taskboard.exception;

public class ColumnContainsCardException extends FlowDeckException {

  private Object columnId;

  public Object getColumnId() {
    return columnId;
  }

  public ColumnContainsCardException(Object columnId) {
    super(formatMessage(columnId));
    this.columnId = columnId;
  }

  private static String formatMessage(Object columnId) {
    return "Couldn't delete column " + columnId + " because it contains cards.";
  }
}
