package il.org.spartan.spartanizer.ast;

import org.eclipse.jdt.core.dom.*;

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
    return new DeclarationInitializerStatementTerminatingScope().cantTip(hop.prevToFirstLastExpressionFragment(¢));
  }

  public static boolean declarationInitializerStatementTerminatingScope(final WhileStatement ¢) {
    return new DeclarationInitializerStatementTerminatingScope().cantTip(hop.prevToFirstLastExpressionFragment(¢));
  }

  public static boolean forRenameInitializerToCent(final ForStatement ¢) {
    return new ForRenameInitializerToCent().cantTip(az.variableDeclarationExpression(¢));
  }
}