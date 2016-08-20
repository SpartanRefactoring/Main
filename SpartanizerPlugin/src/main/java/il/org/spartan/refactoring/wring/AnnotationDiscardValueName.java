package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.utils.*;
import il.org.spartan.utils.*;

/** Removes the "value" member from annotations that only have a single member,
 * converting <code>@SuppressWarnings(value = "unchecked")</code> to
 * <code>@SuppressWarnings("unchecked")</code>
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016-04-02 */
public final class AnnotationDiscardValueName //
    extends Wring.ReplaceCurrentNode<NormalAnnotation> implements Kind.SyntacticBaggage {
  @Override String description(final NormalAnnotation a) {
    return "Remove the \"value\" member from the @" + a.getTypeName().getFullyQualifiedName() + " annotation";
  }

  @Override ASTNode replacement(final NormalAnnotation a) {
    final MemberValuePair p = Utils.onlyOne(expose.values(a));
    if (p == null || !"value".equals(p.getName().toString()))
      return null;
    final SingleMemberAnnotation $ = a.getAST().newSingleMemberAnnotation();
    $.setTypeName(newSimpleName(a, a.getTypeName().getFullyQualifiedName()));
    $.setValue(duplicate(p.getValue()));
    return $;
  }
}
