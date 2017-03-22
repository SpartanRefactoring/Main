package il.org.spartan.spartanizer.ast.factory;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** An empty {@code enum} for fluent programming. The name should say it all:
 * The name, followed by a dot, followed by a method name, should read like a
 * sentence phrase.
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016 */
public enum flatten {
  ;
  /** Flatten the list of arguments to an {@link InfixExpression}, e.g., convert
   * an expression such as {@code (a + b) + c} whose inner form is roughly
   * "+(+(a,b),c)", into {@code a + b + c}, whose inner form is (roughly)
   * "+(a,b,c)".
   * @param $ JD
   * @return a duplicate of the argument, with the a flattened list of
   *         operands. */
  public static InfixExpression of(@NotNull final InfixExpression $) {
    assert $ != null;
    final Operator o = $.getOperator();
    assert o != null;
    return subject.operands(flatten.into(o, hop.operands($), new ArrayList<>())).to(copy.of($).getOperator());
  }

  @NotNull
  private static List<Expression> add(final Expression x, @NotNull final List<Expression> $) {
    $.add(x);
    return $;
  }

  @NotNull
  private static List<Expression> into(final Operator o, final Expression x, @NotNull final List<Expression> $) {
    @Nullable final Expression core = core(x);
    @Nullable final InfixExpression inner = az.infixExpression(core);
    return inner == null || inner.getOperator() != o ? add(!iz.noParenthesisRequired(core) ? x : core, $)
        : flatten.into(o, copy.adjust(o, hop.operands(inner)), $);
  }

  @NotNull
  private static List<Expression> into(final Operator o, @NotNull final Iterable<Expression> xs, @NotNull final List<Expression> $) {
    xs.forEach(λ -> into(o, λ, $));
    return $;
  }
}
