package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** Convert {@code ""+x} to {@code x+""}
 * @author Dan Greenstein
 * @author Niv Shalmon
 * @since 2016 */
public final class InfixConcatenationEmptyStringLeft extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.Idiomatic {
  private static final long serialVersionUID = -0xABED385B90F4612L;

  private static InfixExpression replace(final InfixExpression ¢) {
    final List<Expression> $ = extract.allOperands(¢);
    lisp2.swap($, 0, 1);
    return subject.operands($).to(op.PLUS2);
  }

  @Override public String description(final InfixExpression ¢) {
    return "Append, rather than prepend, \"\", to " + left(¢);
  }

  @Override public ASTNode replacement(final InfixExpression ¢) {
    return !iz.emptyStringLiteral(left(¢)) || !iz.infixPlus(¢) ? null : replace(¢);
  }
}
