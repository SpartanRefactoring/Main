package op.z;

import java.util.*;
import java.util.function.*;

import fluent.ly.*;

/** Fluent delegating container.
 * @param <C> contained type
 * @param <Self> self type
 * @author Ori Roth
 * @since 2017-09-01 */
public interface DelegatorContainer<C, Self extends DelegatorContainer<C, Self>> extends AssignableFrom<C>, Selfie<Self> {
  Collection<C> inner();
  default Self add(C c) {
    inner().add(c);
    return self();
  }
  default Self remove(C c) {
    inner().remove(c);
    return self();
  }
  default void delegate(Consumer<C> delegation) {
    for (C c : inner())
      delegation.accept(c);
  }
}
