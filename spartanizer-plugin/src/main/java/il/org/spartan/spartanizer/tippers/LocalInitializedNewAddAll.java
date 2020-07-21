package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.utils.*;

/** convert {@code
 * T a = an.empty.list()
 * a.addAll(x)
 * } to {@code
 * T a = as.list(x)
 * } tests in {@link Issue1314}
 * @author Yossi Gil
 * @since 2017-03-02 */
public final class LocalInitializedNewAddAll extends LocalInitialized {
  private static final long serialVersionUID = 0x2452166B40798782L;

  public LocalInitializedNewAddAll() {
    needs("Initializer is a 'new Type(...)' expression", //
        () -> newExpression = az.classInstanceCreation(initializer));
    andAlso("Instance creation takes no argments ", //
        () -> newExpression.arguments().isEmpty()); //
    needs("Type", //
        () -> type = newExpression.getType());
    needs("next statement is an expression statement", //
        () -> nextExpressionStatement = az.expressionStatement(nextStatement));
    needs("Next statement is a method invocation", //
        () -> methodInvocation = az.methodInvocation(nextExpressionStatement.getExpression()));
    andAlso("Receiver of invocation is current variable", //
        () -> wizard.eq(name, methodInvocation.getExpression()));
    andAlso("Method name is 'addAll'", //
        () -> "addAll".equals(methodInvocation.getName() + ""));
    needs("Single argument to 'addAll'", //
        () -> argument = the.onlyOneOf(arguments(methodInvocation)));
  }
  @Override public String description() {
    return "Collapse addAll into constructor of '" + name + "'";
  }
  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    $.getListRewrite(newExpression, ClassInstanceCreation.ARGUMENTS_PROPERTY).insertFirst(copy.of(argument), g);
    remove.statement(nextStatement, $, g);
    return $;
  }

  ClassInstanceCreation newExpression;
  @SuppressWarnings({ "unused", "FieldCanBeLocal" }) private Type type;
  Expression argument;
  MethodInvocation methodInvocation;
  ExpressionStatement nextExpressionStatement;

  @Override public Examples examples() {
    return convert("List<T> x = new ArrayList<>(); x.addAll(ys);").to("List<T> x = new ArrayList<>(ys);");
  }
}
