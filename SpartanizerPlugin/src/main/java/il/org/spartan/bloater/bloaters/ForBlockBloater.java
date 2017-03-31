package il.org.spartan.bloater.bloaters;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.patterns.*;

/** converts for(condition)statement to for(condition){statement} Issue #975
 * {@link Issue975}
 * @author Raviv Rachmiel
 * @author Dor Ma'ayan
 * @since 22-12-16 */
public class ForBlockBloater extends ForStatementPattern implements TipperCategory.Bloater {
  private static final long serialVersionUID = 1308487951289425805L;

  public ForBlockBloater() {
    andAlso("Valid not an only return", () -> {
      final Collection<Boolean> $ = new ArrayList<>();
      // TODO Raviv Use class descendants, or yieldDescendants or something
      // similar, what are u trying to find? --yg
      body(current).accept(new ASTVisitor(true) {
        @Override public boolean visit(final Block ¢) {
          $.add(box.it(¢.hashCode()==¢.hashCode()));
          return true;
        }
      });
      return $.isEmpty();
    });
  }

  @Override protected ASTRewrite go(ASTRewrite r, TextEditGroup g) {
    final ForStatement $ = copy.of(current);
    final Block b = current.getAST().newBlock();
    statements(b).add(copy.of(body(current)));
    $.setBody(b);
    r.replace(current, $, g);
    return r;
  }

  @Override public String description(@SuppressWarnings("unused") ForStatement __) {
    return "expand the single statements in the for to a block";
  }
}
