package org.spartan.refactoring.utils;

import java.util.Iterator;

public class as {
  public static <T> Iterable<T> iterable(final T... ts) {
    return new Iterable<T>() {
      @Override public Iterator<T> iterator() {
        return new Iterator<T>() {
          private int next = 0;
          @Override public boolean hasNext() {
            return next < ts.length;
          }
          @Override public T next() {
            return ts[next++];
          }
          @Override public void remove() {
            throw new UnsupportedOperationException("Cannot remove an element of an array.");
          }
        };
      }
    };
  }
}
