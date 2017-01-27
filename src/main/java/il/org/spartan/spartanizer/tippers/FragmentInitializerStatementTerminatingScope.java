package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static il.org.spartan.lisp.*;

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
import il.org.spartan.spartanizer.java.*;

/** Convert <code>int a=3;b=a;</code> into <code>b = a;</code>
 * @author Yossi Gil
 * @since 2015-08-07 */
public final class FragmentInitializerStatementTerminatingScope extends $FragementInitializerStatement //
    implements TipperCategory.Inlining {
  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Inline local " + ¢.getName() + " into subsequent statement";
  }

  @Override protected ASTRewrite go(final ASTRewrite $, final VariableDeclarationFragment f, final SimpleName n, final Expression initializer,
      final Statement nextStatement, final TextEditGroup g) {
    if (f == null || extract.core(f.getInitializer()) instanceof LambdaExpression || initializer == null || haz.annotation(f)
        || iz.enhancedFor(nextStatement) && iz.simpleName(az.enhancedFor(nextStatement).getExpression())
            && !(az.simpleName(az.enhancedFor(nextStatement).getExpression()) + "").equals(n + "") && !iz.simpleName(initializer)
            && !iz.literal(initializer)
        || Inliner.forbiddenOperationOnPrimitive(f, nextStatement) || Inliner.isArrayInitWithUnmatchingTypes(f))
      return null;
    final VariableDeclarationStatement currentStatement = az.variableDeclrationStatement(f.getParent());
    if (currentStatement == null)
      return null;
    boolean found = false;
    for (final VariableDeclarationFragment ff : fragments(currentStatement))
      if (!found)
        found = ff == f;
      else if (!collect.usesOf(n).in(ff.getInitializer()).isEmpty())
        return null;
    final Block parent = az.block(currentStatement.getParent());
    if (parent == null)
      return null;
    final List<Statement> ss = statements(parent);
    if (!lastIn(nextStatement, ss) || !penultimateIn(currentStatement, ss) || !collect.definitionsOf(n).in(nextStatement).isEmpty())
      return null;
    final List<SimpleName> uses = collect.usesOf(n).in(nextStatement);
    if (!sideEffects.free(initializer)) {
      final SimpleName use = onlyOne(uses);
      if (use == null || haz.unknownNumberOfEvaluations(use, nextStatement))
        return null;
    }
    for (final SimpleName use : uses)
      if (Inliner.never(use, nextStatement) || Inliner.isPresentOnAnonymous(use, nextStatement))
        return null;
    final Expression v = Inliner.protect(initializer, currentStatement);
    final InlinerWithValue i = new Inliner(n, $, g).byValue(v);
    final Statement newStatement = copy.of(nextStatement);
    if (i.addedSize(newStatement) - removalSaving(f) > 0)
      return null;
    $.replace(nextStatement, newStatement, g);
    i.inlineInto(newStatement);
    remove(f, $, g);
    return $;
  }
}
