package il.org.spartan.athenizer.zoomers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.athenizer.zoom.zoomers.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/** Sort names for Joystick
 * @author Dor Ma'ayan
 * @since 2017-01-10 Issue #979, {@link Issue0979} */
public class MethodDeclarationNameShorter extends EagerTipper<MethodDeclaration>//
    implements Category.Bloater {
  private static final long serialVersionUID = -0x3523CE8186A3EAECL;

  @Override public String description(final MethodDeclaration ¢) {
    return ¢.getName() + "";
  }
  @Override public Tip tip(final MethodDeclaration d) {
    assert d != null;
    if (d.isConstructor() || iz.abstract¢(d) || d.getBody() == null)
      return null;
    final List<SingleVariableDeclaration> ret = parameters(d).stream().collect(Collectors.toList());
    return ret.isEmpty() ? null : new Tip("Rename paraemters", getClass(), d) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        int i = 0;
        for (final SingleVariableDeclaration ¢ : ret) {
          final SimpleName n = d.getAST().newSimpleName((¢.getType() + "").split("<")[0].toLowerCase().charAt(0) + "" + i++);
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
