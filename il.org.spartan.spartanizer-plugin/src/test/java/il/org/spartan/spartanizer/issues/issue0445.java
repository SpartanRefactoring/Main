package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Tests of {@link ThisClass#thatFunction}
 * @author Yossi Gil
 * @since 2016 */
//
@SuppressWarnings({ "static-method", "javadoc" }) //
public class issue0445 {
  @Test public void a() {
    trimmingOf(//
        "@Override" + //
            "    public String toString() {" + //
            "      StringBuilder builder = new StringBuilder(5 * size());" + //
            "      builder.append('[').append(array[start]);" + //
            "      for (final Integer ¢: range.from(start+1).to(end))" + //
            "        builder.append(\", \").append(array[¢]);" + //
            "      return builder.append(']') + \"\";"//
    )//
        .gives(//
            "@Override" + //
                "    public String toString() {" + //
                "      StringBuilder $ = new StringBuilder(5 * size());" + //
                "      $.append('[').append(array[start]);" + //
                "      for (final Integer ¢: range.from(start+1).to(end))" + //
                "        $.append(\", \").append(array[¢]);" + //
                "      return $.append(']') + \"\";"//
        )//
        .stays();
  }
}
