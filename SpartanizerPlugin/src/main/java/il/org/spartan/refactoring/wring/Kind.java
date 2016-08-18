package il.org.spartan.refactoring.wring;

public interface Kind {
  String description();

  interface NoImpact extends S {
    @Override default String description() {
      return "Neutral elements and null impact";
    }
  }

  interface SyntacticBaggage extends S {
    @Override default String description() {
      return "Syntactic baggage";
    }
  }

  interface DistributiveRefactoring extends S {
    @Override default String description() {
      return "Distributive refactoring";
    }
  }

  interface Ternarization extends S {
    @Override default String description() {
      return "Ternarization";
    }
  }

  interface Canonicalization extends S {
    @Override default String description() {
      return "Canonicalization";
    }
  }
  interface Inlining extends S {
    @Override default String description() {
      return "Inlining";
    }
  }
  interface ScopeReduction extends S {
    @Override default String description() {
      return "Scope reduction";
    }
  }


  interface N1 extends N {
    @Override default String description() {
      return "Unused arguments";
    }
  }

  interface N2 extends N {
    @Override default String description() {
      return "Centification";
    }
  }

  interface N3 extends N {
    @Override default String description() {
      return "Abbreviation";
    }
  }

  interface N4 extends N {
    @Override default String description() {
      return "Dollarization";
    }
  }
}

interface N extends Kind {
}

interface S extends Kind {
}

interface M extends Kind {
}
