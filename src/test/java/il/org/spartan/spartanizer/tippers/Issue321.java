package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.research.patterns.*;

/** Tests of {@link CachingPattern}
 * @author Ori Marcovitch
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue321 {
  @Test public void testCachingPattern() {
    trimmingOf( //
        "public static Toolbox defaultInstance() {" + //
            " if (instance == null)" + //
            "   instance = allTippers();" + //
            " return instance;" + //
            "}").gives( //
                "public static Toolbox defaultInstance() {" + //
                    " return  instance != null ? instance : (instance = allTippers());" + //
                    "}");
  }
}
