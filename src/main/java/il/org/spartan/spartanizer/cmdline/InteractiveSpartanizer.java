package il.org.spartan.spartanizer.cmdline;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.trimming.*;

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
      final GuessedContext c = GuessedContext.find(input);
      System.err.println("output: " + new InteractiveSpartanizer()
          .fixedPoint(c.name().equals(GuessedContext.COMPILATION_UNIT_LOOK_ALIKE) ? input : c.intoCompilationUnit(input) + ""));
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

  public Configuration configuration = Configurations.all();

  public InteractiveSpartanizer disable(final Class<? extends TipperCategory> ¢) {
    configuration.disable(¢);
    return this;
  }

  /** Apply trimming repeatedly, until no more changes
   * @param from what to process
   * @return trimmed text */
  public String fixedPoint(final String from) {
    return new Trimmer(configuration).fixed(from);
  }

  public String fixedPoint(final ASTNode from) {
    return new Trimmer(configuration).fixed(from + "");
  }

  public String once(final String from) {
    return new Trimmer(configuration).once(from);
  }

  boolean changed;

  @SafeVarargs public final <N extends ASTNode> InteractiveSpartanizer add(final Class<N> c, final Tipper<N>... ts) {
    if (!changed)
      configuration = Configurations.allClone();
    changed = true;
    configuration.add(c, ts);
    return this;
  }

  @SafeVarargs public final <N extends ASTNode> InteractiveSpartanizer remove(final Class<N> c, final Tipper<N>... ts) {
    if (!changed)
      configuration = Configurations.allClone();
    changed = true;
    configuration.remove(c, ts);
    return this;
  }

  @SafeVarargs public final <N extends ASTNode> InteractiveSpartanizer add(final Integer i, final Tipper<N>... ts) {
    if (!changed)
      configuration = Configurations.allClone();
    changed = true;
    configuration.add(i, ts);
    return this;
  }
}
