package il.org.spartan.spartanizer.cmdline.good;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** TODO Yossi Gil please add a description
 * @author Yossi Gil
 * @author Matteo Orru'
 * @since 2016 */
public class InteractiveSpartanizer {
  /** @param fileNames if present, will process these as batch */
  public static void main(final String[] fileNames) {
    if (fileNames.length != 0)
      BatchSpartanizer.fire(fileNames); // change from main to fire
    else {
      System.err.println("input: "); //
      final String input = read();
      System.err.println("output: " + new InteractiveSpartanizer().fixedPoint(GuessedContext.find(input).intoCompilationUnit(input) + ""));
    }
  }
  static String read() {
    String $ = "";
    try (Scanner s = new Scanner(System.in)) {
      for (s.useDelimiter("\n"); s.hasNext(); $ += s.next() + "\n")
        if (!s.hasNext())
          return $;
    }
    return $;
  }
  public InteractiveSpartanizer disable(final Class<? extends Category> ¢) {
    traversals.traversal.toolbox.disable(¢);
    return this;
  }
  /** Apply trimming repeatedly, until no more changes
   * @param from what to process
   * @return trimmed text */
  public String fixedPoint(final String from) {
    return traversals.fixed(from);
  }
  public String fixedPoint(final ASTNode from) {
    return traversals.fixed(from + "");
  }
  public String once(final String from) {
    return traversals.once(from);
  }

  public final TextualTraversals traversals = new TextualTraversals();

  @SafeVarargs public final <N extends ASTNode> InteractiveSpartanizer add(final Class<N> c, final Tipper<N>... ts) {
    traversals.traversal.toolbox.add(c, ts);
    return this;
  }
  @SafeVarargs public final <N extends ASTNode> InteractiveSpartanizer remove(final Class<N> c, final Tipper<N>... ts) {
    traversals.traversal.toolbox.remove(c, ts);
    return this;
  }
}
