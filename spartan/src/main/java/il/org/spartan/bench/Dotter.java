package il.org.spartan.bench;

import fluent.ly.idiomatic;

/** @author Yossi Gil
 * @since 01/05/2011 */
public class Dotter implements java.io.Serializable {
  public static final int DOTS_IN_LINE = 60;
  private static final long serialVersionUID = 1;
  private final long initTime;
  private int n;
  private int line;
  private boolean cleared;
  private long lineStartTime = -1;

  public Dotter() {
    initTime = System.nanoTime();
    clear();
  }

  public void clear() {
    if (cleared)
      return;
    cleared = true;
    n = line = 0;
    lineStart();
  }

  public void click() {
    cleared = false;
    System.err.print(++n % 10 == 0 ? '*' : '.');
    if (n % DOTS_IN_LINE != 0)
      return;
    nl();
    lineStart();
  }

  public void end() {
    nl();
  }

  public int line() {
    return line;
  }

  public int n() {
    return n;
  }

  private void lineStart() {
    lineStartTime = System.nanoTime();
    System.err.printf("%3d", idiomatic.box(++line));
  }

  private void nl() {
    final long now = System.nanoTime();
    System.err.println(
        " " + Unit.formatNanoseconds(now - lineStartTime) + " Total: " + Unit.formatNanoseconds(now - initTime));
  }
}
