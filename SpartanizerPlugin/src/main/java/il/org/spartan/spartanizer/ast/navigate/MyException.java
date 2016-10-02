package il.org.spartan.spartanizer.ast.navigate;

/** @author Ori Marcovitch
 * @since 2016 */
public class MyException extends Exception {
  /**
   *
   */
  public MyException() {
  }

  /** @param message */
  public MyException(final String message) {
    super(message);
    // TODO Auto-generated constructor stub
  }

  /** @param message
   * @param cause */
  public MyException(final String message, final Throwable cause) {
    super(message, cause);
    // TODO Auto-generated constructor stub
  }

  /** @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace */
  public MyException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    // TODO Auto-generated constructor stub
  }

  /** @param cause */
  public MyException(final Throwable cause) {
    super(cause);
    // TODO Auto-generated constructor stub
  }
}
