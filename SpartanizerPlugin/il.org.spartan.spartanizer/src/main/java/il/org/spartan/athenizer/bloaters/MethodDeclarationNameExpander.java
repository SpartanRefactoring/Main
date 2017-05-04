package il.org.spartan.athenizer.bloaters;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.tipping.*;

/** An expander to rename short or unnecessarily understandable variable names
 * in a method dec to more common or intuitive names (s.e i for an integer
 * variable and ret for a return variable) : Important - the $ will always
 * change to ret by convention for more naming conventions information -
 * {@link https://github.com/SpartanRefactoring/Spartanizer/wiki/Naming-Rules}
 * {@code int f(int ¢) { int $ = ¢; x($); return $; } } ==> {@code int f(int i)
 * { int res = i; x(res); return res; } }
 * @author Raviv Rachmiel {@code  raviv.rachmiel@gmail.com }
 * @since 2017-01-10 Issue #979, {@link Issue0979} */
// TODO Raviv Rachmiel take care of single var declaration, tests
public class MethodDeclarationNameExpander extends CarefulTipper<MethodDeclaration>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = -0x3523CE8186A3EAECL;

  @Override public String description(final MethodDeclaration ¢) {
    return ¢.getName() + "";
  }

  @Override public Tip tip(final MethodDeclaration d) {
    assert d != null;
    if (d.isConstructor() || iz.abstract¢(d) || d.getBody() == null)
      return null;
    final List<SingleVariableDeclaration> $ = parameters(d).stream()
        .filter(λ -> (!is.in(λ.getName().getIdentifier(),"$") || !scope.hasInScope(body(d), "result")) && !is.in(λ.getName().getIdentifier(),"result" )
            && !nameMatch(λ.getName().getIdentifier(), step.type(λ)))
        .collect(Collectors.toList());
    return $.isEmpty() ? null : new Tip("Rename paraemters", getClass(), d) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        for (final SingleVariableDeclaration ¢ : $) {
          misc.rename(¢.getName(),
              make.from(d).identifier(is.in(¢.getName().getIdentifier(),"$") ? "result" : scope.newName(body(d), step.type(¢), prefix(step.type(¢)))),
              d, r, g);
        }
      }
    }.spanning(d);
  }

  private static boolean nameMatch(final String s, final Type t) {
    final String $ = prefix(t);
    return s.length() >= $.length() && s.substring(0, $.length()).equals($) && s.substring($.length(), s.length()).matches("[0-9]*");
  }

  static String prefix(final Type ¢) {
    return abbreviate.it(¢);
  }
}
