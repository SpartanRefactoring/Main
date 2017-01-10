package il.org.spartan.spartanizer.research.analyses;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** @param <N>
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-10 */
public class Inspector<N extends ASTNode> extends InteractiveSpartanizer {
  final NanoPatternTipper<N> inspected;

  public Inspector(final NanoPatternTipper<N> np) {
    add(Box.it(np.nodeTypeHolder.getNodeType()), new Tipper<N>() {
      @Override public boolean canTip(N ¢) {
        return np.canTip(¢);
      }

      @Override public Tip tip(N ¢) {
        return new Tip("", ¢, this.getClass()) {
          @Override public void go(ASTRewrite r, TextEditGroup g) {
            System.out.println(¢);
            np.tip(¢).go(r, g);
          }
        };
      }

      @Override public String description(@SuppressWarnings("unused") N __) {
        return "";
      }
    });
    inspected = np;
  }
}
