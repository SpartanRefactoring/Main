package il.org.spartan.athenizer.zoomers;

import static il.org.spartan.spartanizer.ast.navigate.step.parameters;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.athenizer.zoom.zoomers.Issue0979;
import il.org.spartan.spartanizer.ast.factory.misc;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.engine.nominal.abbreviate;
import il.org.spartan.spartanizer.tipping.EagerTipper;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.spartanizer.utils.tdd.getAll;

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
    implements Category.Bloater {
  private static final long serialVersionUID = -0x3523CE8186A3EAECL;

  @Override public String description(final MethodDeclaration ¢) {
    return ¢.getName() + "";
  }
  @Override public Tip tip(final MethodDeclaration d) {
    assert d != null;
    if (d.isConstructor() || iz.abstract¢(d) || d.getBody() == null)
      return null;
    final List<SingleVariableDeclaration> $ = parameters(d).stream().filter(λ -> λ.getName().getIdentifier().length() <= 2)
        .collect(Collectors.toList());
    return $.isEmpty() ? null : new Tip("Rename paraemters", getClass(), d) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        int i = 0;
        for (final SingleVariableDeclaration ¢ : $) {
          final SimpleName n = d.getAST().newSimpleName((¢.getType() + "").split("<")[0].toLowerCase() + i++);
          while (checkContains(getAll.names(d), n))
            n.setIdentifier(¢.getType() + "" + i++);
          misc.rename(¢.getName(), n, d, r, g);
        }
      }
    }.spanning(d);
  }
  static boolean checkContains(final List<SimpleName> ns, final SimpleName n) {
    for (final SimpleName ¢ : ns)
      if (¢.getIdentifier().equals(n.getIdentifier()))
        return true;
    return false;
  }
  static String prefix(final Type ¢) {
    return abbreviate.it(¢);
  }
}
