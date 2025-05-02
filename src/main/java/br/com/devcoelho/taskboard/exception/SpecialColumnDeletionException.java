package br.com.devcoelho.taskboard.exception;

import br.com.devcoelho.taskboard.model.BoardColumnKind;

/**
 * Exception thrown when an attempt is made to delete a special column from a board.
 *
 * <p>This exception is typically thrown by the BoardColumnService when trying to delete a column
 * with a special kind (INITIAL, FINAL, CANCEL). Special columns are required for the proper
 * functioning of the board workflow and cannot be deleted.
 *
 * <p>The HTTP status code associated with this exception is 400 Bad Request.
 *
 * @author Gabriel Coelho Soares
 * @version 1.0
 * @since 1.0
 * @see br.com.devcoelho.taskboard.model.BoardColumnKind
 * @see br.com.devcoelho.taskboard.service.BoardColumnService#delete(Long)
 */
public class SpecialColumnDeletionException extends FlowDeckException {

  private BoardColumnKind columnKind;
  private Object columnId;

  public BoardColumnKind getColumnKind() {
    return columnKind;
  }

  public Object getColumnId() {
    return columnId;
  }

  public SpecialColumnDeletionException(BoardColumnKind columnKind, Object columnId) {
    super(formatMessage(columnKind, columnId));
    this.columnKind = columnKind;
    this.columnId = columnId;
  }

  private static String formatMessage(BoardColumnKind columnKind, Object columnId) {
    return "Couldn't delete the column with "
        + columnKind
        + " kind on "
        + columnId
        + " because it's from a special kind";
  }
}
