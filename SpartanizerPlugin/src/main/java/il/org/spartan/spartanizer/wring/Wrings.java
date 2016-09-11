package il.org.spartan.spartanizer.wring;

import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.ast.step.*;
import static il.org.spartan.spartanizer.engine.ExpressionComparator.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;

/** A number of utility functions common to all wrings.
 * @author Yossi Gil
 * @since 2015-07-17 */
public enum Wrings {
  ;
  static void addAllReplacing(final List<Statement> to, final List<Statement> from, final Statement substitute, final Statement by1,
      final List<Statement> by2) {
    for (final Statement s : from)
      if (s != substitute)
        duplicate.into(s, to);
      else {
        duplicate.into(by1, to);
        duplicate.into(by2, to);
      }
  }

  static IfStatement blockIfNeeded(final IfStatement s, final ASTRewrite r, final TextEditGroup g) {
    if (!iz.blockRequired(s))
      return s;
    final Block b = subject.statement(s).toBlock();
    r.replace(s, b, g);
    return (IfStatement) first(statements(b));
  }

  static Expression eliminateLiteral(final InfixExpression x, final boolean b) {
    final List<Expression> operands = extract.allOperands(x);
    wizard.removeAll(b, operands);
    switch (operands.size()) {
      case 0:
        return x.getAST().newBooleanLiteral(b);
      case 1:
        return duplicate.of(first(operands));
      default:
        return subject.operands(operands).to(x.getOperator());
    }
  }

  static boolean endsWithSequencer(final Statement s) {
    return iz.sequencer(hop.lastStatement(s));
  }

  static ListRewrite insertAfter(final Statement where, final List<Statement> what, final ASTRewrite r, final TextEditGroup g) {
    final ListRewrite $ = r.getListRewrite(where.getParent(), Block.STATEMENTS_PROPERTY);
    for (int i = what.size() - 1; i >= 0; --i)
      $.insertAfter(what.get(i), where, g);
    return $;
  }

  static ListRewrite insertBefore(final Statement where, final List<Statement> what, final ASTRewrite r, final TextEditGroup g) {
    final ListRewrite $ = r.getListRewrite(where.getParent(), Block.STATEMENTS_PROPERTY);
    for (final Statement s : what)
      $.insertBefore(s, where, g);
    return $;
  }

  static IfStatement invert(final IfStatement s) {
    return subject.pair(step.elze(s), step.then(s)).toNot(s.getExpression());
  }

  static int length(final ASTNode... ns) {
    int $ = 0;
    for (final ASTNode n : ns)
      $ += (n + "").length();
    return $;
  }

  static IfStatement makeShorterIf(final IfStatement s) {
    final List<Statement> then = extract.statements(step.then(s));
    final List<Statement> elze = extract.statements(step.elze(s));
    final IfStatement $ = invert(s);
    if (then.isEmpty())
      return $;
    final IfStatement main = duplicate.of(s);
    if (elze.isEmpty())
      return main;
    final int rankThen = Wrings.sequencerRank(lisp.last(then));
    final int rankElse = Wrings.sequencerRank(lisp.last(elze));
    return rankElse > rankThen || rankThen == rankElse && !Wrings.thenIsShorter(s) ? $ : main;
  }

  static boolean mixedLiteralKind(final List<Expression> xs) {
    if (xs.size() <= 2)
      return false;
    int previousKind = -1;
    for (final Expression e : xs)
      if (e instanceof NumberLiteral || e instanceof CharacterLiteral) {
        final int currentKind = new LiteralParser(e + "").type().ordinal();
        assert currentKind >= 0;
        if (previousKind == -1)
          previousKind = currentKind;
        else if (previousKind != currentKind)
          return true;
      }
    return false;
  }

  static void rename(final SimpleName oldName, final SimpleName newName, final MethodDeclaration d, final ASTRewrite r, final TextEditGroup g) {
    new LocalInliner(oldName, r, g).byValue(newName)//
        .inlineinto(Collect.usesOf(oldName).in(d).toArray(new Expression[] {}));
  }

  static ASTRewrite replaceTwoStatements(final ASTRewrite r, final Statement what, final Statement by, final TextEditGroup g) {
    final Block parent = az.block(what.getParent());
    final List<Statement> siblings = extract.statements(parent);
    final int i = siblings.indexOf(what);
    siblings.remove(i);
    siblings.remove(i);
    siblings.add(i, by);
    final Block $ = parent.getAST().newBlock();
    duplicate.into(siblings, step.statements($));
    r.replace(parent, $, g);
    return r;
  }

  static boolean shoudlInvert(final IfStatement s) {
    final int rankThen = sequencerRank(hop.lastStatement(step.then(s)));
    final int rankElse = sequencerRank(hop.lastStatement(step.elze(s)));
    return rankElse > rankThen || rankThen == rankElse && !Wrings.thenIsShorter(s);
  }

  static int size(final ASTNode... ns) {
    int $ = 0;
    for (final ASTNode n : ns)
      $ += nodesCount(n);
    return $;
  }

  static boolean thenIsShorter(final IfStatement s) {
    final Statement then = step.then(s);
    final Statement elze = step.elze(s);
    if (elze == null)
      return true;
    final int s1 = ExpressionComparator.lineCount(then);
    final int s2 = ExpressionComparator.lineCount(elze);
    if (s1 < s2)
      return true;
    if (s1 > s2)
      return false;
    assert s1 == s2;
    final int n2 = extract.statements(elze).size();
    final int n1 = extract.statements(then).size();
    if (n1 < n2)
      return true;
    if (n1 > n2)
      return false;
    assert n1 == n2;
    final IfStatement $ = invert(s);
    return positivePrefixLength($) >= positivePrefixLength(invert($));
  }

  private static int positivePrefixLength(final IfStatement $) {
    return Wrings.length($.getExpression(), step.then($));
  }

  private static int sequencerRank(final ASTNode n) {
    return iz.index(n.getNodeType(), BREAK_STATEMENT, CONTINUE_STATEMENT, RETURN_STATEMENT, THROW_STATEMENT);
  }
}
