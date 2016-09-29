package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.step.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** convert <code>
 * int a = 3;
 * while(Panic) {
 *    ++OS.is.in.denger;
 * }
 * </code> to <code>
 * for(int a = 3; Panic;) {
 *    ++OS.is.in.denger;
 * }
 * </code>
 * @author Alex Kopzon
 * @since 2016 */
public final class DeclarationAndWhileToFor extends ReplaceToNextStatementExclude<VariableDeclarationFragment> implements TipperCategory.Collapse {
  private static ForStatement buildForStatement(final VariableDeclarationFragment f, final WhileStatement ¢) {
    final ForStatement $ = ¢.getAST().newForStatement();
    $.setBody(duplicate.of(body(¢)));
    step.initializers($).add(dupInitializers(f));
    final List<VariableDeclarationFragment> intoFragments = new ArrayList<>();
    $.setExpression(pullInitializers(ofType(f), withModifiers(f), fromWhileExpression(¢), intoFragments));
    return $;
  }

  @SuppressWarnings("unchecked") private static Expression dupInitializers(final VariableDeclarationFragment ¢) {
    final VariableDeclarationStatement parent = duplicate.of(az.variableDeclrationStatement(¢.getParent()));
    final VariableDeclarationExpression $ = parent.getAST().newVariableDeclarationExpression(duplicate.of(¢));
    $.fragments().addAll(nextFragmentsOf(parent));
    $.setType(duplicate.of(parent.getType()));
    step.extendedModifiers($).addAll(modifiersOf(parent));
    return $;
  }

  private static boolean fitting(@SuppressWarnings("unused") final WhileStatement __) {
    // TODO: check that the variables declared before the loop doesn't in use
    // after the scope.
    return true;
  }

  private static Expression fromWhileExpression(final WhileStatement ¢) {
    return duplicate.of(expression(¢));
  }

  private static List<IExtendedModifier> modifiersOf(final VariableDeclarationStatement parent) {
    final List<IExtendedModifier> modifiers = new ArrayList<>();
    duplicate.modifiers(step.extendedModifiers(parent), modifiers);
    return modifiers;
  }

  private static List<VariableDeclarationFragment> nextFragmentsOf(final VariableDeclarationStatement parent) {
    final List<VariableDeclarationFragment> fragments = new ArrayList<>();
    duplicate.into(step.fragments(parent), fragments);
    return minus.firstElem(fragments);
  }

  private static Type ofType(final VariableDeclarationFragment ¢) {
    return az.variableDeclrationStatement(¢.getParent()).getType();
  }

  /** @param t JD
   * @param from JD (already duplicated)
   * @param to is the list that will contain the pulled out initializations from
   *        the given expression.
   * @return expression to the new for loop, without the initializers. */
  private static Expression pullInitializers(final Type t, final List<IExtendedModifier> ms, final Expression from,
      final List<VariableDeclarationFragment> to) {
    return from;
  }

  public static ASTNode replace(final VariableDeclarationFragment f, final WhileStatement ¢) {
    return !fitting(¢) ? null : buildForStatement(f, ¢);
  }

  private static List<IExtendedModifier> withModifiers(final VariableDeclarationFragment ¢) {
    return step.extendedModifiers(az.variableDeclrationStatement(¢.getParent()));
  }

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Merge with subequent 'while', making a for (" + ¢ + "; " + expression(az.whileStatement(extract.nextStatement(¢))) + "loop";
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final Statement nextStatement, final TextEditGroup g,
      final ExclusionManager exclude) {
    if (f == null || r == null || nextStatement == null || exclude == null)
      return null;
    final Statement parent = az.asStatement(f.getParent());
    if (parent == null)
      return null;
    final WhileStatement s = az.whileStatement(nextStatement);
    if (s == null)
      return null;
    exclude.excludeAll(step.fragments(az.variableDeclrationStatement(f.getParent())));
    r.remove(parent, g);
    r.replace(s, replace(f, s), g);
    return r;
  }
}
