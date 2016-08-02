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
   * @param c
   *          JD
   * @return a reference to the list of arguments in the argument
   */
  @SuppressWarnings("unchecked") public static List<Expression> arguments(final ClassInstanceCreation c) {
    return c.arguments();
  }
  /**
   * Expose the list of arguments in a {@link MethodInvocation}
   *
   * @param i
   *          JD
   * @return a reference to the list of arguments in the argument
   */
  @SuppressWarnings("unchecked") public static List<Expression> arguments(final MethodInvocation i) {
    return i.arguments();
  }
  /**
   * Expose the list of arguments in a {@link SuperMethodInvocation}
   *
   * @param i
   *          JD
   * @return a reference to the list of arguments in the argument
   */
  @SuppressWarnings("unchecked") public static List<Expression> arguments(final SuperMethodInvocation i) {
    return i.arguments();
  }
  @SuppressWarnings("unchecked") public static List<BodyDeclaration> bodyDeclarations(final AbstractTypeDeclaration d) {
    return d.bodyDeclarations();
  }
  @SuppressWarnings("unchecked") public static List<BodyDeclaration> bodyDeclarations(final AnonymousClassDeclaration d) {
    return d.bodyDeclarations();
  }
  @SuppressWarnings("unchecked") public static List<CatchClause> catchClauses(final TryStatement s) {
    return s.catchClauses();
  }
  /**
   * Expose the list of extended operands in an {@link InfixExpression}
   *
   * @param e
   *          JD
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
   * @param d
   *          JD
   * @return a reference to the list of fragments in the argument
   */
  @SuppressWarnings("unchecked") public static List<VariableDeclarationFragment> fragments(final FieldDeclaration d) {
    return d.fragments();
  }
  /**
   * Expose the list of fragments in a {@link VariableDeclarationExpression}
   *
   * @param e
   *          JD
   * @return a reference to the list of fragments in the argument
   */
  @SuppressWarnings("unchecked") public static List<VariableDeclarationFragment> fragments(final VariableDeclarationExpression e) {
    return e != null ? e.fragments() : new ArrayList<>();
  }
  /**
   * Expose the list of fragments in a {@link VariableDeclarationStatement}
   *
   * @param s
   *          JD
   * @return a reference to the list of fragments in the argument
   */
  @SuppressWarnings("unchecked") public static List<VariableDeclarationFragment> fragments(final VariableDeclarationStatement s) {
    return  s.fragments();
  }
  /**
   * Expose the list of initializers contained in a {@link ForStatement}
   *
   * @param s
   *          JD
   * @return a reference to the list of initializers contained in the argument
   */
  @SuppressWarnings("unchecked") public static List<Expression> initializers(final ForStatement s) {
    return s.initializers();
  }
  @SuppressWarnings("unchecked") public static List<IExtendedModifier> modifiers(final VariableDeclarationStatement s) {
    return s.modifiers();
  }
  @SuppressWarnings("unchecked") public static List<IExtendedModifier> modifiers(final BodyDeclaration d) {
    return d.modifiers();
  }
  /**
   * Expose the list of parameters in a {@link MethodDeclaration}
   *
   * @param ¢
   *          JD
   *
   * @return result of method {@link MethodDeclaration#parameters} downcasted to
   *         its correct type
   */
  @SuppressWarnings("unchecked") public static List<SingleVariableDeclaration> parameters(final MethodDeclaration ¢) {
    return ¢.parameters();
  }
  /**
   * Expose the list of resources contained in a {@link TryStatement}
   *
   * @param s
   *          JD
   * @return a reference to the list of resources contained in the argument
   */
  @SuppressWarnings("unchecked") public static List<VariableDeclarationExpression> resources(final TryStatement s) {
    return s.resources();
  }
  /**
   * Expose the list of statements contained in a {@link Block}
   *
   * @param b
   *          JD
   * @return a reference to the list of statements contained in the argument
   */
  @SuppressWarnings("unchecked") public static List<Statement> statements(final Block b) {
    return  b.statements();
  }
  @SuppressWarnings("unchecked") public static List<TagElement> tags(final Javadoc j) {
    return j.tags();
  }
  @SuppressWarnings("unchecked") public static List<ParameterizedType> typeArguments(final ParameterizedType t) {
    return t.typeArguments();
  }
}
