package il.org.spartan.utils;

import org.jetbrains.annotations.NotNull;

import java.util.function.*;

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
      @NotNull
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
}