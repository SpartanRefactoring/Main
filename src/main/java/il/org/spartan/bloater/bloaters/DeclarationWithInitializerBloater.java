package il.org.spartan.bloater.bloaters;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** Test case is {@link Issue0996} Issue #996 convert <code>
 * int a = 3;
 * </code> to <code>
 * int a;
 * a = 3;
 * </code> For now the expander do not expand if the declaration has some
 * annotation (so that no warnings would be created)
 * @author Tomer Dragucki
 * @since 23-12-2016 */
public class DeclarationWithInitializerBloater extends CarefulTipper<VariableDeclarationStatement>//
    implements TipperCategory.Bloater {
  @Override @SuppressWarnings("unused") public String description(final VariableDeclarationStatement __) {
    return "Split declaration with initialization into two statemenets";
  }

  @Override protected boolean prerequisite(final VariableDeclarationStatement ¢) {
    return !haz.annotation(¢) && ¢.fragments().size() == 1 && ((VariableDeclarationFragment) ¢.fragments().get(0)).getInitializer() != null
        && ((VariableDeclarationFragment) ¢.fragments().get(0)).getInitializer().getNodeType() != ASTNode.ARRAY_INITIALIZER;
  }

  @Override public Tip tip(final VariableDeclarationStatement ¢) {
    final VariableDeclarationStatement $ = copy.of(¢);
    ((VariableDeclarationFragment) $.fragments().get(0)).setInitializer(null);
    final Assignment a = ¢.getAST().newAssignment();
    final VariableDeclarationFragment f2 = (VariableDeclarationFragment) ¢.fragments().get(0);
    a.setLeftHandSide(copy.of(az.expression(f2.getName())));
    a.setRightHandSide(copy.of(az.expression(f2.getInitializer())));
    return new Tip(description(¢), ¢, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final ListRewrite l = r.getListRewrite(¢.getParent(), !SwitchStatement.STATEMENTS_PROPERTY.getNodeClass().isInstance(¢.getParent())
            ? Block.STATEMENTS_PROPERTY : SwitchStatement.STATEMENTS_PROPERTY);
        l.insertAfter(¢.getAST().newExpressionStatement(a), ¢, g);
        l.insertAfter($, ¢, g);
        l.remove(¢, g);
      }
    };
  }
}
