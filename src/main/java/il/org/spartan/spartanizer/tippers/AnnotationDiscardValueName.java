package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.lisp.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Removes the "value" member from annotations that only have a single member,
 * converting {@code @SuppressWarnings(value = "unchecked")} to
 * {@code @SuppressWarnings("unchecked")}
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016-04-02 */
public final class AnnotationDiscardValueName extends ReplaceCurrentNode<NormalAnnotation>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = 8644185174325773037L;

  @Override public String description(final NormalAnnotation ¢) {
    return "Remove the \"value\" member from the @" + ¢.getTypeName().getFullyQualifiedName() + " annotation";
  }

  @Override public ASTNode replacement(final NormalAnnotation a) {
    final MemberValuePair p = onlyOne(values(a));
    if (p == null || !"value".equals(p.getName() + ""))
      return null;
    final SingleMemberAnnotation $ = a.getAST().newSingleMemberAnnotation();
    $.setTypeName(copy.of(a.getTypeName()));
    $.setValue(copy.of(p.getValue()));
    return $;
  }
}
