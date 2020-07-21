package il.org.spartan.java.temporal;

import java.util.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-11-01 */
public abstract class Temporal implements Operation {
  private List<Operation> befores = new LinkedList<>();
  private List<Operation> afters = new LinkedList<>();
  private List<Operation> collaterals = new LinkedList<>();

  protected abstract void body();
  @Override public final void go() {
    for (Operation o : befores)
      o.go();
    for (Operation o : collaterals)
      o.go();
    body();
    for (Operation o : afters)
      o.go();
  }
  @Override public final List<Operation> knownBefores() {
    return befores;
  }
  @Override public final List<Operation> knownAfters() {
    return afters;
  }
  @Override public final List<Operation> knownCollaterals() {
    return collaterals;
  }
  public final void register(Operation o) {
    List<Operation> ocs = o.knownCollaterals(), removed = new LinkedList<>();
    for (Operation oc : ocs)
      if (temporalsRegisterNonCollateral(this, oc))
        removed.add(oc);
    ocs.removeAll(removed);
    if (!registerNonCollateral(this, o) && !registerNonCollateralToList(o, collaterals))
      collaterals.add(o);
    collaterals.addAll(ocs);
  }
  private static boolean registerNonCollateral(Operation current, Operation o) {
    if (temporalsRegisterNonCollateral(current, o))
      return true;
    if (current.isBefore(o) || o.isAfter(current)) {
      current.knownBefores().add(o);
      return true;
    } else if (current.isAfter(o) || o.isBefore(current)) {
      current.knownAfters().add(o);
      return true;
    }
    return false;
  }
  private static boolean temporalsRegisterNonCollateral(Operation current, Operation o) {
    return registerNonCollateralToList(o, current.knownBefores()) || registerNonCollateralToList(o, current.knownAfters());
  }
  private static boolean registerNonCollateralToList(Operation o, List<? extends Operation> l) {
    for (Operation x : l)
      if (registerNonCollateral(x, o))
        return true;
    return false;
  }
}
