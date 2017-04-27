package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.utils.Proposition.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.patterns.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;
import nano.ly.*;

/** See {@link #examples()}
 * @author Yossi Gil
 * @since 2015-08-07 */
public final class LocalInitializedReturnExpression extends LocalInitializedStatement //
    implements TipperCategory.Inlining {
  private static final long serialVersionUID = 0x5CC8F62FB4ED7DCBL;
  private ReturnStatement returnStatement;
  private Expression returnValue;

  public LocalInitializedReturnExpression() {//
    andAlso("Variable not used in subsequent initializers", //
        () -> !usedInLaterInitializers());//
    andAlso("Next statement is return", //
        () -> not.nil(returnStatement = az.returnStatement(nextStatement)));//
    andAlso("Next statement returns a value return", //
        () -> not.nil(returnValue = returnStatement.getExpression()));//
    andAlso("Returned value is not a lambda expression ", //
        () -> !iz.lambdaExpression(initializer));//
    andAlso(//
        that("Returned value is identical to local variable", //
            () -> wizard.eq(name, returnValue)//
        ).or("Initializer has no side effects", //
            () -> sideEffects.free(initializer)//
        ).and("Replacement reduces the numer of tokens", //
            () -> saving() > waste()));
    andAlso("Returned expression does not modify our local", //
        () -> compute.updateSpots(returnValue).stream().noneMatch(λ -> wizard.eq(λ, name)));
  }

  @Override public Examples examples() {
    return convert("int a = 3; return a;").to("return 3;"). //
        convert("int a = 3; return 2 * a;").to("return 2 * 3;") //
        .ignores("int a = 3; return a *=2;")//
        .ignores("int a = 3; return a =2;");//
  }

  @Override public String description() {
    return "Eliminate local " + name + " and inline its value into the expression of the subsequent return statement";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    new Inliner(name, $, g).byValue(Inliner.protect(initializer)).inlineInto(returnValue);
    remove.deadFragment(current, $, g);
    return $;
  }
}
