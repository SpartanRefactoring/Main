package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.lisp;

/** Sorts switch branches according to the metrics: 1. Depth - height of ast 2.
 * Length measured in statements 3. Length measured in nodes 4. Sequencer level
 * 5. Number of case that use the branch
 * Test case is {@link Issue0861}
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-11 [[SuppressWarningsSpartan]] */
public class SwitchBranchSort extends ReplaceCurrentNode<SwitchStatement> implements TipperCategory.Sorting {
  @Override public ASTNode replacement(final SwitchStatement n) {
    final List<Branch> b = intoBranches(n);
    int ind = -1;
    for (int i = 0; i < b.size() - 1; ++i)
      if (!b.get(i).compareTo(b.get(i + 1))) {
        ind = i;
        break;
      }
    if (ind < 0)
      return null;
    final SwitchStatement s = n.getAST().newSwitchStatement();
    s.setExpression(copy.of(step.expression(n)));
    final List<Statement> l = step.statements(s);
    for (int i = 0; i < b.size(); ++i)
      if (i == ind) {
        b.get(i + 1).addAll(l);
        b.get(i++).addAll(l);
      } else
        b.get(i).addAll(l);
    return s;
  }

  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Sort switch branches";
  }

  @SuppressWarnings("null")
  private static List<Branch> intoBranches(final SwitchStatement n) {
    List<SwitchCase> c = null;
    List<Statement> s = null;
    final List<Branch> b = new ArrayList<>();
    List<Statement> l = step.statements(n);
    boolean nextBranch = true;
    assert iz.switchCase(lisp.first(l));
    for(int i = 0; i < l.size()-1; ++i) {
      if(nextBranch) {
        c = new ArrayList<>();
        s = new ArrayList<>();
        b.add(new Branch(c, s));
        nextBranch = false;
        while(iz.switchCase(l.get(i)) && i < l.size()-1)
          c.add(az.switchCase(l.get(i++)));
        if(i >= l.size()-1)
          break;
      }
      if(iz.switchCase(l.get(i+1)) && iz.sequencerComplex(l.get(i)))
        nextBranch = true;
      s.add(l.get(i));
    }
    if(iz.switchCase(lisp.last(l))) {
      if(!s.isEmpty()) {
        c = new ArrayList<>();
        s = new ArrayList<>();
        b.add(new Branch(c, s));
      }
      c.add(az.switchCase(lisp.last(l)));
      s.add(n.getAST().newBreakStatement());
    }
    else
      s.add(lisp.last(l));
    return b;
  }
}

/** [[SuppressWarningsSpartan]] */
class Branch {
  List<SwitchCase> cases;
  List<Statement> statements;
  int hasDefault;
  int numOfStatements;
  int numOfNodes;
  int depth;
  int sequencerLevel;

  public Branch(final List<SwitchCase> cases, final List<Statement> statements) {
    this.cases = cases;
    this.statements = statements;
    numOfNodes = numOfStatements = depth = sequencerLevel = -1; // lazy evaluation
    hasDefault = -1;
  }
  
  boolean hasDefault() {
    if(hasDefault == -1) {
      hasDefault = 0;
      for(SwitchCase c : cases)
        if(c.isDefault()) {
          hasDefault = 1;
          break;
        }
    }
    return hasDefault == 1;
  }
  
  int depth() {
    if (depth < 0)
      depth = metrics.height(statements, 0);
    return depth;
  }

  int statementsNum() {
    if (numOfStatements < 0)
      numOfStatements = metrics.countStatements(statements);
    return numOfStatements;
  }

  int nodesNum() {
    if (numOfNodes < 0)
      numOfNodes = metrics.nodes(statements);
    return numOfNodes;
  }

  int casesNum() {
    return cases.size();
  }

  int sequencerLevel() {
    if(sequencerLevel < 0) {
      int th = metrics.countStatementsOfType(statements, ASTNode.THROW_STATEMENT);
      int re = metrics.countStatementsOfType(statements, ASTNode.RETURN_STATEMENT);
      int br = metrics.countStatementsOfType(statements, ASTNode.BREAK_STATEMENT);
      int co = metrics.countStatementsOfType(statements, ASTNode.CONTINUE_STATEMENT);
      int sum = th+re+br+co;
      assert sum > 0;
      if(sum > th && sum > re && sum > br && sum > co)
        sequencerLevel = 0;
      else if(th > 0)
        sequencerLevel = 1;
      else if(re > 0)
        sequencerLevel = 2;
      else if(br > 0)
        sequencerLevel = 3;
      else
        sequencerLevel = 4;
    }
    return sequencerLevel;
  }

  /** @param b
   * @return returns 1 if _this_ has better metrics than b (i.e should come before b in the switch), -1 otherwise
   */
  boolean compare(final Branch b) {
    if (hasDefault())
      return false;
    if (b.hasDefault())
      return true;
    if (sequencerLevel() > 0 && b.sequencerLevel() > 0) {
      if(sequencerLevel() > b.sequencerLevel())
        return false;
      if(sequencerLevel() < b.sequencerLevel())
        return true;
    }
    if (depth() < b.depth())
      return true;
    if (statementsNum() < b.statementsNum())
      return true;
    if (nodesNum() < b.nodesNum())
      return true;
    if (casesNum() < b.casesNum())
      return true;
    
    return false;
  }
  
  boolean compareTo(final Branch b) {
    if(compare(b))
      return true;
    if(!b.compare(this))
      return this.hashCode() < b.hashCode();
    return false;
  }

  void addAll(final List<Statement> ss) {
    for (final SwitchCase ¢ : cases)
      ss.add(copy.of(¢));
    for (final Statement ¢ : statements)
      ss.add(copy.of(¢));
  }
}
