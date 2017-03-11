package il.org.spartan.spartanizer.research.nanos;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.spartanizer.research.nanos.deprecated.*;

/** Actually contains {@link Select} and {@link CopyCollection}
 * @nano for(X x : Y) if(Z) w.add(x);
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-18 */
public class Collect extends NanoPatternTipper<EnhancedForStatement> {
  private static final long serialVersionUID = -2812051728758407982L;
  private static final BlockNanoPatternContainer blockTippers = new BlockNanoPatternContainer() {
    @SuppressWarnings("hiding") static final long serialVersionUID = 1L;
    {
      statementsPattern("$T1 $N1 = new $T2(); for($T3 $N2 : $X1) if($X2) $N1.add($N2);", //
          "$T1 $N1 = ($X1).stream().filter($N2 -> $X2).collect(toList());", //
          "Go Fluent: filter pattern");
      statementsPattern("$T1 $N1 = new $T2(); for($T3 $N2 : $X1) if($X2) $N1.add($X3);", //
          "$T1 $N1 = ($X1).stream().filter($N2 -> $X2).map($N2 -> $X3).collect(toList());", //
          "Go Fluent: filter pattern");
      statementsPattern("$T1 $N1 = new $T2(); for($T3 $N2 : $X1) $N1.add($N2);", //
          "$T1 $N1 = ($X1).stream().collect(toList());", //
          "Go Fluent: filter pattern");
      statementsPattern("$T1 $N1 = new $T2(); for($T3 $N2 : $X1) $N1.add($X3);", //
          "$T1 $N1 = ($X1).stream().map($N2 -> $X3).collect(toList());", //
          "Go Fluent: filter pattern");
    }
  };
  static final NanoPatternContainer<EnhancedForStatement> tippers = new NanoPatternContainer<EnhancedForStatement>() {
    @SuppressWarnings("hiding") static final long serialVersionUID = 1L;
    {
      patternTipper("for($T1 $N2 : $X1) if($X2) $N1.add($N2);", //
          "$N1.addAll(($X1).stream().filter($N2 -> $X2).collect(toList()));", //
          "Go Fluent: filter pattern");
      patternTipper("for($T1 $N2 : $X1) if($X2) $N1.add($X3);", //
          "$N1.addAll(($X1).stream().filter($N2 -> $X2).map($N2->$X3).collect(toList()));", //
          "Go Fluent: filter pattern");
      patternTipper("for($T1 $N2 : $X1) $N1.add($N2);", //
          "$N1.addAll(($X1).stream().collect(toList()));", //
          "Go Fluent: filter pattern");
      patternTipper("for($T1 $N2 : $X1) $N1.add($X3);", //
          "$N1.addAll(($X1).stream().map($N2->$X3).collect(toList()));", //
          "Go Fluent: filter pattern");
    }
  };

  public static class defender extends NanoPatternTipper<EnhancedForStatement> {
    private static final long serialVersionUID = -1531336007723130062L;

    @Override protected Tip pattern(final EnhancedForStatement ¢) {
      return firstTip(tippers, ¢);
    }

    @Override public boolean check(final EnhancedForStatement ¢) {
      return anyTips(tippers, ¢);
    }
  }

  @Override public boolean check(final EnhancedForStatement ¢) {
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

  @Override public String technicalName() {
    return "collect C [s.t. P(·)]";
  }
}
