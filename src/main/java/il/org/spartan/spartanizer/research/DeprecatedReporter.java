package il.org.spartan.spartanizer.research;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class DeprecatedReporter extends NanoPatternTipper<MethodDeclaration> {
  @Override public boolean canTip(final MethodDeclaration ¢) {
    return Marker.isMarkedForReport(¢);
  }

  @Override public String description(final MethodDeclaration __) {
    return "report";
  }

  @Override public Tip tip(final MethodDeclaration ¢) {
    return new Tip(description(¢), ¢, this.getClass()) {
      @Override public void go(final ASTRewrite __, final TextEditGroup g) {
        Marker.unmarkForReport(¢);
        System.out.println(Marker.getMarkers(¢));
      }
    };
  }
}
