package il.org.spartan.utils;

import java.util.*;
import java.util.stream.*;

import org.jetbrains.annotations.*;

public interface Nested<@¢ T> extends Bolt<T> {
  interface Root<@¢ T> extends Nested<T>, Bolt.Atomic<T> {
    //
  }

  @Override @NotNull default Compounder<T> compounder() {
    return (self, others) -> {
      Stream<T> $ = Stream.empty();
      for (@NotNull final Bolt<T> ¢ : others)
        $ = Stream.concat(¢.stream(), streamSelf());
      return $;
    };
  }

  interface Compound<@¢ T> extends Nested<T>, Bolt.Compound<T> {
    @NotNull Nested<T> parent();

    @Override @NotNull default Iterable<Bolt<T>> next() {
      return Arrays.asList(parent());
    }
  }
}
