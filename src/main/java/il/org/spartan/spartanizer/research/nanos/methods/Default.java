package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** @nano a method which is empty or contains one statement which return a
 *       default value of some type.
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-28 */
public class Default extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = -8671479380276353771L;
  private static final Collection<UserDefinedTipper<Statement>> tippers = new HashSet<UserDefinedTipper<Statement>>() {
    @SuppressWarnings("hiding") static final long serialVersionUID = 1L;
    {
      add(patternTipper("return $D;", "", ""));
    }
  };

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return empty(¢)//
        || anyTips(tippers, onlyStatement(¢));
  }

  @Override public Category category() {
    return Category.Default;
  }
}
