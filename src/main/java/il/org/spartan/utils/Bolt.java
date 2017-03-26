package il.org.spartan.utils;

import java.util.stream.*;

public interface Bolt<T> {
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

  interface Atomic<@¢ T> extends Bolt<T> {
    @Override  default Stream<T> stream() {
      return streamSelf();
    }
  }

  interface Compound<@¢ T> extends Bolt<T> {
     Iterable<? extends Bolt<T>> next();

    @Override default Stream<T> stream() {
      return compounder().compound(self(), next());
    }
  }

  @FunctionalInterface
  interface Compounder<T> {
     Stream<T> compound(T self, Iterable<? extends Bolt<T>> others);

     static <T> Compounder<T> empty() {
      return (self, others) -> Stream.empty();
    }
  }
}
