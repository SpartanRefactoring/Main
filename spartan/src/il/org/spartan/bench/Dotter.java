/**
 *
 */
package il.org.spartan.bench;

import static il.org.spartan.utils.Box.*;

/** @author Yossi Gil
 * @since 01/05/2011 */
public class Dotter implements java.io.Serializable {
  public static final int DOTS__IN__LINE = 60;
  private static final long serialVersionUID = 1L;
  private final long initTime;
  private int n = 0;
  private int line = 0;
  private boolean cleared = false;
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
    if (n % DOTS__IN__LINE != 0)
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
    System.err.printf("%3d", box(++line));
  }

  private void nl() {
    final long now = System.nanoTime();
    System.err.println(" " + Unit.formatNanoseconds(now - lineStartTime) + " Total: " + Unit.formatNanoseconds(now - initTime));
  }
}
