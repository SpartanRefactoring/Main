package il.org.spatan.iteration;

public interface NamedEntity {
  static final String INVERTED = "'";

  /** Which name is associated with this entity?
   * @return the name, if known, of this entity, or the empty string. */
  String name();
}
