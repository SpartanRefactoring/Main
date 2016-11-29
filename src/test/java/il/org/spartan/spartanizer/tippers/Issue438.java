package il.org.spartan.spartanizer.tippers;

import java.io.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;

/** Failing (were ignored) tests of {@link TrimmerLogTest}
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Ignore
@SuppressWarnings("static-method")
public class Issue438 {
  @Test public void test01() {
    TrimmerLog.tip(null, null);
    assert false;
  }

  @Test public void test06() {
    final String path = "/home/matteo/MUTATION_TESTING_REFACTORING/test-common-lang/commons-lang/src/main/java/org/apache/commons/lang3/ArrayUtils.java";
    final CompilationUnit cu = (CompilationUnit) makeAST.COMPILATION_UNIT.from(new File(path));
    final Trimmer trimmer = new Trimmer();
    System.out.println(TrimmerTestsUtils.countOpportunities(trimmer, cu));
    for (final Tip ¢ : trimmer.collectSuggestions(cu))
      System.out.println(¢.description);
  }
}
