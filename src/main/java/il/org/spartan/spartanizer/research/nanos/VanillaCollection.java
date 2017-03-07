package il.org.spartan.spartanizer.research.nanos;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;
import static il.org.spartan.lisp.*;

/** Nano matches fields
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-03-07 */
public final class VanillaCollection extends NanoPatternTipper<FieldDeclaration> {
  private static final long serialVersionUID = 7636169535439478114L;
  private static final List<String> abstractTypes = Arrays.asList("List", "Set", "Map");
  private static final List<String> specificTypes = Arrays.asList("ArrayList", "HashSet", "TreeSet", "HashMap");

  @Override public boolean canTip(final FieldDeclaration $) {
    return abstractTypes.contains(type(az.parameterizedType(type($))) + "")
        && specificTypes.contains(type(az.parameterizedType(type(az.classInstanceCreation(initializer(onlyOne(fragments($))))))) + "");
  }

  @Override public Tip pattern(final FieldDeclaration ¢) {
    return new Tip(description(), ¢, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.remove(¢, g);
      }
    };
  }
}
