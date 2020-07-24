package il.org.spartan.utils;

import java.util.function.Supplier;

public interface Example {
  interface Converts extends Example {
    String from();

    String to();
  }

  @FunctionalInterface interface Ignores extends Example, Supplier<String> {
    /**/ }
}