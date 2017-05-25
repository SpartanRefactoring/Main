package il.org.spartan.spartanizer.tipping;

import java.lang.reflect.*;
import java.util.*;
import java.util.Map.*;

import fluent.ly.*;
import il.org.spartan.plugin.preferences.revision.PreferencesResources.*;

/** Classification of tippers
 * @author Yossi Gil
 * @since Sep 28, 2016 */
@FunctionalInterface
public interface TipperCategory {
  String description();
  /** Returns the preference group to which the tipper belongs to. This method
   * should be overridden for each tipper and should return one of the values of
   * {@link TipperGroup}
   * @return preference group this tipper belongs to */
  default TipperGroup tipperGroup() {
    return TipperGroup.find(this);
  }
  default Class<? extends TipperCategory> lowestCategory() {
    Class<? extends TipperCategory> $ = TipperCategory.class;
    for (final Class<? extends TipperCategory> ¢ : hierarchy.keySet())
      if (¢.isInstance(this) && $.isAssignableFrom(¢))
        $ = ¢;
    return $;
  }

  Map<Class<? extends TipperCategory>, List<Class<? extends TipperCategory>>> hierarchy = anonymous.ly(() -> {
    final Map<Class<? extends TipperCategory>, List<Class<? extends TipperCategory>>> $ = new HashMap<>();
    $.put(Nominal.class, Arrays.asList(Abbreviation.class, Anonymization.class, NameOfResult.class));
    $.put(Structural.class, Arrays.asList(Collapse.class, Loops.class, Deadcode.class, EarlyReturn.class, NOP.class, ScopeReduction.class,
        Shortcircuit.class, SyntacticBaggage.class));
    $.put(Abbreviation.class, an.empty.list());
    $.put(Anonymization.class, an.empty.list());
    $.put(Arithmetics.class, an.empty.list());
    $.put(Loops.class, an.empty.list());
    $.put(Bloater.class, an.empty.list());
    $.put(Centification.class, an.empty.list());
    $.put(CommnonFactoring.class, Arrays.asList(Ternarization.class));
    $.put(Deadcode.class, an.empty.list());
    $.put(NameOfResult.class, an.empty.list());
    $.put(EarlyReturn.class, an.empty.list());
    $.put(EmptyCycles.class, an.empty.list());
    $.put(Idiomatic.class, Arrays.asList(Sorting.class));
    $.put(Inlining.class, an.empty.list());
    $.put(Modular.class, an.empty.list());
    $.put(Nanos.class, an.empty.list());
    $.put(NOP.class, Arrays.asList(NOP.onBooleans.class, NOP.onNumbers.class, NOP.onStrings.class));
    $.put(NOP.onBooleans.class, an.empty.list());
    $.put(NOP.onNumbers.class, an.empty.list());
    $.put(NOP.onStrings.class, an.empty.list());
    $.put(ScopeReduction.class, an.empty.list());
    $.put(Shortcircuit.class, an.empty.list());
    $.put(Sorting.class, an.empty.list());
    $.put(SyntacticBaggage.class, an.empty.list());
    $.put(Ternarization.class, an.empty.list());
    $.put(Collapse.class, Arrays.asList(CommnonFactoring.class));
    return $;
  });
  Map<Class<? extends TipperCategory>, Class<? extends TipperCategory>> reversedHierarchy = anonymous.ly(() -> {
    final Map<Class<? extends TipperCategory>, Class<? extends TipperCategory>> $ = new HashMap<>();
    for (final Entry<Class<? extends TipperCategory>, List<Class<? extends TipperCategory>>> e : hierarchy.entrySet())
      for (final Class<? extends TipperCategory> ¢ : e.getValue())
        $.put(¢, e.getKey());
    return $;
  });
  Map<Class<? extends TipperCategory>, String> descriptions = anonymous.ly(() -> {
    final Map<Class<? extends TipperCategory>, String> $ = new HashMap<>();
    for (final Class<? extends TipperCategory> c : hierarchy.keySet())
      try {
        for (final Field ¢ : c.getDeclaredFields())
          if ("toString".equals(¢.getName()))
            $.put(c, (String) ¢.get(null));
      } catch (IllegalAccessException | IllegalArgumentException | SecurityException ¢) {
        note.bug(¢);
      }
    return $;
  });

