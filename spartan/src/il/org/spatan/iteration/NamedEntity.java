package il.org.spatan.iteration;

import org.eclipse.jdt.annotation.*;

public interface NamedEntity {
  String INVERTED = "'";

  /** Which name is associated with this entity?
   * @return the name, if known, of this entity, or the empty string. */
   String name();
}
