package il.org.spartan.spartanizer.dispatch;

import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A number of utility functions common to all tippers.
 * @author Yossi Gil
 * @since 2015-07-17 */
public enum Tippers {
  ;
  public static void addAllReplacing(@NotNull final List<Statement> to, @NotNull final List<Statement> from, final Statement substitute, final Statement by1,
                                     @NotNull final List<Statement> by2) {
    for (final Statement ¢ : from)
      if (¢ != substitute)
        copy.into(¢, to);
      else {
        copy.into(by1, to);
        copy.into(by2, to);
      }
  }

  @NotNull
  public static IfStatement blockIfNeeded(@NotNull final IfStatement s, @NotNull final ASTRewrite r, final TextEditGroup g) {
    if (!iz.blockRequired(s))
      return s;
    final Block $ = subject.statement(s).toBlock();
    r.replace(s, $, g);
    return (IfStatement) first(statements($));
  }

  public static Expression eliminateLiteral(@NotNull final InfixExpression x, final boolean b) {
    final List<Expression> $ = extract.allOperands(x);
    wizard.removeAll(b, $);
    switch ($.size()) {
      case 1:
        return copy.of(first($));
      case 0:
        return x.getAST().newBooleanLiteral(b);
      default:
        return subject.operands($).to(x.getOperator());
    }
  }

  /** Determines if we can be certain that a {@link Statement} ends with a
   * sequencer ({@link ReturnStatement}, {@link ThrowStatement},
   * {@link BreakStatement}, {@link ContinueStatement}).
   * @param ¢ JD
   * @return true <b>iff</b> the Statement can be verified to end with a
   *         sequencer. */
  public static boolean endsWithSequencer(@Nullable final Statement ¢) {
    if (¢ == null)
      return false;
    final Statement $ = (Statement) hop.lastStatement(¢);
    if ($ == null)
      return false;
    switch ($.getNodeType()) {
      case BLOCK:
        return endsWithSequencer(last(step.statements((Block) $)));
      case BREAK_STATEMENT:
      case CONTINUE_STATEMENT:
      case RETURN_STATEMENT:
      case THROW_STATEMENT:
        return true;
      case DO_STATEMENT:
        return endsWithSequencer(((DoStatement) $).getBody());
      case LABELED_STATEMENT:
        return endsWithSequencer(((LabeledStatement) $).getBody());
      case IF_STATEMENT:
        return endsWithSequencer(then((IfStatement) $)) && endsWithSequencer(elze((IfStatement) $));
      default:
        return false;
    }
  }

  @NotNull
  public static ListRewrite insertAfter(@NotNull final Statement where, @NotNull final List<Statement> what, @NotNull final ASTRewrite r, final TextEditGroup g) {
    final ListRewrite $ = r.getListRewrite(where.getParent(), Block.STATEMENTS_PROPERTY);
    for (int ¢ = what.size() - 1;; $.insertAfter(what.get(¢--), where, g))
      if (¢ < 0)
        return $;
  }

  @NotNull
  public static ListRewrite insertBefore(@NotNull final Statement where, @NotNull final List<Statement> what, @NotNull final ASTRewrite r, final TextEditGroup g) {
    final ListRewrite $ = r.getListRewrite(parent(where), Block.STATEMENTS_PROPERTY);
    what.forEach(λ -> $.insertBefore(λ, where, g));
    return $;
  }

  @NotNull
  public static IfStatement invert(@NotNull final IfStatement ¢) {
    return subject.pair(elze(¢), then(¢)).toNot(¢.getExpression());
  }

  @Nullable
  public static IfStatement makeShorterIf(@NotNull final IfStatement s) {
    final List<Statement> then = extract.statements(then(s)), elze = extract.statements(elze(s));
    final IfStatement $ = invert(s);
    if (then.isEmpty())
      return $;
    final IfStatement main = copy.of(s);
    if (elze.isEmpty())
      return main;
    final int rankThen = Tippers.sequencerRank(lisp.last(then)), rankElse = Tippers.sequencerRank(lisp.last(elze));
    return rankElse > rankThen || rankThen == rankElse && !Tippers.thenIsShorter(s) ? $ : main;
  }

  public static boolean mixedLiteralKind(@NotNull final List<Expression> xs) {
    if (xs.size() <= 2)
      return false;
    int previousKind = -1;
    for (final Expression x : xs)
      if (x instanceof NumberLiteral || x instanceof CharacterLiteral) {
        final int currentKind = new NumericLiteralClassifier(x + "").type().ordinal();
        assert currentKind >= 0;
        if (previousKind == -1)
          previousKind = currentKind;
        else if (previousKind != currentKind)
          return true;
      }
    return false;
  }

  public static void rename(final SimpleName oldName, final SimpleName newName, final ASTNode region, final ASTRewrite r, final TextEditGroup g) {
    new Inliner(oldName, r, g).byValue(newName)//
        .inlineInto(collect.usesOf(oldName).in(region).toArray(new Expression[] {}));
  }

  @NotNull
  public static ASTRewrite replaceTwoStatements(@NotNull final ASTRewrite r, @NotNull final Statement what, final Statement by, final TextEditGroup g) {
    final Block parent = az.block(what.getParent());
    final List<Statement> siblings = extract.statements(parent);
    final int i = siblings.indexOf(what);
    siblings.remove(i);
    siblings.remove(i);
    siblings.add(i, by);
    final Block $ = parent.getAST().newBlock();
    copy.into(siblings, statements($));
    r.replace(parent, $, g);
    return r;
  }

  public static boolean shoudlInvert(@NotNull final IfStatement s) {
    final int $ = sequencerRank(hop.lastStatement(then(s))), rankElse = sequencerRank(hop.lastStatement(elze(s)));
    return rankElse > $ || $ == rankElse && !Tippers.thenIsShorter(s);
  }

  public static boolean thenIsShorter(@NotNull final IfStatement s) {
    final Statement then = then(s), elze = elze(s);
    if (elze == null)
      return true;
    final int s1 = count.lines(then), s2 = count.lines(elze);
    if (s1 < s2)
      return true;
    if (s1 > s2)
      return false;
    assert s1 == s2;
    final int n2 = extract.statements(elze).size(), n1 = extract.statements(then).size();
    if (n1 < n2)
      return true;
    if (n1 > n2)
      return false;
    assert n1 == n2;
    final IfStatement $ = invert(s);
    return positivePrefixLength($) >= positivePrefixLength(invert($));
  }

  private static int positivePrefixLength(@NotNull final IfStatement $) {
    return metrics.length($.getExpression(), then($));
  }

  private static int sequencerRank(@NotNull final ASTNode ¢) {
    return iz.index(¢.getNodeType(), BREAK_STATEMENT, CONTINUE_STATEMENT, RETURN_STATEMENT, THROW_STATEMENT);
  }

  public static void remove(@NotNull final ASTRewrite r, @NotNull final Statement s, final TextEditGroup g) {
    r.getListRewrite(parent(s), Block.STATEMENTS_PROPERTY).remove(s, g);
  }

  public static <T> void removeLast(@NotNull final List<T> ¢) {
    ¢.remove(¢.size() - 1);
  }
}
