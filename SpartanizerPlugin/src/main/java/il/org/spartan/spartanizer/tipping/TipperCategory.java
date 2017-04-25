package il.org.spartan.spartanizer.tipping;

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

  interface Abbreviation extends Nominal {
    String toString = "Abbreviation";

    @Override default String description() {
      return toString;
    }
  }

  interface Anonymization extends Nominal {
    String toString = "Unused arguments";

    @Override default String description() {
      return toString;
    }
  }

  interface Arithmetic extends TipperCategory {
    String toString = "Change expression to a more familiar structure (often shorter)";

    @Override default String description() {
      return toString;
    }
  }

  interface Loops extends Structural {
    String toString = "More efficient use of Java loop structures";

    @Override default String description() {
      return toString;
    }
  }

  interface Bloater extends TipperCategory {
    String toString = "Make the code as verbose as possible";

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

  /** A specialized {@link Unite} carried out, by factoring out some common
   * element */
  interface CommnonFactoring extends Unite { // S2
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

  interface Dollarization extends Nominal {
    String toString = "Dollarization";

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

  @FunctionalInterface
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

  interface Unite extends Structural {
    String toString = "Shorten code by merging two adjacent syntactical elements into one";

    @Override default String description() {
      return toString;
    }
  }
}
