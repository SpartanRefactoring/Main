package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.spartanizer.research.nanos.deprecated.*;

/** @nano Appply statement for each element in collection
 * @author Ori Marcovitch */
public class ForEach extends NanoPatternTipper<EnhancedForStatement> {
  private static final long serialVersionUID = -4378523020212222986L;
  private static final List<UserDefinedTipper<EnhancedForStatement>> tippers = new ArrayList<UserDefinedTipper<EnhancedForStatement>>() {
    @SuppressWarnings("hiding")
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("for($T $N1 : $N2) $X;", "$N2.forEach($N1 -> $X);", "ForEach pattern: conevrt to fluent API"));
      add(patternTipper("for($T $N1 : $X1) $X2;", "($X1).forEach($N1 -> $X2);", "ForEachThat pattern: conevrt to fluent API"));
      add(patternTipper("for($T1 $N1 : $N2) try{ $X; } catch($T2 $N3) $B", "$N2.forEach($N1 -> {try{ $X; } catch($T2 $N3) $B});",
          "ForEach pattern: conevrt to fluent API"));
      add(patternTipper("for($T1 $N1 : $X1) try{ $X2; } catch($T2 $N2) $B", "($X1).forEach($N1 -> {try{ $X2; } catch($T2 $N2) $B});",
          "ForEach pattern: conevrt to fluent API"));
      add(patternTipper("for($T1 $N1 : $N2) try{ $X; } catch($T2 $N3) $B1 catch($T3 $N4) $B2",
          "$N2.forEach($N1 -> {try{ $X; } catch($T2 $N3) $B1 catch($T3 $N4) $B2});", "ForEach pattern: conevrt to fluent API"));
      add(patternTipper("for($T1 $N1 : $X1) try{ $X2; } catch($T2 $N2) $B1 catch($T3 $N3) $B2",
          "($X1).forEach($N1 -> {try{ $X2; } catch($T2 $N2) $B1 catch($T3 $N3) $B2});", "ForEach pattern: conevrt to fluent API"));
    }
  };
  protected static final List<NanoPatternTipper<EnhancedForStatement>> rivals = new ArrayList<NanoPatternTipper<EnhancedForStatement>>() {
    @SuppressWarnings("hiding")
    static final long serialVersionUID = 1L;
    {
      add(new HoldsForAll());
      add(new HoldsForAny());
      add(new Aggregate());
      add(new Collect.defender());
      add(new Select());
      add(new CountIf());
      add(new FlatMap());
    }
  };

  @Override public boolean canTip(final EnhancedForStatement ¢) {
    return anyTips(tippers, ¢)//
        && nonTips(rivals, ¢);
  }

  @Override public Tip pattern(final EnhancedForStatement ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public String description() {
    return "Iterate a collection and apply a statement for each element";
  }

  @Override public String technicalName() {
    return "foreach C [s.t. P(·)] do S(·)";
  }

  @Override public String example() {
    return firstPattern(tippers);
  }

  @Override public String symbolycReplacement() {
    return firstReplacement(tippers);
  }
}
