package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Removes the "value" member from annotations that only have a single member,
 * converting <code>@SuppressWarnings(value = "unchecked")</code> to
 * <code>@SuppressWarnings("unchecked")</code>
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016-04-02 */
public final class AnnotationDiscardValueName //
    extends ReplaceCurrentNode<NormalAnnotation> implements TipperCategory.SyntacticBaggage {
  @Override public String description(final NormalAnnotation ¢) {
    return "Remove the \"value\" member from the @" + ¢.getTypeName().getFullyQualifiedName() + " annotation";
  }
  @Override public ASTNode replacement(final NormalAnnotation a) {
    final MemberValuePair p = onlyOne(step.values(a));
    if (p == null || !"value".equals(p.getName() + ""))
      return null;
    final SingleMemberAnnotation $ = a.getAST().newSingleMemberAnnotation();
    $.setTypeName(duplicate.of(a.getTypeName()));
    $.setValue(duplicate.of(p.getValue()));
    return $;
  }
}
