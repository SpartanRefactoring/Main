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

  interface Demo {
    Consumer<String> c = λ -> {/**/};
    Consumer<Consumer<String>> cc = λ -> {/**/};
    Consumer<Consumer<Consumer<String>>> ccc = λ -> {/**/};
    Supplier<String> s = () -> "s";
    Supplier<Supplier<String>> ss = () -> () -> "ss";
    Supplier<Supplier<Supplier<String>>> sss = () -> () -> () -> "AS";
    Supplier<Consumer<String>> sc = () -> λ -> {/**/};
    Consumer<Supplier<String>> cs = λ -> {/**/};
  }

  // TODO Yossi: decide whether to move this to {@link Example} --or
  static Ignores ignores(final String code) {
    return () -> code;
  }
}