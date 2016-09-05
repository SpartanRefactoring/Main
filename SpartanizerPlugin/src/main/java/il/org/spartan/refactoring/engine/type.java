package il.org.spartan.refactoring.engine;

import java.util.*;

/** @author Yossi Gil
 * @since 2016 */
interface type {
  /** All type that were ever born */
  static Map<String, type> community = new LinkedHashMap<>();

  // TODO: Matteo. Nano-pattern of values: not implemented
  static type get() {
    throw new NotImplementedException("code of this function was not implemented yet");
  }

  default Primitive.Doubt asPrimitiveDoubt() {
    return null;
  }

  default boolean canB(@SuppressWarnings("unused") Primitive.Doubt __) {
    return false;
  }

  /** @return the name of this type, i.e., the key under which it is stored in
   *         {@link #community} */
  String name();

  default type join() {
    community.put(name(), this);
    return this;
  }

  static type baptize(String name) {
    return have(name) ? bring(name) : new type() {
      @Override public String name() {
        return name;
      }
    }.join();
  }

  static type bring(String name) {
    return community.get(name);
  }

  static boolean have(String name) {
    return community.containsKey(name);
  }

  public static class NotImplementedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotImplementedException(String message) {
      super(message);
    }
  }

  interface Primitive {
    enum Doubt implements type {
    };
  }
} // end of interface type