package br.com.devcoelho.taskboard.model;

import java.util.stream.Stream;

/** Represents the different kinds of columns that can exist in a board. */
public enum BoardColumnKind {
  /** Initial column where new cards are created */
  INITIAL,

  /** Final column that represents completed cards */
  FINAL,

  /** Column for canceled cards */
  CANCEL,

  /** Regular column for cards in progress */
  PENDING;

  /**
   * Finds a BoardColumnKind by its name.
   *
   * @param name the name of the kind
   * @return the BoardColumnKind
   * @throws IllegalArgumentException if no matching kind is found
   */
  public static BoardColumnKind findByName(final String name) {
    return Stream.of(BoardColumnKind.values())
        .filter(kind -> kind.name().equals(name))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid column kind: " + name));
  }
}
