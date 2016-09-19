package il.org.spartan.spartanizer.dispatch;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wringing.*;

public class BatchApplicator {

  /** Apply trimming repeatedly, until no more changes
   * @param from what to process
   * @return trimmed text */
  public static String fixedPoint(final String from) {
    return new Trimmer().fixed(from);
  }

  static ASTVisitor collect(final List<Suggestion> $) {
    Toolbox.refresh();
    return new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        final Wring<N> w = Toolbox.defaultInstance().find(n);
        return w == null || w.cantSuggest(n) || Trimmer.prune(w.suggest(n, exclude), $);
      }
    };
  }
}
