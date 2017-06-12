package il.org.spartan.spartanizer.tippers;

import static fluent.ly.is.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.utils.*;

/** inline inc/decrement into variable initialization tests in {@link Issue1146}
 * @author Niv Shalmon <tt>shalmon.niv@gmail.com</tt>
 * @since 2017-03-26 */
public class LocalInitializedIncrementDecrementInline extends LocalInitialized//
    implements Category.Inlining {
  private static final long serialVersionUID = -0x4EB708E6DF95CDCDL;

  public LocalInitializedIncrementDecrementInline() {
    andAlso("Has increment/decrement of variable afterwards", () -> {
      final ExpressionStatement s = az.expressionStatement(nextStatement);
      if (s == null)
        return false;
      final PrefixExpression $ = az.prefixExpression(s.getExpression());
      return $ != null && in($.getOperator(), INCREMENT, DECREMENT) //
          && iz.simpleName($.getOperand()) //
          && az.simpleName($.getOperand()).getIdentifier().equals(name.getIdentifier())
          && collect.usesOf(name).in(extract.fragments(declaration)).size() == 1;
    });
  }
  @Override public String description() {
    return "Consolidate initialization of " + name + " with subsequent increment/decrement";
  }
  @Override public Examples examples() {
    return //
    convert("int x = 1; ++x;")//
        .to("int x = 1+1;")//
        .ignores("int x = 1, y = x; ++x;");
  }
  @Override protected ASTNode[] span() {
    return as.array(current, nextStatement);
  }
  @Override protected ASTRewrite go(final ASTRewrite r, final TextEditGroup g) {
    final InfixExpression.Operator o = az.prefixExpression(az.expressionStatement(nextStatement)//
        .getExpression()).getOperator().equals(INCREMENT) ? op.PLUS2 : op.MINUS2;
    final VariableDeclarationFragment $ = copy.of(current());
    $.setInitializer(subject.operands(initializer, //
        initializer.getAST().newNumberLiteral("1")).to(o));
    r.replace(current(), $, g);
    r.remove(nextStatement, g);
    return r;
  }
}
