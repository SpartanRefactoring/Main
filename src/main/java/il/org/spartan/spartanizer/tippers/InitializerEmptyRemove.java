package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

// This code is not working: we never managed to get this to work. Stav was the
// last one working on it
/** Remove unnecessary ',' from array initialization list
 * <code>"int[] a = new int[] {..,..,..,};"</code> to :
 *
 * <pre>
 * <code>"int[] a = new int[] {..,..,..};"</code>
 * </pre>
 *
 * @author Dor Ma'ayan<code><dor.d.ma [at] gmail.com></code>
 * @author Niv Shalmon<code><shalmon.niv [at] gmail.com></code>
 * @since 2016-8-27 */
public final class InitializerEmptyRemove extends RemovingTipper<Initializer> implements TipperCategory.InVain {
  @Override protected boolean prerequisite(final Initializer ¢) {
    final Block $ = ¢.getBody();
    return ¢.getJavadoc() == null && ($ == null || statements($).isEmpty());
  }

  @Override public String description() {
    return "Remove empty initializer";
  }

  @Override public String description(final Initializer ¢) {
    return "Remove empty " + (iz.static¢(¢) ? "" : "non-") + "static initializer";
  }
}
