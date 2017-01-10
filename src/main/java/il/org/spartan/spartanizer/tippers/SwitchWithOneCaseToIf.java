package il.org.spartan.spartanizer.tippers;

import java.util.*;
import static il.org.spartan.lisp.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;

/** convert
 *
 * <pre>
* switch (x) {
* case a: (commands)
*   break;
* default: (other commands)
* }
 * </pre>
 *
 * into
 *
 * <pre>
* if(x == a) {
*   (commands)
* }
* else {
*   (other commands)
* }
 * </pre>
 *
 * . Tested in {@link Issue0916}
 * @author Yuval Simon
 * @since 2016-12-18 */
public class SwitchWithOneCaseToIf extends ReplaceCurrentNode<SwitchStatement> implements TipperCategory.Collapse {
  @SuppressWarnings("unchecked")
  @Override public ASTNode replacement(final SwitchStatement s) {
    if (s == null)
      return null;
    final List<SwitchCase> $ = extract.switchCases(s);
    final List<Statement> ll = statements(s);
    AST a = s.getAST();
    Block res = a.newBlock();
    IfStatement r = a.newIfStatement();
    res.statements().add(r);
    Block b1 = a.newBlock();
    Block b2 = a.newBlock();
    r.setThenStatement(b1);
    r.setElseStatement(b2);
    InfixExpression cond = makeFrom(s, $, a);
    r.setExpression(cond);
    addStatements(firstComOfCase(s), s, b1);
    addStatements(firstComOfDefault(s), s, b2);
    return res;
  }

  @Override protected boolean prerequisite(final SwitchStatement s) {
    final List<SwitchCase> $ = extract.switchCases(s);
    final List<Statement> ll = statements(s);
    int ind = nsBranchBreakOrRetInd(s, 1);
    int ind2 = nsBranchBreakOrRetInd(s, 2);
    if(numBranches(s) != 2 || !defaultSingleBranch(s))
      return false;
    if(ind < 0)
      return false;
    if((iz.breakStatement(ll.get(ind)) && iz.switchCase(ll.get(ind - 1))) || (ind2 > 0 && iz.breakStatement(ll.get(ind2)) && iz.switchCase(ll.get(ind2 - 1))))
      return false;
    if(iz.switchCase(last(ll)))
      return false;
    if(iz.block(last(ll)))
      return false;
    return true;
  }

  @Override @SuppressWarnings("unused") public String description(final SwitchStatement __) {
    return "Convert switch statement to if-else statement";
  }
  
  @SuppressWarnings("unchecked")
  private static void addStatements(int x, SwitchStatement s, Block b) {
    int i;
    List<Statement> ll = statements(s);
    for(i = x; i < ll.size() - 1; ++i) {
      if(iz.switchCase(ll.get(i+1))) {
        if(!iz.breakStatement(ll.get(i)))
          b.statements().add(copy.of(ll.get(i)));
        break;
      }
      b.statements().add(copy.of(ll.get(i)));
    }
    if(i == ll.size() - 1 && !iz.breakStatement(ll.get(i)))
      b.statements().add(copy.of(ll.get(i)));
  }
  private static boolean defaultSingleBranch(SwitchStatement s) {
    List<Statement> l = statements(s);
    for(int i = 0; i < l.size(); ++i) {
      if(iz.switchCase(l.get(i)) && az.switchCase(l.get(i)).isDefault()) {
        if((i > 0 && iz.switchCase(l.get(i-1))) || (i < l.size() - 1 && iz.switchCase(l.get(i+1))))
          return false;
        return true;
      }
    }
    return false;
  }

  private static int nsBranchBreakOrRetInd(final SwitchStatement s, int n) {
    final List<Statement> l = step.statements(az.switchStatement(s));
    int x;
    int cur = n;
    for(x = 1; x < l.size(); ++x)
      if(iz.switchCase(l.get(x)) && !iz.switchCase(l.get(x-1)) && --cur == 0)
        break;
    if(x == l.size())
      return -1;
    Statement k = l.get(--x);
    if(iz.breakStatement(k) || iz.returnStatement(k))
      return x;
    return -1;
  }
  
  private static int numBranches(final SwitchStatement s) {
    final List<Statement> l = step.statements(az.switchStatement(s));
    int count = 1;
    for(int i = 1; i < l.size(); ++i)
      if(iz.switchCase(l.get(i)) && !iz.switchCase(l.get(i-1)))
        count++;
    return count;
  }
  
  private static InfixExpression makeFrom(SwitchStatement s, List<SwitchCase> l, AST a) {
    InfixExpression $ = null;
    for(SwitchCase c : l) {
      if(c.isDefault())
        continue;
      InfixExpression n = a.newInfixExpression();
      n.setOperator(InfixExpression.Operator.EQUALS);
      n.setLeftOperand(copy.of(expression(s)));
      n.setRightOperand(copy.of(expression(c)));
      if($ == null)
        $ = n;
      else {
        InfixExpression nn = a.newInfixExpression();
        nn.setOperator(InfixExpression.Operator.CONDITIONAL_OR);
        nn.setLeftOperand($);
        nn.setRightOperand(n);
        $ = nn;
      }
    }
    return $;
  }
  
  private static int firstComOfDefault(SwitchStatement s) {
    List<Statement> l = statements(s);
    for(int x = 0; x < l.size(); ++x)
      if(iz.switchCase(l.get(x)) && az.switchCase(l.get(x)).isDefault())
        return x+1;
    return -1;
  }
  
  private static int firstComOfCase(SwitchStatement s) {
    List<Statement> l = statements(s);
    for(int x = 0; x < l.size(); ++x)
      if(iz.switchCase(l.get(x)) && !az.switchCase(l.get(x)).isDefault() && !iz.switchCase(l.get(x+1)))
        return x+1;
    return -1;
  }
}
