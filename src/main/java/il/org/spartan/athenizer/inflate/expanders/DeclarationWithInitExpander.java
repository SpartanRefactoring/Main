package il.org.spartan.athenizer.inflate.expanders;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Test case is {@link DeclarationWithInitExpanderTest} Issue No. 996 convert
 * <code>
 * int a = 3;
 * </code> to <code>
 * int a;
 * a = 3;
 * </code> For now the expander do not expand if the declaration has some
 * annotation (so that no warnings would be created)
 * @author Tomer Dragucki
 * @since 23-12-2016 */
public class DeclarationWithInitExpander extends CarefulTipper<VariableDeclarationStatement> implements TipperCategory.InVain {
  @Override @SuppressWarnings("unused") public String description(final VariableDeclarationStatement __) {
    return "Split declaration with initialization into two statemenets";
  }

  @Override protected boolean prerequisite(final VariableDeclarationStatement ¢) {
    return !haz.annotation(¢) && ¢.fragments().size() == 1 && ((VariableDeclarationFragment) ¢.fragments().get(0)).getInitializer() != null
        && ((VariableDeclarationFragment) ¢.fragments().get(0)).getInitializer().getNodeType() != ASTNode.ARRAY_INITIALIZER;
  }

  @Override public Tip tip(final VariableDeclarationStatement ¢) {
    final VariableDeclarationStatement $ = duplicate.of(¢);
    ((VariableDeclarationFragment) $.fragments().get(0)).setInitializer(null);
    final Assignment a = ¢.getAST().newAssignment();
    final VariableDeclarationFragment f2 = (VariableDeclarationFragment) ¢.fragments().get(0);
    a.setLeftHandSide(duplicate.of(az.expression(f2.getName())));
    a.setRightHandSide(duplicate.of(az.expression(f2.getInitializer())));
    return new Tip(description(¢), ¢, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final ListRewrite l = r.getListRewrite(¢.getParent(), Block.STATEMENTS_PROPERTY);
        l.insertAfter(¢.getAST().newExpressionStatement(a), ¢, g);
        l.insertAfter($, ¢, g);
        l.remove(¢, g);
      }
    };
  }
}
