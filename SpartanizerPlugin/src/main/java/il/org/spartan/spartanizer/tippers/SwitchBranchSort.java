package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Sorts switch branches according to the metrics: 1. Depth - height of ast 2.
 * Length measured in statements 3. Length measured in nodes 4. Sequencer level
 * 5. Number of case that use the branch
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-11 [[SuppressWarningsSpartan]] */
public class SwitchBranchSort extends ReplaceCurrentNode<SwitchStatement> implements TipperCategory.Sorting {
  @Override public ASTNode replacement(final SwitchStatement n) {
    final List<Branch> b = intoBranches(n);
    int ind = -1;
    for (int i = 0; i < b.size() - 1; ++i)
      if (b.get(i).compareTo(b.get(i + 1)) <= 0) {
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
        b.get(i).addAll(l);
        ++i;
      } else
        b.get(i).addAll(l);
    return s;
  }

  @Override public String description(@SuppressWarnings("unused") final SwitchStatement __) {
    // TODO Auto-generated method stub
    return null;
  }

  private static List<Branch> intoBranches(final SwitchStatement n) {
    List<SwitchCase> c = new ArrayList<>();
    List<Statement> s = new ArrayList<>();
    final List<Branch> b = new ArrayList<>();
    b.add(new Branch(c, s));
    boolean nextBranch = false;
    boolean passedCases = false;
    for (final Statement ss : step.statements(n)) {
      if (nextBranch && iz.switchCase(ss))
        passedCases = false;
      if (nextBranch) {
        b.add(new Branch(c, s));
        c = new ArrayList<>();
        s = new ArrayList<>();
        nextBranch = false;
      }
      if (!passedCases) {
        if (iz.switchCase(ss)) {
          c.add(az.switchCase(ss));
          continue;
        }
        passedCases = true;
      }
      s.add(ss);
      if (iz.sequencer(ss))
        nextBranch = true;
      else
        nextBranch = false;
    }
    return b;
  }
}

/** [[SuppressWarningsSpartan]] */
class Branch {
  List<SwitchCase> cases;
  List<Statement> statements;
  boolean hasDefault;
  int numOfStatements;
  int numOfNodes;
  int depth;

  public Branch(final List<SwitchCase> cases, final List<Statement> statements) {
    this.cases = cases;
    this.statements = statements;
    numOfNodes = numOfStatements = depth = -1; // lazy evaluation
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

  @SuppressWarnings("static-method") int sequencerLevel() {
    // TODO: finish this
    return 0;
  }

  int compareTo(final Branch b) {
    if (hasDefault)
      return -1;
    if (b.hasDefault)
      return 1;
    if (depth() < b.depth())
      return 1;
    if (statementsNum() < b.statementsNum())
      return 1;
    if (nodesNum() < b.nodesNum())
      return 1;
    if (sequencerLevel() < b.sequencerLevel()) // check what should be here
      return 1;
    if (casesNum() < b.casesNum())
      return 1;
    return -1;
  }

  void addAll(final List<Statement> ss) {
    for (final SwitchCase ¢ : cases)
      ss.add(copy.of(¢));
    for (final Statement ¢ : statements)
      ss.add(copy.of(¢));
  }
}
