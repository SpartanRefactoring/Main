package il.org.spartan.java.temporal;

import java.util.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-11-01 */
public abstract class Temporal implements Operation {
  public interface Before extends Operation {/**/}

  public interface After extends Operation {/**/}

  protected List<Operation> unaffiliated = new LinkedList<>();
  protected List<Before> befores = new LinkedList<>();
  protected List<After> afters = new LinkedList<>();
  protected List<Operation> collaterals = new LinkedList<>();

  @Override public void go() {
    consumeTraits();
    for (Operation o : befores)
      o.go();
    for (Operation o : collaterals)
      o.go();
    // Do nothing here
    for (Operation o : afters)
      o.go();
  }
  public void traitForLater(Operation o) {
    unaffiliated.add(o);
  }
  public void consumeTraits() {
    List<Operation> removed = new LinkedList<>();
    do {
      removed.clear();
      for (Operation o : unaffiliated)
        if (registerNonCollateral(o) || registerNonCollateralToList(o, collaterals))
          removed.add(o);
      unaffiliated.removeAll(removed);
    } while (!removed.isEmpty());
    collaterals.addAll(unaffiliated);
    unaffiliated.clear();
  }
  public void register(Operation o) {
    if (!registerNonCollateral(o) && !registerNonCollateralToList(o, collaterals))
      collaterals.add(o);
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
  protected static boolean registerNonCollateralToList(Operation o, List<? extends Operation> l) {
    for (Operation x : l)
      if (x.registerNonCollateral(o))
        return true;
    return false;
  }
}
