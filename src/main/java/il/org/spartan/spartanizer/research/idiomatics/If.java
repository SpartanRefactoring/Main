package il.org.spartan.spartanizer.research.idiomatics;

import java.util.function.*;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since Dec 6, 2016 */
public enum If {
  ;
  public static <N> If0 Null(final N ¢) {
    return new If0(¢ == null);
  }

  public static If0 True(final boolean ¢) {
    return new If0(¢);
  }

  static class If0 {
    final boolean b;

    If0(final boolean b) {
      this.b = b;
    }

    public void returns() {
      //
    }

    public static <N extends Exception> void throwz(final Supplier<N> ¢) throws N {
      throw ¢.get();
    }

    public void returnsNull() {
      if (b)
        returnNull();
    }
  }

  public static void returnNull() {
    //
  }
}
