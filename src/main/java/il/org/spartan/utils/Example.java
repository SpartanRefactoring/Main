package il.org.spartan.utils;

import java.util.function.*;

public interface Example {
  interface Converts extends Example {
    String from();

    String to();
  }

  /** Auxiliary class for FAPI
   * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
   * @since 2017-03-07 */
  interface Converter {
    Converts to(String to);
  }

  static Converter convert(final String from) {
    return to -> new Converts() {
      @Override public String from() {
        return from;
      }

      @Override public String to() {
        return to;
      }
    };
  }

  @FunctionalInterface
  interface Ignores extends Example, Supplier<String> { /**/ }

  static Ignores ignores(final String code) {
    return () -> code;
  }
}