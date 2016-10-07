package il.org.spartan.spartanizer.ast.navigate;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tippers.*;

/** An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase. Generally here comes all the checks, and
 * coercions related to tips ordering and collisions.
 * @author Alex Kopzon
 * @since 2.5 */
public enum cantTip {
  ;
  public static boolean declarationInitializerStatementTerminatingScope(final ForStatement ¢) {
    final VariableDeclarationFragment f = hop.precidingFragmentToLastExpression(¢);
    return f == null || new DeclarationInitializerStatementTerminatingScope().cantTip(f);
  }

  public static boolean declarationInitializerStatementTerminatingScope(final WhileStatement ¢) {
    final VariableDeclarationFragment f = hop.prevFragmentToLastExpression(¢);
    return f == null || new DeclarationInitializerStatementTerminatingScope().cantTip(f);
  }

  /** [[SuppressWarningsSpartan]] */
  public static boolean declarationRedundantInitializer(final ForStatement ¢) {
    for (final VariableDeclarationFragment f : extract.fragments(step.body(¢)))
      if (new DeclarationRedundantInitializer().canTip(f))
        return false;
    return true;
  }

  /** [[SuppressWarningsSpartan]] */
  public static boolean declarationRedundantInitializer(final WhileStatement ¢) {
    for (final VariableDeclarationFragment f : extract.fragments(step.body(¢)))
      if (new DeclarationRedundantInitializer().canTip(f))
        return false;
    return true;
  }

  public static boolean forRenameInitializerToCent(final ForStatement ¢) {
    final VariableDeclarationExpression e = az.variableDeclarationExpression(¢);
    return e == null || new ForRenameInitializerToCent().cantTip(e);
  }

  /** [[SuppressWarningsSpartan]] */
  public static boolean remvoeRedundantIf(final ForStatement ¢) {
    for (final IfStatement s : extract.ifStatements(step.body(¢)))
      if (new RemoveRedundantIf().canTip(s))
        return false;
    return true;
  }

  /** [[SuppressWarningsSpartan]] */
  public static boolean remvoeRedundantIf(final WhileStatement ¢) {
    for (final IfStatement s : extract.ifStatements(step.body(¢)))
      if (new RemoveRedundantIf().canTip(s))
        return false;
    return true;
  }
}