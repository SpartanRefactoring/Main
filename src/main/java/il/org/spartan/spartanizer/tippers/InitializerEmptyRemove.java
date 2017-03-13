package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tipping.Tipper.Example.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Restructuring
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-21 */
public final class InitializerEmptyRemove extends RemovingTipper<Initializer>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = 8587376936334392420L;

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

  @Override public Example[] examples() {
    return new Example[] { //
        convert("class C {{}}") //
            .to("class C {}"), //
        convert("class C {static {}}") //
            .to("class C {}"), //
        ignores("class C {/***/ {}}"), //
        ignores("class C {/***/ static {}}") };
  }
}
