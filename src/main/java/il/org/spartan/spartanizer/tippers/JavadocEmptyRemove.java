package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.utils.Example.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** convert {@code if (a){g();}} into {@code if(a)g();}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016-12-14 */
public final class JavadocEmptyRemove extends RemovingTipper<Javadoc>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = 0x46D49EDB4E92226AL;

  @Override public String description(final Javadoc ¢) {
    return String.format("Remove empty Javadoc comment of %d characters", box.it(metrics.length(¢)));
  }

  @Override @NotNull public Example[] examples() {
    return new Example[] { //
        convert("/***/ void f() {}") //
            .to("void f() {}"), //
        Example.ignores("/** Meaningful comment */ void f() {}") //
    };
  }

  @Override public boolean prerequisite(final Javadoc ¢) {
    return iz.empty(¢);
  }
}
