package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert
 *
 * <pre>
 * switch (x) {
 * }
 * </pre>
 *
 * into
 *
 * <pre>
 * </pre>
 *
 * .
 * @author Yuval Simon
 * @since 2016-11-20 */
public final class SwitchEmpty extends CarefulTipper<SwitchStatement> implements TipperCategory.Collapse {
  // @Override public ASTNode replacement(@SuppressWarnings("unused") final
  // SwitchStatement s) {
  // if(s == null)
  // return null;
  //
  // @SuppressWarnings("unchecked") List<Statement> ll = s.statements();
  // return null;
  // if(ll.isEmpty())
  // return wizard.ast(";");
  //
  // if (!(ll.get(0) + "").contains("default"))
  // return s;
  //
  // if ((ll.get(ll.size() - 1) + "").contains("break"))
  // ll.remove(ll.size() - 1);
  // ll.remove(0);
  // return subject.ss(ll).toBlock();
  // }
  @Override public Tip tip(final SwitchStatement s) {
    return new Tip(description(s), s, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        @SuppressWarnings("unchecked") final List<Statement> ll = s.statements();
        if (ll.isEmpty()) {
          r.remove(s, g);
          return;
        }
        if ((ll.get(ll.size() - 1) + "").contains("break"))
          ll.remove(ll.size() - 1);
        ll.remove(0);
        final Block b = subject.ss(ll).toBlock();
        final Block bb = subject.statement(b).toBlock();
        r.replace(s, bb, g);
      }
    };
  }

  @Override protected boolean prerequisite(final SwitchStatement ¢) {
    // return ¢ != null && (¢.statements().isEmpty() || (¢.statements().get(0) +
    // "").contains("default"));
    return ¢ != null && ¢.statements().isEmpty();
  }

  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Remove empty switch statement or switch statement with only default case";
  }
}
