package il.org.spartan.spartanizer.java;

import java.util.NoSuchElementException;
import java.util.Iterator;

public abstract class range implements Iterable<Integer> {
  static int from;
  static int to;
  static boolean inclusive;

  static void  initialize(){
    from=0;
    inclusive=false;
  }
  public static RangeIterator to(int ¢) {
    to = ¢;
    return new RangeIterator();
  }

  public static BeforeTo from(int ¢) {
    from = ¢;
    return new BeforeTo();
  }

  @Override public Iterator<Integer> iterator() {
    return new RangeIterator();
  }

  static class BeforeTo {
    public static RangeIterator to(int ¢) {
      to = ¢;
      return new RangeIterator();
    } 
  }
  

  static class RangeIterator implements Iterator<Integer>, Iterable<Integer> {
    private int cursor;

    public RangeIterator() {
      this.cursor = from;
    }
    
    public RangeIterator inclusive(){
      inclusive=true;
      return this;
    }
    
    public RangeIterator notInclusive(){
      inclusive=false;
      return this;
    }

    @Override public boolean hasNext() {
      boolean $ = inclusive ? this.cursor <= to : this.cursor < to;
      if(!$)
        initialize();
      return $;

    }

    @SuppressWarnings("boxing") @Override public Integer next() {
      if (!this.hasNext())
        throw new NoSuchElementException();
      int $ = cursor;
      ++cursor;
      return $;
    }

    @Override public void remove() {
      throw new UnsupportedOperationException();
    }

    @Override public Iterator<Integer> iterator() {
      return this;
    }
  }
}
