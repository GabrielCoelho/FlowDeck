package br.com.devcoelho.taskboard.exception;

/**
 * Base exception class for all custom exceptions in the FlowDeck application. Extends
 * RuntimeException to provide unchecked exception behavior, allowing exceptions to propagate
 * without explicit handling.
 *
 * <p>This class serves as the root of the exception hierarchy for the application, enabling
 * consistent handling of all FlowDeck-specific exceptions.
 *
 * @author Gabriel Coelho Soares
 * @version 1.0
 * @since 1.0
 */
public class FlowDeckException extends RuntimeException {

  public FlowDeckException() {
    super();
  }

  public FlowDeckException(String message) {
    super(message);
  }

  public FlowDeckException(String message, Throwable cause) {
    super(message, cause);
  }

  public FlowDeckException(Throwable cause) {
    super(cause);
  }
}
