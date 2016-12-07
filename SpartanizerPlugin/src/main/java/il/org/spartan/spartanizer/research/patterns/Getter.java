package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Getter extends JavadocMarkerNanoPattern<MethodDeclaration> {
  private static Set<UserDefinedTipper<Statement>> tippers = new HashSet<UserDefinedTipper<Statement>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.patternTipper("return $N;", "", ""));
      add(TipperFactory.patternTipper("return this.$N;", "", ""));
      add(TipperFactory.patternTipper("return ($T)$N;", "", ""));
      add(TipperFactory.patternTipper("return ($T)this.$N;", "", ""));
    }
  };

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return statements(body(¢)) != null && !statements(body(¢)).isEmpty() && parameters(¢).isEmpty() && anyTips(onlyOne(statements(¢)));
  }

  static boolean anyTips(final Statement s) {
    return tippers.stream().anyMatch(t -> t.canTip(s));
  }
}
