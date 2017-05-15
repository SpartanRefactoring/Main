package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tippers.*;

/** An empty {@code enum} for fluent programming. The name should say it all:
 * The name, followed by a dot, followed by a method name, should read like a
 * sentence phrase. Generally here comes all the checks, and coercions related
 * to tips ordering and collisions.
 * @author Alex Kopzon
 * @since 2.5 */
public enum cantTip {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  public static boolean declarationInitializerStatementTerminatingScope(final ForStatement ¢) {
    final VariableDeclarationFragment $ = hop.penultimateFragment(¢);
    return $ == null || new LocalInitializedStatementTerminatingScope().cantTip($);
  }
  public static boolean declarationInitializerStatementTerminatingScope(final WhileStatement ¢) {
    final VariableDeclarationFragment $ = hop.penultimate(¢);
    return $ == null || new LocalInitializedStatementTerminatingScope().cantTip($);
  }
  public static boolean declarationRedundantInitializer(final ForStatement s) {
    return extract.fragments(body(s)).stream().noneMatch(λ -> new FieldInitializedDefaultValue().check(λ));
  }
  public static boolean declarationRedundantInitializer(final WhileStatement s) {
    return extract.fragments(body(s)).stream().noneMatch(λ -> new FieldInitializedDefaultValue().check(λ));
  }
  public static boolean forRenameInitializerToIt(final ForStatement ¢) {
    final VariableDeclarationExpression $ = az.variableDeclarationExpression(¢);
    return $ == null || new ForRenameInitializerToIt().cantTip($);
  }
  public static boolean removeRedundantIf(final ForStatement s) {
    return extract.ifStatements(step.body(s)).stream().noneMatch(λ -> new IfDeadRemove().check(λ));
  }
  public static boolean remvoeRedundantIf(final WhileStatement s) {
    return extract.ifStatements(step.body(s)).stream().noneMatch(λ -> new IfDeadRemove().check(λ));
  }
}