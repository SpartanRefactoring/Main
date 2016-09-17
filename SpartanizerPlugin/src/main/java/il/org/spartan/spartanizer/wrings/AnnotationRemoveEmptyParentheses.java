package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.ast.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** Removes the parentheses from annotations that do not take arguments,
 * converting <code><pre>@Override()</pre></code> to
 * <code><pre>@Override</pre></code>
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016-04-02 */
public final class AnnotationRemoveEmptyParentheses extends ReplaceCurrentNode<NormalAnnotation> implements Kind.SyntacticBaggage {
  @Override public String description(final NormalAnnotation ¢) {
    return "Remove redundant parentheses from the @" + ¢.getTypeName().getFullyQualifiedName() + " annotation";
  }

  @Override public ASTNode replacement(final NormalAnnotation a) {
    if (!values(a).isEmpty())
      return null;
    final MarkerAnnotation $ = a.getAST().newMarkerAnnotation();
    $.setTypeName(duplicate.of(a.getTypeName()));
    return $;
  }
}
