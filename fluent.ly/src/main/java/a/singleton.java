package a;

import java.util.List;

import fluent.ly.as;

/** Singleton collections.
 * @author Ori Roth
 * @since 2017-04-16 */
public interface singleton {
  /** Singleton list. */
  static <T> List<T> list(final T ¢) {
    return as.list(¢);
  }

  /** Singleton array. */
  static <T> T[] array(final T ¢) {
    return as.array(¢);
  }
}
