package il.org.spartan.athenizer.bloaters;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.athenizer.*;
import il.org.spartan.athenizer.zoomin.expanders.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Expands {@code a += 3} to {@code a = a + 3}. Capable of dealing with
 * inclusion and all operator types: {@code a |= b &= c} ->
 * {@code a = a | (b &= c)} -> {@code a = a | (b = b & c)} Test file:
 * {@link Issue1001}
 * @author Ori Roth {@code ori.rothh@gmail.com}
 * @since 2016-12-28 */
public class AssignmentOperatorBloater extends CarefulTipper<Assignment>//
    implements BloaterCategory.Unshortcut {
  private static final long serialVersionUID = 0x4501859892D5B1C9L;

  @Override public String description(@SuppressWarnings("unused") final Assignment __) {
    return "use simple assignment with binary operation";
  }
  @Override protected boolean prerequisite(final Assignment ¢) {
    return ¢.getAST().hasResolvedBindings() && validTypes(¢) && op.assign2infix(¢.getOperator()) != null;
  }
  @Override public Tip tip(final Assignment ¢) {
    return new Tip(description(¢), getClass(), ¢) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        // TODO Ori Roth: use class subject
        final InfixExpression e = ¢.getAST().newInfixExpression();
        e.setLeftOperand(copy.of(left(¢)));
        e.setRightOperand(make.plant(copy.of(right(¢))).into(e));
        e.setOperator(op.assign2infix(¢.getOperator()));
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
