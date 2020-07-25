package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.statements;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Initializer;

import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.RemovingTipper;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.Examples;

/** Restructuring
 * @author Yossi Gil
 * @since 2017-01-21 */
public final class InitializerEmptyRemove extends RemovingTipper<Initializer>//
    implements Category.SyntacticBaggage {
  private static final long serialVersionUID = 0x772C7DD36297FC64L;

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
  @Override public Examples examples() {
    return //
    convert("class C {{}}") //
        .to("class C {}"). //
        convert("class C {static {}}") //
        .to("class C {}") //
        .ignores("class C {/***/ {}}") //
        .ignores("class C {/***/ static {}}");
  }
}
