package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** convert
 *
 * <pre>
 * a = 3;
 * whatever(a);
 * </pre>
 *
 * to
 *
 * <pre>
 * whatever(3);
 * </pre>
 *
 * @author Ori Marcovitch
 * @since 2016-11-27 */
public final class DeclarationFragmentInlineIntoNext extends ReplaceToNextStatement<VariableDeclarationFragment> implements TipperCategory.Collapse {
  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Inline assignment to " + name(¢) + " with its subsequent statement";
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final Statement nextStatement, final TextEditGroup g) {
    final Statement parent = az.statement(f.getParent());
    if (parent == null || iz.forStatement(parent) || nextStatement == null || iz.forStatement(nextStatement) || cannotInlineInto(nextStatement)
        || initializer(f) == null)
      return null;
    SimpleName id = peelIdentifier(nextStatement, identifier(name(f)));
    if (id == null || anyFurtherUsage(parent, nextStatement, identifier(id)) || leftSide(nextStatement, identifier(id)) || preOrPostfix(id))
      return null;
    r.remove(parent, g);
    r.replace(id, initializer(f), g);
    return r;
  }

  private static boolean cannotInlineInto(final Statement nextStatement) {
    return iz.returnStatement(nextStatement) || iz.whileStatement(nextStatement) || iz.doStatement(nextStatement)
        || iz.synchronizedStatement(nextStatement) || iz.tryStatement(nextStatement) || containsClassInstanceCreation(nextStatement);
  }

  /** @param id
   * @return */
  private static boolean preOrPostfix(SimpleName id) {
    ASTNode p = parent(id);
    return iz.prefixExpression(p) || iz.postfixExpression(p);
  }

  /** @param nextStatement
   * @return */
  private static boolean containsClassInstanceCreation(Statement nextStatement) {
    return !searchDescendants.forClass(ClassInstanceCreation.class).from(nextStatement).isEmpty();
  }

  private static boolean anyFurtherUsage(final Statement originalStatement, final Statement nextStatement, String id) {
    Bool $ = new Bool();
    final ASTNode parent = nextStatement.getParent();
    parent.accept(new ASTVisitor() {
      @Override public boolean preVisit2(ASTNode ¢) {
        if (parent.equals(¢))
          return true;
        if (!¢.equals(nextStatement) && !¢.equals(originalStatement) && iz.statement(¢) && !occurencesOf(az.statement(¢), id).isEmpty())
          $.inner = true;
        return false;
      }
    });
    return $.inner;
  }

  private static boolean leftSide(final Statement nextStatement, String id) {
    Bool $ = new Bool();
    nextStatement.accept(new ASTVisitor() {
      @Override public boolean visit(Assignment ¢) {
        if (iz.simpleName(left(¢)) && identifier(az.simpleName(left(¢))).equals(id))
          $.inner = true;
        return true;
      }
    });
    return $.inner;
  }

  static SimpleName peelIdentifier(Statement s, String id) {
    List<SimpleName> li = occurencesOf(s, id);
    return li.size() != 1 ? null : li.get(0);
  }

  static List<SimpleName> occurencesOf(Statement s, String id) {
    return searchDescendants.forClass(SimpleName.class).suchThat(x -> identifier(x).equals(id)).from(s);
  }
}
