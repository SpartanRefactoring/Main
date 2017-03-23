package il.org.spartan.utils;

import org.jetbrains.annotations.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil {@code yossi.gil@gmail.com}
 * @since 2017-03-19 */
public class ReduceStringConcatenate extends Reduce<String> {
  @Override @NotNull public String reduce(final String s1, final String s2) {
    return s1 + s2;
  }

  @Override @NotNull public String reduce() {
    return "";
  }
}
