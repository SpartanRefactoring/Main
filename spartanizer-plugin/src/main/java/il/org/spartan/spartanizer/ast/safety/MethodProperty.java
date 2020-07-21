package il.org.spartan.spartanizer.ast.safety;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-06-27 */
public interface MethodProperty {
  static boolean callingOtherMethods(final Stream<ASTNode> ¢) {
    return ¢.noneMatch(MethodProperty::callingOtherMethods);
  }
  static boolean callingOtherMethods(final Block ¢) {
    return callingOtherMethods(statements(¢));
  }
  static boolean callingOtherMethods(final List<Statement> ss) {
    return callingOtherMethods(ss.stream().map(λ -> (ASTNode) λ));
  }
  static boolean callingOtherMethods(final MethodDeclaration ¢) {
    return callingOtherMethods(body(¢));
  }
  static boolean callingOtherMethods(final ASTNode ¢) {
    return iz.nodeTypeIn(¢, ARRAY_CREATION, METHOD_INVOCATION, CLASS_INSTANCE_CREATION, CONSTRUCTOR_INVOCATION, ANONYMOUS_CLASS_DECLARATION,
        SUPER_CONSTRUCTOR_INVOCATION, SUPER_METHOD_INVOCATION, LAMBDA_EXPRESSION);
  }
  static boolean letItBeIn(final List<Statement> ¢) {
    return ¢.size() == 2 && the.firstOf(¢) instanceof VariableDeclarationStatement;
  }
}
