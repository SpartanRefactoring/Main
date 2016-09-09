/**
 *
 */
package il.org.spartan.utils;

/** @author Yossi Gil
 * @since 10 November 2011 */
public class Counter {
  private int value = 0;

  public int add() {
    return ++value;
  }

  public int increment() {
    return ++value;
  }

  public int value() {
    return value;
  }
}
