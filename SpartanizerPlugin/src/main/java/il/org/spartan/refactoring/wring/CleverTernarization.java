package il.org.spartan.refactoring.wring;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.utils.*;

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
 * <pre>
 * a ? "abracadabra" : "abba"
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
 * <pre>
 * "ab" + (a ? "racadabr" : "b") + "a"
 * </pre>
 *
 * @author Dor Ma'ayan
 * @author Niv Shalmon
 * @since 2016-09-1 */
public final class CleverTernarization extends Wring.ReplaceCurrentNode<ConditionalExpression> implements Kind.Ternarization {
  @Override String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Replace ternarization with more clever one";
  }

  
  private static Expression simplify(StringLiteral then, StringLiteral elze, Expression condition){
    final String thenStr =  then.getLiteralValue();
    final String elseStr = elze.getLiteralValue();
    final int commonPrefixIndex = findCommonPrefix(thenStr, elseStr);
    if (commonPrefixIndex != 0)
      return replacementPrefix(thenStr, elseStr, commonPrefixIndex, condition);
    final int commonSuffixLength = findCommonSuffix(thenStr, elseStr);
    return commonSuffixLength == 0 ? null : replacementSuffix(thenStr, elseStr, commonSuffixLength, condition);
  }
  
  
  private static Expression simplify(StringLiteral then,InfixExpression elze, Expression condition){
    String thenStr = then.getLiteralValue();
    assert elze.getOperator()==wizard.PLUS2;
    final List<Expression> elzeOperands = extract.allOperands(elze);
    if(elzeOperands.get(0).getNodeType()==ASTNode.STRING_LITERAL){
      String elzeStr = ((StringLiteral)elzeOperands.get(0)).getLiteralValue();
      int commonPrefixIndex = findCommonPrefix(thenStr,elzeStr);
      if(commonPrefixIndex!=0){
          final StringLiteral prefix = condition.getAST().newStringLiteral();
          prefix.setLiteralValue(thenStr.substring(0, commonPrefixIndex));
          final StringLiteral thenPost = condition.getAST().newStringLiteral();
          thenPost.setLiteralValue(thenStr.length() == commonPrefixIndex ? //
              "" : thenStr.substring(commonPrefixIndex));
          final StringLiteral elzePost = condition.getAST().newStringLiteral();
          elzePost.setLiteralValue(elzeStr.length() == commonPrefixIndex ? //
              "" : elzeStr.substring(commonPrefixIndex));
          lisp.chop(elzeOperands);
          elzeOperands.add(0,elzePost);
          return subject.pair(prefix, subject.pair(thenPost, //
              subject.operands(elzeOperands).to(wizard.PLUS2)).toCondition(condition)).to(wizard.PLUS2);
      }
        
    }
    return null;
  }
  

  @Override Expression replacement(final ConditionalExpression x) {
    final Expression then = x.getThenExpression();
    final Expression elze = x.getElseExpression();
    final Expression condition = x.getExpression();
    if (then.getNodeType() == ASTNode.STRING_LITERAL && elze.getNodeType() == ASTNode.STRING_LITERAL)
     return simplify((StringLiteral)then,(StringLiteral)elze,condition);
   if (then.getNodeType()==ASTNode.STRING_LITERAL && elze.getNodeType()==ASTNode.INFIX_EXPRESSION)
     return simplify((StringLiteral)then,(InfixExpression)elze,condition);
   if (then.getNodeType()==ASTNode.INFIX_EXPRESSION && elze.getNodeType()==ASTNode.STRING_LITERAL)
     return simplify((StringLiteral)elze,(InfixExpression)then,//
         subject.operand(condition).to(PrefixExpression.Operator.NOT));
    return null;

  }

  private static Expression replacementPrefix(final String thenStr, final String elseStr, final int commonPrefixIndex,
      final Expression condition) {
    final StringLiteral prefix = condition.getAST().newStringLiteral();
    prefix.setLiteralValue(thenStr.substring(0, commonPrefixIndex));
    final StringLiteral thenPost = condition.getAST().newStringLiteral();
    thenPost.setLiteralValue(thenStr.length() == commonPrefixIndex ? //
        "" : thenStr.substring(commonPrefixIndex));
    final StringLiteral elsePost = condition.getAST().newStringLiteral();
    elsePost.setLiteralValue(elseStr.length() == commonPrefixIndex ? //
        "" : elseStr.substring(commonPrefixIndex));
    return subject.pair(prefix, subject.pair(thenPost, //
        elsePost).toCondition(condition)).to(wizard.PLUS2);
  }

  private static Expression replacementSuffix(final String thenStr, final String elseStr, final int commonSuffixLength,
      final Expression condition) {
    final StringLiteral suffix = condition.getAST().newStringLiteral();
    suffix.setLiteralValue(thenStr.substring(thenStr.length() - commonSuffixLength));
    final StringLiteral thenPre = condition.getAST().newStringLiteral();
    thenPre.setLiteralValue(thenStr.length() == commonSuffixLength ? //
        "" : thenStr.substring(0, thenStr.length() - commonSuffixLength));
    final StringLiteral elsePre = condition.getAST().newStringLiteral();
    elsePre.setLiteralValue(elseStr.length() == commonSuffixLength ? //
        "" : elseStr.substring(0, elseStr.length() - commonSuffixLength));
    final ParenthesizedExpression pe = condition.getAST().newParenthesizedExpression();
    pe.setExpression(subject.pair(thenPre, elsePre).toCondition(condition));
    return subject.pair(pe, suffix).to(wizard.PLUS2);
  }

  private static int findCommonPrefix(final String str1, final String str2) {
    final char[] str1Array = str1.toCharArray();
    final char[] str2Array = str2.toCharArray();
    int $ = 0;
    for (; $ < str1Array.length && $ < str2Array.length; ++$)
      if (str1Array[$] != str2Array[$])
        break;
    return $;
  }

  private static int findCommonSuffix(final String str1, final String str2) {
    int i = 0;
    String sub = "";
    for (; i < Math.max(str1.length(), str2.length()); ++i) {
      sub = str2.substring(i);
      if (str1.endsWith(sub))
        break;
    }
    return i == Math.max(str1.length(), str2.length()) ? 0 : sub.length();
  }
}
