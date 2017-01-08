package il.org.spartan.spartanizer.research.nanos.methods;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-28 */
public class Default extends JavadocMarkerNanoPattern {
  private static final Set<UserDefinedTipper<Statement>> tippers = new HashSet<UserDefinedTipper<Statement>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("return;", "", ""));
      add(patternTipper("return 0;", "", ""));
      add(patternTipper("return false;", "", ""));
      add(patternTipper("return 0L;", "", ""));
      add(patternTipper("return null;", "", ""));
      add(patternTipper("return 0.;", "", ""));
    }
  };

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return empty(¢)//
        || anyTips(tippers, onlyStatement(¢));
  }
}
