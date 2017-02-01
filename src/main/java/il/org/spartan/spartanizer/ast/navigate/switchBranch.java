/** TODO: YuvalSimon <yuvaltechnion@gmail.com> please add a description
 * @author YuvalSimon <yuvaltechnion@gmail.com>
 * @since Jan 15, 2017 */
package il.org.spartan.spartanizer.ast.navigate;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class switchBranch {
  private final List<SwitchCase> cases;
  private final List<Statement> statements;
  private int hasDefault;
  private int numOfStatements;
  private int numOfNodes;
  private int depth;
  private int sequencerLevel;
  public static final int MAX_CASES_FOR_SPARTANIZATION = 10;

  public switchBranch(final List<SwitchCase> cases, final List<Statement> statements) {
    this.cases = cases;
    this.statements = statements;
    hasDefault = numOfNodes = numOfStatements = depth = sequencerLevel = -1;
  }

  public List<SwitchCase> cases() {
    return cases;
  }

  public List<Statement> statements() {
    return statements;
  }

  @SuppressWarnings("boxing") public boolean hasDefault() {
    if (hasDefault == -1)
      hasDefault = cases.stream().filter(SwitchCase::isDefault).map(λ -> 1).findFirst().orElse(hasDefault);
    return hasDefault == 1;
  }

  public int depth() {
    if (depth < 0)
      depth = metrics.height(statements, 0);
    return depth;
  }

  public int statementsNum() {
    if (numOfStatements < 0)
      numOfStatements = metrics.countStatements(statements);
    return numOfStatements;
  }

  public int nodesNum() {
    if (numOfNodes < 0)
      numOfNodes = metrics.nodes(statements);
    return numOfNodes;
  }

  public int casesNum() {
    return cases.size();
  }

  public int sequencerLevel() {
    if (sequencerLevel < 0) {
      final int th = metrics.countStatementsOfType(statements, ASTNode.THROW_STATEMENT),
          re = metrics.countStatementsOfType(statements, ASTNode.RETURN_STATEMENT),
          br = metrics.countStatementsOfType(statements, ASTNode.BREAK_STATEMENT),
          co = metrics.countStatementsOfType(statements, ASTNode.CONTINUE_STATEMENT), sum = th + re + br + co;
      assert sum > 0;
      sequencerLevel = sum > th && sum > re && sum > br && sum > co ? 0 : th > 0 ? 1 : re > 0 ? 2 : br > 0 ? 3 : 4;
    }
    return sequencerLevel;
  }

  /** @param ¢
   * @return returns 1 if _this_ has better metrics than b (i.e should come
   *         before b in the switch), -1 otherwise */
  private boolean compare(@NotNull final switchBranch ¢) {
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

  public boolean compareTo(@NotNull final switchBranch ¢) {
    final boolean $ = compare(¢);
    return $ != ¢.compare(this) ? $ : (lisp.first(cases) + "").compareTo(lisp.first(¢.cases()) + "") < 0;
  }

  private void addAll(@NotNull final List<Statement> ¢) {
    ¢.addAll(cases.stream().map(copy::of).collect(Collectors.toList()));
    ¢.addAll(statements.stream().map(copy::of).collect(Collectors.toList()));
  }

  private static void addAll(@NotNull final List<Statement> ss, @NotNull final List<switchBranch> bs) {
    bs.forEach(λ -> λ.addAll(ss));
  }

  @NotNull
  public static SwitchStatement makeSwitchStatement(@NotNull final List<switchBranch> bs, final Expression x, @NotNull final AST t) {
    final SwitchStatement $ = t.newSwitchStatement();
    $.setExpression(copy.of(x));
    addAll(step.statements($), bs);
    return $;
  }

  @NotNull
  @SuppressWarnings("null") public static List<switchBranch> intoBranches(@NotNull final SwitchStatement n) {
    List<SwitchCase> c = null;
    List<Statement> s = null;
    final List<switchBranch> $ = new ArrayList<>();
    final List<Statement> l = step.statements(n);
    boolean nextBranch = true;
    assert iz.switchCase(lisp.first(l));
    for (int ¢ = 0; ¢ < l.size() - 1; ++¢) {
      if (nextBranch) {
        c = new ArrayList<>();
        s = new ArrayList<>();
        $.add(new switchBranch(c, s));
        nextBranch = false;
        while (iz.switchCase(l.get(¢)) && ¢ < l.size() - 1)
          c.add(az.switchCase(l.get(¢++)));
        if (¢ >= l.size() - 1)
          break;
      }
      if (iz.switchCase(l.get(¢ + 1)) && iz.sequencerComplex(l.get(¢)))
        nextBranch = true;
      s.add(l.get(¢));
    }
    if (!iz.switchCase(lisp.last(l))) {
      s.add(lisp.last(l));
      if (!iz.sequencerComplex(lisp.last(l)))
        s.add(n.getAST().newBreakStatement());
    } else {
      if (!s.isEmpty()) {
        c = new ArrayList<>();
        s = new ArrayList<>();
        $.add(new switchBranch(c, s));
      }
      c.add(az.switchCase(lisp.last(l)));
      s.add(n.getAST().newBreakStatement());
    }
    return $;
  }

  public boolean hasSameBody(@NotNull final switchBranch ¢) {
    return wizard.same(functionalCommands(), ¢.functionalCommands());
  }

  @NotNull
  private List<Statement> functionalCommands() {
    final List<Statement> $ = new ArrayList<>();
    for (int ¢ = 0; ¢ < statements.size() - 1; ++¢)
      $.add(statements.get(¢));
    if (!iz.breakStatement(lisp.last(statements)))
      $.add(lisp.last(statements));
    return $;
  }

  public boolean hasFallThrough() {
    return statements.stream().anyMatch(iz::switchCase);
  }

  @Nullable
  public static Statement removeBreakSequencer(@NotNull final Statement s) {
    if (!iz.sequencerComplex(s, ASTNode.BREAK_STATEMENT))
      return copy.of(s);
    final AST a = s.getAST();
    Statement $ = null;
    if (iz.ifStatement(s)) {
      final IfStatement t = az.ifStatement(s), f = a.newIfStatement();
      f.setExpression(copy.of(step.expression(t)));
      f.setThenStatement(removeBreakSequencer(step.then(t)));
      f.setElseStatement(removeBreakSequencer(step.elze(t)));
      $ = f;
    } else if (!iz.block(s)) {
      if (iz.breakStatement(s) && iz.block(s.getParent()))
        $ = a.newEmptyStatement();
    } else {
      final Block b = a.newBlock();
      step.statements(b).addAll(removeBreakSequencer(step.statements(az.block(s))));
      $ = b;
    }
    return $;
  }

  @NotNull
  public static List<Statement> removeBreakSequencer(@NotNull final List<Statement> ss) {
    final List<Statement> $ = new ArrayList<>();
    for (final Statement ¢ : ss) {
      final Statement s = removeBreakSequencer(¢);
      if (s != null)
        $.add(s);
    }
    return $;
  }

  public boolean hasStatements() {
    return !statements.isEmpty() && !iz.breakStatement(lisp.first(statements));
  }
}
