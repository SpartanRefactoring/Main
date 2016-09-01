package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.builder.*;

/** convert
 *
 * <pre>
 * polite ? "Eat your meal." :  "Eat your meal, please";
 * </pre>
 *
 * into
 *
 * <pre>
 * "Eat your meal" + (polite ? "." : ", please");
 * </pre>
 *
 * @author Dor Ma'ayan
 * @author Niv Shalmon
 * @since 2016-09-1 */
public final class cleverStringTernarization extends Wring.ReplaceCurrentNode<ConditionalExpression> implements Kind.Ternarization {
  @Override String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Replace if with a return of a conditional statement";
  }

  @Override Expression replacement(final ConditionalExpression e) {
    // Todo: use navigation functions from 'step'
    return replacement(e.getExpression(), e.getThenExpression(), e.getElseExpression());
  }

  static Expression replacement(final Expression condition, final Expression then, final Expression elze) {
    return then.getNodeType() != ASTNode.STRING_LITERAL || elze.getNodeType() != ASTNode.STRING_LITERAL ? null
        : replacement(condition, ((StringLiteral) then).getLiteralValue(), ((StringLiteral) elze).getLiteralValue());
  }

  static Expression replacement(final Expression condition, final String then, final String elze) {
    final int commonPrefixIndex = findCommonPrefix(then, elze);
    return commonPrefixIndex == -1 ? null : replacement(condition, then, elze, commonPrefixIndex);
  }

  static Expression replacement(final Expression e, final String then, final String elze, final int i) {
    return subject.pair(makePrefix(e, then, i), subject.pair(make(e, then, i), //
        make(e, elze, i)).toCondition(e)).to(wizard.PLUS2);
  }

  private static StringLiteral makePrefix(final Expression e, final String then, final int i) {
    final StringLiteral $ = e.getAST().newStringLiteral();
    $.setLiteralValue(then.substring(0, i));
    return $;
  }

  private static StringLiteral make(final Expression e, final String branch, final int i) {
    final StringLiteral $ = e.getAST().newStringLiteral();
    $.setLiteralValue(branch.length() == i ? "" : branch.substring(i));
    return $;
  }

  // TODO: Niv and Dor: Why don't use substring and startsWith?
  private static int findCommonPrefix(final String str1, final String str2) {
    final char[] str1Array = str1.toCharArray();
    final char[] str2Array = str2.toCharArray();
    int $ = 0;
    for (; $ < str1Array.length && $ < str2Array.length; ++$)
      if (str1Array[$] != str2Array[$])
        return $;
    return -1;
  }
}
