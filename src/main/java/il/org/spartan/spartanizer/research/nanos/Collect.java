package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-18 */
public class Collect extends NanoPatternTipper<EnhancedForStatement> {
  private static final List<UserDefinedTipper<Block>> tippers = new ArrayList<UserDefinedTipper<Block>>() {
    static final long serialVersionUID = 1L;
    {
      add(statementsPattern("$T1 $N1 = new $T2(); for($T3 $N2 : $X1) if($X2) $N1.add($N2);", //
          "$T1 $N1 = ($X1).stream().filter($N2 -> $X2).collect(Collectors.toList());", //
          "Go Fluent: filter pattern"));
      add(statementsPattern("$T1 $N1 = new $T2(); for($T3 $N2 : $X1) if($X2) $N1.add($X3);", //
          "$T1 $N1 = ($X1).stream().filter($N2 -> $X2).map($N2 -> $X3).collect(Collectors.toList());", //
          "Go Fluent: filter pattern"));
      add(statementsPattern("$T1 $N1 = new $T2(); for($T3 $N2 : $X1) $N1.add($N2);", //
          "$T1 $N1 = ($X1).stream().collect(Collectors.toList());", //
          "Go Fluent: filter pattern"));
      add(statementsPattern("$T1 $N1 = new $T2(); for($T3 $N2 : $X1) $N1.add($X2);", //
          "$T1 $N1 = ($X1).stream().map($N2 -> $X2).collect(Collectors.toList());", //
          "Go Fluent: filter pattern"));
    }
  };

  public static class defender extends NanoPatternTipper<EnhancedForStatement> {
    private static final List<UserDefinedTipper<EnhancedForStatement>> patterns = new ArrayList<UserDefinedTipper<EnhancedForStatement>>() {
      static final long serialVersionUID = 1L;
      {
        add(patternTipper("for($T $N1 : $X1) if($X2) $N2.add($X3);", "", ""));
        add(patternTipper("for($T $N1 : $X1) $N2.add($X2);", "", ""));
      }
    };

    @Override protected Tip pattern(EnhancedForStatement ¢) {
      return firstTip(patterns, ¢);
    }

    @Override public boolean canTip(EnhancedForStatement ¢) {
      return anyTips(patterns, ¢);
    }
  }

  @Override public boolean canTip(final EnhancedForStatement ¢) {
    return anyTips(tippers, az.block(parent(¢)));
  }

  @Override public Tip pattern(final EnhancedForStatement ¢) {
    return firstTip(tippers, az.block(parent(¢)));
  }

  @Override public Category category() {
    return Category.Collection;
  }

  @Override public String technicalName() {
    return "collect C [s.t. P(·)]";
  }
}
