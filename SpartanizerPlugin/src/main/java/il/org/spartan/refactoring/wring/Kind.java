package il.org.spartan.refactoring.wring;

public interface Kind {
  String description();

  interface S0 extends S {
    @Override default String description() {
      return "Neutral elements and null impact";
    }
  }

  interface S1 extends S {
    @Override default String description() {
      return "Syntactic baggage";
    }
  }

  interface S2 extends S {
    @Override default String description() {
      return "Distributive refactoring";
    }
  }

  interface S3 extends S {
    @Override default String description() {
      return "Ternarization";
    }
  }

  interface S4 extends S {
    @Override default String description() {
      return "Canonicalization";
    }
  }

  interface S5 extends S {
    @Override default String description() {
      return "Inlining and scope reduction";
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
