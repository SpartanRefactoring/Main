package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.step.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert <code>
 * int a = 3;
 * for(;Panic;) {
 *    ++OS.is.in.denger;
 * }
 * </code> to <code>
 * for(int a = 3; Panic;) {
 *    ++OS.is.in.denger;
 * }
 * </code>
 * @author Alex Kopzon
 * @since 2016 */
public final class ForToForInitializers extends ReplaceToNextStatementExclude<VariableDeclarationFragment> implements TipperCategory.Collapse {
  public static ASTNode replace(final VariableDeclarationStatement s, final ForStatement ¢) {
    return !fitting(s, ¢) ? null : buildForStatement(s, ¢);
  }

  private static ForStatement buildForStatement(final VariableDeclarationStatement s, final ForStatement ¢) {
    final ForStatement $ = duplicate.of(¢);
    $.setExpression(pullInitializersFromExpression(dupForExpression(¢), s));
    step.initializers($).add(Initializers(s));
    return $;
  }

  private static boolean compareModifiers(final List<IExtendedModifier> l1, final List<IExtendedModifier> l2) {
    for (final IExtendedModifier ¢ : l1)
      if (!isIn(¢, l2))
        return false;
    return true;
  }

  private static Expression dupForExpression(final ForStatement ¢) {
    return duplicate.of(expression(¢));
  }

  private static boolean fitting(final VariableDeclarationStatement s, final ForStatement ¢) {
    assert ¢ != null : fault.dump() + "\n s = " + s + fault.done();
    final List<Expression> initializers = step.initializers(¢);
    assert initializers != null : fault.dump() + "\n s = " + s + "\n ¢ = " + ¢ + fault.done();
    if (initializers.isEmpty())
      return true;
    final Expression first = first(initializers);
    assert first != null : fault.dump() + "\n s = " + s + "\n ¢ = " + ¢ + "\n initializers = " + initializers + fault.done();
    final VariableDeclarationExpression e = az.variableDeclarationExpression(first);
    if (e == null)
      return false;
    assert e != null : fault.dump() + "\n s = " + s + "\n ¢ = " + ¢ + "\n initializers = " + initializers + "\n first = " + first
        + "\n first.getClass().getSimpleName() = " + first.getClass().getSimpleName() + fault.done();
    final List<IExtendedModifier> extendedModifiers = step.extendedModifiers(e);
    if (extendedModifiers == null)
      return false;
    assert extendedModifiers != null : fault.dump() + "\n s = " + s + "\n ¢ = " + ¢ + "\n initializers = " + initializers + "\n first = " + first
        + "\n e = " + e + fault.done();
    assert s != null : fault.dump() + "\n s = " + s + "\n ¢ = " + ¢ + "\n initializers = " + initializers + "\n first = " + first + "\n e = " + e
        + "\n extendedModifiers = " + extendedModifiers + fault.done();
    final List<IExtendedModifier> extendedModifiers2 = step.extendedModifiers(s);
    if (extendedModifiers2 == extendedModifiers || extendedModifiers == null || extendedModifiers2 == null)
      return false;
    assert extendedModifiers2 != null : fault.dump() + "\n s = " + s + "\n ¢ = " + ¢ + "\n initializers = " + initializers + "\n first = " + first
        + "\n e = " + e + "\n extendedModifiers = " + extendedModifiers + "\n s = " + e + fault.done();
    return e.getType() == s.getType() && compareModifiers(extendedModifiers, extendedModifiers2);
  }

  // Ugly one...
  private static Expression handleInfix(final InfixExpression from, final VariableDeclarationStatement s) {
    final List<Expression> operands = hop.operands(from);
    for (final Expression ¢ : operands)
      if (iz.parenthesizeExpression(¢) && iz.assignment(az.parenthesizedExpression(¢).getExpression())) {
        final Assignment a = az.assignment(az.parenthesizedExpression(¢).getExpression());
        final SimpleName var = az.simpleName(step.left(a));
        for (final VariableDeclarationFragment f : step.fragments(s))
          if (f.getName().toString().equals(var.toString())) {
            f.setInitializer(duplicate.of(step.right(a)));
            operands.set(operands.indexOf(¢), ¢.getAST().newSimpleName(var.toString()));
          }
      }
    final InfixExpression $ = subject.pair(operands.get(0), operands.get(1)).to(from.getOperator());
    // return subject.append($, minus.firstElem(minus.firstElem(operands)));
    return $;
  }

  private static Expression Initializers(final VariableDeclarationStatement ¢) {
    return az.variableDeclarationExpression(¢);
  }

  private static boolean isIn(final IExtendedModifier m, final List<IExtendedModifier> ms) {
    for (final IExtendedModifier ¢ : ms)
      if (IExtendedModifiersOrdering.compare(m, ¢) == 0)
        return true;
    return false;
  }

  /** @param t JD
   * @param from JD (already duplicated)
   * @param to is the list that will contain the pulled out initializations from
   *        the given expression.
   * @return expression to the new for loop, without the initializers. */
  private static Expression pullInitializersFromExpression(final Expression from, final VariableDeclarationStatement f) {
    return !haz.sideEffects(from) ? from : !iz.infix(from) ? from : handleInfix(duplicate.of(az.infixExpression(from)), f);
  }

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Merge with subequent 'for' loop, rewrite as (" + ¢ + "; " + expression(az.forStatement(extract.nextStatement(¢))) + "loop";
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final Statement nextStatement, final TextEditGroup g,
      final ExclusionManager exclude) {
    if (f == null || r == null || nextStatement == null || exclude == null)
      return null;
    final VariableDeclarationStatement parent = az.variableDeclrationStatement(f.getParent());
    if (parent == null)
      return null;
    final ForStatement s = az.forStatement(nextStatement);
    if (s == null || !fitting(parent, s))
      return null;
    exclude.excludeAll(step.fragments(parent));
    // exclude.exclude(s.getExpression());
    r.remove(parent, g);
    r.replace(s, replace(parent, s), g);
    return r;
  }
}
