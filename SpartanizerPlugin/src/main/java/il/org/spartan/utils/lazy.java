package il.org.spartan.utils;

import java.util.function.*;

public interface lazy<T> {
  T get();

  static <T> lazy<T> get(final Supplier<T> ¢) {
    return new lazy<T>() {
      T $;

      @Override public T get() {
        return $ = $ != null ? $ : ¢.get();
      }
    };
  }
}
