package il.org.spartan.bloater.inflate.zoomers;

import static il.org.spartan.Utils.*;
import static il.org.spartan.lisp.*;

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

/** An expander to rename short or unnecessarily understandable variable names
 * to more common or intuitive names (s.e i for an integer variable and ret for
 * a return variable) : <code>
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
 * @since 2017-01-10 Issue #979 */
public class RenameShortNames extends EagerTipper<MethodDeclaration> implements TipperCategory.Expander {
  @Override public String description(final MethodDeclaration ¢) {
    return ¢.getName() + "";
  }

  @Override @SuppressWarnings("unused") public Tip tip(final MethodDeclaration d, final ExclusionManager __) {
    assert d != null;
    if (d.isConstructor() || iz.abstract¢(d))
      return null;
    final SingleVariableDeclaration parameter = onlyOne(parameters(d));
    final SimpleName $ = parameter.getName();
    assert $ != null;
    if (!in($.getIdentifier(), "$", "¢", "__", "_") && $.getIdentifier().length() > 1)
      return null;
    if (in($.getIdentifier(), "$"))
      return new Tip("Rename paraemter $ to ret", d, getClass()) {
        @Override public void go(final ASTRewrite r, final TextEditGroup g) {
          Tippers.rename($, d.getAST().newSimpleName("ret"), d, r, g);
          // SingleVariableDeclarationAbbreviation.fixJavadoc(d, $, "ret", r,
          // g);
        }
      };
    final SimpleName ¢ = d.getAST().newSimpleName(scope.newName(body(d), step.type(parameter)));
    return new Tip("Rename paraemter " + $ + " to " + ¢, d, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Tippers.rename($, ¢, d, r, g);
        // SingleVariableDeclarationAbbreviation.fixJavadoc(d, $, ¢ + "", r, g);
      }
    };
  }
}
