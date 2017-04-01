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

/** convert {@code
 * T a = new ArrayList<>() 
 * a.addAll(x)
 * } to {@code
 * T a = new ArrayList<>(x)
 * }
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM} {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-02 */
public final class LocalInitializedNewAddAll extends LocalInitialized //
    implements TipperCategory.Inlining {
  private static final long serialVersionUID = -228096256168103399L;

  public LocalInitializedNewAddAll() {
    andAlso("Initializer is a 'new Type(...)' expression", //
        () -> iz.not.null¢(newExpression = az.classInstanceCreation(initializer)));
    andAlso("Instance creation takes no argments ", //
        () -> newExpression.arguments().isEmpty()); //
    andAlso("Extract type", //
        () -> iz.not.null¢(type = newExpression.getType()));
    andAlso("Next statement is a method invocation", //
        () -> iz.not.null¢(methodInvocation = az.methodInvocation(nextStatement)));
    andAlso("Receiver of invocation is current variable", //
        () -> wizard.eq(name, methodInvocation.getExpression()));
    andAlso("Method name is 'addAll'", //
        () -> "addAll".equals(methodInvocation.getName() + ""));
    andAlso("Method has only one argument", //
        () -> iz.not.null¢(argument = onlyOne(arguments(methodInvocation))));
  }

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Inline variable '" + name(¢) + "' into next statement";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    final ListRewrite listRewrite = $.getListRewrite(newExpression, ClassInstanceCreation.ARGUMENTS_PROPERTY);
    listRewrite.insertFirst(copy.of(argument), g);
    remove.statement(nextStatement, $, g);
    return $;
  }

  private ClassInstanceCreation newExpression;
  @SuppressWarnings({ "unused", "FieldCanBeLocal" }) private Type type;
  private Expression argument;
  private MethodInvocation methodInvocation;
}
