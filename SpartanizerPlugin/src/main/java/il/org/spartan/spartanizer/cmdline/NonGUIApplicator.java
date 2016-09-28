package il.org.spartan.spartanizer.cmdline;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

public class NonGUIApplicator {
  static ASTVisitor collect(final List<Tip> $) {
    Toolbox.refresh();
    return new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        final Tipper<N> w = Toolbox.defaultInstance().find(n);
        try {
          return w == null || w.cantTip(n) || Trimmer.prune(w.tip(n, exclude), $);
        } catch (final TipperFailure e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        return false;
      }
    };
  }

  /** Apply trimming repeatedly, until no more changes
   * @param from what to process
   * @return trimmed text */
  public static String fixedPoint(final String from) {
    return new Trimmer().fixed(from);
  }

  public static void main(final String[] args) {
    System.out.println(fixedPoint(read()));
  }

  static String read() {
    String $ = "";
    try (Scanner s = new Scanner(System.in).useDelimiter("\\n")) {
      for (; s.hasNext(); $ += s.next() + "\n")
        ;
    }
    return $;
  }
}