  interface Abbreviation extends Nominal {
    String toString = "One letter convention for locals";

    @Override default String description() {
      return toString;
    }
  }

  interface Anonymization extends Nominal {
    String toString = "Naming convention for anonymizing unused parameters";

    @Override default String description() {
      return toString;
    }
  }

  interface Arithmetics extends TipperCategory {
    String toString = "Rewrite expressions in a more canonical form";

    @Override default String description() {
      return toString;
    }
  }

  interface Loops extends Structural {
    String toString = "Spartan use of Java loop syntax";

    @Override default String description() {
      return toString;
    }
  }

  interface Bloater extends TipperCategory {
    String toString = "Make code as verbose as possible";

    @Override default String description() {
      return toString;
    }
  }

  interface Centification extends Nominal {
    String toString = "Centification";

    @Override default String description() {
      return toString;
    }
  }

  /** A specialized {@link Collapse} carried out, by factoring out some common
   * element */
  interface CommnonFactoring extends Collapse { // S2
    String toString = "Distributive refactoring";

    @Override default String description() {
      return toString;
    }
  }

  interface Deadcode extends Structural {
    String toString = "Eliminate code that is never executed";

    @Override default String description() {
      return toString;
    }
  }

  interface NameOfResult extends Nominal {
    String toString = "Naming convention for the result variable";

    @Override default String description() {
      return toString;
    }
  }

  interface EarlyReturn extends Structural {
    String toString = "Early return";

    @Override default String description() {
      return toString;
    }
  }

  interface EmptyCycles extends TipperCategory {
    String toString = "churn";
  }

  interface Idiomatic extends TipperCategory {
    String toString = "Change expression to a more familiar structure (often shorter)";

    @Override default String description() {
      return toString;
    }
  }

  interface Inlining extends Structural {
    String toString = "Structural";

    @Override default String description() {
      return toString;
    }
  }

  /** Auxiliary __: non public intentionally */
  interface Modular extends TipperCategory {
    String toString = "Make modular changes to code";

    @Override default String description() {
      return toString;
    }
  }

  interface Nanos extends TipperCategory {
    String toString = "Nanos";

    @Override default String description() {
      return toString;
    }
  }

  interface NOP extends Structural {
    String toString = "Eliminate an operation whose computation does nothing";

    @Override default String description() {
      return toString;
    }

    interface onBooleans extends NOP {
      @SuppressWarnings("hiding") String toString = "Eliminate an operation whose computation does nothing on booleans";
    }

    interface onNumbers extends NOP {
      @SuppressWarnings("hiding") String toString = "Eliminate an operation whose computation does nothing on numbers";
    }

    interface onStrings extends NOP {
      @SuppressWarnings("hiding") String toString = "Eliminate an operation whose computation does nothing on strings";
    }
  }

  interface ScopeReduction extends Structural {
    String toString = "Scope reduction";

    @Override default String description() {
      return toString;
    }
  }

  interface Shortcircuit extends Structural {
    String toString = "Shortcut of control flow by combining unconditional sequencers, e.g., converting break into return";

    @Override default String description() {
      return toString;
    }
  }

  /** Use alphabetical, or some other ordering, when order does not matter */
  interface Sorting extends Idiomatic {
    String toString = "Sorting";

    @Override default String description() {
      return toString;
    }
  }

  interface SyntacticBaggage extends Structural {// S1
    String toString = "Remove syntactical element that contributes nothing to semantics";

    @Override default String description() {
      return toString;
    }
  }

  interface Ternarization extends CommnonFactoring { // S3
    String toString = "Convert conditional statement to the conditional, ?:, operator";

    @Override default String description() {
      return toString;
    }
  }

  interface Collapse extends Structural {
    String toString = "Shorten code by merging two adjacent syntactical elements into one";

    @Override default String description() {
      return toString;
    }
  }
}
