package il.org.spartan.spartanizer.dispatch;

import il.org.spartan.plugin.preferences.PreferencesResources.*;

/** Classification of tippers
 * @author Yossi Gil
 * @year 2016 */
public interface TipperCategory {
  interface Abbreviation extends Nominal {
    String label = "Abbreviation";

    @Override default String description() {
      return label;
    }
  }

  interface Annonimization extends Nominal {
    String label = "Unused arguments";

    @Override default String description() {
      return label;
    }
  }

  interface Bloater extends TipperCategory {
    String ____ = "Make the code as verbose as possible";

    @Override default String description() {
      return ____;
    }
  }

  interface Centification extends Nominal {
    String label = "Centification";

    @Override default String description() {
      return label;
    }
  }

  /** A specialized {@link Unite} carried out, by factoring out some common
   * element */
  interface CommnoFactoring extends Unite { // S2
    String label = "Distributive refactoring";

    @Override default String description() {
      return label;
    }
  }
  interface Deadcode extends Structural {
    String ____ = "Eliminate code that is never executed";
    String label = Deadcode.class.getSimpleName(); 
    @Override default String description() {
      return ____; 
    }
  }


  interface Dollarization extends Nominal {
    String label = "Dollarization";

    @Override default String description() {
      return label;
    }
  }
  interface EarlyReturn extends Structural {
    String label = "Early return";

    @Override default String description() {
      return label;
    }
  }
  interface Idiomatic extends TipperCategory {
    String label = "Change expression to a more familiar structure (often shorter)"; 
    @Override default String description() {
      return label;
    }
  }
  interface Arithmetic extends TipperCategory {
    String ____ = "Change expression to a more familiar structure (often shorter)"; 

    @Override default String description() {
      return ____;
    }
  }



  interface Inlining extends Structural {
    String label = "Structural";

    @Override default String description() {
      return label;
    }
  }

  interface InVain extends Structural {
    String label = "NOP";

    @Override default String description() {
      return label;
    }
  }

  /** Auxiliary type: non public intentionally */
  interface Modular extends TipperCategory {
    String ____ = "Make modular changes to code";
    String label = Modular.class.getSimpleName(); 
    @Override default String description() {
      return ____; 
    }
  }

  interface Nanos extends Modular {
    String label = "Nanos";

    @Override default String description() {
      return label;
    }
  }

  interface ScopeReduction extends Structural {
    String label = "Scope reduction";

    @Override default String description() {
      return label;
    }
  }

  interface Shunt extends Structural {
    String ____ = "Shortcut control flow through sequencers, e.g., converting break into return";
    String label = Shunt.class.getSimpleName(); 

    @Override default String description() {
      return ____; 
    }
  }

  /** Use alphabetical, or some other ordering, when order does not matter */
  interface Sorting extends Idiomatic {
    String label = "Sorting";

    @Override default String description() {
      return label;
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
    String label = Unite.class.getSimpleName(); 

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