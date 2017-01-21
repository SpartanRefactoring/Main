package il.org.spartan.spartanizer.dispatch;

import il.org.spartan.plugin.preferences.PreferencesResources.*;

/** Classification of tippers
 * @author Yossi Gil
 * @year 2016 */
public interface TipperCategory {
  interface Abbreviation extends Nominal {
    String ____ = "Abbreviation";

    @Override default String description() {
      return ____;
    }
  }

  interface Annonimization extends Nominal {
    String ____ = "Unused arguments";

    @Override default String description() {
      return ____;
    }
  }

  interface Bloater extends TipperCategory {
    String ____ = "Make the code as verbose as possible";

    @Override default String description() {
      return ____;
    }
  }

  interface Centification extends Nominal {
    String ____ = "Centification";

    @Override default String description() {
      return ____;
    }
  }

  /** A specialized {@link Unite} carried out, by factoring out some common
   * element */
  interface CommnonFactoring extends Unite { // S2
    String ____ = "Distributive refactoring";

    @Override default String description() {
      return ____;
    }
  }
  interface Deadcode extends Structural {
    String ____ = "Eliminate code that is never executed";
    @Override default String description() {
      return ____; 
    }
  }


  interface Dollarization extends Nominal {
    String ____ = "Dollarization";

    @Override default String description() {
      return ____;
    }
  }
  interface EarlyReturn extends Structural {
    String ____ = "Early return";

    @Override default String description() {
      return ____;
    }
  }
  interface Idiomatic extends TipperCategory {
    String ____ = "Change expression to a more familiar structure (often shorter)"; 
    @Override default String description() {
      return ____;
    }
  }
  interface Arithmetic extends TipperCategory {
    String ____ = "Change expression to a more familiar structure (often shorter)"; 

    @Override default String description() {
      return ____;
    }
  }



  interface Inlining extends Structural {
    String ____ = "Structural";

    @Override default String description() {
      return ____;
    }
  }

  interface InVain extends Structural {
    String ____ = "NOP";

    @Override default String description() {
      return ____;
    }
  }

  /** Auxiliary type: non public intentionally */
  interface Modular extends TipperCategory {
    String ____ = "Make modular changes to code";
    @Override default String description() {
      return ____; 
    }
  }

  interface Nanos extends TipperCategory {
    String ____ = "Nanos";

    @Override default String description() {
      return ____;
    }
  }

  interface ScopeReduction extends Structural {
    String ____ = "Scope reduction";

    @Override default String description() {
      return ____;
    }
  }

  interface Shunt extends Structural {
    String ____ = "Shortcut of control flow by combining unconditional sequencers, e.g., converting break into return";

    @Override default String description() {
      return ____; 
    }
  }

  /** Use alphabetical, or some other ordering, when order does not matter */
  interface Sorting extends Idiomatic {
    String ____ = "Sorting";

    @Override default String description() {
      return ____;
    }
  }

  interface SyntacticBaggage extends Structural {// S1
    String ____ = "Remove syntactical element that contributes nothing to semantics"; 

    @Override default String description() {
      return ____;
    }
  }

  interface Ternarization extends Structural { // S3
    String ____ = "Convert conditional statement to the conditional, ?:, operator";

    @Override default String description() {
      return ____;
    }
  }

  interface Unite extends Structural {
    String ____ = "Shorten code by merging two adjacent syntactical elements into one";

    @Override default String description() {
      return ____; 
    }
  }

  String description();

  /** Returns the preference group to which the tipper belongs to. This method
   * should be overridden for each tipper and should return one of the values of
   * {@link TipperGroup}
   * @return preference group this tipper belongs to */
  default TipperGroup tipperGroup() {
    return TipperGroup.find(this);
  }
}