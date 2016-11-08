package il.org.spartan.spartanizer.cmdline;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author Yossi Gil
 * @author Matteo Orru'
 * @since 2016 */
public final class InteractiveSpartanizer {
  /** @param fileNames if present, will process these as batch */
  public static void main(final String[] fileNames) {
    if (fileNames.length != 0)
      BatchSpartanizer.fire(fileNames); // change from main to fire
    else {
      final String input = read();
      // System.err.println("input: " + input); //
      final GuessedContext c = GuessedContext.find(input);
      // System.out.println(c.name());
      CompilationUnit cu = null;
      String output;
      if (c.name().equals(GuessedContext.COMPILATION_UNIT_LOOK_ALIKE))
        output = new InteractiveSpartanizer().fixedPoint(input);
      else {
        cu = c.intoCompilationUnit(input);
        assert cu != null;
        output = new InteractiveSpartanizer().fixedPoint(cu + "");
      }
      System.err.println("output: " + output); // new
                                               // InteractiveSpartanizer().fixedPoint(read()));
    }
  }
  static String read() {
    String $ = "";
    try (final Scanner s = new Scanner(System.in)) {
      for (s.useDelimiter("\n"); s.hasNext(); $ += s.next() + "\n")
        if (!s.hasNext())
          return $;
    }
    return $;
  }

  public Toolbox toolbox = Toolbox.defaultInstance();

  public InteractiveSpartanizer disable(final Class<? extends TipperCategory> ¢) {
    toolbox.disable(¢);
    return this;
  }
  /** Apply trimming repeatedly, until no more changes
   * @param from what to process
   * @return trimmed text */
  public String fixedPoint(final String from) {
    return new Trimmer(toolbox).fixed(from);
  }
  ASTVisitor collect(final List<Tip> $) {
    return new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        final Tipper<N> t = toolbox.firstTipper(n);
        return t == null || t.cantTip(n) || Trimmer.prune(t.tip(n, exclude), $);
      }
    };
  }

  boolean changed;

  @SafeVarargs public final <N extends ASTNode> InteractiveSpartanizer add(final Class<N> n, final Tipper<N>... ns) {
    if (!changed)
      toolbox = Toolbox.muttableDefaultInstance();
    changed = true;
    toolbox.add(n, ns);
    return this;
  }
}
