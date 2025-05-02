package br.com.devcoelho.taskboard.exception;

/**
 * Exception thrown when an attempt is made to delete a board column that still contains cards.
 *
 * <p>This exception is typically thrown by the BoardColumnService when trying to delete a column
 * that is not empty. To maintain data integrity, columns must be emptied of all cards before
 * deletion.
 *
 * <p>The HTTP status code associated with this exception is 409 Conflict.
 *
 * @author Gabriel Coelho Soares
 * @version 1.0
 * @since 1.0
 * @see br.com.devcoelho.taskboard.service.BoardColumnService#delete(Long)
 */
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
