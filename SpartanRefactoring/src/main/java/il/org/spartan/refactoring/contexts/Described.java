package il.org.spartan.refactoring.contexts;


import il.org.spartan.lazy.*;

import static il.org.spartan.lazy.Environment.value;
import il.org.spartan.lazy.Environment.Property;

/**
 * a {@link Environment} for described entities
 * 
 * @author Yossi Gil
 *
 * @since 2016`
 */
public class Described implements Environment {
  /** Direct access to the underlying cell */
  protected final Property<String> description = value("Current something");

  /** @return contents of the underlying cell; may trigger computation */
  public final String description() {
    return description.ϑ();
  }

  /**
   * A class which is both {@link Described} and
   * {@link il.org.spartan.refactoring.contexts.Monitored}, merging in fact 
   * their cookbooks.
   * 
   * @author Yossi Gil
   *
   * @since 2016`
   */
  public abstract class Monitored extends il.org.spartan.refactoring.contexts.Monitored {
    @Override String defaultName() {
      return super.defaultName() + description();
    }
  }
}
