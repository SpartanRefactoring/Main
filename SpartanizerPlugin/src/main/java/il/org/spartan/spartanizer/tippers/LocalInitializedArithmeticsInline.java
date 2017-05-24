package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.utils.*;

/** inline arithmetics into variable initialization tests in {@link Issue0187}
 * such as += and -=
 * @author Niv Shalmon <tt>shalmon.niv@gmail.com</tt>
 * @since 2017-04-18 */
public class LocalInitializedArithmeticsInline extends LocalInitialized {
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
      return o != ASSIGN && iz.simpleName($.getLeftHandSide()) && az.simpleName($.getLeftHandSide()).getIdentifier().equals(name.getIdentifier())
          && collect.usesOf(name).in(extract.fragments(declaration)).size() == 1 && collect.usesOf(name).in(rightHandSide).isEmpty()
          && extract.fragments(declaration).stream()
              .allMatch((final VariableDeclarationFragment f) -> collect.usesOf(f.getName()).in(rightHandSide).isEmpty());
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
    return as.array(current, nextStatement);
  }
  @Override protected ASTRewrite go(final ASTRewrite r, final TextEditGroup g) {
    final VariableDeclarationFragment $ = copy.of(current());
    $.setInitializer(subject.operands(subject.operand(initializer).parenthesis(), //
        subject.operand(rightHandSide).parenthesis()).to(op.assign2infix(o)));
    r.replace(current(), $, g);
    r.remove(nextStatement, g);
    return r;
  }
}
