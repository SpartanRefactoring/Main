package il.org.spartan.refactoring.wring;

import static il.org.spartan.idiomatic.*;
import static il.org.spartan.refactoring.utils.Funcs.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.Wring.*;

/**
 * A wring to remove ineffective cases from a switch statement.
 *
 * TODO Ori: add care of sequencers in switch statements (??)
 *
 * @author Ori Roth
 * @since 2016/05/11
 */
public class SwitchSimplify extends ReplaceCurrentNode<SwitchStatement> implements Kind.SWITCH_IF_CONVERTION {
  private final ASTMatcher matcher = new ASTMatcher();

  @SuppressWarnings("unchecked") @Override ASTNode replacement(final SwitchStatement s) {
    final Map<SwitchCase, List<Statement>> bs = branchesOf(s);
    final List<List<SwitchCase>> cc = consolidateCases(bs);
    Collections.sort(cc, (o1, o2) -> escapeLevel(bs.get(o1.get(0)).get(bs.get(o1.get(0)).size() - 1), false)
        - escapeLevel(bs.get(o2.get(0)).get(bs.get(o2.get(0)).size() - 1), false));
    final SwitchStatement $ = s.getAST().newSwitchStatement();
    $.setExpression(scalpel.duplicate(s.getExpression()));
    for (final List<SwitchCase> cl : cc) {
      scalpel.duplicateInto(cl, $.statements());
      final List<Statement>[] ls = new List[cl.size()];
      for (int i = 0; i < cl.size(); ++i)
        ls[i] = bs.get(cl.get(i));
      scalpel.duplicateInto(scalpel.duplicateWith(ls), $.statements());
      if (cc.indexOf(cl) != cc.size() - 1 && !containsSequencer((Statement) $.statements().get($.statements().size() - 1), false))
        $.statements().add(s.getAST().newBreakStatement());
    }
    return unless(s.subtreeMatch(matcher, $)).eval($);
  }
  static int escapeLevel(final Statement s, final boolean b) {
    return !b && Is.sequencer(s) || s instanceof ThrowStatement || s instanceof ReturnStatement
        ? s instanceof ThrowStatement ? 0 : s instanceof ReturnStatement ? 1 : s instanceof ContinueStatement ? 2 : 3
        : s instanceof IfStatement
            ? Math.max(escapeLevel(((IfStatement) s).getThenStatement(), b), escapeLevel(((IfStatement) s).getElseStatement(), b))
            : s instanceof ForStatement ? escapeLevel(((ForStatement) s).getBody(), true)
                : s instanceof WhileStatement ? escapeLevel(((WhileStatement) s).getBody(), true)
                    : !(s instanceof Block) || ((Block) s).statements().isEmpty() ? 4
                        : escapeLevel((Statement) ((Block) s).statements().get(((Block) s).statements().size() - 1), b);
  }
  @SuppressWarnings({ "javadoc" }) public static boolean containsSequencer(final Statement s, final boolean b) {
    return !b && Is.sequencer(s) || (s instanceof IfStatement
        ? containsSequencer(((IfStatement) s).getThenStatement(), b) && containsSequencer(((IfStatement) s).getElseStatement(), b)
        : s instanceof ForStatement && containsSequencer(((ForStatement) s).getBody(), true)
            || s instanceof WhileStatement && containsSequencer(((WhileStatement) s).getBody(), true)
            || s instanceof Block && !((Block) s).statements().isEmpty()
                && containsSequencer((Statement) ((Block) s).statements().get(((Block) s).statements().size() - 1), b));
  }
  @SuppressWarnings("javadoc") public static void insertUntilSequencer(final List<Statement> f, final List<Statement> ss,
      final int i) {
    for (int c = i; c < f.size(); ++c) {
      if (!(f.get(c) instanceof SwitchCase))
        ss.add(f.get(c));
      if (containsSequencer(f.get(c), false))
        return;
    }
  }
  @SuppressWarnings({ "javadoc", "unchecked" }) public static Map<SwitchCase, List<Statement>> branchesOf(final SwitchStatement n) {
    final Map<SwitchCase, List<Statement>> $ = new LinkedHashMap<>();
    for (final Statement s : (Iterable<Statement>) n.statements())
      if (s instanceof SwitchCase) {
        final List<Statement> l = new ArrayList<>();
        $.put((SwitchCase) s, l);
        insertUntilSequencer(n.statements(), l, n.statements().indexOf(s) + 1);
      }
    return $;
  }
  @SuppressWarnings("javadoc") public static List<List<SwitchCase>> consolidateCases(final Map<SwitchCase, List<Statement>> bs) {
    final List<List<SwitchCase>> $ = new LinkedList<>();
    cases: for (final SwitchCase s : bs.keySet()) {
      for (final List<SwitchCase> cl : $)
        if (same(bs.get(s), bs.get(cl.get(0)))) {
          if (s.isDefault()) {
            cl.clear();
            cl.add(s);
          } else if (!cl.get(0).isDefault())
            cl.add(s);
          continue cases;
        }
      final List<SwitchCase> cl = new ArrayList<>();
      cl.add(s);
      $.add(cl);
    }
    return $;
  }
  @Override String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "Simplify switch statement";
  }
}
