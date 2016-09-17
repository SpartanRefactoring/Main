package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.lisp.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** Removes the "value" member from annotations that only have a single member,
 * converting <code>@SuppressWarnings(value = "unchecked")</code> to
 * <code>@SuppressWarnings("unchecked")</code>
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016-04-02 */
public final class AnnotationDiscardValueName //
    extends ReplaceCurrentNode<NormalAnnotation> implements Kind.SyntacticBaggage {
  @Override public String description(final NormalAnnotation ¢) {
    return "Remove the \"value\" member from the @" + ¢.getTypeName().getFullyQualifiedName() + " annotation";
  }

  @Override public ASTNode replacement(final NormalAnnotation a) {
    final MemberValuePair p = onlyOne(step.values(a));
    if (p == null || !"value".equals(p.getName() + ""))
      return null;
    final SingleMemberAnnotation $ = a.getAST().newSingleMemberAnnotation();
    $.setTypeName(make.newSimpleName(a, a.getTypeName().getFullyQualifiedName()));
    $.setValue(duplicate.of(p.getValue()));
    return $;
  }
}
