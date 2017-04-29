package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.utils.*;
import nano.ly.*;

/** convert {@code
 * T a = an.empty.list()
 * a.addAll(x)
 * } to {@code
 * T a = as.list(x)
 * }
 * @author Yossi Gil
 * @author Yossi Gil
 * @since 2017-03-02 */
public final class LocalInitializedNewAddAll extends LocalInitialized {
  private static final long serialVersionUID = 2617178983354697602L;

  public LocalInitializedNewAddAll() {
    andAlso("Initializer is a 'new Type(...)' expression", //
        () -> not.nil(newExpression = az.classInstanceCreation(initializer)));
    andAlso("Instance creation takes no argments ", //
        () -> newExpression.arguments().isEmpty()); //
    andAlso("Extract __", //
        () -> not.nil(type = newExpression.getType()));
    andAlso("Next statement is a method invocation", //
        () -> not.nil(methodInvocation = az.methodInvocation(nextStatement)));
    andAlso("Receiver of invocation is current variable", //
        () -> wizard.eq(name, methodInvocation.getExpression()));
    andAlso("Method name is 'addAll'", //
        () -> "addAll".equals(methodInvocation.getName() + ""));
    andAlso("Method has only one argument", //
        () -> not.nil(argument = the.onlyOneOf(arguments(methodInvocation))));
  }

  @Override public String description() {
    return "Inline variable '" + name + "' into next statement";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    $.getListRewrite(newExpression, ClassInstanceCreation.ARGUMENTS_PROPERTY).insertFirst(copy.of(argument), g);
    remove.statement(nextStatement, $, g);
    return $;
  }

  private ClassInstanceCreation newExpression;
  @SuppressWarnings({ "unused", "FieldCanBeLocal" }) private Type type;
  private Expression argument;
  private MethodInvocation methodInvocation;

  // TODO: add Example Tests that work
  @Override public Examples examples() {
    return null;
  }
}
