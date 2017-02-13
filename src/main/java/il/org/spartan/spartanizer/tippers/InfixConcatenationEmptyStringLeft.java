package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Convert {@code ""+x} to {@code x+""}
 * @author Dan Greenstein
 * @author Niv Shalmon
 * @since 2016 */
public final class InfixConcatenationEmptyStringLeft extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.Idiomatic {
  private static InfixExpression replace(final InfixExpression ¢) {
    final List<Expression> $ = extract.allOperands(¢);
    wizard.swap($, 0, 1);
    return subject.operands($).to(wizard.PLUS2);
  }

  // TODO: Yossi Gil: this should probably be in lisp, but I can't access its
  // source anymore

  @Override public String description(final InfixExpression ¢) {
    return "Append, rather than prepend, \"\", to " + left(¢);
  }

  @Override public ASTNode replacement(final InfixExpression ¢) {
    return !iz.emptyStringLiteral(left(¢)) || !iz.infixPlus(¢) ? null : replace(¢);
  }
}
