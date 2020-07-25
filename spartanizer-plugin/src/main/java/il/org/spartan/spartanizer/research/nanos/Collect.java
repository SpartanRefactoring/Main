package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.ast.navigate.step.parent;

import java.util.NoSuchElementException;

import org.eclipse.jdt.core.dom.EnhancedForStatement;

import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.research.nanos.common.BlockNanoPatternContainer;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternContainer;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternTipper;
import il.org.spartan.spartanizer.research.nanos.deprecated.Select;
import il.org.spartan.spartanizer.tipping.Tip;

/** Actually contains {@link Select} and {@link CopyCollection}
 * @nano for(X x : Y) if(Z) w.add(x);
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-18 */
public class Collect extends NanoPatternTipper<EnhancedForStatement> {
  private static final long serialVersionUID = -0x27066A25FEAB032EL;
  private static final BlockNanoPatternContainer blockTippers = new BlockNanoPatternContainer()//
      .statementsPattern("$T1 $N1 = new $T2(); for($T3 $N2 : $X1) if($X2) $N1.add($N2);", //
          "$T1 $N1 = ($X1).stream().filter($N2 -> $X2).collect(toList());", //
          "Go Fluent: filter pattern")
      .statementsPattern("$T1 $N1 = new $T2(); for($T3 $N2 : $X1) if($X2) $N1.add($X3);", //
          "$T1 $N1 = ($X1).stream().filter($N2 -> $X2).map($N2 -> $X3).collect(toList());", //
          "Go Fluent: filter pattern")
      .statementsPattern("$T1 $N1 = new $T2(); for($T3 $N2 : $X1) $N1.add($N2);", //
          "$T1 $N1 = ($X1).stream().collect(toList());", //
          "Go Fluent: filter pattern")
      .statementsPattern("$T1 $N1 = new $T2(); for($T3 $N2 : $X1) $N1.add($X3);", //
          "$T1 $N1 = ($X1).stream().map($N2 -> $X3).collect(toList());", //
          "Go Fluent: filter pattern");
  static final NanoPatternContainer<EnhancedForStatement> tippers = new NanoPatternContainer<EnhancedForStatement>()
      .add("for($T1 $N2 : $X1) if($X2) $N1.add($N2);", //
          "$N1.addAll(($X1).stream().filter($N2 -> $X2).collect(toList()));", //
          "Go Fluent: filter pattern")
      .add("for($T1 $N2 : $X1) if($X2) $N1.add($X3);", //
          "$N1.addAll(($X1).stream().filter($N2 -> $X2).map($N2->$X3).collect(toList()));", //
          "Go Fluent: filter pattern")
      .add("for($T1 $N2 : $X1) $N1.add($N2);", //
          "$N1.addAll(($X1).stream().collect(toList()));", //
          "Go Fluent: filter pattern")
      .add("for($T1 $N2 : $X1) $N1.add($X3);", //
          "$N1.addAll(($X1).stream().map($N2->$X3).collect(toList()));", //
          "Go Fluent: filter pattern");

  public static class defender extends NanoPatternTipper<EnhancedForStatement> {
    private static final long serialVersionUID = -0x154065FC541A1CCEL;

    @Override protected Tip pattern(final EnhancedForStatement ¢) {
      return firstTip(tippers, ¢);
    }
    @Override public boolean canTip(final EnhancedForStatement ¢) {
      return anyTips(tippers, ¢);
    }
  }

  @Override public boolean canTip(final EnhancedForStatement ¢) {
    return anyTips(blockTippers, az.block(parent(¢)))//
        || anyTips(tippers, ¢);
  }
  @Override public Tip pattern(final EnhancedForStatement $) {
    try {
      return firstTip(blockTippers, az.block(parent($)));
    } catch (@SuppressWarnings("unused") final NoSuchElementException __) {
      return firstTip(tippers, $);
    }
  }
  @Override public Category category() {
    return Category.Iterative;
  }
  @Override public String tipperName() {
    return "SelectBy";
  }
}
