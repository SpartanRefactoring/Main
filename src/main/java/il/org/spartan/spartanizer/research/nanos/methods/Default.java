package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import org.eclipse.jdt.core.dom.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** @nano a method which is empty or contains one statement which return a
 *       default value of some type.
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2016-12-28 */
public class Default extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = -8671479380276353771L;
  private static final UserDefinedTipper<Statement> returnDefault = patternTipper("return $D;", "", "");

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return empty(¢)//
        || returnDefault.canTip(onlyStatement(¢));
  }

  @Override  public String nanoName() {
    return "DefaultValue";
  }
}
