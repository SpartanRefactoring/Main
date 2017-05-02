package il.org.spartan.utils;

import org.jetbrains.annotations.*;

/** This class provides exception-related services.
 * @author Itay Maman, The Technion @since, Aug 24, 2007 */
public enum Exceptions {
  ;
  /** Translate any exception to an unchecked exception
   * @param ¢ Exception to translate
   * @return An unchecked exception */
  @NotNull public static RuntimeException toRuntimeException(final Throwable ¢) {
    if (¢ instanceof RuntimeException)
      return (RuntimeException) ¢;
    @NotNull final RuntimeException $ = new RuntimeException(¢);
    $.setStackTrace(¢.getStackTrace());
    return $;
  }
}
