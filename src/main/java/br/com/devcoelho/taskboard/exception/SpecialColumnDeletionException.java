package br.com.devcoelho.taskboard.exception;

import br.com.devcoelho.taskboard.model.BoardColumnKind;

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
