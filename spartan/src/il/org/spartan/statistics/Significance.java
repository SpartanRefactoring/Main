package il.org.spartan.statistics;

import org.jetbrains.annotations.*;

public enum Significance {
  INSIGNIFICANT {
    @Override @NotNull public String toString() {
      return "sig. <95%";
    }
  },
  FIVE_PERCENT {
    @Override @NotNull public String toString() {
      return "sig.> 95%";
    }
  },
  ONE_PERCENT {
    @Override @NotNull public String toString() {
      return "sig.> 99%";
    }
  },
  ONE_PERMILLE {
    @Override @NotNull public String toString() {
      return "sig.> 99.9%";
    }
  };
  @NotNull public static Significance signifcance(final double z) {
    return Math.abs(z) < 1.960 ? INSIGNIFICANT : Math.abs(z) < 2.575 ? FIVE_PERCENT : Math.abs(z) < 3.08 ? ONE_PERCENT : ONE_PERMILLE;
  }

  @NotNull public static Significance signifcance(@NotNull final Kendall.Charectristics c) {
    if (c.n > 10)
      return signifcance(c.z);
    double thresholdA;
    double thresholdB;
    switch (c.n) {
      case 5:
        thresholdA = 1;
        thresholdB = 0.800;
        break;
      case 6:
        thresholdA = 0.8667;
        thresholdB = 0.7333;
        break;
      case 7:
        thresholdA = 0.8095;
        thresholdB = 0.6190;
        break;
      case 8:
        thresholdA = 0.7143;
        thresholdB = 0.5714;
        break;
      case 9:
        thresholdA = 0.6667;
        thresholdB = 0.5000;
        break;
      case 10:
        thresholdA = 0.600;
        thresholdB = 0.4667;
        break;
      default:
        thresholdA = 1.5;
        thresholdB = 1.5;
    }
    return Math.abs(c.tau) < thresholdB ? INSIGNIFICANT : Math.abs(c.tau) < thresholdA ? FIVE_PERCENT : ONE_PERCENT;
  }
}