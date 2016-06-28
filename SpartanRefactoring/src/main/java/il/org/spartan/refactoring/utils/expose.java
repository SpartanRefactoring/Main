package il.org.spartan.refactoring.utils;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/**
 * An empty <code><b>enum</b></code> for fluent programming. The name should say
 * it all: The name, followed by a dot, followed by a method name, should read
 * like a sentence phrase.
 *
 * @author Yossi Gil
 * @since 2015-07-16
 */
public enum expose {
  ;
  /**
   * Expose the list of arguments in a {@link ClassInstanceCreation}
   *
   * @param c JD
   * @return a reference to the list of arguments in the argument
   */
  public static List<Expression> arguments(final ClassInstanceCreation c) {
    return As.expressions(c.arguments());
  }
  /**
   * Expose the list of arguments in a {@link MethodInvocation}
   *
   * @param i JD
   * @return a reference to the list of arguments in the argument
   */
  public static List<Expression> arguments(final MethodInvocation i) {
    return As.expressions(i.arguments());
  }
  /**
   * Expose the list of arguments in a {@link SuperMethodInvocation}
   *
   * @param i JD
   * @return a reference to the list of arguments in the argument
   */
  public static List<Expression> arguments(final SuperMethodInvocation i) {
    return As.expressions(i.arguments());
  }
  /**
   * Expose the list of extended operands in an {@link InfixExpression}
   *
   * @param e JD
   * @return a reference to the list of extended operands contained in the
   *         parameter
   */
  public static List<Expression> extendedOperands(final InfixExpression e) {
    @SuppressWarnings("unchecked") final List<Expression> $ = e.extendedOperands();
    return $;
  }
  /**
   * Expose the list of fragments in a {@link FieldDeclaration}
   *
   * @param d JD
   * @return a reference to the list of fragments in the argument
   */
  public static List<VariableDeclarationFragment> fragments(final FieldDeclaration d) {
    return As.fragments(d.fragments());
  }
  /**
   * Expose the list of fragments in a {@link VariableDeclarationExpression}
   *
   * @param e JD
   * @return a reference to the list of fragments in the argument
   */
  public static List<VariableDeclarationFragment> fragments(final VariableDeclarationExpression e) {
    return As.fragments(e.fragments());
  }
  /**
   * Expose the list of fragments in a {@link VariableDeclarationStatement}
   *
   * @param s JD
   * @return a reference to the list of fragments in the argument
   */
  public static List<VariableDeclarationFragment> fragments(final VariableDeclarationStatement s) {
    return As.fragments(s.fragments());
  }
  /**
   * Expose the list of initializers contained in a {@link ForStatement}
   *
   * @param s JD
   * @return a reference to the list of initializers contained in the argument
   */
  @SuppressWarnings("unchecked") static List<VariableDeclarationExpression> initializers(final ForStatement s) {
    return s.initializers();
  }
  /**
   * Expose the list of resources contained in a {@link TryStatement}
   *
   * @param s JD
   * @return a reference to the list of resources contained in the argument
   */
  @SuppressWarnings("unchecked") static List<VariableDeclarationExpression> resources(final TryStatement s) {
    return s.resources();
  }
  /**
   * Expose the list of statements contained in a {@link Block}
   *
   * @param b JD
   * @return a reference to the list of statements contained in the argument
   */
  public static List<Statement> statements(final Block b) {
    return As.statements(b.statements());
  }
}
