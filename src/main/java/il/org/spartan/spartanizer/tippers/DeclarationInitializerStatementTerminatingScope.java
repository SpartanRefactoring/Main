package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.Inliner.*;

/** Convert <code>int a=3;b=a;</code> into <code>b = a;</code>
 * @author Yossi Gil
 * @since 2015-08-07 */
public final class DeclarationInitializerStatementTerminatingScope extends $VariableDeclarationFragementAndStatement
    implements TipperCategory.Inlining {
  static boolean isPresentOnAnonymous(final SimpleName n, final Statement s) {
    for (final ASTNode ancestor : searchAncestors.until(s).ancestors(n))
      if (iz.nodeTypeEquals(ancestor, ANONYMOUS_CLASS_DECLARATION))
        return true;
    return false;
  }
  static boolean never(final SimpleName n, final Statement s) {
    for (final ASTNode ancestor : searchAncestors.until(s).ancestors(n))
      if (iz.nodeTypeIn(ancestor, TRY_STATEMENT, SYNCHRONIZED_STATEMENT))
        return true;
    return false;
  }
  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Inline local " + ¢.getName() + " into subsequent statement";
  }
  @Override protected ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final SimpleName n, final Expression initializer,
      final Statement nextStatement, final TextEditGroup g) {
    if (initializer == null || haz.annotation(f))
      return null;
    final VariableDeclarationStatement currentStatement = az.variableDeclrationStatement(f.getParent());
    if (currentStatement == null)
      return null;
    final Block parent = az.block(currentStatement.getParent());
    if (parent == null)
      return null;
    final List<Statement> ss = statements(parent);
    if (!lastIn(nextStatement, ss) || !penultimateIn(currentStatement, ss) || !Collect.definitionsOf(n).in(nextStatement).isEmpty())
      return null;
    final List<SimpleName> uses = Collect.usesOf(n).in(nextStatement);
    if (haz.sideEffects(initializer)) {
      final SimpleName use = onlyOne(uses);
      if (use == null || haz.unknownNumberOfEvaluations(use, nextStatement))
        return null;
    }
    for (final SimpleName use : uses)
      if (never(use, nextStatement) || isPresentOnAnonymous(use, nextStatement))
        return null;
    final Expression v = fixArrayInitializer(initializer, currentStatement);
    final InlinerWithValue i = new Inliner(n, r, g).byValue(v);
    final Statement newStatement = duplicate.of(nextStatement);
    final int addedSize = i.addedSize(newStatement);
    final int removalSaving = removalSaving(f);
    if (addedSize - removalSaving > 0)
      return null;
    r.replace(nextStatement, newStatement, g);
    i.inlineInto(newStatement);
    remove(f, r, g);
    return r;
  }
  private static Expression fixArrayInitializer(final Expression initializer, final VariableDeclarationStatement currentStatement) {
    if (!(initializer instanceof ArrayInitializer))
      return initializer;
    final ArrayCreation $ = initializer.getAST().newArrayCreation();
    $.setType((ArrayType) duplicate.of(currentStatement.getType()));
    $.setInitializer(duplicate.of((ArrayInitializer) initializer));
    return $;
  }
}
