package il.org.spartan.utils;

import java.util.*;
import java.util.stream.*;

public interface Nested<@¢ T> extends Streamer<T> {
  interface Root<@¢ T> extends Nested<T>, Streamer.Atomic<T> {
    //
  }

  @Override default Compounder<T> compounder() {
    return (self, others) -> {
      Stream<T> $ = Stream.empty();
      for (final Streamer<T> ¢ : others) {
        $ = Stream.concat(¢.stream(), streamSelf());
      }
      return $;
    };
  }

  interface Compound<@¢ T> extends Nested<T>, Streamer.Compound<T> {
    Nested<T> parent();

    @Override default Iterable<Streamer<T>> next() {
      return Arrays.asList(parent());
    }
  }
}
