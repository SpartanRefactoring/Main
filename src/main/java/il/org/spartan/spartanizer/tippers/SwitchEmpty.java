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
 * case a:
 * }
 * 
 * switch(x) {
 * default: (some commands)
 * }
 * </pre>
 *
 * into
 *
 * <pre>
 * (some commands)
 * </pre>
 *
 * .
 * @author Yuval Simon
 * @since 2016-11-20 */
public final class SwitchEmpty extends CarefulTipper<SwitchStatement> implements TipperCategory.Collapse {
  @Override public Tip tip(SwitchStatement s) {
    return new Tip(description(s), s, this.getClass()) {
      @Override public void go(ASTRewrite r, TextEditGroup g) {
        @SuppressWarnings("unchecked") List<Statement> ll = s.statements();
        if (ll.isEmpty() || ll.size() == 1 || ll.size() == 2 && (ll.get(1) + "").contains("break")) {
          r.remove(s, g);
          return;
        }
        if ((ll.get(ll.size() - 1) + "").contains("break"))
          ll.remove(ll.size() - 1);
        ll.remove(0);
        Block b = subject.ss(ll).toBlock();
        Block bb = subject.statement(b).toBlock();
        r.replace(s, bb, g);
      }
    };
  }
  
  @Override protected boolean prerequisite(SwitchStatement s) {
    List<SwitchCase> l = getCases(s);
    @SuppressWarnings("unchecked") List<Statement> ll = s.statements();
    return l.isEmpty() || ll.size() == 1 || (ll.size() == 2 && (ll.get(1) + "").contains("break") || l.size() == 1 && l.get(0).isDefault());
  }
  
  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Remove empty switch statement or switch statement with only default case";
  }
  
  private static List<SwitchCase> getCases(SwitchStatement s) {
    List<SwitchCase> $ = new ArrayList<>();
    s.accept(new ASTVisitor() {
      @Override public boolean visit(SwitchCase node) {
        $.add(node);
        return super.visit(node);
      }
    });
    return $;
  }
}
