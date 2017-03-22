package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.Inliner.*;
import il.org.spartan.spartanizer.java.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Convert {@code int a=3;b=a;} into {@code b = a;}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-08-07 */
public final class FragmentInitializerStatementTerminatingScope extends $FragementInitializerStatement //
    implements TipperCategory.Inlining {
  private static final long serialVersionUID = -221763355000543721L;

  @NotNull
  @Override public String description(@NotNull final VariableDeclarationFragment ¢) {
    return "Inline local " + ¢.getName() + " into subsequent statement";
  }

  @Override protected ASTRewrite go(@NotNull final ASTRewrite $, final TextEditGroup g) {
    if (fragment() == null || extract.core(fragment().getInitializer()) instanceof LambdaExpression || initializer() == null
        || haz.annotation(fragment())
        || iz.enhancedFor(nextStatement()) && iz.simpleName(az.enhancedFor(nextStatement()).getExpression())
            && !(az.simpleName(az.enhancedFor(nextStatement()).getExpression()) + "").equals(name() + "") && !iz.simpleName(initializer())
            && !iz.literal(initializer())
        || wizard.frobiddenOpOnPrimitive(fragment(), nextStatement()) || Inliner.isArrayInitWithUnmatchingTypes(fragment()))
      return null;
    @Nullable final VariableDeclarationStatement currentStatement = az.variableDeclrationStatement(fragment().getParent());
    boolean searching = true;
    for (@NotNull final VariableDeclarationFragment ff : fragments(currentStatement))
      if (searching)
        searching = ff != fragment();
      else if (!collect.usesOf(name()).in(ff.getInitializer()).isEmpty())
        return null;
    @Nullable final Block parent = az.block(currentStatement.getParent());
    if (parent == null)
      return null;
    @NotNull final List<Statement> ss = statements(parent);
    if (!lastIn(nextStatement(), ss) || !penultimateIn(currentStatement, ss) || !collect.definitionsOf(name()).in(nextStatement()).isEmpty())
      return null;
    final List<SimpleName> uses = collect.usesOf(name()).in(nextStatement());
    if (!sideEffects.free(initializer())) {
      final SimpleName use = onlyOne(uses);
      if (use == null || Coupling.unknownNumberOfEvaluations(use, nextStatement()))
        return null;
    }
    for (final SimpleName use : uses)
      if (Inliner.never(use, nextStatement()) || Inliner.isPresentOnAnonymous(use, nextStatement()))
        return null;
    final Expression v = Inliner.protect(initializer(), currentStatement);
    @NotNull final InlinerWithValue i = new Inliner(name(), $, g).byValue(v);
    final Statement newStatement = copy.of(nextStatement());
    if (i.addedSize(newStatement) - Inliner2.removalSaving(fragment()) > 0)
      return null;
    $.replace(nextStatement(), newStatement, g);
    i.inlineInto(newStatement);
    remove($, g);
    return $;
  }
}
