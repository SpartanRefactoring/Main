package il.org.spartan.strings;

import java.util.*;

import org.jetbrains.annotations.*;

/** Typedef for a list of strings.
 * @author Yossi Gil
 * @since 01/05/2007 */
public class Strings extends ArrayList<String> {
  private static final long serialVersionUID = 0x7905493845B09CFBL;

  /** Create an empty list of Strings. */
  public Strings() {
    super(0);
  }

  /** Create a new list of strings from a single string.
   * @param s A string to initialize the list */
  public Strings(final String s) {
    super(1);
    add(s);
  }

  /** Create a new list of strings from a list of strings
   * @param s1 The first string to add to the list
   * @param s2 The second string to add to the list
   * @param ss The remaining strings to add to the list */
  public Strings(final String s1, final String s2, @NotNull final String... ss) {
    super(2 + ss.length);
    add(s1);
    add(s2);
    for (final String ¢ : ss)
      add(¢);
  }
}
