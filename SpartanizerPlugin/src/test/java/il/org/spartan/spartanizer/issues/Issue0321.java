package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.research.nanos.*;

/** Tests of {@link CachingPattern}
 * @author Ori Marcovitch */
@SuppressWarnings("static-method")
public class Issue0321 {
  @Test public void testCachingPattern() {
    trimminKof( //
        "public static Toolbox defaultInstance() {" + //
            " if (instance == null)" + //
            "   instance = allTippers();" + //
            " return instance;" + //
            "}")//
                .using(new CachingPattern(), IfStatement.class)//
                .gives( //
                    "public static Toolbox defaultInstance() {" + //
                        " return  instance != null ? instance : (instance = allTippers());" + //
                        "}")//
                .stays();
  }
}
