package il.org.spartan.refactoring.contexts;


import il.org.spartan.lazy.*;

import static il.org.spartan.lazy.Cookbook.value;

/**
 * a {@link Cookbook} for described entities
 * 
 * @author Yossi Gil
 *
 * @since 2016`
 */
public class Described implements Cookbook {
  /** Direct access to the underlying cell */
  protected final Cell<String> description = value("Current project");

  /** @return contents of the underlying cell; may trigger computation */
  public final String description() {
    return description.get();
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
