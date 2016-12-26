package il.org.spartan.athenizer.inflate.expanders;

import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/**
 * converts
 * 
 * <pre>
 * arr[i++] = y;
 * 
 * arr[++i] = z;
 * <pre>
 * 
 * to
 * 
 * <pre>
 * arr[i] = y;
 * ++i;
 * 
 * ++i;
 * arr[i] = z;
 * <pre>
 * 
 * does not expand if right hand side includes access index operand, such as in arr[i]=i
 * 
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2016-12-25
 */
public class OutlineArrayAccess extends CarefulTipper<ArrayAccess> implements TipperCategory.InVain {
  @SuppressWarnings("unused")
  @Override public String description(ArrayAccess n) {
    return null;
  }
  @Override public Tip tip(ArrayAccess a) {
    Expression e = duplicate.of(a.getIndex());
    ASTNode b = extract.containingStatement(a);
    AST t = b.getParent().getAST();
    return new Tip(description(a), a, this.getClass()) {
      @Override public void go(ASTRewrite r, TextEditGroup g) {
        final ListRewrite l = r.getListRewrite(b.getParent(), Block.STATEMENTS_PROPERTY);
        ArrayAccess newa = duplicate.of(a);
        if(iz.postfixExpression(e)) {
          SimpleName ee = a.getAST().newSimpleName(identifier(az.simpleName(operand(az.postfixExpression(e)))));
          newa.setIndex(ee);
          l.insertAfter(t.newExpressionStatement(e), b, g);
        }
        else {
          SimpleName ee = a.getAST().newSimpleName(identifier(az.simpleName(operand(az.prefixExpression(e)))));
          newa.setIndex(ee);
          l.insertBefore(t.newExpressionStatement(e), b, g);
        }
        r.replace(a, newa, g);
      }
    };
  }
  @Override protected boolean prerequisite(ArrayAccess a) {
    Expression e = a.getIndex();
    SimpleName name;
    ASTNode b = extract.containingStatement(a);
    if(!iz.block(b.getParent()))
      return false;
    if(!iz.expressionStatement(b))
      return false;
    if(!iz.incrementOrDecrement(e) || iz.assignment(e))
      return false;
    if(iz.prefixExpression(e))
      name = az.simpleName(az.prefixExpression(e));
    else
      name = az.simpleName(az.postfixExpression(e));
    if(name == null)
      return false;
    ExpressionStatement ss = az.expressionStatement(b);
    Expression s = expression(ss);
    if(!iz.assignment(s) || !left(az.assignment(s)).equals(a))
      return false;
    Expression n = right(az.assignment(s));
    return iz.plainAssignment(az.assignment(s)) && !containsName(name, n);
  }
   
  private static boolean containsName(SimpleName n, Expression e) {
    searchDescendants<SimpleName> f = searchDescendants.forClass(SimpleName.class);
    return !f.suchThat(new Predicate<SimpleName>() {
      @Override public boolean test(SimpleName t) {
        return identifier(t).equals(identifier(n));
      }
    }).inclusiveFrom(e).isEmpty();
  }
}