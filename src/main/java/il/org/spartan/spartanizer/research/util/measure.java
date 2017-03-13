package il.org.spartan.spartanizer.research.util;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.lisp.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.*;

/** Utility class to measure number of statemetns\expressions in an ASTNode's
 * subtree, skipping some ASTNode objects
 * @author Ori Marcovitch
 * @since Oct 28, 2016 */
public enum measure {
  ;
  public static int expressions(final ASTNode n) {
    if (n == null)
      return 0;
    final Int $ = new Int();
    n.accept(new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode ¢) {
        if (iz.expression(¢) && !excluded(az.expression(¢)))
          $.step();
      }
    });
    return $.inner;
  }

  public static int commands(final ASTNode n) {
    final Int $ = new Int();
    if (n == null)
      return 0;
    n.accept(new ASTVisitor(true) {
      @Override public boolean preVisit2(final ASTNode ¢) {
        if (iz.statement(¢) && !excluded(az.statement(¢)))
          $.step();
        return !iz.classInstanceCreation(¢);
      }
    });
    return $.inner;
  }

  static boolean excluded(final Statement ¢) {
    return as.list(//
        Block.class, //
        // BreakStatement.class, //
        // continueStatement.class, //
        EmptyStatement.class //
    // LabeledStatement.class, //
    // SwitchCase.class, //
    // TypeDeclarationStatement.class, //
    // VariableDeclarationStatement.class //11
    )//
        .contains(¢.getClass()) //
        || iz.variableDeclarationStatement(¢)//
            && initializer(onlyOne(fragments(az.variableDeclarationStatement(¢)))) == null;
  }

  static boolean excluded(final Expression ¢) {
    return as.list(//
        Annotation.class //
    // ArrayAccess.class, //
    // ArrayCreation.class, //
    // ArrayInitializer.class, //
    // BooleanLiteral.class, //
    // CharacterLiteral.class, //
    // ClassInstanceCreation.class, //
    // CreationReference.class, //
    // ExpressionMethodReference.class, //
    // FieldAccess.class, //
    // InfixExpression.class, //
    // MethodInvocation.class, //
    // MethodReference.class, //
    // Name.class, //
    // NullLiteral.class, //
    // NumberLiteral.class, //
    // ParenthesizedExpression.class, //
    // PostfixExpression.class, //
    // PrefixExpression.class, //
    // StringLiteral.class, //
    // SuperFieldAccess.class, //
    // SuperMethodInvocation.class, //
    // SuperMethodReference.class, //
    // ThisExpression.class, //
    // TypeLiteral.class, //
    // TypeMethodReference.class, //
    // VariableDeclarationExpression.class//
    )//
        .contains(¢.getClass());
  }
}
