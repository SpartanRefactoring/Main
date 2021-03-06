package il.org.spartan.athenizer.zoomers;

import static il.org.spartan.spartanizer.ast.navigate.step.body;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;

import java.util.Collection;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.box;
import il.org.spartan.athenizer.zoom.zoomers.Issue1023;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.tippers.ForEachStatementPattern;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.Examples;

/** converts foreach statement to foreach {statement} Test case
 * is{@link Issue1023}
 * @author Raviv Rachmiel
 * @since 10-01-17 */
public class ForEachBlockBloater extends ForEachStatementPattern//
    implements Category.Bloater {
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
  @Override protected ASTRewrite go(final ASTRewrite ret, final TextEditGroup g) {
    final EnhancedForStatement $ = copy.of(current);
    final Block b = current.getAST().newBlock();
    statements(b).add(copy.of(body(current)));
    $.setBody(b);
    ret.replace(current, $, g);
    return ret;
  }
  @Override public String description() {
    return "expand the single statement in the foreach to a block";
  }
}
