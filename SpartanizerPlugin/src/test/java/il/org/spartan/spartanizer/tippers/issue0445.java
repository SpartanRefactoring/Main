package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** Tests of {@link ThisClass#thatFunction}
 * @author Yossi Gil {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
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