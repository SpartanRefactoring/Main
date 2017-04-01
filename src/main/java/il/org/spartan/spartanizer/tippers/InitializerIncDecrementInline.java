package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;
import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import static il.org.spartan.Utils.*;

/** inline inc/decrement into variable initialization
 * @author Niv Shalmon <tt>shalmon.niv@gmail.com</tt>
 * @since 2017-03-26 */
public class InitializerIncDecrementInline extends EagerTipper<VariableDeclarationFragment>//
    implements TipperCategory.Inlining {
  private static final long serialVersionUID = 7932037869493886974L;

  @Override public Tip tip(@NotNull final VariableDeclarationFragment a) {
    return !canWork(a) ? null : new Tip(description(a), a, getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        ExpressionStatement sss = az.expressionStatement(extract.nextStatement(a));
        if (sss == null)
          return;
        PrefixExpression e = az.prefixExpression(sss.getExpression());
        InfixExpression.Operator o = e.getOperator().equals(INCREMENT) ? PLUS2 : MINUS2;
        VariableDeclarationFragment $ = copy.of(a);
        $.setInitializer(subject.operands(a.getInitializer(), a.getAST().newNumberLiteral("1")).to(o));
        r.replace(a, $, g);
        r.remove(extract.nextStatement(a), g);
      }
    };
  }

  private static boolean canWork(final VariableDeclarationFragment a) {
    VariableDeclarationStatement p = az.variableDeclarationStatement(a.getParent());
    if (p == null)
      return false;
    ExpressionStatement s = az.expressionStatement(extract.nextStatement(a));
    if (s == null)
      return false;
    PrefixExpression e = az.prefixExpression(s.getExpression());
    if (e == null || !in(e.getOperator(), INCREMENT, DECREMENT) || !iz.simpleName(e.getOperand()))
      return false;
    SimpleName n = az.simpleName(e.getOperand());
    if (n.getIdentifier().equals(a.getName().getIdentifier()) && a.getInitializer() != null)
      return true;
    return false;
  }

  @Override public String description(@SuppressWarnings("unused") VariableDeclarationFragment n) {
    return "inline increment/decrement of variable into variable initialization";
  }
}
