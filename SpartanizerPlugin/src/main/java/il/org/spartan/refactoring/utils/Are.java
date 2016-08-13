package il.org.spartan.refactoring.utils;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-16 */
public enum Are {
  ;
  /** Determine whether a <i>all</i> elements list of {@link Expression} are
   * provably not a string.
   * @param es JD
   * @return <code><b>true</b></code> <i>iff</i> all elements in the argument
   *         are provably not a {@link String}.
   * @see Is#notString(Expression) */
  public static boolean notString(final List<Expression> es) {
    for (final Expression e : es)
      if (!Is.notStringDown(e))
        return false;
    return true;
  }
}
