package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.Utils.*;
import static il.org.spartan.spartanizer.ast.step.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.Inliner.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.wring.strategies.*;

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
public final class DeclarationInitializerStatementTerminatingScope extends VariableDeclarationFragementAndStatement implements Kind.Inlining {
  private static boolean forbidden(final SimpleName n, final Statement s) {
    ASTNode child = null;
    for (final ASTNode ancestor : searchAncestors.until(s).ancestors(n)) {
      if (iz.is(ancestor, WHILE_STATEMENT, DO_STATEMENT, ANONYMOUS_CLASS_DECLARATION)
          || iz.is(ancestor, FOR_STATEMENT) && step.initializers((ForStatement) ancestor).indexOf(child) != -1
          || iz.is(ancestor, ENHANCED_FOR_STATEMENT) && ((EnhancedForStatement) ancestor).getExpression() != child)
        return true;
      child = ancestor;
    }
    return false;
  }

  private static boolean never(final SimpleName n, final Statement s) {
    for (final ASTNode ancestor : searchAncestors.until(s).ancestors(n))
      if (iz.is(ancestor, TRY_STATEMENT, SYNCHRONIZED_STATEMENT))
        return true;
    return false;
  }

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Inline local " + ¢.getName() + " into subsequent statement";
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final SimpleName n, final Expression initializer,
      final Statement nextStatement, final TextEditGroup g) {
    if (initializer == null || hasAnnotation(f) || initializer instanceof ArrayInitializer)
      return null;
    for (final IExtendedModifier ¢ : step.modifiers((VariableDeclarationStatement) f.getParent()))
      if (¢.isModifier() && ((Modifier) ¢).isFinal())
        return null;
    final Statement s = extract.statement(f);
    if (s == null)
      return null;
    final Block parent = az.block(s.getParent());
    if (parent == null)
      return null;
    final List<Statement> ss = statements(parent);
    if (!lastIn(nextStatement, ss) || !penultimateIn(s, ss) || !Collect.definitionsOf(n).in(nextStatement).isEmpty())
      return null;
    final List<SimpleName> uses = Collect.usesOf(f.getName()).in(nextStatement);
    if (!sideEffects.free(initializer)) {
      if (uses.size() > 1)
        return null;
      for (final SimpleName use : uses)
        if (forbidden(use, nextStatement))
          return null;
    }
    for (final SimpleName use : uses)
      if (never(use, nextStatement))
        return null;
    final InlinerWithValue i = new Inliner(n, r, g).byValue(initializer);
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
