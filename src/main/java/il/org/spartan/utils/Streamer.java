package il.org.spartan.utils;

import java.util.stream.*;

public interface Streamer<@¢ T> {
  default T self() {
    return null;
  }

  default Compounder<T> compounder() {
    return Compounder.empty();
  }

  default Stream<T> streamSelf() {
    return self() == null ? Stream.empty() : Stream.of(self());
  }

  default Stream<T> stream() {
    return Stream.empty();
  }

  interface Atomic<@¢ T> extends Streamer<T> {
    @Override default Stream<T> stream() {
      return streamSelf();
    }
  }

  interface Compound<@¢ T> extends Streamer<T> {
    Stream<Streamer<T>> next();

    @Override default Stream<T> stream() {
      return compounder().compound(self(), next());
    }
  }

  @FunctionalInterface
  interface Compounder<T> {
    Stream<T> compound(T self, Stream<Streamer<T>> others);

    static <T> Compounder<T> empty() {
      return new Compounder<T>() {
        @SuppressWarnings("unused") @Override public Stream<T> compound(T self, Stream<Streamer<T>> others) {
          return Stream.empty();
        }
      };
    }
  }
}
