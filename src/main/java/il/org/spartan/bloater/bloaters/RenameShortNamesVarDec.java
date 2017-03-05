package il.org.spartan.bloater.bloaters;

import static il.org.spartan.Utils.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** An expander to rename short or unnecessarily understandable variable names
 * in a block to more common or intuitive names (s.e i for an integer variable
 * and ret for a return variable) : Important - the $ will always change to ret
 * by convention for more naming conventions information -
 * {@link https://github.com/SpartanRefactoring/Spartanizer/wiki/Naming-Rules}
 * {@code int f(int ret) { int a = 5; x(a); ret = a; return ret; } } ==>
 * {@code int f(int ret) { int i1 = 5; x(i1); ret = i1; return ret; } }
 * @author Raviv Rachmiel <tt> raviv.rachmiel@gmail.com </tt>
 * @since 2017-01-10 Issue #979, {@link Issue0979} */
// TODO: Raviv Rachmiel take care of single var decleration, tests
public class RenameShortNamesVarDec extends EagerTipper<VariableDeclarationStatement>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = 7211961334502931214L;

  @Override public String description(final VariableDeclarationStatement ¢) {
    return ¢ + "";
  }

  @Override @SuppressWarnings("unused") public Tip tip(final VariableDeclarationStatement s, final ExclusionManager __) {
    assert s != null;
    try {
      final List<SimpleName> prev = new ArrayList<>(), after = new ArrayList<>();
      for (final Object v : make.variableDeclarationExpression(s).fragments()) {
        final SimpleName $ = ((VariableDeclaration) v).getName();
        if (!in($.getIdentifier(), namer.specials) && $.getIdentifier().length() > 1)
          return null;
        if (in($.getIdentifier(), "$")) {
          prev.add($);
          after.add(make.from(s).identifier("result"));
          continue;
        }
        if (s.getParent() == null)
          return null;
        final SimpleName ¢ = make.from(s).identifier(scope.newName(s.getParent(), step.type(s)));
        prev.add($);
        after.add(¢);
      }
      return s.getParent() == null || prev.isEmpty() ? null : new Tip("Rename parameters", s, getClass()) {
        @Override public void go(final ASTRewrite r, final TextEditGroup g) {
          int counter = 0;
          for (final SimpleName ¢ : prev) {
            Tippers.rename(¢, after.get(counter), s.getParent(), r, g);
            ++counter;
          }
        }
      };
    } catch (final Exception ¢) {
      return monitor.logProbableBug(this, ¢);
    }
  }
}
