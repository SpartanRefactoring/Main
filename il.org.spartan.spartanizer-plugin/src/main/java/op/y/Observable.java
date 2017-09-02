package op.y;

import java.util.*;
import java.util.function.*;

import fluent.ly.*;

/** Observable object.
 * @author Ori Roth
 * @since 2017-09-01 */
public class Observable<Self extends Observable<Self>> implements Selfie<Self> {
  /** Listener for an {@link Observable}.
   * @author Ori Roth
   * @since 2017-09-01 */
  public class Listener {
    protected <L extends Listener> void delegate(Collection<L> inner, Consumer<L> delegation) {
      for (L listener : inner)
        delegation.accept(listener);
    }
  }
}
