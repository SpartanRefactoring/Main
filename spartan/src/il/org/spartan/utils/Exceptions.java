package il.org.spartan.utils;

import org.jetbrains.annotations.NotNull;

/** This class provides exception-related services.
 * @author Itay Maman, The Technion @since, Aug 24, 2007 */
public enum Exceptions {
  ;
  /** Translate any exception to an unchecked exception
   * @param t Exception to translate
   * @return An unchecked exception */
  @NotNull
  public static RuntimeException toRuntimeException(final Throwable t) {
    if (t instanceof RuntimeException)
      return (RuntimeException) t;
    final RuntimeException $ = new RuntimeException(t);
    $.setStackTrace(t.getStackTrace());
    return $;
  }
}
