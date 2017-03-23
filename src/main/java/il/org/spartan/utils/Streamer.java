package il.org.spartan.utils;

import java.util.stream.*;

import org.jetbrains.annotations.*;

public interface Streamer<@¢ T> {
  @Nullable default T self() {
    return null;
  }

  @NotNull default Compounder<T> compounder() {
    return Compounder.empty();
  }

  @NotNull default Stream<T> streamSelf() {
    return self() == null ? Stream.empty() : Stream.of(self());
  }

  default Stream<T> stream() {
    return Stream.empty();
  }

  interface Atomic<@¢ T> extends Streamer<T> {
    @Override @NotNull default Stream<T> stream() {
      return streamSelf();
    }
  }

  interface Compound<@¢ T> extends Streamer<T> {
    @NotNull Iterable<? extends Streamer<T>> next();

    @Override default Stream<T> stream() {
      return compounder().compound(self(), next());
    }
  }

  @FunctionalInterface
  interface Compounder<T> {
    @NotNull Stream<T> compound(T self, Iterable<? extends Streamer<T>> others);

    @NotNull static <T> Compounder<T> empty() {
      return (self, others) -> Stream.empty();
    }
  }
}
