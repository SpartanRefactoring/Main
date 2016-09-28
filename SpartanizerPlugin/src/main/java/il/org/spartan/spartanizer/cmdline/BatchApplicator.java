package il.org.spartan.spartanizer.cmdline;
import il.org.spartan.spartanizer.dispatch.TipperCategory;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

public final class BatchApplicator {
  public final Toolbox toolbox = new Toolbox();

  ASTVisitor collect(final List<Tip> $) {
    return new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        final Tipper<N> t = toolbox.find(n);
        try {
          return t == null || t.cantTip(n) || Trimmer.prune(t.tip(n, exclude), $);
        } catch (final TipperFailure e) {
          e.printStackTrace();
        }
        return false;
      }
    };
  }

  public BatchApplicator disable(Class<? extends TipperCategory> ¢) {
    toolbox.disable(¢);
    return this;
  }

  /** Apply trimming repeatedly, until no more changes
   * @param from what to process
   * @return trimmed text */
  public String fixedPoint(final String from) {
    return new Trimmer(toolbox).fixed(from);
  }

  public static void main(final String[] args) {
    System.out.println(new BatchApplicator().fixedPoint(read()));
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
}
