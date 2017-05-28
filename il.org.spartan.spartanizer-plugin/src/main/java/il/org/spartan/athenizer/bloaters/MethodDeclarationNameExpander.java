package il.org.spartan.athenizer.bloaters;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.athenizer.zoomin.expanders.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/** An expander to rename short or unnecessarily understandable variable names
 * in a method dec to more common or intuitive names (s.e i for an integer
 * variable and ret for a return variable) : Important - the $ will always
 * change to ret by convention for more naming conventions information -
 * {@link https://github.com/SpartanRefactoring/Spartanizer/wiki/Naming-Rules}
 * {@code int f(int ¢) { int $ = ¢; x($); return $; } } ==> {@code int f(int i)
 * { int res = i; x(res); return res; } }
 * @author Raviv Rachmiel {@code  raviv.rachmiel@gmail.com }
 * @author Dor Ma'ayan
 * @since 2017-01-10 Issue #979, {@link Issue0979} */
public class MethodDeclarationNameExpander extends EagerTipper<MethodDeclaration>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = -0x3523CE8186A3EAECL;

  @Override public String description(final MethodDeclaration ¢) {
    return ¢.getName() + "";
  }
  @Override public Tip tip(final MethodDeclaration d) {
    assert d != null;
    if (d.isConstructor() || iz.abstract¢(d) || d.getBody() == null)
      return null;
    final List<SingleVariableDeclaration> $ = parameters(d).stream().filter(λ -> λ.getName().getIdentifier().length() == 1)
        .collect(Collectors.toList());
    return $.isEmpty() ? null : new Tip("Rename paraemters", getClass(), d) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        int i = 0;
        for (final SingleVariableDeclaration ¢ : $) {
          SimpleName n = d.getAST().newSimpleName(¢.getType().toString().toLowerCase() + i++);
          while (checkContains(getAll.names(d), n))
            n.setIdentifier(¢.getType().toString() + (i++));
          misc.rename(¢.getName(), n, d, r, g);
        }
      }
    }.spanning(d);
  }
  static boolean checkContains(List<SimpleName> names, SimpleName name) {
    for (SimpleName n : names) {
      if (n.getIdentifier().equals(name.getIdentifier()))
        return true;
    }
    return false;
  }
  static String prefix(final Type ¢) {
    return abbreviate.it(¢);
  }
}
