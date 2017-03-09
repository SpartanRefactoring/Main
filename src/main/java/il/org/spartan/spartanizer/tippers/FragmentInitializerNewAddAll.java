package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.lisp.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** convert {@code
 * T a = new ArrayList<>() / new HashSet();
 * a.addAll(x)
 * } to {@code
 * T a = new ArrayList
 * }
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @author Yossi Gil
 *         {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since 2017-03-02 */
public final class FragmentInitializerNewAddAll extends ReplaceToNextStatement<VariableDeclarationFragment>//
    implements TipperCategory.Inlining {
  @SuppressWarnings("unused") private Type type;

  @Override public boolean prerequisite(final VariableDeclarationFragment f) {
    final ClassInstanceCreation instanceCreation = az.classInstanceCreation(f.getInitializer());
    if (instanceCreation == null)
      return false;
    type = instanceCreation.getType();
    return true;
  }

  private static final long serialVersionUID = -228096256168103399L;

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Inline assignment to " + name(¢) + " into next statement";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final VariableDeclarationFragment f, final Statement nextStatement, final TextEditGroup g) {
    if (containsClassInstanceCreation(nextStatement)//
        || Tipper.forbiddenOpOnPrimitive(f, nextStatement))
      return null;
    switch (nodeType(nextStatement)) {
      case ASTNode.DO_STATEMENT:
      case ASTNode.ENHANCED_FOR_STATEMENT:
      case ASTNode.FOR_STATEMENT:
      case ASTNode.RETURN_STATEMENT:
      case ASTNode.SYNCHRONIZED_STATEMENT:
      case ASTNode.TRY_STATEMENT:
      case ASTNode.WHILE_STATEMENT:
        return null;
      default:
        if (containsClassInstanceCreation(nextStatement))
          return null;
        if (containsLambda(nextStatement))
          return null;
    }
    final Expression initializer = initializer(f);
    if (initializer == null)
      return null;
    final Statement parent = az.statement(parent(f));
    if (parent == null//
        || iz.forStatement(parent))
      return null;
    final SimpleName n = peelIdentifier(nextStatement, identifier(name(f)));
    if (n == null//
        || anyFurtherUsage(parent, nextStatement, identifier(n))//
        || leftSide(nextStatement, identifier(n))//
        || preOrPostfix(n))
      return null;
    Expression e = !iz.castExpression(initializer) ? initializer : subject.operand(initializer).parenthesis();
    final VariableDeclarationStatement pp = az.variableDeclarationStatement(parent);
    if (pp != null)
      e = Inliner.protect(e, pp);
    if (pp == null//
        || fragments(pp).size() <= 1)
      $.remove(parent, g);
    else {
      if (nodeType(type(pp)) == ASTNode.ARRAY_TYPE)
        return null;
      final VariableDeclarationStatement pn = copy.of(pp);
      final List<VariableDeclarationFragment> l = fragments(pp);
      for (int ¢ = l.size() - 1; ¢ >= 0; --¢) {
        if (l.get(¢).equals(f)) {
          fragments(pn).remove(¢);
          break;
        }
        if (iz.containsName(name(f), initializer(l.get(¢))))
          return null;
      }
      $.replace(parent, pn, g);
    }
    $.replace(n, e, g);
    return $;
  }

  private static boolean containsLambda(final Statement nextStatement) {
    return !descendants.whoseClassIs(LambdaExpression.class).from(nextStatement).isEmpty();
  }

  private static boolean preOrPostfix(final SimpleName id) {
    final ASTNode $ = parent(id);
    return iz.prefixExpression($)//
        || iz.postfixExpression($);
  }

  private static boolean containsClassInstanceCreation(final Statement nextStatement) {
    return !descendants.whoseClassIs(ClassInstanceCreation.class).from(nextStatement).isEmpty();
  }

  private static boolean anyFurtherUsage(final Statement originalStatement, final Statement nextStatement, final String id) {
    final Bool $ = new Bool();
    final ASTNode parent = parent(nextStatement);
    parent.accept(new ASTVisitor(true) {
      @Override public boolean preVisit2(final ASTNode ¢) {
        if (parent.equals(¢))
          return true;
        if (!¢.equals(nextStatement)//
            && !¢.equals(originalStatement)//
            && iz.statement(¢)//
            && !occurencesOf(az.statement(¢), id).isEmpty())
          $.inner = true;
        return false;
      }
    });
    return $.inner;
  }

  private static boolean leftSide(final Statement nextStatement, final String id) {
    final Bool $ = new Bool();
    nextStatement.accept(new ASTVisitor(true) {
      @Override public boolean visit(final Assignment ¢) {
        if (iz.simpleName(left(¢))//
            && identifier(az.simpleName(left(¢))).equals(id))
          $.inner = true;
        return true;
      }
    });
    return $.inner;
  }

  private static SimpleName peelIdentifier(final Statement s, final String id) {
    final List<SimpleName> $ = occurencesOf(s, id);
    return $.size() != 1 ? null : first($);
  }

  static List<SimpleName> occurencesOf(final ASTNode $, final String id) {
    return descendants.whoseClassIs(SimpleName.class).suchThat(λ -> identifier(λ).equals(id)).from($);
  }
}
