package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.Utils.*;
import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.step.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.Inliner.*;
import il.org.spartan.spartanizer.java.*;

/** convert
 *
 * <pre>
 * int a = 3;
 * b = a;
 * </pre>
 *
 * into
 *
 * <pre>
 * b = a
 * </pre>
 *
 * @author Yossi Gil
 * @since 2015-08-07 */
public final class DeclarationInitializerStatementTerminatingScope extends $VariableDeclarationFragementAndStatement implements Kind.Inlining {
  static boolean isPresentOnAnymous(final SimpleName n, final Statement s) {
    for (final ASTNode ancestor : searchAncestors.until(s).ancestors(n))
      if (iz.is(ancestor, ANONYMOUS_CLASS_DECLARATION))
        return true;
    return false;
  }

  static boolean never(final SimpleName n, final Statement s) {
    for (final ASTNode ancestor : searchAncestors.until(s).ancestors(n))
      if (iz.is(ancestor, TRY_STATEMENT, SYNCHRONIZED_STATEMENT))
        return true;
    return false;
  }

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Inline local " + ¢.getName() + " into subsequent statement";
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final SimpleName name, final Expression initializer,
      final Statement nextStatement, final TextEditGroup g) {
    if (initializer == null || haz.annotation(f) || initializer instanceof ArrayInitializer)
      return null;
    final VariableDeclarationStatement currentStatement = az.variableDeclrationStatement(f.getParent());
    if (currentStatement == null)
      return null;
    final Block parent = az.block(currentStatement.getParent());
    if (parent == null)
      return null;
    final List<Statement> ss = statements(parent);
    if (!lastIn(nextStatement, ss) || !penultimateIn(currentStatement, ss) || !Collect.definitionsOf(name).in(nextStatement).isEmpty())
      return null;
    final List<SimpleName> uses = Collect.usesOf(name).in(nextStatement);
    if (!sideEffects.free(initializer)) {
      final SimpleName use = onlyOne(uses);
      if (use == null || haz.unknownNumberOfEvaluations(use, nextStatement))
        return null;
    }
    for (final SimpleName use : uses) {
      if (never(use, nextStatement))
        return null;
      if (isPresentOnAnymous(use, nextStatement))
        return null;
    }
    final InlinerWithValue i = new Inliner(name, r, g).byValue(initializer);
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
}
