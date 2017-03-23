package il.org.spartan.utils;

import java.util.function.*;

import org.jetbrains.annotations.*;

public interface Example {
  interface Converts extends Example {
    @NotNull String from();

    String to();
  }

  /** Auxiliary class for FAPI
   * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
   * @since 2017-03-07 */
  interface Converter {
    @NotNull Converts to(String to);
  }

  static Converter convert(@NotNull final String from) {
    return to -> new Converts() {
      @Override @NotNull public String from() {
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