package il.org.spartan.spartanizer.research.nanos;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Create a collection out of a copy of another
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-03 */
public final class CopyCollection extends NanoPatternTipper<ClassInstanceCreation> {
  private static final long serialVersionUID = 5603169704272959211L;
  private static final BlockNanoPatternContainer tippers = new BlockNanoPatternContainer() {
    @SuppressWarnings("hiding") static final long serialVersionUID = 1L;
    {
      statementsPattern("$T1 $N = new $T2();  $N.addAll($X);", "$T1 $N = Create.from($X);", "CreateFrom pattern");
    }
  };

  @Override public String description(@SuppressWarnings("unused") final ClassInstanceCreation __) {
    return "";
  }

  @Override public boolean interesting(final ClassInstanceCreation x) {
    return tippers.canTip(az.block(parent(parent(parent(x)))));
  }

  @Override public Tip pattern(final ClassInstanceCreation x) {
    return tippers.firstTip(az.block(parent(parent(parent(x)))));
  }

  @Override public Category category() {
    return Category.Iterative;
  }

  @Override public String technicalName() {
    return "AssignXWithNewTAddCollectionToX";
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }
}
