package op.z;

/** Inheritance/implementation declaration.
 * @author Ori Roth
 * @since 2017-09-01 */
public interface AssignableFrom<C> {
  @SuppressWarnings("unchecked") public default C convert() {
    return (C) this;
  }
}
