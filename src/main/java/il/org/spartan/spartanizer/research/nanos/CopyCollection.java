package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Create a collection out of a copy of another
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-03 */
public final class CopyCollection extends NanoPatternTipper<ClassInstanceCreation> {
  private static final long serialVersionUID = 0x4DC27528BF6B96EBL;
  private static final BlockNanoPatternContainer tippers = new BlockNanoPatternContainer()//
      .statementsPattern("$T1 $N = new $T2();  $N.addAll($X);", "$T1 $N = Create.from($X);", "CreateFrom pattern");

  @Override public String description(@SuppressWarnings("unused") final ClassInstanceCreation __) {
    return "";
  }

  @Override public boolean canTip(final ClassInstanceCreation x) {
    return tippers.canTip(az.block(parent(parent(parent(x)))));
  }

  @Override @Nullable public Tip pattern(final ClassInstanceCreation x) {
    return tippers.firstTip(az.block(parent(parent(parent(x)))));
  }

  @Override @NotNull public Category category() {
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
