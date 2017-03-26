package il.org.spartan.bloater.bloaters;

import static il.org.spartan.utils.Example.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** convert {@code
 * ++i; --i;
} to * {@code
 * i+=1; i-=1;
 *
 * } Test case is {@link Issue1005}
 * @author YuvalSimon {@code yuvaltechnion@gmail.com}
 * @since 2016-12-24 */
public class PrefixToInfix extends ReplaceCurrentNode<PrefixExpression>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = 0x3465E06532ABDE60L;

  @Override @NotNull public Example[] examples() {
    return new Example[] { 
        convert("++i;").to("i += 1;"),
        convert("--i;").to("i -= 1"),
    };
  }
  
  @Override public ASTNode replacement(@NotNull final PrefixExpression ¢) {
    final NumberLiteral $ = ¢.getAST().newNumberLiteral();
    $.setToken("1");
    return subject.pair(step.operand(¢), $)
        .to(step.operator(¢) != PrefixExpression.Operator.DECREMENT ? Operator.PLUS_ASSIGN : Operator.MINUS_ASSIGN);
  }

  @Override protected boolean prerequisite(final PrefixExpression ¢) {
    @NotNull final ASTNode $ = parent(¢);
    return (step.operator(¢) == PrefixExpression.Operator.INCREMENT || step.operator(¢) == PrefixExpression.Operator.DECREMENT)
        && (iz.expressionStatement($) || iz.forStatement($));
  }

  @Override @SuppressWarnings("unused") @Nullable public String description(final PrefixExpression __) {
    return null;
  }
}
