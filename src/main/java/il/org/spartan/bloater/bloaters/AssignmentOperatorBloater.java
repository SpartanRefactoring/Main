package il.org.spartan.bloater.bloaters;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;
import org.jetbrains.annotations.NotNull;

/** Expands {@code a += 3} to {@code a = a + 3}. Capable of dealing with
 * inclusion and all operator types: {@code a |= b &= c} ->
 * {@code a = a | (b &= c)} -> {@code a = a | (b = b & c)} Test file:
 * {@link Issue1001}
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2016-12-28 */
public class AssignmentOperatorBloater extends CarefulTipper<Assignment>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = 4972402353938739657L;

  @NotNull
  @Override public String description(@SuppressWarnings("unused") final Assignment __) {
    return "use simple assignment with binary operation";
  }

  @Override protected boolean prerequisite(@NotNull final Assignment ¢) {
    return ¢.getAST().hasResolvedBindings() && validTypes(¢) && wizard.convertToInfix(¢.getOperator()) != null;
  }

  @NotNull
  @Override public Tip tip(@NotNull final Assignment ¢) {
    return new Tip(description(¢), ¢, getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        // TODO Ori Roth: use class subject
        final InfixExpression e = ¢.getAST().newInfixExpression();
        e.setLeftOperand(copy.of(left(¢)));
        e.setRightOperand(make.plant(copy.of(right(¢))).into(e));
        e.setOperator(wizard.convertToInfix(¢.getOperator()));
        final Assignment a = ¢.getAST().newAssignment();
        a.setLeftHandSide(copy.of(left(¢)));
        a.setRightHandSide(e);
        a.setOperator(Operator.ASSIGN);
        r.replace(¢, a, g);
      }
    };
  }

  private static boolean validTypes(final Assignment ¢) {
    final ITypeBinding $ = left(¢).resolveTypeBinding(), br = right(¢).resolveTypeBinding();
    return $ != null && br != null && $.isPrimitive() && br.isPrimitive() && $.isEqualTo(br);
  }
}
