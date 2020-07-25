package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.expressions;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

import fluent.ly.the;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.Examples;

/** Use {@link #examples()} for documentation
 * @author Yossi Gil
 * @since 2017-02-10 */
public final class AnnotationRemoveSingletonArrray extends ReplaceCurrentNode<SingleMemberAnnotation>//
    implements Category.SyntacticBaggage {
  private static final long serialVersionUID = 0x73DFB5B8FFF2B25EL;

  @Override public String description(final SingleMemberAnnotation ¢) {
    return "Remove the curly brackets in the @" + ¢.getTypeName().getFullyQualifiedName() + " annotation";
  }
  @Override public Examples examples() {
    return convert("@SuppressWarnings({\"unchecked\"}) void f() {}") //
        .to("@SuppressWarnings(\"unchecked\") void f() {}") //
        .ignores("@SuppressWarnings(\"unchecked\") void f() {}") //
        .ignores("@SuppressWarnings() void f() {}") //
        .ignores("@SuppressWarnings void f() {}")//
    ;
  }
  @Override public ASTNode replacement(final SingleMemberAnnotation a) {
    final Expression x = the.onlyOneOf(expressions(az.arrayInitializer(a.getValue())));
    if (x == null)
      return null;
    final SingleMemberAnnotation $ = copy.of(a);
    $.setValue(copy.of(x));
    return $;
  }
}
