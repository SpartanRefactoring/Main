package il.org.spartan.classfiles.reify;

/** @author Yossi Gil
 * @since 24 November 2011 */
public class NamedEntity {
  public final String name;

  /** Instantiate {@link NamedEntity}.
   * @param name */
  public NamedEntity(final String name) {
    this.name = name;
  }

  public final String name() {
    return name;
  }
}
