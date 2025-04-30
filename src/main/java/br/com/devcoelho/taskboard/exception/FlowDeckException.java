package br.com.devcoelho.taskboard.exception;

/**
 * Classe base para todas as exceções específicas do sistema FlowDeck. Estende RuntimeException para
 * manter o comportamento de exceções não-verificadas.
 */
public class FlowDeckException extends RuntimeException {

  /** Construtor padrão. */
  public FlowDeckException() {
    super();
  }

  /**
   * Construtor com mensagem de erro.
   *
   * @param message a mensagem de erro
   */
  public FlowDeckException(String message) {
    super(message);
  }

  /**
   * Construtor com mensagem de erro e causa raiz.
   *
   * @param message a mensagem de erro
   * @param cause a causa raiz (exceção original)
   */
  public FlowDeckException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Construtor com causa raiz.
   *
   * @param cause a causa raiz (exceção original)
   */
  public FlowDeckException(Throwable cause) {
    super(cause);
  }
}
