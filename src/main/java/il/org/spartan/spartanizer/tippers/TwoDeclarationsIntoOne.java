package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static il.org.spartan.utils.Example.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Test case is {@link Issue1012} Issue #1012 Convert: {@code
 * int a = 0;
 * int b = 1;
 * int c = 2;
 * } to: {@code
 * int a = 0, b = 1, c = 2;
 * }
 * @author tomerdragucki <tt>tomerd@campus.technion.ac.il</tt>
 * @since 2017-01-13 */
public class TwoDeclarationsIntoOne extends ReplaceToNextStatement<VariableDeclarationStatement>//
    implements TipperCategory.Unite {
  private static final long serialVersionUID = -401300117746539825L;

  @Nullable
  @Override protected ASTRewrite go(@NotNull final ASTRewrite $, @NotNull final VariableDeclarationStatement s, final Statement nextStatement, final TextEditGroup g) {
    if (!canTip(s, nextStatement))
      return null;
    final VariableDeclarationStatement sc = copy.of(s);
    fragments(az.variableDeclarationStatement(nextStatement)).forEach(λ -> fragments(sc).add(copy.of(λ)));
    $.replace(s, sc, g);
    $.remove(nextStatement, g);
    return $;
  }

  @NotNull
  @Override public String description(@SuppressWarnings("unused") final VariableDeclarationStatement __) {
    return "Unify two variable declarations of the same type into one";
  }

  @NotNull
  @Override public Example[] examples() {
    return new Example[] { //
        convert("int a; int b; int c; f(a, b, c);") //
            .to("int a, b; int c; f(a, b, c);"), //
        convert("int a, b; int c; f(a, b, c);") //
            .to("int a, b, c; f(a, b, c);"), //
        convert("final int a = 1; final int b = 2; f(a, b);") //
            .to("final int a = 1, b = 2; f(a, b);"), //
        ignores("int a = 1; final int b = 2; f(a, b);") //
    };
  }

  private static boolean canTip(@NotNull final VariableDeclarationStatement $, final Statement nextStatement) {
    @Nullable final Block parent = az.block(parent($));
    return (parent == null || !lastIn(nextStatement, statements(parent))) && iz.variableDeclarationStatement(nextStatement)
        && (type(az.variableDeclarationStatement(nextStatement)) + "").equals(type($) + "")
        && az.variableDeclarationStatement(nextStatement).getModifiers() == $.getModifiers()
        && sameAnnotations(extract.annotations($), extract.annotations(az.variableDeclarationStatement(nextStatement)));
  }

  private static boolean sameAnnotations(@NotNull final List<Annotation> l1, @NotNull final List<Annotation> l2) {
    return l1.size() == l2.size() && l1.stream().allMatch(λ -> (λ + "").equals(l2.get(l1.indexOf(λ)) + ""));
  }
}
