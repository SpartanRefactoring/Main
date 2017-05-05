package il.org.spatan.iteration;

import java.util.*;

import org.jetbrains.annotations.*;

public class ArrayIterator<T> implements Iterator<T> {
  @NotNull public static <E> Iterator<E> make(@NotNull final E[] ¢) {
    return make(¢, 0);
  }
  @NotNull public static <E> Iterator<E> make(@NotNull final E[] es, final int begin) {
    return make(es, begin, es.length);
  }
  @NotNull public static <E> Iterator<E> make(final E[] es, final int begin, final int end) {
    return new ArrayIterator<>(es, begin, end);
  }

  private T[] ts;
  private int index;
  private int end;

  public ArrayIterator(@NotNull final T[] ts) {
    this(ts, 0);
  }
  public ArrayIterator(@NotNull final T[] ts, final int begin) {
    this(ts, begin, ts.length);
  }
  public ArrayIterator(final T[] ts, final int begin, final int end) {
    this.ts = ts;
    index = begin;
    this.end = end;
  }
  @Override public boolean hasNext() {
    return index < end;
  }
  @Override public T next() {
    return ts[index++];
  }
}
