package il.org.spartan.bloater.bloaters;

import static il.org.spartan.Utils.*;

import java.util.*;
import java.util.stream.*;

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
import il.org.spartan.zoomer.zoomin.expanders.*;

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
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = 0x6416089F84E63B0EL;

  @Override public String description(final VariableDeclarationStatement ¢) {
    return ¢ + "";
  }

  @Override public Tip tip(final VariableDeclarationStatement s, @SuppressWarnings("unused") final ExclusionManager __) {
    assert s != null;
    if (s.getParent() == null)
      return null;
    final List<VariableDeclarationFragment> $ = step.fragments(s).stream()
        .filter(λ -> (!in(λ.getName().getIdentifier(), "$") || !scope.hasInScope(s, "result")) && !in(λ.getName().getIdentifier(), "result")
            && !nameMatch(λ.getName().getIdentifier(), step.type(λ)))
        .collect(Collectors.toList());
    return $.isEmpty() ? null : new Tip("Rename parameters", getClass(), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        for (VariableDeclarationFragment ss : $)
          action.rename(ss.getName(),
              make.from(s).identifier(in(ss.getName().getIdentifier(), "$") ? "result" : scope.newName(s, step.type(s), prefix(step.type(s)))),
              s.getParent(), r, g);
      }
    };
  }
  
  private static boolean nameMatch(String s, Type t) {
    String $ = prefix(t);
    return (s.length() >= $.length() && s.substring(0,$.length()).equals($) && s.substring($.length(), s.length()).matches("[0-9]*"));
  }
  
  static String prefix(Type ¢) {
    return namer.shorten(¢);
  }
}
