package il.org.spartan.bloater.bloaters;

import static il.org.spartan.Utils.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** An expander to rename short or unnecessarily understandable variable names
 * in a method dec to more common or intuitive names (s.e i for an integer
 * variable and ret for a return variable) : <code>
 * Important - the $ will always change to ret by convention
 * for more naming conventions information - {@link https://github.com/SpartanRefactoring/Spartanizer/wiki/Naming-Rules}
 * int f(int ¢) {
 * int $ = ¢;
 * x($);
 * return $;
 * }
 * </code> ==> <code>
 * int f(int i) {
 * int res = i;
 * x(res);
 * return res;
 * }
 * </code>
 * @author Raviv Rachmiel <tt> raviv.rachmiel@gmail.com </tt>
 * @since 2017-01-10 Issue #979, {@link Issue0979} */
// TODO: take care of single var decleration, tests
public class RenameShortNamesMethodDec extends EagerTipper<MethodDeclaration> implements TipperCategory.Expander {
  @Override public String description(final MethodDeclaration ¢) {
    return ¢.getName() + "";
  }

  @Override @SuppressWarnings("unused") public Tip tip(final MethodDeclaration d, final ExclusionManager __) {
    assert d != null;
    if (d.isConstructor() || iz.abstract¢(d))
      return null;
    final List<SingleVariableDeclaration> parameters = parameters(d);
    List<SimpleName> prev = new ArrayList<SimpleName>();
    List<SimpleName> after = new ArrayList<SimpleName>();
    for (SingleVariableDeclaration parameter : parameters) {
      final SimpleName $ = parameter.getName();
      assert $ != null;
      if ((!in($.getIdentifier(), "$", "¢", "__", "_")) && $.getIdentifier().length() > 1)
        continue;
      if (in($.getIdentifier(), "$")) {
        prev.add($);
        after.add(d.getAST().newSimpleName("ret"));
        continue;
      }
      final SimpleName ¢ = d.getAST().newSimpleName(scope.newName(body(d), step.type(parameter)));
      prev.add($);
      after.add(¢);
    }
    return prev.isEmpty() ? null : new Tip("Rename paraemters", d, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        int counter = 0;
        for (SimpleName ¢ : prev) {
          Tippers.rename(¢, after.get(counter), d, r, g);
          ++counter;
        }
      }
    };
  }
}
