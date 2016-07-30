package il.org.spartan.refactoring.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

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
   * Expose the list of arguments in a {@link ClassInstanceCreation}
   *
   * @param c
   *          JD
   * @return a reference to the list of arguments in the argument
   */
  public static List<Expression> arguments(final ClassInstanceCreation c) {
    return MakeAST.expressions(c.arguments());
  }
  /**
   * Expose the list of arguments in a {@link MethodInvocation}
   *
   * @param i
   *          JD
   * @return a reference to the list of arguments in the argument
   */
  public static List<Expression> arguments(final MethodInvocation i) {
    return MakeAST.expressions(i.arguments());
  }
  /**
   * Expose the list of arguments in a {@link SuperMethodInvocation}
   *
   * @param i
   *          JD
   * @return a reference to the list of arguments in the argument
   */
  public static List<Expression> arguments(final SuperMethodInvocation i) {
    return MakeAST.expressions(i.arguments());
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
  public static List<VariableDeclarationFragment> fragments(final FieldDeclaration d) {
    return MakeAST.fragments(d.fragments());
  }
  /**
   * Expose the list of fragments in a {@link VariableDeclarationExpression}
   *
   * @param e
   *          JD
   * @return a reference to the list of fragments in the argument
   */
  public static List<VariableDeclarationFragment> fragments(final VariableDeclarationExpression e) {
    if (e == null)
      return new ArrayList<>();
    return MakeAST.fragments(e.fragments());
  }
  /**
   * Expose the list of fragments in a {@link VariableDeclarationStatement}
   *
   * @param s
   *          JD
   * @return a reference to the list of fragments in the argument
   */
  public static List<VariableDeclarationFragment> fragments(final VariableDeclarationStatement s) {
    return MakeAST.fragments(s.fragments());
  }
  /**
   * Expose the list of initializers contained in a {@link ForStatement}
   *
   * @param s
   *          JD
   * @return a reference to the list of initializers contained in the argument
   */
  @SuppressWarnings("unchecked") static List<Expression> initializers(final ForStatement s) {
    return s.initializers();
  }
  /**
   * Expose the list of resources contained in a {@link TryStatement}
   *
   * @param s
   *          JD
   * @return a reference to the list of resources contained in the argument
   */
  @SuppressWarnings("unchecked") static List<VariableDeclarationExpression> resources(final TryStatement s) {
    return s.resources();
  }
  /**
   * Expose the list of statements contained in a {@link Block}
   *
   * @param b
   *          JD
   * @return a reference to the list of statements contained in the argument
   */
  public static List<Statement> statements(final Block b) {
    return MakeAST.statements(b.statements());
  }
  @SuppressWarnings("unchecked") public static List<BodyDeclaration> bodyDeclarations(AbstractTypeDeclaration d) {
    return d.bodyDeclarations();
  }
  @SuppressWarnings("unchecked") public static List<BodyDeclaration> bodyDeclarations(AnonymousClassDeclaration d) {
    return d.bodyDeclarations();
  }
  @SuppressWarnings("unchecked") public static List<IExtendedModifier> modifiers(VariableDeclarationStatement s) {
    return s.modifiers();
  }
  @SuppressWarnings("unchecked") public static List<TagElement> tags(Javadoc j) {
    return j.tags();
  }
}
