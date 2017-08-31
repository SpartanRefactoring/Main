package op.z;

import java.util.*;
import java.util.function.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-08-31 */
public interface Listener {
  public interface ListenerContainer<L extends Listener> extends Listener, Collection<L> {/**/}

  public class ListenerContainerImplementation<L extends Listener> extends LinkedList<L> implements ListenerContainer<L> {
    private static final long serialVersionUID = -4606598464188455025L;

    protected void delegate(Consumer<L> delegation) {
      for (L listener : this)
        delegation.accept(listener);
    }
  }
}
