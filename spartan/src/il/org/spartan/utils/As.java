package il.org.spartan.utils;

import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.streotypes.*;

/** A utility encapsulating conversion function.
 * @author Yossi Gil
 * @since February 20, 2010 */
@Utility public enum As {
  ;
  /** Converts a boolean into the equivalent binary value
   * @param ¢ an arbitrary boolean value
   * @return 1 if the parameter is <b><code>true</code></b>, 0 if it is <b>
   *         <code>false</code></b> */
  public static int binary(final boolean ¢) {
    return as.bit(¢);
  }

  /** C like conversion of a pointer into a boolean.
   * @param ¢ an arbitrary object
   * @return <code>0</code> if the parameter is <code><b>0</b></code>;
   *         <code>1</code> otherwise */
  public static int binary(@Nullable final Object ¢) {
    return ¢ == null ? 0 : 1;
  }
}
