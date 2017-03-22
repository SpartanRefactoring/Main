package il.org.spartan.utils;

import org.jetbrains.annotations.NotNull;

/** TODO Yossi Gil: document class
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-03-19 */
public class ReduceStringConcatenate extends Reduce<String> {
  @NotNull
  @Override public String reduce(final String s1, final String s2) {
    return s1 + s2;
  }

  @NotNull
  @Override public String reduce() {
    return "";
  }
}
