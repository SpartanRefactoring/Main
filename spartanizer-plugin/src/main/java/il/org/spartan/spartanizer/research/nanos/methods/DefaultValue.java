package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;

import il.org.spartan.spartanizer.research.UserDefinedTipper;
import il.org.spartan.spartanizer.research.nanos.common.JavadocMarkerNanoPattern;

/** @nano a method which is empty or contains one statement which return a
 *       default value of some __.
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2016-12-28 */
public class DefaultValue extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = -0x7857488D8A7F1AEBL;
  private static final UserDefinedTipper<Statement> returnDefault = patternTipper("return $D;", "", "");

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return empty(¢)//
        || returnDefault.canTip(onlyStatement(¢));
  }
  @Override public String tipperName() {
    return "DefaultValue";
  }
}
