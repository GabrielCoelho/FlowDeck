package br.com.devcoelho.taskboard.exception;

import br.com.devcoelho.taskboard.model.BoardColumnKind;

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
