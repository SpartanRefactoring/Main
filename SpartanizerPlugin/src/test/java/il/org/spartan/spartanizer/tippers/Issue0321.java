package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.*;

/** Tests of {@link CachingPattern}
 * @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings({ "static-method" }) //
public class Issue0321 {
  @Test public void testCachingPattern() {
    trimmingOf( //
        "public static Toolbox defaultInstance() {" + //
            " if (instance == null)" + //
            "   instance = allTippers();" + //
            " return instance;" + //
            "}")//
                .using(IfStatement.class, new CachingPattern())//
                .gives( //
                    "public static Toolbox defaultInstance() {" + //
                        " return  instance != null ? instance : (instance = allTippers());" + //
                        "}")//
                .stays();
  }
}
