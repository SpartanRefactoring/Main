package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.spartanizer.ast.navigate.step.*;
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
    final VariableDeclarationFragment $ = hop.penultimateFragment(¢);
    return $ == null || new FragmentInitializerStatementTerminatingScope().cantTip($);
  }

  public static boolean declarationInitializerStatementTerminatingScope(final WhileStatement ¢) {
    final VariableDeclarationFragment $ = hop.penultimate(¢);
    return $ == null || new FragmentInitializerStatementTerminatingScope().cantTip($);
  }

  public static boolean declarationRedundantInitializer(final ForStatement s) {
    return extract.fragments(body(s)).stream().noneMatch(λ -> new FragmentInitializerDead().canTip(λ));
  }

  public static boolean declarationRedundantInitializer(final WhileStatement s) {
    return extract.fragments(body(s)).stream().noneMatch(λ -> new FragmentInitializerDead().canTip(λ));
  }

  public static boolean forRenameInitializerToCent(final ForStatement ¢) {
    final VariableDeclarationExpression $ = az.variableDeclarationExpression(¢);
    return $ == null || new ForRenameInitializerToCent().cantTip($);
  }

  public static boolean remvoeRedundantIf(final ForStatement s) {
    return extract.ifStatements(step.body(s)).stream().noneMatch(λ -> new IfDeadRemove().canTip(λ));
  }

  public static boolean remvoeRedundantIf(final WhileStatement s) {
    return extract.ifStatements(step.body(s)).stream().noneMatch(λ -> new IfDeadRemove().canTip(λ));
  }
}