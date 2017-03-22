package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.utils.Example.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;
import org.jetbrains.annotations.NotNull;

/** Use {@link #examples()} for documentation
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-02-10 */
public final class AnnotationRemoveSingletonArrray extends ReplaceCurrentNode<SingleMemberAnnotation>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = 8349592040340894302L;

  @NotNull @Override public String description(@NotNull final SingleMemberAnnotation ¢) {
    return "Remove the curly brackets in the @" + ¢.getTypeName().getFullyQualifiedName() + " annotation";
  }

  @NotNull @Override public Example[] examples() {
    return new Example[] { //
        convert("@SuppressWarnings({\"unchecked\"}) void f() {}") //
            .to("@SuppressWarnings(\"unchecked\") void f() {}"), //
        ignores("@SuppressWarnings(\"unchecked\") void f() {}"), //
        ignores("@SuppressWarnings() void f() {}"), //
        ignores("@SuppressWarnings void f() {}") };
  }

  @Override public ASTNode replacement(@NotNull final SingleMemberAnnotation a) {
    final Expression x = lisp.onlyOne(expressions(az.arrayInitializer(a.getValue())));
    if (x == null)
      return null;
    final SingleMemberAnnotation $ = copy.of(a);
    $.setValue(copy.of(x));
    return $;
  }
}
