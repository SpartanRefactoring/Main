package il.org.spartan.spartanizer.utils;

import java.io.*;
import java.util.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.plugin.*;

/** An abstract representation of our test suite, which is represented in
 * directory tree.
 * @author Yossi Gil
 * @since 2014/05/24
 * @author Yossi GIl */
@SuppressWarnings("unused")
public class FileTestUtils {
  /** A String determines whereas we are at the IN or OUT side of the test See
   * TestCases test files for reference. */
  static final String testKeyword = "<Test Result>";
  /** Suffix for test files. */
  static final String testSuffix = ".test";
  /** Folder in which all test cases are found */
  public static final File location = new File("src/test/resources");

  /** Convert a canonical name of a class into a {@link Class} object, if
   * possible, otherwise generate an assertion fault
   * @param name the canonical name of some class
   * @return object representing this class
   * @since 2014/05/23 */
  private static Class<?> asClass(final String ret) {
    try {
      return Class.forName(ret);
    } catch (final ClassNotFoundException ¢) {
      azzert.fail(ret + ": class not found. " + ¢.getMessage());
      return null;
    }
  }
  /** Creates a temporary file - including lazy deletion.
   * @param b
   * @param d
   * @param f
   * @return */
  static File createTempFile(final StringBuilder b, final TestDirection d, final File f) {
    return createTemporaryRandomAccessFile(createTempFile(d, f), b + "");
  }
  private static File createTempFile(final TestDirection ret, final File f) {
    try {
      return File.createTempFile(f.getName().replace(".", ""), "." + (ret == TestDirection.In ? "in" : "out"));
    } catch (final IOException e) {
      return null; // Failed to create temporary file
    }
  }
  private static File createTemporaryRandomAccessFile(final File ret, final String s) {
    try (RandomAccessFile fh = new RandomAccessFile(ret, "rw")) {
      fh.writeBytes(s);
      if (ret != null)
        ret.deleteOnExit();
    } catch (final IOException ¢) {
      note.bug(¢); // Probably permissions problem
    }
    return ret;
  }
  private static StringBuilder deleteTestKeyword(final StringBuilder $) {
    if ($.indexOf(testKeyword) > 0)
      $.delete($.indexOf(testKeyword), $.length());
    return $;
  }
  private static GUITraversal error(final String message, final Class<?> c, final Throwable t) {
    System.err.println(message + " '" + c.getCanonicalName() + "' " + t.getMessage());
    return null;
  }
  /** Instantiates a {@link Class} object if possible, otherwise generate an
   * assertion fault
   * @param commandLineApplicator an arbitrary class object
   * @return an instance of the parameter */
  public static Object getInstance(final Class<?> ret) {
    try {
      return ret.newInstance();
    } catch (final SecurityException ¢) {
      error("Security exception in instantiating ", ret, ¢);
    } catch (final ExceptionInInitializerError ¢) {
      error("Error in instantiating class", ret, ¢);
    } catch (final InstantiationException ¢) {
      error("Nullary constructor threw an exception in class", ret, ¢);
    } catch (final IllegalAccessException ¢) {
      error("Missing public constructor (probably) in class", ret, ¢);
    }
    return null;
  }
  /** Makes an Input file out of a Test file */
  protected static File makeInFile(final File ¢) {
    return createTempFile(deleteTestKeyword(makeAST.COMPILATION_UNIT.builder(¢)), TestDirection.In, ¢);
  }
  static GUITraversal makeApplicator(final File ¢) {
    return makeApplicator(¢.getName());
  }
  static GUITraversal makeApplicator(final String folderForClass) {
    final Class<?> c = asClass(folderForClass);
    assert c != null;
    final Object ret = getInstance(c);
    assert ret != null;
    return (GUITraversal) ret;
  }
  /** Makes an Output file out of a Test file */
  protected static File makeOutFile(final File ¢) {
    final StringBuilder ret = makeAST.COMPILATION_UNIT.builder(¢);
    if (ret.indexOf(testKeyword) > 0)
      ret.delete(0, ret.indexOf(testKeyword) + testKeyword.length() + (ret.indexOf("\r\n") > 0 ? 2 : 1));
    return createTempFile(ret, TestDirection.Out, ¢);
  }

  /** An abstract class to be extended and implemented by client, while
   * overriding {@link #go(List,File)} as per customer's need.
   * @seTestUtils.SATestSuite.Files
   * @see FileTestUtils.Traverse
   * @author Yossi Gil
   * @since 2014/05/24 */
  public abstract static class Directories extends FileTestUtils.Traverse {
    /** Adds a test case to the collection of all test cases generated in the
     * traversal */
    @Override public final void go(final List<Object[]> $, final File f) {
      if (!f.isDirectory())
        return;
      final Object[] c = makeCase(f);
      if (c != null)
        $.add(c);
    }
    abstract Object[] makeCase(File d);
  }

  /** An abstract class to be extended and implemented by client, while
   * overriding {@link #go(List,File)} as per customer's need.
   * @seTestUtils.SATestSuite.Directories
   * @see FileTestUtils.Traverse
   * @author Yossi Gil
   * @since 2014/05/24 */
  public abstract static class Files extends FileTestUtils.Traverse {
    @Override public void go(final List<Object[]> $, final File d) {
      for (final File f : d.listFiles()) // NANO
        if (f != null && f.isFile() && f.exists()) {
          final Object[] c = makeCase(makeApplicator(d), d, f, f.getName());
          if (c != null)
            $.add(c);
        }
    }
    abstract Object[] makeCase(GUITraversal t, File d, File f, String name);
  }

  /* Auxiliary function for test suite inherited classes */
  enum TestDirection {
    In, Out
  }

  /** An abstract class representing the concept of traversing the
   * {@link #location} while generating test cases.
   * @seTestUtils.SATestSuite.Files
   * @seTestUtils.SATestSuite.Directories
   * @author Yossi Gil
   * @since 2014/05/24 */
  public abstract static class Traverse extends FileTestUtils {
    /** @return a collection of all test cases generated in the traversal */
    public final Collection<Object[]> go() {
      assert location != null;
      assert location.listFiles() != null;
      final List<Object[]> ret = an.empty.list();
      for (final File ¢ : location.listFiles()) {
        assert ¢ != null;
        go(ret, ¢);
      }
      return ret;
    }
    /** Collect test cases from each file in {@link #location}
     * @param $ where to save the collected test cases
     * @param f an entry in {@link #location} */
    public abstract void go(List<Object[]> $, File f);
  }
}
