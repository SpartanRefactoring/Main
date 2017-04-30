package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Constructor just assigning fields
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-03-09 */
public class PojoConstructor extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 0x39E5FF7D92D18405L;

  @Override public boolean prerequisites(final MethodDeclaration ¢) {
    return iz.constructor(¢)//
        && setter(¢) //
        && notEmpty(¢);
  }

  @Override public String tipperName() {
    return "Pojo";
  }
}
