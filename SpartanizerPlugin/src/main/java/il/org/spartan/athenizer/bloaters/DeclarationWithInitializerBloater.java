package il.org.spartan.athenizer.bloaters;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.athenizer.zoomin.expanders.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** Test case is {@link Issue0996} Issue #996 convert {@code
 * int a = 3;
 * } to {@code
 * int a;
 * a = 3;
 * } For now the expander do not expand if the declaration has some annotation
 * (so that no warnings would be created)
 * @author tomerdragucki
 * @author Dor Ma'ayan
 * @since 23-12-2016 */
public class DeclarationWithInitializerBloater extends CarefulTipper<VariableDeclarationStatement>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = 0xF445D29A172AE7CL;

  @Override @SuppressWarnings("unused") public String description(final VariableDeclarationStatement __) {
    return "Split declaration with initialization into two statemenets";
  }
  @Override protected boolean prerequisite(final VariableDeclarationStatement ¢) {
    return !haz.annotation(¢) && ¢.fragments().size() == 1 && ((VariableDeclaration) the.headOf(fragments(¢))).getInitializer() != null
        && ((VariableDeclaration) the.headOf(fragments(¢))).getInitializer().getNodeType() != ASTNode.ARRAY_INITIALIZER;
  }
  @Override public Tip tip(final VariableDeclarationStatement ¢) {
    final VariableDeclarationStatement $ = copy.of(¢);
    ((VariableDeclaration) the.headOf(fragments($))).setInitializer(null);
    final VariableDeclarationFragment f2 = the.headOf(fragments(¢));
    final Assignment a = subject.pair(copy.of(az.expression(f2.getName())), copy.of(az.expression(f2.getInitializer())))
        .to(Assignment.Operator.ASSIGN);
    return new Tip(description(¢), getClass(), ¢) {
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
