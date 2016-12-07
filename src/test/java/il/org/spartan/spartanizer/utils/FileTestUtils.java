package il.org.spartan.spartanizer.utils;

import java.io.*;
import java.util.*;

import il.org.spartan.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.engine.*;

/** An abstract representation of our test suite, which is represented in
 * directory tree.
 * @author Yossi Gil
 * @since 2014/05/24
 * @author Yossi GIl */
@SuppressWarnings({ "unused" })
public abstract class FileTestUtils {
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
      for (final File f : d.listFiles())
        if (f != null && f.isFile() && f.exists()) {
          final Object[] c = makeCase(makeLaconizationObject(d), d, f, f.getName());
          if (c != null)
            $.add(c);
        }
    }

    abstract Object[] makeCase(final AbstractGUIApplicator a, final File d, final File f, final String name);
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
      final List<Object[]> $ = new ArrayList<>();
      for (final File ¢ : location.listFiles()) {
        assert ¢ != null;
        go($, ¢);
      }
      return $;
    }

    /** Collect test cases from each file in {@link #location}
     * @param $ where to save the collected test cases
     * @param f an entry in {@link #location} */
    public abstract void go(List<Object[]> $, final File f);
  }

  /** A String determines whereas we are at the IN or OUT side of the test See
   * TestCases test files for reference. */
  static final String testKeyword = "<Test Result>";
  /** Suffix for test files. */
  protected static final String testSuffix = ".test";
  /** Folder in which all test cases are found */
  public static final File location = new File("src/test/resources");

  /** Convert a canonical name of a class into a {@link Class} object, if
   * possible, otherwise generate an assertion fault
   * @param name the canonical name of some class
   * @return object representing this class
   * @since 2014/05/23 */
  private static Class<?> asClass(final String name) {
    try {
      return Class.forName(name);
    } catch (final ClassNotFoundException ¢) {
      azzert.fail(name + ": class not found. " + ¢.getMessage());
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

  private static File createTempFile(final TestDirection d, final File f) {
    try {
      return File.createTempFile(f.getName().replace(".", ""), "." + (d == TestDirection.In ? "in" : "out"));
    } catch (final IOException e) {
      return null; // Failed to create temporary file
    }
  }

  private static File createTemporaryRandomAccessFile(final File $, final String s) {
    try (final RandomAccessFile fh = new RandomAccessFile($, "rw")) {
      fh.writeBytes(s);
      if ($ != null)
        $.deleteOnExit();
    } catch (final IOException ¢) {
      monitor.log(¢); // Probably permissions problem
    }
    return $;
  }

  private static StringBuilder deleteTestKeyword(final StringBuilder $) {
    if ($.indexOf(testKeyword) > 0)
      $.delete($.indexOf(testKeyword), $.length());
    return $;
  }

  private static AbstractGUIApplicator error(final String message, final Class<?> c, final Throwable t) {
    System.err.println(message + " '" + c.getCanonicalName() + "' " + t.getMessage());
    return null;
  }

  /** Instantiates a {@link Class} object if possible, otherwise generate an
   * assertion fault
   * @param c an arbitrary class object
   * @return an instance of the parameter */
  public static Object getInstance(final Class<?> c) {
    try {
      return c.newInstance();
    } catch (final SecurityException ¢) {
      error("Security exception in instantiating ", c, ¢);
    } catch (final ExceptionInInitializerError ¢) {
      error("Error in instantiating class", c, ¢);
    } catch (final InstantiationException ¢) {
      error("Nullary constructor threw an exception in class", c, ¢);
    } catch (final IllegalAccessException ¢) {
      error("Missing public constructor (probably) in class", c, ¢);
    }
    return null;
  }

  /** Makes an Input file out of a Test file */
  protected static File makeInFile(final File ¢) {
    return createTempFile(deleteTestKeyword(makeAST.COMPILATION_UNIT.builder(¢)), TestDirection.In, ¢);
  }

  static AbstractGUIApplicator makeLaconizationObject(final File ¢) {
    return makeLaconizationObject(¢.getName());
  }

  static AbstractGUIApplicator makeLaconizationObject(final String folderForClass) {
    final Class<?> c = asClass(folderForClass);
    assert c != null;
    final Object $ = getInstance(c);
    assert $ != null;
    return (AbstractGUIApplicator) $;
  }

  /** Makes an Output file out of a Test file */
  protected static File makeOutFile(final File ¢) {
    final StringBuilder $ = makeAST.COMPILATION_UNIT.builder(¢);
    if ($.indexOf(testKeyword) > 0)
      $.delete(0, $.indexOf(testKeyword) + testKeyword.length() + ($.indexOf("\r\n") > 0 ? 2 : 1));
    return createTempFile($, TestDirection.Out, ¢);
  }
}
