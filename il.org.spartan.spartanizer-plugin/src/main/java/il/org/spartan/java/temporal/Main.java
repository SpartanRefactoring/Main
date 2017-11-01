package il.org.spartan.java.temporal;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-11-01 */
public class Main {
  static class WriteToFile extends Temporal {
    interface Before extends Temporal.Before {/**/}

    interface After extends Temporal.After {/**/}

    @Override public void go() {
      consumeTraits();
      for (Operation o : befores)
        o.go();
      for (Operation o : collaterals)
        o.go();
      System.out.println("write to file");
      for (Operation o : afters)
        o.go();
    }
    /** Not the same as {@link Temporal#registerNonCollateral}! */
    @Override public boolean registerNonCollateral(Operation o) {
      if (registerNonCollateralToList(o, befores) || registerNonCollateralToList(o, afters))
        return true;
      if (o instanceof Before) {
        befores.add((Before) o);
        return true;
      } else if (o instanceof After) {
        afters.add((After) o);
        return true;
      }
      return false;
    }
  }

  static class OpenFile extends Temporal implements WriteToFile.Before {
    interface Before extends Temporal.Before {/**/}

    interface After extends Temporal.After {/**/}

    @Override public void go() {
      consumeTraits();
      for (Operation o : befores)
        o.go();
      for (Operation o : collaterals)
        o.go();
      System.out.println("open file");
      for (Operation o : afters)
        o.go();
    }
    @Override public boolean registerNonCollateral(Operation o) {
      if (registerNonCollateralToList(o, befores) || registerNonCollateralToList(o, afters))
        return true;
      if (o instanceof Before) {
        befores.add((Before) o);
        return true;
      } else if (o instanceof After) {
        afters.add((After) o);
        return true;
      }
      return false;
    }
  }

  static class LogFileOpened extends Temporal implements OpenFile.After {
    interface Before extends Temporal.Before {/**/}

    interface After extends Temporal.After {/**/}

    @Override public void go() {
      consumeTraits();
      for (Operation o : befores)
        o.go();
      for (Operation o : collaterals)
        o.go();
      System.out.println("logged file openning");
      for (Operation o : afters)
        o.go();
    }
    @Override public boolean registerNonCollateral(Operation o) {
      if (registerNonCollateralToList(o, befores) || registerNonCollateralToList(o, afters))
        return true;
      if (o instanceof Before) {
        befores.add((Before) o);
        return true;
      } else if (o instanceof After) {
        befores.add((Before) o);
        return true;
      }
      return false;
    }
  }

  public static void main(String[] args) {
    Temporal w = new WriteToFile();
    w.register(new LogFileOpened());
    w.register(new OpenFile());
    w.go();
  }
}
