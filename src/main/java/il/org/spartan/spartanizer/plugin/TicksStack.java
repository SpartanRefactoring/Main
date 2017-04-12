package il.org.spartan.spartanizer.plugin;

import java.util.*;

/** A simple stack, to be extended with more delegators, in the unlikely event
 * that the need arises.
 * <p>
 * Vanilla flavor to used for quick implementation of {@link Listener}.
 * @see #push(Object...)
 * @see #pop(Object...)
 * @see #empty()
 * @author Yossi Gil
 * @since Oct 22, 2016 */
public class TicksStack {
  public final Stack<Object[]> inner = new Stack<>();

  public Object[] pop() {
    return inner.pop();
  }

  public void push(final Object... ¢) {
    inner.push(¢);
  }

  public Object[] top() {
    return inner.lastElement();
  }
}
