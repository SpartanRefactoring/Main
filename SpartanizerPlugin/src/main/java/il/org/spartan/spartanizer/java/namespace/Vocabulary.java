package il.org.spartan.spartanizer.java.namespace;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import org.jetbrains.annotations.NotNull;

/** TODO: Yossi Gil <tt>yossi.gil@gmail.com</tt> please add a description
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-18 */
public class Vocabulary extends HashMap<String, MethodDeclaration> {
  private static final long serialVersionUID = 1L;

  @NotNull
  public static String mangle(@NotNull final Assignment ¢) {
    return mangle(¢.getOperator());
  }

  @NotNull
  public static String mangle(final Assignment.Operator mangle) {
    return mangle + "";
  }

  public static String mangle(@NotNull final Class<? extends ASTNode> mangle) {
    return mangle.getSimpleName();
  }

  @NotNull
  public static String mangle(final InfixExpression.Operator o, final int arity) {
    return o + "/" + arity;
  }

  @NotNull
  public static String mangle(@NotNull final MethodDeclaration ¢) {
    return mangle(¢.getName(), step.parameters(¢).size());
  }

  @NotNull
  public static String mangle(@NotNull final MethodInvocation ¢) {
    return mangle(¢.getName(), step.arguments(¢).size());
  }

  @NotNull
  public static String mangle(@NotNull final PostfixExpression ¢) {
    return mangle(¢.getOperator());
  }

  @NotNull
  public static String mangle(final PostfixExpression.Operator ¢) {
    return ¢ + (PrefixExpression.Operator.toOperator(¢ + "") == null ? "" : "(post)");
  }

  @NotNull
  public static String mangle(@NotNull final PrefixExpression ¢) {
    return mangle(¢.getOperator());
  }

  @NotNull
  public static String mangle(final PrefixExpression.Operator ¢) {
    return ¢ + (PostfixExpression.Operator.toOperator(¢ + "") == null ? "" : "(pre)");
  }

  @NotNull
  public static String mangle(final SimpleName n, final int arity) {
    return n + "/" + arity;
  }
}
