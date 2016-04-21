package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.duplicate;
import static il.org.spartan.refactoring.utils.Funcs.newSimpleName;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;

/**
 * A {@link Wring} to remove the "value" member from annotations that only have
 * a single member, converting
 * <code><pre>@SuppressWarnings(value = "unchecked")</pre></code> to
 * <code><pre>@SuppressWarnings("unchecked")</pre></code>
 *
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016-04-02
 */
public class AnnotationDiscardValueName extends Wring.ReplaceCurrentNode<NormalAnnotation> {
  @Override ASTNode replacement(final NormalAnnotation a) {
    if (a.values().size() != 1)
      return null;
    final MemberValuePair p = (MemberValuePair) a.values().get(0);
    if (!p.getName().toString().equals("value"))
      return null;
    final SingleMemberAnnotation $ = a.getAST().newSingleMemberAnnotation();
    $.setTypeName(newSimpleName(a, a.getTypeName().getFullyQualifiedName()));
    $.setValue(duplicate(p.getValue()));
    return $;
  }
  @Override String description(final NormalAnnotation a) {
    return "Discard the \"value\" member from the @" + a.getTypeName().getFullyQualifiedName() + " annotation";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.OPTIMIZE_ANNOTATIONS;
  }
}
