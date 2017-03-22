package il.org.spartan.utils;

import java.util.stream.*;

public interface Nested<@¢ T> {
  default Stream<T> ancestors() {
    return Stream.empty();
  }

  default T self() {
    return null;
  }

  default Stream<T> streamSelf() {
    return self() == null ? Stream.empty() : Stream.of(self());
  }

  interface Root<@¢ T> extends Nested<T> {
    @Override default Stream<T> ancestors() {
      return streamSelf();
    }
  }

  interface Compound<@¢ T> extends Nested<T> {
    Nested<T> parent();

    @Override default Stream<T> ancestors() {
      return Stream.concat(parent().ancestors(), streamSelf());
    }
  }
}
