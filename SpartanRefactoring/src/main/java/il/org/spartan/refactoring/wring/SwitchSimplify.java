package il.org.spartan.refactoring.wring;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.Is;
import il.org.spartan.refactoring.wring.Wring.ReplaceCurrentNode;

/**
 * A wring to remove ineffective cases from a switch statement. TODO Ori: add
 * tests
 *
 * @author Ori Roth
 * @since 2016/05/11
 */
public class SwitchSimplify extends ReplaceCurrentNode<SwitchStatement> {
  private final static boolean TRIM_AFTER_DEFAULT = true;
  private final ASTMatcher matcher = new ASTMatcher();

  @SuppressWarnings("javadoc") public static boolean compareStatementsLists(List<Statement> l1, List<Statement> l2, ASTMatcher m) {
    if (l1.size() != l2.size())
      return false;
    for (int i = 0; i < l1.size(); ++i)
      if (!l1.get(i).subtreeMatch(m, l2.get(i)))
        return false;
    return true;
  }
  @SuppressWarnings("javadoc") public static void insertUntilSequencer(List<Statement> f, List<Statement> ss, int i, AST t) {
    int c = i;
    while (c < f.size()) {
      if (!(f.get(c) instanceof SwitchCase))
        ss.add(f.get(c));
      if (Is.sequencer(f.get(c)))
        return;
      ++c;
    }
    ss.add(t.newBreakStatement());
  }
  static int evaluate(Statement c) {
    return c instanceof ThrowStatement ? 0 : c instanceof ReturnStatement ? 1 : c instanceof ContinueStatement ? 2 : 3;
  }
  @SuppressWarnings("unchecked") @Override ASTNode replacement(SwitchStatement n) {
    boolean d = false;
    boolean ed = false;
    final List<Map.Entry<SwitchCase, List<Statement>>> m1 = new ArrayList<>();
    for (final Statement s : (Iterable<Statement>) n.statements())
      if (s instanceof SwitchCase) {
        final List<Statement> l = new ArrayList<>();
        m1.add(m1.size(), new AbstractMap.SimpleEntry<>((SwitchCase) s, l));
        insertUntilSequencer(n.statements(), l, n.statements().indexOf(s) + 1, n.getAST());
        if (((SwitchCase) s).isDefault()) {
          d = true;
          if (n.statements().indexOf(s) == n.statements().size() - 1 || n.statements().indexOf(s) == n.statements().size() - 2
              && n.statements().get(n.statements().size() - 1) instanceof BreakStatement)
            ed = true;
          if (TRIM_AFTER_DEFAULT)
            break;
        }
      }
    final Map<SwitchCase, List<Statement>> m2 = new HashMap<>();
    for (final Map.Entry<SwitchCase, List<Statement>> c : m1)
      m2.put(c.getKey(), c.getValue());
    final List<List<SwitchCase>> m3 = new ArrayList<>();
    for (final Map.Entry<SwitchCase, List<Statement>> c : m1) {
      if ((!d || d && ed) && (c.getValue().isEmpty() || c.getValue().size() == 1 && c.getValue().get(0) instanceof BreakStatement))
        continue;
      boolean a = true;
      for (final List<SwitchCase> cl : m3)
        if (compareStatementsLists(c.getValue(), m2.get(cl.get(0)), matcher)) {
          if (!c.getKey().isDefault()) {
            if (!cl.get(0).isDefault())
              cl.add(cl.size(), c.getKey());
          } else {
            cl.clear();
            cl.add(c.getKey());
          }
          a = false;
          break;
        }
      if (a) {
        final List<SwitchCase> l = new ArrayList<>();
        l.add(c.getKey());
        m3.add(m3.size(), l);
      }
    }
    Collections.sort(m3, new Comparator<List<SwitchCase>>() {
      @Override public int compare(List<SwitchCase> o1, List<SwitchCase> o2) {
        return o1.get(0).isDefault() ? 1
            : o2.get(0).isDefault() ? -1
                : evaluate(m2.get(o1.get(0)).get(m2.get(o1.get(0)).size() - 1))
                    - evaluate(m2.get(o2.get(0)).get(m2.get(o2.get(0)).size() - 1));
      }
    });
    final SwitchStatement $ = n.getAST().newSwitchStatement();
    $.setExpression(scalpel.duplicate(n.getExpression()));
    for (final List<SwitchCase> cl : m3) {
      scalpel.duplicateInto(cl, $.statements());
      final List<Statement>[] ls = new List[cl.size()];
      for (int i = 0; i < cl.size(); ++i)
        ls[i] = m2.get(cl.get(i));
      scalpel.duplicateInto(scalpel.duplicateWith(ls), $.statements());
    }
    return !n.subtreeMatch(matcher, $) ? $ : null;
  }
  @Override String description(@SuppressWarnings("unused") SwitchStatement __) {
    return "Simplify switch statement";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.SWITCH_IF_CONVERTION; // TODO Ori: change wring group
  }
}
