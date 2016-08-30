package il.org.spartan.refactoring.utils;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

public interface jump {

  static CompilationUnit compilationUnit(final ASTNode ¢) {
    return (CompilationUnit) AncestorSearch.forType(COMPILATION_UNIT).from(¢);
  }

  /** @param ¢ JD
   * @return ASTNode of the type if one of ¢'s parent ancestors is a container
   *         type and null otherwise */
  static ASTNode containerType(final ASTNode ¢) {
    for (final ASTNode $ : navigate.ancestors(¢.getParent()))
      if (iz.is($, ANONYMOUS_CLASS_DECLARATION //
          , ANNOTATION_TYPE_DECLARATION //
          , ENUM_DECLARATION //
          , TYPE_DECLARATION //
          , ENUM_CONSTANT_DECLARATION //
      ))
        return $;
    return null;
  }

  /** Makes a list of all operands of an expression, comprising the left
   * operand, the right operand, followed by extra operands when they exist.
   * @param e JD
   * @return a list of all operands of an expression */
  static List<Expression> operands(final InfixExpression e) {
    if (e == null)
      return null;
    final List<Expression> $ = new ArrayList<>();
    $.add(navigate.left(e));
    $.add(navigate.right(e));
    if (e.hasExtendedOperands())
      $.addAll(navigate.extendedOperands(e));
    return $;
  }

  /** Find the last statement residing under a given {@link Statement}
   * @param s JD
   * @return last statement residing under a given {@link Statement}, or
   *         <code><b>null</b></code> if not such statements exists. */
  static ASTNode lastStatement(final Statement s) {
    return last(extract.statements(s));
  }

  /** @param root the node whose children we return
   * @return A list containing all the nodes in the given root'¢ sub tree */
  static List<ASTNode> descendants(final ASTNode root) {
    if (root == null)
      return null;
    final List<ASTNode> $ = new ArrayList<>();
    root.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        $.add(¢);
      }
    });
    $.remove(0);
    return $;
  }
}
