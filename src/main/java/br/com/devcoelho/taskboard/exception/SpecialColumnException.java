package br.com.devcoelho.taskboard.exception;

import br.com.devcoelho.taskboard.model.BoardColumnKind;

/**
 * Exception thrown when an attempt is made to create a special column in a board that already has a
 * column of the same special type.
 *
 * <p>This exception is typically thrown by the BoardColumnService when trying to create a special
 * column (INITIAL, FINAL, CANCEL) in a board that already has a column of that type. Each board can
 * have only one column of each special type.
 *
 * <p>The HTTP status code associated with this exception is 400 Bad Request.
 *
 * @author Gabriel Coelho Soares
 * @version 1.0
 * @since 1.0
 * @see br.com.devcoelho.taskboard.model.BoardColumnKind
 * @see br.com.devcoelho.taskboard.service.BoardColumnService#create(Long, BoardColumn)
 */
public class SpecialColumnException extends FlowDeckException {

  private BoardColumnKind columnKind;
  private Object boardId;

  public BoardColumnKind getColumnKind() {
    return columnKind;
  }

  public Object getBoardId() {
    return boardId;
  }

  public SpecialColumnException(BoardColumnKind columnKind, Object boardId) {
    super(formatMessage(columnKind, boardId));
    this.columnKind = columnKind;
    this.boardId = boardId;
  }

  private static String formatMessage(BoardColumnKind columnKind, Object boardId) {
    return "Couldn't create a column with " + columnKind + " kind on " + boardId;
  }
}
