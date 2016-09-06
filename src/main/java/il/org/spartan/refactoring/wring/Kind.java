package il.org.spartan.refactoring.wring;

import il.org.spartan.refactoring.plugin.PluginPreferencesResources.*;

public interface Kind {
  String description();

  /** Returns the preference group to which the wring belongs to. This method
   * should be overridden for each wring and should return one of the values of
   * {@link WringGroup}
   * @return preference group this wring belongs to */
  default WringGroup wringGroup() {
    return WringGroup.find(this);
  }

  interface Abbreviation extends Nominal {
    static final String label = "Abbreviation";

    @Override default String description() {
      return label;
    }
  }

  interface Canonicalization extends Structural { // S4
    static final String label = "Canonicalization";

    @Override default String description() {
      return label;
    }
  }

  interface Centification extends Nominal {
    String label = "Centification";

    @Override default String description() {
      return label;
    }
  }

  interface DistributiveRefactoring extends Structural { // S2
    static String label = "Distributive refactoring";

    @Override default String description() {
      return label;
    }
  }

  interface Dollarization extends Nominal {
    static final String label = "Dollarization";

    @Override default String description() {
      return label;
    }
  }

  interface Inlining extends Structural { // S5
    static final String label = "Inlining";

    @Override default String description() {
      return label;
    }
  }

  interface NoImpact extends Structural { // S0
    static final String label = "0+x, 1*y, 0*y, true, false, and other neutral elements and null impact operations";

    @Override default String description() {
      return label;
    }
  }

  interface ScopeReduction extends Structural { // S6
    static final String label = "Scope reduction";

    @Override default String description() {
      return label;
    }
  }

  interface Sorting extends Structural {
    static final String label = "Sorting";

    @Override default String description() {
      return label;
    }
  }

  interface SyntacticBaggage extends Structural {// S1
    static final String label = "Syntactic baggage";

    @Override default String description() {
      return label;
    }
  }

  interface Ternarization extends Structural { // S3
    static String label = "Ternarization";

    @Override default String description() {
      return label;
    }
  }

  interface UnusedArguments extends Nominal {
    static final String label = "Unused arguments";

    @Override default String description() {
      return label;
    }
  }
}

/** Auxiliary type: non public intentionally */
interface Modular extends Kind {
  /* Empty intentionally */
}

/** Auxiliary type: non public intentionally */
interface Nominal extends Kind {
  /* Empty intentionally */
}

/** Auxiliary type: non public intentionally */
interface Structural extends Kind {
  /* Empty intentionally */
}
