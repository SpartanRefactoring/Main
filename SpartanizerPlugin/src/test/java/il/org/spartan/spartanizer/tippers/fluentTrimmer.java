/** TODO: Yossi Gil <yossi.gil@gmail.com> please add a description
 * @author Yossi Gil <yossi.gil@gmail.com>
 * @since Sep 25, 2016 */
package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;

public class fluentTrimmer extends Trimmer {
  @SafeVarargs public <N extends ASTNode> fluentTrimmer(@NotNull final Class<N> clazz, final Tipper<N>... ws) {
    super(Toolbox.make(clazz, ws));
  }

  public fluentTrimmerApplication of(final String codeFragment) {
    return new fluentTrimmerApplication(this, codeFragment);
  }
}
