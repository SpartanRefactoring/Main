package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.spartanizer.research.nanos.deprecated.*;

/** @nano Apply statement for each element in collection
 * @author Ori Marcovitch */
public class ForEach extends NanoPatternTipper<EnhancedForStatement> {
  private static final long serialVersionUID = -0x3CC3A37D0C9EBC0AL;
  private static final List<UserDefinedTipper<EnhancedForStatement>> tippers = as.list(
      patternTipper("for($T $N1 : $N2) $X;", "$N2.forEach($N1 -> $X);", "ForEach pattern: conevrt to fluent API"),
      patternTipper("for($T $N1 : $X1) $X2;", "($X1).forEach($N1 -> $X2);", "ForEachThat pattern: conevrt to fluent API"),
      patternTipper("for($T1 $N1 : $N2) try{ $X; } catch($T2 $N3) $B", "$N2.forEach($N1 -> {try{ $X; } catch($T2 $N3) $B});",
          "ForEach pattern: conevrt to fluent API"),
      patternTipper("for($T1 $N1 : $X1) try{ $X2; } catch($T2 $N2) $B", "($X1).forEach($N1 -> {try{ $X2; } catch($T2 $N2) $B});",
          "ForEach pattern: conevrt to fluent API"),
      patternTipper("for($T1 $N1 : $N2) try{ $X; } catch($T2 $N3) $B1 catch($T3 $N4) $B2",
          "$N2.forEach($N1 -> {try{ $X; } catch($T2 $N3) $B1 catch($T3 $N4) $B2});", "ForEach pattern: conevrt to fluent API"),
      patternTipper("for($T1 $N1 : $X1) try{ $X2; } catch($T2 $N2) $B1 catch($T3 $N3) $B2",
          "($X1).forEach($N1 -> {try{ $X2; } catch($T2 $N2) $B1 catch($T3 $N3) $B2});", "ForEach pattern: conevrt to fluent API"));
  protected static final Collection<NanoPatternTipper<EnhancedForStatement>> rivals = as.list(//
      new HoldsForAll(), //
      new HoldsForAny(), //
      new Aggregate(), //
      new Collect.defender(), //
      new Select(), //
      new CountIf(), //
      new FlatMap()//
  );

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
