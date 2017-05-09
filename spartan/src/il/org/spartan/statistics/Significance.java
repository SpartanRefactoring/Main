package il.org.spartan.statistics;

public enum Significance {
  INSIGNIFICANT {
    @Override public String toString() {
      return "sig. <95%";
    }
  },
  FIVE_PERCENT {
    @Override public String toString() {
      return "sig.> 95%";
    }
  },
  ONE_PERCENT {
    @Override public String toString() {
      return "sig.> 99%";
    }
  },
  ONE_PERMILLE {
    @Override public String toString() {
      return "sig.> 99.9%";
    }
  };
  public static Significance signifcance(final double z) {
    return Math.abs(z) < 1.960 ? INSIGNIFICANT : Math.abs(z) < 2.575 ? FIVE_PERCENT : Math.abs(z) < 3.08 ? ONE_PERCENT : ONE_PERMILLE;
  }
  public static Significance signifcance(final Kendall.Charectristics c) {
    if (c.n > 10)
      return signifcance(c.z);
    double $, thresholdB;
    switch (c.n) {
      case 5:
        $ = 1;
        thresholdB = 0.800;
        break;
      case 6:
        $ = 0.8667;
        thresholdB = 0.7333;
        break;
      case 7:
        $ = 0.8095;
        thresholdB = 0.6190;
        break;
      case 8:
        $ = 0.7143;
        thresholdB = 0.5714;
        break;
      case 9:
        $ = 0.6667;
        thresholdB = 0.5000;
        break;
      case 10:
        $ = 0.600;
        thresholdB = 0.4667;
        break;
      default:
        $ = 1.5;
        thresholdB = 1.5;
    }
    return Math.abs(c.tau) < thresholdB ? INSIGNIFICANT : Math.abs(c.tau) < $ ? FIVE_PERCENT : ONE_PERCENT;
  }
}