package il.org.spartan.utils;

import java.util.*;
import java.util.stream.*;

import org.jetbrains.annotations.*;

public interface Nested<@¢ T> extends Streamer<T> {
  interface Root<@¢ T> extends Nested<T>, Streamer.Atomic<T> {
    //
  }

  @Override @NotNull default Compounder<T> compounder() {
    return (self, others) -> {
      Stream<T> $ = Stream.empty();
      for (final Streamer<T> ¢ : others)
        $ = Stream.concat(¢.stream(), streamSelf());
      return $;
    };
  }

  interface Compound<@¢ T> extends Nested<T>, Streamer.Compound<T> {
    @NotNull Nested<T> parent();

    @Override @NotNull default Iterable<Streamer<T>> next() {
      return Arrays.asList(parent());
    }
  }
}
