package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import il.org.spartan.refactoring.preferences.*;

import org.eclipse.jdt.core.dom.*;

/**
 * A {@link Wring} to remove the "value" member from annotations that only have
 * a single member, converting
 * <code>@SuppressWarnings(value = "unchecked")</code> to
 * <code>@SuppressWarnings("unchecked")</code>
 *
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016-04-02
 */
public class AnnotationDiscardValueName extends Wring.ReplaceCurrentNode<NormalAnnotation> implements Kind.OPTIMIZE_ANNOTATIONS {
  @Override ASTNode replacement(final NormalAnnotation a) {
    if (a.values().size() != 1)
      return null;
    final MemberValuePair p = (MemberValuePair) a.values().get(0);
    if (!"value".equals(p.getName().toString()))
      return null;
    final SingleMemberAnnotation $ = a.getAST().newSingleMemberAnnotation();
    $.setTypeName(newSimpleName(a, a.getTypeName().getFullyQualifiedName()));
    $.setValue(duplicate(p.getValue()));
    return $;
  }
  @Override String description(final NormalAnnotation a) {
    return "Discard the \"value\" member from the @" + a.getTypeName().getFullyQualifiedName() + " annotation";
  }
}
