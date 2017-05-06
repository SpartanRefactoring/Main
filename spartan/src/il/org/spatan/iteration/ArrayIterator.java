package il.org.spatan.iteration;

import java.util.*;

import org.eclipse.jdt.annotation.*;

public class ArrayIterator<T> implements Iterator<T> {
   public static <E> Iterator<E> make( final E[] ¢) {
    return make(¢, 0);
  }
   public static <E> Iterator<E> make( final E[] es, final int begin) {
    return make(es, begin, es.length);
  }
   public static <E> Iterator<E> make(final E[] es, final int begin, final int end) {
    return new ArrayIterator<>(es, begin, end);
  }

  private T[] ts;
  private int index;
  private int end;

  public ArrayIterator( final T[] ts) {
    this(ts, 0);
  }
  public ArrayIterator( final T[] ts, final int begin) {
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
