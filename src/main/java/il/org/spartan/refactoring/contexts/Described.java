package il.org.spartan.refactoring.contexts;

import static il.org.spartan.lazy.Environment.*;

import il.org.spartan.lazy.*;
import il.org.spartan.lazy.Environment.*;

/** a {@link Environment} for described entities
 * @author Yossi Gil
 * @since 2016` */
public class Described {
  /** Direct access to the underlying cell */
  protected final Property<String> description = value("Current something");

  /** @return contents of the underlying cell; may trigger computation */
  public final String description() {
    return description.¢();
  }

  /** A class which is both {@link Described} and
   * {@link il.org.spartan.refactoring.contexts.Monitored}, merging in fact
   * their cookbooks.
   * @author Yossi Gil
   * @since 2016` */
  public class Monitored extends il.org.spartan.refactoring.contexts.Monitored {
    @Override String defaultName() {
      return super.defaultName() + description();
    }

    /** Inner class, inheriting all of its container's {@link Property}s, and
     * possibly adding some of its own. Access to container's c {@link Property}
     * is through the {@link #¢} variable.
     * <p>
     * Clients extend this class to create more specialized contexts, adding
     * more
     * @see {@link Environment#undefined()}
     * @see {@link Environment#function()}
     * @see {@link Environment#bind(Function1)}
     * @see {@link Environment#bind(Function2)}
     * @see {@link Environment#bind(Function3)}
     * @see {@link Environment#bind(Function4)}
     * @since 2016`
     * @author Yossi Gil */
    public abstract class ¢ {
      /** the containing instance */
      @SuppressWarnings("hiding") public final Monitored ¢ = Monitored.this;
    }
  }
}