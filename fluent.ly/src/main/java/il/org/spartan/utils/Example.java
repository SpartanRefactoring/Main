package il.org.spartan.utils;

import java.util.function.*;

import il.org.spartan.utils.Example;

public interface Example {
  interface Converts extends Example {
    String from();

    String to();
  }

  @FunctionalInterface interface Ignores extends Example, Supplier<String> {
    /**/ }
}