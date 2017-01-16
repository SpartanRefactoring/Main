package il.org.spartan.bloater.bloaters;

import static il.org.spartan.Utils.*;



import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** An expander to rename short or unnecessarily understandable variable names
 * in a block to more common or intuitive names (s.e i for an integer variable
 * and ret for a return variable) : <code>
 * Important - the $ will always change to ret by convention
 * for more naming conventions information - {@link https://github.com/SpartanRefactoring/Spartanizer/wiki/Naming-Rules}
 * int f(int ret) {
 * int a = 5;
 * x(a);
 * ret = a;
 * return ret;
 * }
 * </code> ==> <code>
 * int f(int ret) {
 * int i1 = 5;
 * x(i1);
 * ret = i1;
 * return ret;
 * }
 * </code>
 * @author Raviv Rachmiel <tt> raviv.rachmiel@gmail.com </tt>
 * @since 2017-01-10 Issue #979, {@link Issue0979} */
// TODO: take care of single var decleration, tests
public class RenameShortNamesVarDec extends EagerTipper<SingleVariableDeclaration> implements TipperCategory.Expander {
  @Override public String description(final SingleVariableDeclaration ¢) {
    return ¢.getName() + "";
  }

  @Override @SuppressWarnings("unused") public Tip tip(final SingleVariableDeclaration d, final ExclusionManager __) {
    assert d != null;
    final SimpleName $ = d.getName();
    assert $ != null;
    if ((!in($.getIdentifier(), "$", "¢", "__", "_")) && $.getIdentifier().length() > 1)
      return null;
    if (in($.getIdentifier(), "$"))
      return new Tip("Rename paraemter " + $ + " to ret", d, getClass()) {
        @Override public void go(final ASTRewrite r, final TextEditGroup g) {
          Tippers.rename($, d.getAST().newSimpleName("ret"), d, r, g);
        }
      };
    final SimpleName ¢ = d.getAST().newSimpleName(scope.newName(d, step.type(d)));
    return new Tip("Rename paraemter " + $ + " to " + ¢, d, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        Tippers.rename($, ¢, d, r, g);
        // SingleVariableDeclarationAbbreviation.fixJavadoc(d, $, ¢ + "", r, g);
      }
    };
  }
}
