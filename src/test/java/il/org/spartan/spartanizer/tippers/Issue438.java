package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import java.io.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Failing (were ignored) tests of {@link TrimmerLogTest}
 * @since 2016 */
@SuppressWarnings("static-method")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Ignore
public class Issue438 {
  @Test public void test01() {
    final Tipper<ASTNode> w = null;
    final ASTNode n = null;
    TrimmerLog.tip(w, n);
    assertTrue(false);
  }

  @Test public void test06() {
    final String path = "/home/matteo/MUTATION_TESTING_REFACTORING/test-common-lang/commons-lang/src/main/java/org/apache/commons/lang3/ArrayUtils.java";
    final File f = new File(path);
    final CompilationUnit cu = (CompilationUnit) makeAST.COMPILATION_UNIT.from(f);
    final Trimmer trimmer = new Trimmer();
    final int opp = TrimmerTestsUtils.countOpportunities(trimmer, cu);
    System.out.println(opp);
    for (final Tip ¢ : trimmer.collectSuggesions(cu))
      System.out.println(¢.description);
  }
}
