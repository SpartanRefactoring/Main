package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.patterns.*;
import il.org.spartan.utils.*;
import il.org.spartan.utils.fluent.*;

/** convert {@code
 * T a = new ArrayList<>()
 * a.addAll(x)
 * } to {@code
 * T a = new ArrayList<>(x)
 * }
 * @author Yossi Gil
 * @author Yossi Gil
 * @since 2017-03-02 */
public final class LocalInitializedNewAddAll extends LocalInitialized //
    implements TipperCategory.Inlining {
  private static final long serialVersionUID = -0x32A5C56237F0DE7L;

  public LocalInitializedNewAddAll() {
    andAlso("Initializer is a 'new Type(...)' expression", //
        () -> not.null¢(newExpression = az.classInstanceCreation(initializer)));
    andAlso("Instance creation takes no argments ", //
        () -> newExpression.arguments().isEmpty()); //
    andAlso("Extract type", //
        () -> not.null¢(type = newExpression.getType()));
    andAlso("Next statement is a method invocation", //
        () -> not.null¢(methodInvocation = az.methodInvocation(nextStatement)));
    andAlso("Receiver of invocation is current variable", //
        () -> wizard.eq(name, methodInvocation.getExpression()));
    andAlso("Method name is 'addAll'", //
        () -> "addAll".equals(methodInvocation.getName() + ""));
    andAlso("Method has only one argument", //
        () -> not.null¢(argument = onlyOne(arguments(methodInvocation))));
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

  @Override public Examples examples() {
    return convert("T a = new ArrayList<>(); a.addAll(x);")//
        .to("new ArrayList<>(x)");
  }
}
