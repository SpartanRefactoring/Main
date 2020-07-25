package il.org.spartan.athenizer.zoomers;

import static fluent.ly.is.in;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.is;
import il.org.spartan.athenizer.zoom.zoomers.Issue0979;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.factory.misc;
import il.org.spartan.spartanizer.ast.navigate.step;
import il.org.spartan.spartanizer.engine.nominal.abbreviate;
import il.org.spartan.spartanizer.java.namespace.scope;
import il.org.spartan.spartanizer.tipping.EagerTipper;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** An expander to rename short or unnecessarily understandable variable names
 * in a block to more common or intuitive names (s.e i for an integer variable
 * and ret for a return variable) : Important - the $ will always change to ret
 * by convention for more naming conventions information -
 * {@link https://github.com/SpartanRefactoring/Spartanizer/wiki/Naming-Rules}
 * {@code int f(int ret) { int a = 5; x(a); ret = a; return ret; } } ==>
 * {@code int f(int ret) { int i1 = 5; x(i1); ret = i1; return ret; } }
 * @author Raviv Rachmiel {@code  raviv.rachmiel@gmail.com }
 * @since 2017-01-10 Issue #979, {@link Issue0979} */
// TODO Raviv Rachmiel take care of single var decleration, tests
public class VariableDeclarationStatementExpand extends EagerTipper<VariableDeclarationStatement>//
    implements Category.Bloater {
  private static final long serialVersionUID = 0x6416089F84E63B0EL;

  @Override public String description(final VariableDeclarationStatement ¢) {
    return ¢ + "";
  }
  @Override public Tip tip(final VariableDeclarationStatement s) {
    assert s != null;
    if (s.getParent() == null)
      return null;
    final List<VariableDeclarationFragment> $ = step.fragments(s).stream()
        .filter(λ -> (!is.in(λ.getName().getIdentifier(), "$") || !scope.hasInScope(s, "result")) && !in(λ.getName().getIdentifier(), "result")
            && !nameMatch(λ.getName().getIdentifier(), step.type(λ)))
        .collect(Collectors.toList());
    return $.isEmpty() ? null : new Tip("Verbosify parameter names", getClass(), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        for (final VariableDeclarationFragment ss : $)
          misc.rename(ss.getName(),
              make.from(s).identifier(is.in(ss.getName().getIdentifier(), "$") ? "result" : scope.newName(s, step.type(s), prefix(step.type(s)))),
              s.getParent(), r, g);
      }
    }.spanning(s.getParent());
  }
  private static boolean nameMatch(final String s, final Type t) {
    final String $ = prefix(t);
    return s.length() >= $.length() && s.substring(0, $.length()).equals($) && s.substring($.length(), s.length()).matches("[0-9]*");
  }
  static String prefix(final Type ¢) {
    return abbreviate.it(¢);
  }
}
