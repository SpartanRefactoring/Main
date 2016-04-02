package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.duplicate;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;

/**
 * A {@link Wring} to remove the parentheses from annotations that do not take
 * arguments, converting <code><pre>@Override()</pre></code> to
 * <code><pre>@Override</pre></code>
 *
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016-04-02
 */
public class NormalAnnotationRemoveEmptyParentheses extends Wring.ReplaceCurrentNode<NormalAnnotation> {
  @Override ASTNode replacement(final NormalAnnotation n) {
    if (n.values().size() > 0)
      return null;
    final MarkerAnnotation $ = n.getAST().newMarkerAnnotation();
    $.setTypeName(duplicate(n.getTypeName()));
    return $;
  }
  @Override String description(final NormalAnnotation n) {
    return "Remove redundant parentheses from the " + n.getTypeName().getFullyQualifiedName().toLowerCase() + " annotation";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.REFACTOR_INEFFECTIVE; // TODO this should go in a new
                                            // category
  }
}
