package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.Javadoc;

import fluent.ly.box;
import il.org.spartan.spartanizer.ast.nodes.metrics.Metrics;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.RemovingTipper;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.Examples;

/** convert {@code if (a){g();}} into {@code if(a)g();}
 * @author Yossi Gil
 * @since 2016-12-14 */
public final class JavadocEmptyRemove extends RemovingTipper<Javadoc>//
    implements Category.SyntacticBaggage {
  private static final long serialVersionUID = 0x46D49EDB4E92226AL;

  @Override public String description(final Javadoc ¢) {
    return String.format("Remove empty Javadoc comment of %d characters", box.it(Metrics.length(¢)));
  }
  @Override public Examples examples() {
    return //
    convert("/***/ void f() {}") //
        .to("void f() {}") //
        .ignores("/** Some comment */ void f() {}") //
    ;
  }
  @Override public boolean prerequisite(final Javadoc ¢) {
    return iz.empty(¢);
  }
}
