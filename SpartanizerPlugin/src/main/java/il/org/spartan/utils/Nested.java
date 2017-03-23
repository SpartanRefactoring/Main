package il.org.spartan.utils;

import java.util.stream.*;

import org.jetbrains.annotations.*;

public interface Nested<@¢ T> {
  default Stream<T> ancestors() {
    return Stream.empty();
  }

  @Nullable default T self() {
    return null;
  }

  @NotNull default Stream<T> streamSelf() {
    return self() == null ? Stream.empty() : Stream.of(self());
  }

  interface Root<@¢ T> extends Nested<T> {
    @Override @NotNull default Stream<T> ancestors() {
      return streamSelf();
    }
  }

  interface Compound<@¢ T> extends Nested<T> {
    @NotNull Nested<T> parent();

    @Override default Stream<T> ancestors() {
      return Stream.concat(parent().ancestors(), streamSelf());
    }
  }
}
