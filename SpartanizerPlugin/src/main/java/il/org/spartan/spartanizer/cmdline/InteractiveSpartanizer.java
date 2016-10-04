package il.org.spartan.spartanizer.cmdline;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** TODO: Ori Marcovtich: Document
 * @author Ori Marcovitch
 * @since 2016 */
public final class InteractiveSpartanizer {
  /** @param fileNames if present, will process these as batch */
  public static void main(final String[] args) {
    if (args.length != 0)
      BatchSpartanizer.main(args);
    else
      System.out.println(new InteractiveSpartanizer().fixedPoint(read()));
  }

  static String read() {
    String $ = "";
    try (Scanner s = new Scanner(System.in).useDelimiter("\\n")) {
      for (; s.hasNext(); $ += s.next() + "\n")
        if (!s.hasNext())
          return $;
    }
    return $;
  }

  public final Toolbox toolbox = new Toolbox();

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
        try {
          return t == null || t.cantTip(n) || Trimmer.prune(t.tip(n, exclude), $);
        } catch (final TipperFailure e) {
          e.printStackTrace();
        }
        return false;
      }
    };
  }
}
