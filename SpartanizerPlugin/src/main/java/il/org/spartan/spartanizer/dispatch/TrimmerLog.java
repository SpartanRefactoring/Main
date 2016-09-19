package il.org.spartan.spartanizer.dispatch;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import static il.org.spartan.spartanizer.ast.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wringing.*;

/** Logging stuff
 * @author Yossi Gil
 * @year 2016 */
public class TrimmerLog {
  private static int maxVisitations = 30;
  private static int maxSuggestions = 20;
  private static int maxApplications = 10;

  static void visitation(ASTNode n) {
    if (--maxVisitations <= 0) {
      if (maxVisitations == 0)
        System.out.println("Stopped logging visitations");
      return;
    }
    System.out.println("VISIT: '" + tide.clean(n+ "") + "' [" + n.getLength() + "] (" + clazz(n) + ")" + "parent=" + clazz(parent(n))); 
  }

  private static String clazz(ASTNode n) {
    return n.getClass().getSimpleName();
  }

  static <N extends ASTNode> void suggestion(Wring<N> w, N n) {
    if (--maxSuggestions <= 0) {
      if (maxSuggestions == 0)
        System.out.println("Stopped logging suggestions");
      return;
    }
    System.out.println("       Wring: " + w.getClass().getSimpleName());
    System.out.println("       Named: " + w.description());
    System.out.println("        Kind: " + w.wringGroup());
    System.out.println("   Described: " + w.description(n));
    System.out.println(" Can suggest: " + w.canSuggest(n));
    System.out.println("    Suggests: " + w.suggest(n));
  }

  static void application(final ASTRewrite r, final Suggestion s) {
    if (--maxApplications <= 0) {
      if (maxApplications == 0)
        System.out.println("Stopped logging applications");
      s.go(r, null);
      return;
    }
    System.out.println("      Before: " + r);
    s.go(r, null);
    System.out.println("       After: " + r);
  }
}
