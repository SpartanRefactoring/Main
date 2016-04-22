package il.org.spartan.refactoring.utils;

import java.util.List;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
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
   * Get the list of arguments in a {@link ClassInstanceCreation}
   *
   * @param c JD
   * @return a reference to the list of arguments in the argument
   */
  public static List<Expression> arguments(ClassInstanceCreation c) {
    return As.expressions(c.arguments());
  }
  /**
   * Get the list of arguments in a {@link MethodInvocation}
   *
   * @param i JD
   * @return a reference to the list of arguments in the argument
   */
  public static List<Expression> arguments(final MethodInvocation i) {
    return As.expressions(i.arguments());
  }
  /**
   * Get the list of arguments in a {@link SuperMethodInvocation}
   *
   * @param i JD
   * @return a reference to the list of arguments in the argument
   */
  public static List<Expression> arguments(final SuperMethodInvocation i) {
    return As.expressions(i.arguments());
  }
  /**
   * Get the list of extended operands in an {@link InfixExpression}
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
   * Get the list of fragments in a {@link VariableDeclarationExpression}
   *
   * @param e JD
   * @return a reference to the list of fragments in the argument
   */
  public static List<VariableDeclarationFragment> fragments(VariableDeclarationExpression e) {
    return As.fragments(e.fragments());
  }
  /**
   * Get the list of fragments in a {@link VariableDeclarationStatement}
   *
   * @param s JD
   * @return a reference to the list of fragments in the argument
   */
  public static List<VariableDeclarationFragment> fragments(VariableDeclarationStatement s) {
    return As.fragments(s.fragments());
  }
  /**
   * Get the list of statements contained in a {@link Block}
   *
   * @param b JD
   * @return a reference to the list of statements contained in the parameter
   */
  public static List<Statement> statements(Block b) {
    return As.statements(b.statements());
  }
}
