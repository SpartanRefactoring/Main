package il.org.spartan.spartanizer.research.util;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.*;

/** @author Ori Marcovitch
 * @since Oct 28, 2016 */
public enum measure {
  ;
  public static int expressions(final ASTNode n) {
    if (n == null)
      return 0;
    final Int $ = new Int();
    $.inner = 0;
    n.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        if (iz.expression(¢) && !excluded(az.expression(¢)))
          ++$.inner;
      }
    });
    return $.inner;
  }

  public static int statements(final ASTNode n) {
    final Int count = new Int();
    if (n == null)
      return 0;
    n.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        if (iz.statement(¢) && !excluded(az.statement(¢)))
          ++count.inner;
      }
    });
    return count.inner;
  }

  /** @param ¢
   * @return */
  static boolean excluded(Statement ¢) {
    return Arrays.asList(//
        Block.class, //
        BreakStatement.class, //
        ContinueStatement.class, //
        EmptyStatement.class, //
        LabeledStatement.class, //
        SwitchCase.class, //
        TypeDeclarationStatement.class, //
        VariableDeclarationStatement.class //
    )//
        .contains(¢.getClass());
  }

  /** @param ¢
   * @return */
  static boolean excluded(Expression ¢) {
    return Arrays.asList(//
        Annotation.class, //
        ArrayAccess.class, //
        ArrayCreation.class, //
        ArrayInitializer.class, //
        BooleanLiteral.class, //
        CharacterLiteral.class, //
        ClassInstanceCreation.class, //
        CreationReference.class, //
        ExpressionMethodReference.class, //
        FieldAccess.class, //
        InfixExpression.class, //
        MethodInvocation.class, //
        MethodReference.class, //
        Name.class, //
        NullLiteral.class, //
        NumberLiteral.class, //
        ParenthesizedExpression.class, //
        PostfixExpression.class, PrefixExpression.class, //
        StringLiteral.class, //
        SuperFieldAccess.class, //
        SuperMethodInvocation.class, //
        SuperMethodReference.class, //
        ThisExpression.class, //
        TypeLiteral.class, //
        TypeMethodReference.class, //
        VariableDeclarationExpression.class//
    )//
        .contains(¢.getClass());
  }
}
