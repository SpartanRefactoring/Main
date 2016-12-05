package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Getter extends JavadocMarkerNanoPattern<MethodDeclaration> {
  Set<UserDefinedTipper<Statement>> tippers = new HashSet<UserDefinedTipper<Statement>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.patternTipper("return $N;", "", ""));
      add(TipperFactory.patternTipper("return this.$N;", "", ""));
      add(TipperFactory.patternTipper("return ($N2)$N;", "", ""));
      add(TipperFactory.patternTipper("return ($N2)this.$N;", "", ""));
    }
  };

  @Override protected boolean prerequisites(final MethodDeclaration d) {
    if (statements(body(d)) == null || statements(body(d)).isEmpty() || !parameters(d).isEmpty())
      return false;
    for (final UserDefinedTipper<Statement> ¢ : tippers)
      if (¢.canTip(onlyOne(statements(d))))
        return true;
    return false;
  }
}
