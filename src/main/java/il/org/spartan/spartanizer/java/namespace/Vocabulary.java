package il.org.spartan.spartanizer.java.namespace;

import java.util.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** TODO: Yossi Gil <tt>yossi.gil@gmail.com</tt> please add a description
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-18 */
public class Vocabulary extends HashMap<String, MethodDeclaration> {
  private static final long serialVersionUID = 1L;

  public static String mangle(final Assignment ¢) {
    return mangle(¢.getOperator());
  }

  public static String mangle(final Assignment.Operator mangle) {
    return mangle + "";
  }

  public static String mangle(final Class<? extends ASTNode> mangle) {
    return mangle.getSimpleName();
  }

  public static String mangle(final InfixExpression.Operator o, final int arity) {
    return o + "/" + arity;
  }

  public static String mangle(final MethodDeclaration ¢) {
    return mangle(¢.getName(), step.parameters(¢).size());
  }

  public static String mangle(final MethodInvocation ¢) {
    return mangle(¢.getName(), step.arguments(¢).size());
  }

  public static String mangle(final PostfixExpression ¢) {
    return mangle(¢.getOperator());
  }

  public static String mangle(final PostfixExpression.Operator ¢) {
    return ¢ + (PrefixExpression.Operator.toOperator(¢ + "") == null ? "" : "(post)");
  }

  public static String mangle(final PrefixExpression ¢) {
    return mangle(¢.getOperator());
  }

  public static String mangle(final PrefixExpression.Operator ¢) {
    return ¢ + (PostfixExpression.Operator.toOperator(¢ + "") == null ? "" : "(pre)");
  }

  public static String mangle(final SimpleName n, final int arity) {
    return n + "/" + arity;
  }
}
