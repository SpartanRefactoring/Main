package il.org.spartan.spartanizer.ast.navigate;

/** @author Yossi Gil
 * @since 2016 */
public class MyException extends Exception {
  private static final long serialVersionUID = 1L;

  /**
   *
   */
  public MyException() {
  }

  /** @param message */
  public MyException(final String message) {
    super(message);
    // TODO Yossi Auto-generated constructor stub
  }

  /** @param message
   * @param cause */
  public MyException(final String message, final Throwable cause) {
    super(message, cause);
    // TODO Yossi Auto-generated constructor stub
  }

  /** @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace */
  public MyException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    // TODO Yossi Auto-generated constructor stub
  }

  /** @param cause */
  public MyException(final Throwable cause) {
    super(cause);
    // TODO Yossi Auto-generated constructor stub
  }
}
