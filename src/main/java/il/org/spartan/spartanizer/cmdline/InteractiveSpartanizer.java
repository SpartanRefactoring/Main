package il.org.spartan.spartanizer.cmdline;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;

/** TODO: Yossi Gil please add a description
 * @author Yossi Gil
 * @author Matteo Orru'
 * @since 2016 */
public class InteractiveSpartanizer {
  /** @param fileNames if present, will process these as batch */
  public static void main(@NotNull final String[] fileNames) {
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

  @NotNull
  static String read() {
    String $ = "";
    try (Scanner s = new Scanner(System.in)) {
      for (s.useDelimiter("\n"); s.hasNext(); $ += s.next() + "\n")
        if (!s.hasNext())
          return $;
    }
    return $;
  }

  public Toolbox toolbox = Toolbox.defaultInstance();

  @NotNull
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

  public String fixedPoint(final ASTNode from) {
    return new Trimmer(toolbox).fixed(from + "");
  }

  @NotNull ASTVisitor collect(final List<Tip> $) {
    return new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        final Tipper<N> t = toolbox.firstTipper(n);
        return t == null || t.cantTip(n) || Trimmer.prune(t.tip(n, exclude), $);
      }
    };
  }

  boolean changed;

  @NotNull
  @SafeVarargs public final <N extends ASTNode> InteractiveSpartanizer add(final Class<N> c, final Tipper<N>... ts) {
    if (!changed)
      toolbox = Toolbox.mutableDefaultInstance();
    changed = true;
    toolbox.add(c, ts);
    return this;
  }

  @NotNull
  @SafeVarargs public final <N extends ASTNode> InteractiveSpartanizer remove(final Class<N> c, final Tipper<N>... ts) {
    if (!changed)
      toolbox = Toolbox.mutableDefaultInstance();
    changed = true;
    toolbox.remove(c, ts);
    return this;
  }

  @NotNull
  @SafeVarargs public final <N extends ASTNode> InteractiveSpartanizer add(final Integer i, final Tipper<N>... ts) {
    if (!changed)
      toolbox = Toolbox.mutableDefaultInstance();
    changed = true;
    toolbox.add(i, ts);
    return this;
  }
}
