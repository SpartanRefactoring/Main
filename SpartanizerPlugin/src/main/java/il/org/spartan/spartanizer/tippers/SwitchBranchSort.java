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
 * @since 2017-01-11 */
public class SwitchBranchSort extends ReplaceCurrentNode<SwitchStatement> implements TipperCategory.Sorting {
  @Override public ASTNode replacement(final SwitchStatement s) {
    final List<SwitchBranch> b = SwitchBranch.intoBranches(s);
    int ind = -1;
    for (int ¢ = 0; ¢ < b.size() - 1; ++¢)
      if (!b.get(¢).compareTo(b.get(¢ + 1))) {
        ind = ¢;
        break;
      }
    if (ind < 0)
      return null;
    final SwitchStatement $ = s.getAST().newSwitchStatement();
    $.setExpression(copy.of(step.expression(s)));
    final List<Statement> l = step.statements($);
    for (int ¢ = 0; ¢ < b.size(); ++¢)
      if (¢ != ind)
        b.get(¢).addAll(l);
      else {
        b.get(¢ + 1).addAll(l);
        b.get(¢++).addAll(l);
      }
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Sort switch branches";
  }
}

class SwitchBranch {
  List<SwitchCase> cases;
  List<Statement> statements;
  int hasDefault;
  int numOfStatements;
  int numOfNodes;
  int depth;
  int sequencerLevel;

  public SwitchBranch(final List<SwitchCase> cases, final List<Statement> statements) {
    this.cases = cases;
    this.statements = statements;
    hasDefault = numOfNodes = numOfStatements = depth = sequencerLevel = -1;
  }
  
  boolean hasDefault() {
    if(hasDefault == -1) {
      hasDefault = 0;
      for(SwitchCase ¢ : cases)
        if(¢.isDefault()) {
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
      sequencerLevel = sum > th && sum > re && sum > br && sum > co ? 0 : th > 0 ? 1 : re > 0 ? 2 : br > 0 ? 3 : 4;
    }
    return sequencerLevel;
  }

  /** @param ¢
   * @return returns 1 if _this_ has better metrics than b (i.e should come before b in the switch), -1 otherwise
   */
  boolean compare(final SwitchBranch ¢) {
    if (hasDefault())
      return false;
    if (¢.hasDefault())
      return true;
    if (sequencerLevel() > 0 && ¢.sequencerLevel() > 0) {
      if (sequencerLevel() > ¢.sequencerLevel())
        return false;
      if (sequencerLevel() < ¢.sequencerLevel())
        return true;
    }
    return depth() < ¢.depth() || statementsNum() < ¢.statementsNum() || nodesNum() < ¢.nodesNum() || casesNum() < ¢.casesNum();
  }
  
  boolean compareTo(final SwitchBranch ¢) {
    return compare(¢) || (!¢.compare(this) && this.hashCode() < ¢.hashCode());
  }

  void addAll(final List<Statement> ss) {
    for (final SwitchCase ¢ : cases)
      ss.add(copy.of(¢));
    for (final Statement ¢ : statements)
      ss.add(copy.of(¢));
  }
  
  @SuppressWarnings("null")
  static List<SwitchBranch> intoBranches(final SwitchStatement n) {
    List<SwitchCase> c = null;
    List<Statement> s = null;
    final List<SwitchBranch> $ = new ArrayList<>();
    List<Statement> l = step.statements(n);
    boolean nextBranch = true;
    assert iz.switchCase(lisp.first(l));
    for(int ¢ = 0; ¢ < l.size()-1; ++¢) {
      if(nextBranch) {
        c = new ArrayList<>();
        s = new ArrayList<>();
        $.add(new SwitchBranch(c, s));
        nextBranch = false;
        while(iz.switchCase(l.get(¢)) && ¢ < l.size()-1)
          c.add(az.switchCase(l.get(¢++)));
        if(¢ >= l.size()-1)
          break;
      }
      if(iz.switchCase(l.get(¢+1)) && iz.sequencerComplex(l.get(¢)))
        nextBranch = true;
      s.add(l.get(¢));
    }
    if (!iz.switchCase(lisp.last(l)))
      s.add(lisp.last(l));
    else {
      if (!s.isEmpty()) {
        c = new ArrayList<>();
        s = new ArrayList<>();
        $.add(new SwitchBranch(c, s));
      }
      c.add(az.switchCase(lisp.last(l)));
      s.add(n.getAST().newBreakStatement());
    }
    return $;
  }
}