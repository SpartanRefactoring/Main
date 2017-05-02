package il.org.spatan.iteration;

import org.jetbrains.annotations.*;

public interface NamedEntity {
  String INVERTED = "'";

  /** Which name is associated with this entity?
   * @return the name, if known, of this entity, or the empty string. */
  @NotNull String name();
}
