package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.spartanizer.patterns.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;
import org.eclipse.jdt.core.dom.Assignment.Operator;

/** inline arithmetics into variable initialization tests in {@link Issue0187}
 *  such as += and -=
 * @author Niv Shalmon <tt>shalmon.niv@gmail.com</tt>
 * @since 2017-04-18 */
public class LocalInitializedArithmeticsInline extends LocalInitialized//
    implements TipperCategory.Inlining {
  Operator o;
  Expression rightHandSide;
  public LocalInitializedArithmeticsInline() {
    andAlso("Has arithmetics of variable afterwards", () -> {
      final ExpressionStatement s = az.expressionStatement(nextStatement);
      if (s == null)
        return false;
      final Assignment $ = az.assignment(s.getExpression());
      if ($ == null)
        return false;
      o = $.getOperator();
      rightHandSide = $.getRightHandSide();
      return o != ASSIGN //
          && az.simpleName($.getLeftHandSide()).getIdentifier().equals(name.getIdentifier())
          && collect.usesOf(name).in(extract.fragments(declaration)).size() == 1;
    });
  }

  private static final long serialVersionUID = 0x6E14426AA4211FFEL;

  @Override public String description() {
    return "Consolidate initialization of " + name + " with subsequent arithmetics assign";
  }

  @Override public Examples examples() {
    return //
    convert("int x = 1; x+=1;")//
        .to("int x = 1+1;")//
        .ignores("int x = 1, y = x; x*=3;");
  }
  
  @Override protected ASTNode[] span() {
    return as.array(current,nextStatement);
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final TextEditGroup g) {
    final VariableDeclarationFragment $ = copy.of(current());
    $.setInitializer(subject.operands(subject.operand(initializer).parenthesis(), //
        rightHandSide).to(assign2infix(o)));
    r.replace(current(), $, g);
    r.remove(nextStatement, g);
    return r;
  }
}
