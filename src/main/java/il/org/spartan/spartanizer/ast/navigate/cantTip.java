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
    final VariableDeclarationFragment $ = hop.precidingFragmentToLastExpression(¢);
    return $ == null || new DeclarationInitializerStatementTerminatingScope().cantTip($);
  }

  public static boolean declarationInitializerStatementTerminatingScope(final WhileStatement ¢) {
    final VariableDeclarationFragment $ = hop.prevFragmentToLastExpression(¢);
    return $ == null || new DeclarationInitializerStatementTerminatingScope().cantTip($);
  }

  public static boolean declarationRedundantInitializer(final ForStatement s) {
    for (final VariableDeclarationFragment ¢ : extract.fragments(step.body(s)))
      if (new DeclarationRedundantInitializer().canTip(¢))
        return false;
    return true;
  }

  public static boolean declarationRedundantInitializer(final WhileStatement s) {
    for (final VariableDeclarationFragment ¢ : extract.fragments(step.body(s))) // NANO?
      if (new DeclarationRedundantInitializer().canTip(¢))
        return false;
    return true;
  }

  public static boolean forRenameInitializerToCent(final ForStatement ¢) {
    final VariableDeclarationExpression $ = az.variableDeclarationExpression(¢);
    return $ == null || new ForRenameInitializerToCent().cantTip($);
  }

  public static boolean remvoeRedundantIf(final ForStatement s) {
    for (final IfStatement ¢ : extract.ifStatements(step.body(s)))
      if (new RemoveRedundantIf().canTip(¢))
        return false;
    return true;
  }

  public static boolean remvoeRedundantIf(final WhileStatement s) {
    for (final IfStatement ¢ : extract.ifStatements(step.body(s)))
      if (new RemoveRedundantIf().canTip(¢))
        return false;
    return true;
  }
}