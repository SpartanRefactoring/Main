package il.org.spartan.spartanizer.java;

import java.util.NoSuchElementException;
import java.util.Iterator;

/**
 * A fluent api implementation for range 
 * 
 * @author Dor Ma'ayan
 * @since 26-11-2016
 */
public abstract class range implements Iterable<Integer> {
  static int from;
  static int to;
  static boolean inclusive;
  static int step=1;

  static void  initialize(){
    from=0;
    inclusive=false;
    step=1;
  }
  public static AfterTo to(int ¢) {
    to = ¢;
    return new AfterTo();
  }

  public static BeforeTo from(int ¢) {
    from = ¢;
    return new BeforeTo();
  }

  @Override public Iterator<Integer> iterator() {
    return new RangeIterator();
  }

  static class BeforeTo {
    public static AfterTo to(int ¢) {
      to = ¢;
      return new AfterTo();
    } 
    
    public static AfterTo step(int ¢){
      step=¢;
      return new AfterTo();
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
      cursor+=step;
      return $;
    }

    @Override public void remove() {
      throw new UnsupportedOperationException();
    }

    @Override public Iterator<Integer> iterator() {
      return this;
    }
  }
  
  static class AfterTo implements Iterator<Integer>, Iterable<Integer> {
    private int cursor;

    public AfterTo() {
      this.cursor = from;
    }
    
    public static RangeIterator step(int ¢){
      step=¢;
      return new RangeIterator();
    }
    
    public static RangeIterator inclusive(){
      inclusive=true;
      return new RangeIterator();
    }
    
    public static RangeIterator notInclusive(){
      inclusive=false;
      return new RangeIterator();
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
