package a;

import java.util.*;

import il.org.spartan.*;

/** Singleton collections.
 * @author Ori Roth
 * @since 2017-04-16 */
public interface singleton {
  /** Singleton list. */
  static <T> List<T> list(T ¢) {
    return as.list(¢);
  }

  /** Singleton array. */
  static <T> T[] array(T ¢) {
    return as.array(¢);
  }
}
