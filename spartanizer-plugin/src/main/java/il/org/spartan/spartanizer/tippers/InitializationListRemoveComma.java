package il.org.spartan.spartanizer.tippers;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayInitializer;

import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

// This code is not working: we never managed to get this to work. Stav was the
// last one working on it
/** Remove unnecessary ',' from array initialization list {@code "int[] a = new
 * int[] {..,..,..,};"} to: {@code "int[] a = new int[] {..,..,..};"}
 * @author Dor Ma'ayan<code><dor.d.ma [at] gmail.com></code>
 * @author Niv Shalmon<code><shalmon.niv [at] gmail.com></code>
 * @since 2016-8-27 */
public final class InitializationListRemoveComma extends ReplaceCurrentNode<ArrayInitializer>//
    implements Category.SyntacticBaggage {
  private static final long serialVersionUID = 0x791E91FF8A0EE109L;

  @Override public String description() {
    return "Remove Unecessary ','";
  }
  @Override public String description(@SuppressWarnings("unused") final ArrayInitializer __) {
    return "Remove Unecessary ','";
  }
  @Override public ASTNode replacement(final ArrayInitializer $) {
    final List<?> expressions = $.expressions();
    if (!expressions.isEmpty())
      expressions.remove(expressions.size() - 1);
    return $;
  }
}
