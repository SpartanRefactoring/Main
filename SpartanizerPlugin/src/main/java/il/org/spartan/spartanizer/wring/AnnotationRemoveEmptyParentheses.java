package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.ast.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;

/** Removes the parentheses from annotations that do not take arguments,
 * converting <code><pre>@Override()</pre></code> to
 * <code><pre>@Override</pre></code>
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016-04-02 */
public final class AnnotationRemoveEmptyParentheses extends Wring.ReplaceCurrentNode<NormalAnnotation> implements Kind.SyntacticBaggage {
  @Override String description(final NormalAnnotation ¢) {
    return "Remove redundant parentheses from the @" + ¢.getTypeName().getFullyQualifiedName() + " annotation";
  }

  @Override ASTNode replacement(final NormalAnnotation a) {
    if (!values(a).isEmpty())
      return null;
    final MarkerAnnotation $ = a.getAST().newMarkerAnnotation();
    $.setTypeName(duplicate.of(a.getTypeName()));
    return $;
  }
}
