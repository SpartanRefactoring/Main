package il.org.spartan.spartanizer.engine;

import java.util.*;

/** A list of {@link Tip}s
 * @author Yossi Gil
 * @since 2017-04-08 */
public class Tips extends ArrayList<Tip> {
  private static final long serialVersionUID = 1;

  /** Suppresses default constructor, ensuring non-instantiability */
  private Tips() {/**/}

  public static Tips empty() {
    return new Tips();
  }
}
