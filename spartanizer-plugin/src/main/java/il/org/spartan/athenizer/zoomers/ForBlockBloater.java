package il.org.spartan.athenizer.zoomers;

import static il.org.spartan.spartanizer.ast.navigate.step.body;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;

import java.util.Collection;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.box;
import il.org.spartan.athenizer.zoom.zoomers.Issue0975;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.tippers.ForStatementPattern;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.Examples;

/** converts for(condition)statement to for(condition){statement} Issue #975
 * {@link Issue0975}
 * @author Raviv Rachmiel
 * @author Dor Ma'ayan
 * @since 22-12-16 */
public class ForBlockBloater extends ForStatementPattern implements Category.Bloater {
  private static final long serialVersionUID = 0x1228AEDBE97C1F8DL;

  @Override public Examples examples() {
    return convert("for(int i=0;i<5;++i) continue;").to("for(int i=0;i<5;++i) { continue; }");
  }
  public ForBlockBloater() {
    andAlso("Valid not an only return", () -> {
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
    final ForStatement $ = copy.of(current);
    final Block b = current.getAST().newBlock();
    statements(b).add(copy.of(body(current)));
    $.setBody(b);
    ret.replace(current, $, g);
    return ret;
  }
  @Override public String description() {
    return "expand the single statements in the for to a block";
  }
}
