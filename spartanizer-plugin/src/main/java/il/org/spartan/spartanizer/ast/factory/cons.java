package il.org.spartan.spartanizer.ast.factory;

import static il.org.spartan.spartanizer.ast.navigate.step.elze;
import static il.org.spartan.spartanizer.ast.navigate.step.extendedModifiers;
import static il.org.spartan.spartanizer.ast.navigate.step.fragments;
import static il.org.spartan.spartanizer.ast.navigate.step.left;
import static il.org.spartan.spartanizer.ast.navigate.step.right;
import static il.org.spartan.spartanizer.ast.navigate.step.then;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.NOT;

import java.util.List;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import fluent.ly.as;
import fluent.ly.the;
import il.org.spartan.spartanizer.ast.navigate.countOf;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.navigate.op;
import il.org.spartan.spartanizer.ast.navigate.step;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tippers.PrefixNotPushdown;

/** An empty {@code enum} for fluent programming. The name should say it all:
 * The name, followed by a dot, followed by a method name, should read like a
 * sentence phrase
 * @author Yossi Gil
 * @since 2017-08-18 */
public interface cons {



  /** Swap the order of the left and right operands to an expression, changing
   * the operator if necessary.
   * @param ¢ JD
   * @return a newly created expression with its operands thus swapped.
   * @throws IllegalArgumentException when the parameter has extra operands.
   * @see InfixExpression#hasExtendedOperands */
  static InfixExpression conjugate(final InfixExpression ¢) {
    if (¢.hasExtendedOperands())
      throw new IllegalArgumentException(¢ + ": flipping undefined for an expression with extra operands ");
    return subject.pair(right(¢), left(¢)).to(op.conjugate(¢.getOperator()));
  }

  static IfStatement invert(final IfStatement ¢) {
    return az.ifStatement(make.newSafeIf(null, elze(¢), then(¢), cons.not(¢.getExpression())));
  }

  static List<Statement> listMe(final Expression ¢) {
    return as.list(¢.getAST().newExpressionStatement(copy.of(¢)));
  }

  static Expression minus(final Expression ¢) {
    final PrefixExpression $ = az.prefixExpression(¢);
    final NumberLiteral l = az.numberLiteral(¢);
    return $ == null ? l == null ? cons.minusOf(¢) //
    : atomic.newLiteral(l, iz.literal0(l) ? "0" : wizard.signAdjust(l.getToken())) //
        : $.getOperator() == op.MINUS1 ? $.getOperand() //
            : $.getOperator() == op.PLUS1 ? subject.operand($.getOperand()).to(op.MINUS1)//
                : ¢;
  }

  static Expression minusOf(final Expression ¢) {
    return iz.literal0(¢) ? ¢ : subject.operand(¢).to(op.MINUS1);
  }

  /** @param ¢ JD
   * @return parameter, but logically negated and simplified */
  static Expression not(final Expression ¢) {
    assert ¢ != null;
    final PrefixExpression $ = subject.operand(copy.of(¢)).to(NOT);
    assert $ != null;
    final Expression $$ = PrefixNotPushdown.simplifyNot($);
    return $$ == null ? $ : $$;
  }

  static IfStatement shorterIf(final IfStatement s) {
    final List<Statement> then = extract.statements(then(s)), elze = extract.statements(elze(s));
    final IfStatement $ = invert(s);
    if (then.isEmpty())
      return $;
    final IfStatement main = copy.of(s);
    if (elze.isEmpty())
      return main;
    final int rankThen = wizard.sequencerRank(the.lastOf(then)), rankElse = wizard.sequencerRank(the.lastOf(elze));
    return rankElse > rankThen || rankThen == rankElse && !misc.thenIsShorter(s) ? $ : main;
  }

  static boolean thenIsShorter(final IfStatement s) {
    final Statement then = then(s), elze = elze(s);
    if (elze == null)
      return true;
    final int s1 = countOf.lines(then), s2 = countOf.lines(elze);
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
    return misc.positivePrefixLength($) >= misc.positivePrefixLength(invert($));
  }

  /** @param ¢ the expression to return in the return statement
   * @return new return statement */
  static ThrowStatement throwOf(final Expression ¢) {
    return subject.operand(¢).toThrow();
  }

  static VariableDeclarationExpression variableDeclarationExpression(final VariableDeclarationStatement ¢) {
    if (¢ == null)
      return null;
    final VariableDeclarationExpression $ = ¢.getAST().newVariableDeclarationExpression(copy.of(the.firstOf(fragments(copy.of(¢)))));
    fragments($).addAll(extract.nextFragmentsOf(¢));
    $.setType(copy.of(step.type(¢)));
    extendedModifiers($).addAll(az.modifiersOf(¢));
    return $;
  }
  
}
