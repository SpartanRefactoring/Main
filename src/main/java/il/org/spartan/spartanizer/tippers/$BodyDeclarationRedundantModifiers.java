package il.org.spartan.spartanizer.tippers;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** convert <code><b>abstract</b> <b>interface</b>a{}</code> to
 * <code><b>interface</b> a{}</code>, etc.
 * @author Yossi Gil
 * @since 2015-07-29 */
abstract class $BodyDeclarationRedundantModifiers<N extends BodyDeclaration> extends ReplaceCurrentNode<N>
    //
    implements TipperCategory.SyntacticBaggage {
  @NotNull
  @Override public String description(@NotNull final BodyDeclaration ¢) {
    return "Remove redundant " + wizard.redundants(¢) + " modifier(s) from declaration";
  }

  @Override public boolean prerequisite(@NotNull final BodyDeclaration ¢) {
    final Set<Predicate<Modifier>> $ = wizard.redundancies(¢);
    return !$.isEmpty() && !wizard.matchess(¢, $).isEmpty();
  }

  @Nullable
  @Override public BodyDeclaration replacement(@NotNull final BodyDeclaration $) {
    final Set<Predicate<Modifier>> predicates = wizard.redundancies($);
    return predicates.isEmpty() ? null : wizard.prune(copy.of($), predicates);
  }
}
