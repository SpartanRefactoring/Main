package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.spartanizer.patterns.*;

/** inline inc/decrement into variable initialization
 * tests in {@link Issue1146}
 * @author Niv Shalmon <tt>shalmon.niv@gmail.com</tt>
 * @since 2017-03-26 */
public class InitializerIncDecrementInline extends LocalVariableInitialized//
    implements TipperCategory.Inlining {
  public InitializerIncDecrementInline() {
    andAlso("Has increment/decrement of variable afterwards",()-> {
      ExpressionStatement s = az.expressionStatement(nextStatement);
      if (s == null)
        return false;
      PrefixExpression $ = az.prefixExpression(s.getExpression());
      return $ != null && in($.getOperator(), INCREMENT, DECREMENT) //
          && iz.simpleName($.getOperand()) //
          && az.simpleName($.getOperand()).getIdentifier().equals(name.getIdentifier());
    }
    );
  }

  private static final long serialVersionUID = 7932037869493886974L;

  @Override public String description(@SuppressWarnings("unused") VariableDeclarationFragment __) {
    return "inline increment/decrement of variable into variable initialization";
  }

  @Override protected ASTRewrite go(ASTRewrite r, TextEditGroup g) {
    InfixExpression.Operator o = az.prefixExpression(az.expressionStatement(nextStatement)//
        .getExpression()).getOperator().equals(INCREMENT) ? PLUS2 : MINUS2;
    VariableDeclarationFragment $ = copy.of(current());
    $.setInitializer(subject.operands(initializer,//
        initializer.getAST().newNumberLiteral("1")).to(o));
    r.replace(current(), $, g);
    r.remove(nextStatement, g);
    return r;
  }
}
