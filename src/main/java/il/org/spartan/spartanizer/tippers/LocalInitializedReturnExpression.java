package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.utils.Proposition.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.patterns.*;
import il.org.spartan.utils.*;

/** See {@link #examples()}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-08-07 */
public final class LocalInitializedReturnExpression extends LocalInitializedStatement //
    implements TipperCategory.Inlining {
  private static final long serialVersionUID = 6685864331590860235L;
  private ReturnStatement returnStatement;
  private Expression returnValue;

  public LocalInitializedReturnExpression() {//
    andAlso("Variable not used in subsequent initializers", //
        () -> !usedInSubsequentInitializers());//
    andAlso("Next statement is return", //
        () -> iz.not.null¢(returnStatement = az.returnStatement(nextStatement)));//
    andAlso("Next statement returns a value return", //
        () -> iz.not.null¢(returnValue = returnStatement.getExpression()));//
    andAlso("Returned value is not a method invocation of a lambda expression ", //
        ()-> !iz.lambdaExpression(initializer));//
    andAlso(//
        that("Returned value is identical to local variable", //
            () -> wizard.eq(name, returnValue)//
        ).or(//
            "Initializer has no side effects", () -> sideEffects.free(initializer)//
        ));
    andAlso("Returned expression does not modify local variable", //
        () -> compute.updateSpots(returnValue).stream().noneMatch(λ -> wizard.eq(λ, name)));
  }

  @Override public Examples examples() {
    return convert("int a = 3; return a;").to("return 3;"). //
        convert("int a = 3; return 2 * a;").to("return 2 * 3;") //
        .ignores("int a = 3; return a *=2;")//
        .ignores("int a = 3; return a =2;");//
  }

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Eliminate local " + ¢.getName() + " and inline its value into the expression of the subsequent return statement";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    new Inliner(name, $, g).byValue(copy.of(initializer)).inlineInto(returnValue);
    remove.deadFragment(current, $, g);
    return $;
  }
}
