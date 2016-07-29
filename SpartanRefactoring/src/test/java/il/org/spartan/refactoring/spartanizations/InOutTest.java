package il.org.spartan.refactoring.spartanizations;

import static il.org.spartan.Utils.*;
import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.spartanizations.TESTUtils.*;
import static org.junit.Assert.*;
import il.org.spartan.refactoring.contexts.*;
import il.org.spartan.refactoring.suggestions.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.*;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * Run tests in which a specific transformation is not supposed to change the
 * input text
 *
 * @author Yossi Gil
 * @since 2014/05/24
 */
@RunWith(Parameterized.class)//
public class InOutTest {
  /**
   * Generate test cases for this parameterized class.
   *
   * @return a collection of cases, where each case is an array of four objects,
   *         the spartanization, the test case name, the input file, and the
   *         output file.
   */
  @Parameters(name = "{index}) \"{0}\" =={2}==>> \"{1}\"")//
  public static Collection<Object[]> cases() {
    return new FileTestUtils.Files() {
      @Override Object[] makeCase(final Spartanization s, final File folder, final File input, final String name) {
        if (name.endsWith(testSuffix) && MakeAST.stringBuilder(input).indexOf(testKeyword) > 0)
          return objects(s, name, input, makeOutFile(input));
        if (!name.endsWith(".in"))
          return null;
        final File output = new File(folder, name.replaceAll("\\.in$", ".out"));
        return !output.exists() ? null : objects(s, name.replaceAll("\\.in$", ""), input, output);
      }
    }.go();
  }
  protected static void go(final CurrentAST c, final File from, final File to) {
    final CompilationUnit u = (CompilationUnit) MakeAST.COMPILIATION_UNIT.from(FileTestUtils.makeInFile(from));
    that(u.toString(), TrimmerTestsUtils.countOpportunities(c, u), is(1));
    TESTUtils.assertOneOpportunity(c, MakeAST.string(from));
    final String expected;
    final Document rewrite;
    if (!from.getName().endsWith(FileTestUtils.testSuffix)) {
      expected = MakeAST.string(to);
      rewrite = TESTUtils.rewrite(c, u, new Document(MakeAST.string(from)));
    } else {
      expected = MakeAST.string(FileTestUtils.makeOutFile(to));
      rewrite = TESTUtils.rewrite(c, u, new Document(MakeAST.string(FileTestUtils.makeInFile(from))));
    }
    assertSimilar(expected, rewrite.get());
  }

  /** An object describing the required transformation */
  @Parameter(0) public Spartanization spartanization;
  /** The name of the specific test for this transformation */
  @Parameter(1) public String name;
  /** Where the input text can be found */
  @Parameter(2) public File input;
  /** Where the expected output can be found */
  @Parameter(3) public File output;

  /**
   * Runs a parameterized test case, based on the instance variables of this
   * instance
   */
  @Test public void go() {
    assertNotNull(spartanization);
    go(spartanization, input, output);
  }
}
