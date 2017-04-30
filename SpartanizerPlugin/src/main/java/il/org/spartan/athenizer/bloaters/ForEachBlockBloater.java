package il.org.spartan.athenizer.bloaters;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.athenizer.zoomin.expanders.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;
import nano.ly.*;

/** converts foreach statement to foreach {statement} Test case
 * is{@link Issue1023}
 * @author Raviv Rachmiel
 * @since 10-01-17 */
public class ForEachBlockBloater extends ForEachStatementPattern//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = 0x38C70470EE42ECEBL;

  @Override public Examples examples() {
    return convert("for(Double i : lili) a=5; b=7;").to("for(Double i : lili){a=5;}b=7;");
  }

  public ForEachBlockBloater() {
    andAlso("Valid when not a block", () -> {
      final Collection<Boolean> $ = an.empty.list();
      // TODO Raviv Use class descendants, or yieldDescendants or something
      // similar, what are u trying to find? --yg
      body(current).accept(new ASTVisitor(true) {
        @Override public boolean visit(final Block ¢) {
          $.add(box.it(¢.hashCode() == ¢.hashCode()));
          return true;
        }
      });
      return $.isEmpty();
    });
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final TextEditGroup g) {
    final EnhancedForStatement $ = copy.of(current);
    final Block b = current.getAST().newBlock();
    statements(b).add(copy.of(body(current)));
    $.setBody(b);
    r.replace(current, $, g);
    return r;
  }

  @Override public String description() {
    return "expand the single statement in the foreach to a block";
  }
}
