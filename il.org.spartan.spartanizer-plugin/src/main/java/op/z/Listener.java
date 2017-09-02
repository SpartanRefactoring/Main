package op.z;

import java.util.*;
import java.util.function.*;

/** Listener for an {@link Observable}.
 * @author Ori Roth
 * @since 2017-09-01 */
public interface Listener {
  public default <L extends Listener> void delegate(Collection<L> inner, Consumer<L> delegation) {
    for (L listener : inner)
      delegation.accept(listener);
  }
}
