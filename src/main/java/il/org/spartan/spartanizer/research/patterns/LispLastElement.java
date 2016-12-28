package il.org.spartan.spartanizer.research.patterns;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;
import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

/** @author Ori Marcovitch
 * @year 2016 */
public final class LispLastElement extends NanoPatternTipper<MethodInvocation> {
  private static final List<UserDefinedTipper<MethodInvocation>> tippers = new ArrayList<UserDefinedTipper<MethodInvocation>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("$X.get($X.size()-1) ", "last($X)", "lisp: last"));
    }
  };

  @Override public boolean canTip(final MethodInvocation ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public Tip pattern(final MethodInvocation ¢) {
    return firstTip(tippers, ¢);
  }

  /** not used...
   * @param u
   * @param r */
  static void addImport(final CompilationUnit u, final ASTRewrite r) {
    final ImportDeclaration d = u.getAST().newImportDeclaration();
    d.setStatic(true);
    d.setOnDemand(true);
    d.setName(u.getAST().newName("il.org.spartan.lisp"));
    wizard.addImport(u, r, d);
  }
}
