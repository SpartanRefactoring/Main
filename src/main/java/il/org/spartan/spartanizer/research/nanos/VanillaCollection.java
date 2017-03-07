package il.org.spartan.spartanizer.research.nanos;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Nano matches fields
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-03-07 */
public final class VanillaCollection extends NanoPatternTipper<FieldDeclaration> {
  private static final long serialVersionUID = 7636169535439478114L;
  private static final NanoPatternContainer<FieldDeclaration> tippers = new NanoPatternContainer<FieldDeclaration>() {
    @SuppressWarnings("hiding") static final long serialVersionUID = 1L;
    {
      patternTipper("public List<$T> $N = new ArrayList<>();", "", "");
      patternTipper("public Set<$T> $N = new HashSet<>();", "", "");
      patternTipper("public Set<$T> $N = new TreeSet<>();", "", "");
      patternTipper("public Map<$T> $N = new HashMap<>();", "", "");
    }
  };

  @Override public boolean canTip(final FieldDeclaration ¢) {
    return tippers.canTip(¢);
  }

  @Override public Tip pattern(final FieldDeclaration ¢) {
    return new Tip(description(), ¢, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.remove(¢, g);
      }
    };
  }
}
