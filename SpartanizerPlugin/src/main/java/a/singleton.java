package a;

import java.util.*;

/** Singleton collections.
 * @author Ori Roth
 * @since 2017-04-16 */
public interface singleton {
  /** Singleton list. */
  static <T> List<T> list(T ¢) {
    return Collections.singletonList(¢);
  }

  /** Singleton array. */
  static <T> Object[] array(T ¢) {
    return new Object[] { ¢ };
  }
}
