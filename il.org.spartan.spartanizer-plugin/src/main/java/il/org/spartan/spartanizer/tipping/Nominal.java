package il.org.spartan.spartanizer.tipping;

/** The {@link TipperCategory} of renaming, and renaming related
 * {@link Tipper}s.
 * @author Yossi Gil
 * @since Sep 28, 2016 */
public interface Nominal extends TipperCategory {
  /** TODO Yossi Gil: document class
   * @author Yossi Gil
   * @since 2017-05-29 */
  interface Fields extends Nominal {
    @SuppressWarnings("hiding") String toString = "Use standard names for fields and parametrs assigned to fields";

    @Override default String description() {
      return toString;
    }
  }

  interface Result extends Nominal {
    @SuppressWarnings("hiding") String toString = "Naming convention for the result variable";

    @Override default String description() {
      return toString;
    }
  }

  interface Trivialization extends Nominal {
    @SuppressWarnings("hiding") String toString = "Centification";

    @Override default String description() {
      return toString;
    }
  }

  interface Abbreviation extends Nominal {
    @SuppressWarnings("hiding") String toString = "One letter convention for locals";

    @Override default String description() {
      return toString;
    }
  }

  interface Anonymization extends Nominal {
    @SuppressWarnings("hiding") String toString = "Naming convention for anonymizing unused parameters";

    @Override default String description() {
      return toString;
    }
  }

  String toString = "Short, idiomatic (some say cryptic) names";

  @Override default String description() {
    return toString;
  }
}
