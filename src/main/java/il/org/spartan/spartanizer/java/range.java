package il.org.spartan.spartanizer.java;

import java.util.NoSuchElementException;
import java.util.Iterator;

public abstract class range implements Iterable<Integer> {
  static int from;
  static int to;
  
  public static RangeIterator to(int ¢){
    to=¢; 
    return new RangeIterator();
  }
  

  @Override
  public Iterator<Integer> iterator() {
    return new RangeIterator();
  }

  private static class RangeIterator implements
                    Iterator<Integer>, Iterable<Integer> {
        private int cursor;
        public RangeIterator() {
            this.cursor = from;
        }
        
        @Override
        public boolean hasNext() {
            return this.cursor < to;
        }

        @SuppressWarnings("boxing")
        @Override
        public Integer next() {
            if (!this.hasNext())
              throw new NoSuchElementException();
            int $ = cursor;
            ++cursor;
            return $;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override public Iterator<Integer> iterator() {
          return this;
        }
    }
}
