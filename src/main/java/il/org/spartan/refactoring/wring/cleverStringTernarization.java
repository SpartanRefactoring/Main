package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.builder.*;

/** convert
 *
 * <pre>
 * polite ? "Eat your meal." : "Eat your meal, please"
 * </pre>
 * 
 * <pre>
 * polite ? "thanks for the meal" : "I hated the meal"
 * </pre>
 *
 * into
 *
 * <pre>
 * "Eat your meal" + (polite ? "." : ", please")
 * </pre>
 * 
 * <pre>
 * (polite ? "thanks for" : "I hated") + "the meal"
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
    final Expression then = e.getThenExpression();
    final Expression elze = e.getElseExpression();
    if (then.getNodeType() != ASTNode.STRING_LITERAL || elze.getNodeType() != ASTNode.STRING_LITERAL)
      return null;
    String thenStr = ((StringLiteral) then).getLiteralValue();
    String elseStr = ((StringLiteral) elze).getLiteralValue();
    int commonPrefixIndex = findCommonPrefix(thenStr, elseStr);
    if (commonPrefixIndex != 0)
      return replacementPrefix(thenStr, elseStr, commonPrefixIndex, e);
    int commonSuffixLength = findCommonSuffix(thenStr, elseStr);
    return commonSuffixLength == 0 ? null : replacementSuffix(thenStr, elseStr, commonSuffixLength, e);
  }

  private static Expression replacementPrefix(String thenStr, String elseStr, int commonPrefixIndex, ConditionalExpression e) {
    final Expression condition = e.getExpression();
    StringLiteral prefix = e.getAST().newStringLiteral();
    prefix.setLiteralValue(thenStr.substring(0, commonPrefixIndex));
    StringLiteral thenPost = e.getAST().newStringLiteral();
    thenPost.setLiteralValue(thenStr.length() == commonPrefixIndex ? //
        "" : thenStr.substring(commonPrefixIndex));
    StringLiteral elsePost = e.getAST().newStringLiteral();
    elsePost.setLiteralValue(elseStr.length() == commonPrefixIndex ? //
        "" : elseStr.substring(commonPrefixIndex));
    return subject.pair(prefix, subject.pair(thenPost, //
        elsePost).toCondition(condition)).to(wizard.PLUS2);
  }

  private static Expression replacementSuffix(String thenStr, String elseStr, int commonSuffixLength, ConditionalExpression e) {
    final Expression condition = e.getExpression();
    StringLiteral suffix = e.getAST().newStringLiteral();
    suffix.setLiteralValue(thenStr.substring(thenStr.length() - commonSuffixLength));
    StringLiteral thenPre = e.getAST().newStringLiteral();
    thenPre.setLiteralValue(thenStr.length() == commonSuffixLength ? //
        "" : thenStr.substring(0, thenStr.length() - commonSuffixLength));
    StringLiteral elsePre = e.getAST().newStringLiteral();
    elsePre.setLiteralValue(elseStr.length() == commonSuffixLength ? //
        "" : elseStr.substring(0, elseStr.length() - commonSuffixLength));
    ParenthesizedExpression pe = e.getAST().newParenthesizedExpression();
    pe.setExpression(subject.pair(thenPre, elsePre).toCondition(condition));
    return subject.pair(pe, suffix).to(wizard.PLUS2);
  }

  private static int findCommonPrefix(String str1, String str2) {
    char[] str1Array = str1.toCharArray();
    char[] str2Array = str2.toCharArray();
    for (int $ = 0; $ < str1Array.length && $ < str2Array.length; ++$)
      if (str1Array[$] != str2Array[$])
        return $;
    return 0;
  }

  private static int findCommonSuffix(String str1, String str2) {
    int i = 0;
    String sub = "";
    for (; i < Math.max(str1.length(), str2.length()); ++i) {
      sub = str2.substring(i);
      if (str1.endsWith(sub))
        break;
    }
    return sub.length();
  }
}
