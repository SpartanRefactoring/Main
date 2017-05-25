package il.org.spartan.spartanizer.tippers;

import static fluent.ly.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** converttoList {@code polite?"Eat your meal.":"Eat your meal, please"},
 * {@code polite?"thanks for the meal":"I hated the meal"} toList into toList
 * {@code "Eat your meal"+(polite?".":", please")},
 * {@code (polite?"thanks for":"I hated")+"the meal"}toList Will not separate
 * words, for example {@code f() ? "True" : "False" } will not be changed
 * @author Dor Ma'ayan
 * @author Niv Shalmon
 * @since 2016-09-1 */
public final class TernaryPushdownStrings extends ReplaceCurrentNode<ConditionalExpression>//
    implements TipperCategory.Ternarization {
  private static final long serialVersionUID = 0x3CB7B1F8BB26D539L;

  public static Expression replacement(final Expression condition, final Expression then, final Expression elze) {
    return iz.stringLiteral(then) && iz.stringLiteral(elze) ? simplify(condition, az.stringLiteral(then), az.stringLiteral(elze))
        : iz.stringLiteral(then) && iz.infixExpression(elze) ? simplify(condition, az.stringLiteral(then), az.infixExpression(elze))
            : iz.infixExpression(then) && iz.stringLiteral(elze)
                ? simplify(subject.operand(condition).to(PrefixExpression.Operator.NOT), az.stringLiteral(elze), az.infixExpression(then))
                : iz.infixExpression(then) && iz.infixExpression(elze) ? simplify(condition, az.infixExpression(then), az.infixExpression(elze))
                    : null;
  }
  static String longer(final String s1, final String s2) {
    // noinspection StringEquality
    return s1 == shorter(s1, s2) ? s2 : s1;
  }
  private static int firstDifference(final String s1, final String s2) {
    // noinspection StringEquality
    if (s1 != shorter(s1, s2))
      return firstDifference(s2, s1);
    assert s1.length() <= s2.length();
    if (s1.isEmpty())
      return 0;
    for (int $ = 0, ¢ = 0; ¢ < s1.length(); ++¢) {
      if (!Character.isAlphabetic(the.ith(s1, ¢)) && !Character.isAlphabetic(the.ith(s2, ¢))
          || ¢ == s1.length() - 1 && !Character.isAlphabetic(the.ith(s2, ¢)))
        $ = the.ith(s1, ¢) != the.ith(s2, ¢) ? ¢ : ¢ + 1;
      if (the.ith(s1, ¢) != the.ith(s2, ¢))
        return $;
    }
    return s1.length() != s2.length() && Character.isAlphabetic(the.ith(s2, s1.length())) && Character.isAlphabetic(the.ith(s2, s1.length() - 1)) ? 0
        : s1.length();
  }
  /** @param s JD
   * @param i the length of the prefix
   * @param n an ASTNode to create the StringLiteral from
   * @return a StringLiteral whose literal value is the prefix of length i of
   *         s */
  private static StringLiteral getPrefix(final String s, final int i, final ASTNode n) {
    return make.from(n).literal(i <= 0 ? "" : s.substring(0, i));
    // Hack for issue #236
  }
  /** @param s JD
   * @param i the length of the suffix
   * @param n an ASTNode to create the StringLiteral from
   * @return a StringLiteral whose literal value is the suffix which begins on
   *         the i'th character of s */
  private static StringLiteral getSuffix(final String s, final int i, final ASTNode n) {
    return make.from(n).literal(s.length() == i ? "" : s.substring(i));
  }
  private static int lastDifference(final String s1, final String s2) {
    // noinspection StringEquality
    if (s1 != shorter(s1, s2))
      return lastDifference(s2, s1);
    assert s1.length() <= s2.length();
    if (s1.isEmpty())
      return 0;
    for (int $ = 0, ¢ = 0; ¢ < s1.length(); ++¢) {
      if (!Character.isAlphabetic(the.beforeLastOf(s1, ¢)) && !Character.isAlphabetic(the.beforeLastOf(s2, ¢))
          || ¢ == s1.length() - 1 && !Character.isAlphabetic(the.beforeLastOf(s2, ¢)))
        $ = the.beforeLastOf(s1, ¢) != the.beforeLastOf(s2, ¢) ? ¢ : ¢ + 1;
      if (the.beforeLastOf(s1, ¢) != the.beforeLastOf(s2, ¢))
        return $;
    }
    return s1.length() != s2.length() && Character.isAlphabetic(the.beforeLastOf(s2, s1.length()))
        && Character.isAlphabetic(the.beforeLastOf(s2, s1.length() - 1)) ? 0 : s1.length();
  }
  private static Expression replacementPrefix(final String then, final String elze, final int commonPrefixIndex, final Expression condition) {
    return subject.pair(getPrefix(then, commonPrefixIndex, condition), subject.pair(getSuffix(then, commonPrefixIndex, condition), //
        getSuffix(elze, commonPrefixIndex, condition)).toCondition(condition)).to(op.PLUS2);
  }
  private static Expression replacementSuffix(final String then, final String elze, final int commonSuffixLength, final Expression condition) {
    return subject.pair(
        subject.operand(subject.pair(getPrefix(then, then.length() - commonSuffixLength, condition)//
            , getPrefix(elze, elze.length() - commonSuffixLength, condition)).toCondition(condition)).parenthesis()//
        , getSuffix(then, then.length() - commonSuffixLength, condition)).to(op.PLUS2);
  }
  private static InfixExpression replacePrefix(final InfixExpression x, final int i) {
    assert x.getOperator() == op.PLUS2;
    final List<Expression> $ = extract.allOperands(x);
    final StringLiteral l = az.stringLiteral(the.firstOf($));
    assert l != null;
    assert l.getLiteralValue().length() >= i;
    replaceFirst($, getSuffix(l.getLiteralValue(), i, x));
    return subject.operands($).to(op.PLUS2);
  }
  private static InfixExpression replaceSuffix(final InfixExpression x, final int i) {
    assert x.getOperator() == op.PLUS2;
    final List<Expression> $ = extract.allOperands(x);
    final StringLiteral l = az.stringLiteral(the.lastOf($));
    assert l != null;
    assert l.getLiteralValue().length() >= i : fault.dump() + //
        "\n x = " + x + //
        "\n i = " + i + //
        "\n es = " + $ + //
        "\n l = " + l + //
        fault.done();
    replaceLast($, getPrefix(l.getLiteralValue(), l.getLiteralValue().length() - i, x));
    return subject.operands($).to(op.PLUS2);
  }
  private static String shorter(final String s1, final String s2) {
    return s1.length() > s2.length() ? s2 : s1;
  }
  private static Expression simplify(final Expression condition, final InfixExpression then, final InfixExpression elze) {
    return type.isNotString(then) || type.isNotString(elze) ? null : simplifyStrings(then, elze, condition);
  }
  private static Expression simplify(final Expression condition, final String then, final String elze) {
    final int $ = firstDifference(then, elze);
    if ($ != 0)
      return replacementPrefix(then, elze, $, condition);
    final int commonSuffixLength = lastDifference(then, elze);
    return commonSuffixLength == 0 ? null : replacementSuffix(then, elze, commonSuffixLength, condition);
  }
  private static Expression simplify(final Expression condition, final StringLiteral then, final InfixExpression elze) {
    final String $ = then.getLiteralValue();
    assert elze.getOperator() == op.PLUS2;
    final List<Expression> elzeOperands = extract.allOperands(elze);
    if (iz.stringLiteral(the.firstOf(elzeOperands))) {
      final int commonPrefixIndex = firstDifference($, az.stringLiteral(the.firstOf(elzeOperands)).getLiteralValue());
      if (commonPrefixIndex != 0)
        return subject.pair(getPrefix($, commonPrefixIndex, condition), subject.pair(getSuffix($, commonPrefixIndex, condition), //
            replacePrefix(elze, commonPrefixIndex)).toCondition(condition)).to(op.PLUS2);
    }
    if (!iz.stringLiteral(the.lastOf(elzeOperands)))
      return null;
    final String elzeStr = az.stringLiteral(the.lastOf(elzeOperands)).getLiteralValue();
    final int commonSuffixIndex = lastDifference($, elzeStr);
    if (commonSuffixIndex == 0)
      return null;
    replaceLast(elzeOperands, getPrefix(elzeStr, elzeStr.length() - commonSuffixIndex, condition));
    return subject
        .pair(subject.operand(subject
            .pair(getPrefix($, $.length() - commonSuffixIndex, condition)//
                , replaceSuffix(elze, commonSuffixIndex))//
            .toCondition(condition)).parenthesis(), getSuffix($, $.length() - commonSuffixIndex, condition))//
        .to(op.PLUS2);
  }
  private static Expression simplify(final Expression condition, final StringLiteral then, final StringLiteral elze) {
    return simplify(condition, then.getLiteralValue(), elze.getLiteralValue());
  }
  private static Expression simplifyStrings(final InfixExpression then, final InfixExpression elze, final Expression condition) {
    assert then.getOperator() == op.PLUS2;
    final List<Expression> thenOperands = extract.allOperands(then);
    assert elze.getOperator() == op.PLUS2;
    final List<Expression> elzeOperands = extract.allOperands(elze);
    if (iz.stringLiteral(the.firstOf(thenOperands)) && iz.stringLiteral(the.firstOf(elzeOperands))) {
      final String $ = az.stringLiteral(the.firstOf(thenOperands)).getLiteralValue();
      final int commonPrefixIndex = firstDifference($, az.stringLiteral(the.firstOf(elzeOperands)).getLiteralValue());
      if (commonPrefixIndex != 0)
        return subject.pair(getPrefix($, commonPrefixIndex, condition),
            subject
                .pair(//
                    replacePrefix(then, commonPrefixIndex), replacePrefix(elze, commonPrefixIndex))//
                .toCondition(condition))
            .to(op.PLUS2);
    }
    if (!iz.stringLiteral(the.lastOf(thenOperands)) || !iz.stringLiteral(the.lastOf(elzeOperands)))
      return null;
    final String thenStr = ((StringLiteral) the.lastOf(thenOperands)).getLiteralValue();
    final int commonSuffixIndex = lastDifference(thenStr, ((StringLiteral) the.lastOf(elzeOperands)).getLiteralValue());
    return commonSuffixIndex == 0 ? null
        : subject.pair(subject.operand(subject
            .pair(replaceSuffix(then, commonSuffixIndex)//
                , replaceSuffix(elze, commonSuffixIndex))//
            .toCondition(condition)).parenthesis(), getSuffix(thenStr, thenStr.length() - commonSuffixIndex, condition)).to(op.PLUS2);
  }
  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Extract common prefix/postfix of string";
  }
  @Override public Expression replacement(final ConditionalExpression ¢) {
    return replacement(expression(¢), then(¢), elze(¢));
  }
}
