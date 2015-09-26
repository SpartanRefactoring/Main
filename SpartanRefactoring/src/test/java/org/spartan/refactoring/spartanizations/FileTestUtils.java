package org.spartan.refactoring.spartanizations;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.spartan.refactoring.utils.As;

/**
 * An abstract representation of our test suite, which is represented in
 * directory tree.
 *
 * @author Yossi Gil
 * @since 2014/05/24
 * @author Yossi GIl
 */
public abstract class FileTestUtils {
  /**
   * A String determines whereas we are at the IN or OUT side of the test See
   * TestCases test files for reference.
   */
  final static String testKeyword = "<Test Result>";
  /**
   * Suffix for test files.
   */
  protected final static String testSuffix = ".test";
  /**
   * Folder in which all test cases are found
   */
  public static final File location = new File("src/test/resources");
  /**
   * Instantiates a {@link Class} object if possible, otherwise generate an
   * assertion failure
   *
   * @param c an arbitrary class object
   * @return an instance of the parameter
   */
  public static Object getInstance(final Class<?> c) {
    try {
      return c.newInstance();
    } catch (final SecurityException e) {
      error("Security exception in instantiating ", c, e);
    } catch (final ExceptionInInitializerError e) {
      error("Error in instantiating class", c, e);
    } catch (final InstantiationException e) {
      error("Nullary constructor threw an exception in class", c, e);
    } catch (final IllegalAccessException e) {
      error("Missing public constructor (probably) in class", c, e);
    }
    return null;
  }
  /**
   * Makes an Input file out of a Test file
   */
  protected static File makeInFile(final File f) {
    return createTempFile(deleteTestKeyword(As.stringBuilder(f)), TestDirection.In, f);
  }
  /**
   * Makes an Output file out of a Test file
   */
  protected static File makeOutFile(final File f) {
    final StringBuilder $ = As.stringBuilder(f);
    if ($.indexOf(testKeyword) > 0)
      $.delete(0, $.indexOf(testKeyword) + testKeyword.length() + ($.indexOf("\r\n") > 0 ? 2 : 1));
    return createTempFile($, TestDirection.Out, f);
  }
  /**
   * Creates a temporary file - including lazy deletion.
   * 
   * @param sb
   * @param d
   * @param f
   * @return
   */
  static File createTempFile(final StringBuilder sb, final TestDirection td, final File f) {
    return createTemporaryRandomAccessFile(createTempFile(td, f), sb.toString());
  }
  static Spartanization makeSpartanizationObject(final File f) {
    return makeSpartanizationObject(f.getName());
  }
  static Spartanization makeSpartanizationObject(final String folderForClass) {
    final Class<?> c = asClass(folderForClass);
    assertNotNull(c);
    final Object $ = getInstance(c);
    assertNotNull($);
    return (Spartanization) $;
  }
  /**
   * Convert a canonical name of a class into a {@link Class} object, if
   * possible, otherwise generate an assertion failure
   *
   * @param name the canonical name of some class
   * @return the object representing this class
   * @since 2014/05/23
   */
  private static Class<?> asClass(final String name) {
    try {
      return Class.forName(name);
    } catch (final ClassNotFoundException e) {
      fail(name + ": class not found. " + e.getMessage());
      return null;
    }
  }
  private static File createTempFile(final TestDirection d, final File f) {
    try {
      return File.createTempFile(f.getName().replace(".", ""), d == TestDirection.In ? ".in" : ".out");
    } catch (final IOException e) {
      return null; // Failed to create temporary file
    }
  }
  private static File createTemporaryRandomAccessFile(final File $, final String s) {
    try (final RandomAccessFile fh = new RandomAccessFile($, "rw")) {
      fh.writeBytes(s);
      if ($ != null)
        $.deleteOnExit();
    } catch (final IOException e) {
      e.printStackTrace(); // Probably permissions problem
    }
    return $;
  }
  private static StringBuilder deleteTestKeyword(final StringBuilder $) {
    if ($.indexOf(testKeyword) > 0)
      $.delete($.indexOf(testKeyword), $.length());
    return $;
  }
  private static Spartanization error(final String message, final Class<?> c, final Throwable t) {
    System.err.println(message + " '" + c.getCanonicalName() + "' " + t.getMessage());
    return null;
  }

  /**
   ** An abstract class to be extended and implemented by client, while
   * overriding {@link #go(List, File)} as per customer's need.
   *
   * @seTestUtils.SATestSuite.Files
   * @see FileTestUtils.Traverse
   * @author Yossi Gil
   * @since 2014/05/24
   */
  public static abstract class Directories extends FileTestUtils.Traverse {
    /**
     * Adds a test case to the collection of all test cases generated in the
     * traversal
     */
    @Override public final void go(final List<Object[]> $, final File f) {
      if (!f.isDirectory())
        return;
      final Object[] c = makeCase(f);
      if (c != null)
        $.add(c);
    }
    abstract Object[] makeCase(File d);
  }

  /**
   ** An abstract class to be extended and implemented by client, while
   * overriding {@link #go(List, File)} as per customer's need.
   *
   * @seTestUtils.SATestSuite.Directories
   * @see FileTestUtils.Traverse
   * @author Yossi Gil
   * @since 2014/05/24
   */
  public static abstract class Files extends FileTestUtils.Traverse {
    /* (non-Javadoc)
     *
     * @see
     * il.ac.technion.cs.ssdl.spartan.refactoring.TestSuite.Traverse#go(java
     * .util.List, java.io.File) */
    @Override public void go(final List<Object[]> $, final File d) {
      for (final File f : d.listFiles())
        if (f.isFile() && f.exists()) {
          final Object[] c = makeCase(makeSpartanizationObject(d), d, f, f.getName());
          if (c != null)
            $.add(c);
        }
    }
    abstract Object[] makeCase(final Spartanization s, final File d, final File f, final String name);
  }

  /**
   * java.lang.AssertionError:
   * org.spartan.refacoring.spartanizations.ExtractMethod: class not found.
   * org.spartan.refacoring.spartanizations.ExtractMethod at
   * org.junit.Assert.fail(Assert.java:88) at
   * org.spartan.refactoring.spartanizations.FileTestUtils.asClass(FileTestUtils
   * .java:84) at org.spartan.refactoring.spartanizations.FileTestUtils.
   * makeSpartanizationObject(FileTestUtils.java:41) at
   * org.spartan.refactoring.spartanizations.FileTestUtils.
   * makeSpartanizationObject(FileTestUtils.java:38) at
   * org.spartan.refactoring.spartanizations.FileTestUtils$Files.go(
   * FileTestUtils.java:161) at
   * org.spartan.refactoring.spartanizations.FileTestUtils$Traverse.go(
   * FileTestUtils.java:108) at
   * org.spartan.refactoring.spartanizations.InOutTest.cases(InOutTest.java:85)
   * at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) at
   * sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:
   * 62) at sun.reflect.DelegatingMethodAccessorImpl.invoke(
   * DelegatingMethodAccessorImpl.java:43) at
   * java.lang.reflect.Method.invoke(Method.java:497) at
   * org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod
   * .java:50) at
   * org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.
   * java:12) at
   * org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.
   * java:47) at
   * org.junit.runners.Parameterized.allParameters(Parameterized.java:280) at
   * org.junit.runners.Parameterized.<init>(Parameterized.java:248) at
   * sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) at
   * sun.reflect.NativeConstructorAccessorImpl.newInstance(
   * NativeConstructorAccessorImpl.java:62) at
   * sun.reflect.DelegatingConstructorAccessorImpl.newInstance(
   * DelegatingConstructorAccessorImpl.java:45) at
   * java.lang.reflect.Constructor.newInstance(Constructor.java:422) at
   * org.junit.internal.builders.AnnotatedBuilder.buildRunner(AnnotatedBuilder.
   * java:104) at org.junit.internal.builders.AnnotatedBuilder.runnerForClass(
   * AnnotatedBuilder.java:86) at
   * org.junit.runners.model.RunnerBuilder.safeRunnerForClass(RunnerBuilder.java
   * :59) at
   * org.junit.internal.builders.AllDefaultPossibilitiesBuilder.runnerForClass(
   * AllDefaultPossibilitiesBuilder.java:26) at
   * org.junit.runners.model.RunnerBuilder.safeRunnerForClass(RunnerBuilder.java
   * :59) at
   * org.junit.internal.requests.ClassRequest.getRunner(ClassRequest.java:33) at
   * org.junit.internal.requests.SortingRequest.getRunner(SortingRequest.java:
   * 21) at org.eclipse.jdt.internal.junit4.runner.JUnit4TestLoader.
   * createUnfilteredTest(JUnit4TestLoader.java:84) at
   * org.eclipse.jdt.internal.junit4.runner.JUnit4TestLoader.createTest(
   * JUnit4TestLoader.java:70) at
   * org.eclipse.jdt.internal.junit4.runner.JUnit4TestLoader.loadTests(
   * JUnit4TestLoader.java:43) at
   * org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(
   * RemoteTestRunner.java:444) at
   * org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(
   * RemoteTestRunner.java:675) at
   * org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.run(RemoteTestRunner
   * .java:382) at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.main(
   * RemoteTestRunner.java:192)
   */
  /**
   * An abstract class representing the concept of traversing the
   * {@link #location} while generating test cases.
   *
   * @seTestUtils.SATestSuite.Files
   * @seTestUtils.SATestSuite.Directories
   * @author Yossi Gil
   * @since 2014/05/24
   */
  public static abstract class Traverse extends FileTestUtils {
    /**
     * @return a collection of all test cases generated in the traversal
     */
    public final Collection<Object[]> go() {
      assertNotNull(location);
      assertNotNull(location.listFiles());
      final List<Object[]> $ = new ArrayList<>();
      for (final File f : location.listFiles()) {
        assertNotNull(f);
        go($, f);
      }
      return $;
    }
    /**
     * Collect test cases from each file in {@link #location}
     *
     * @param $ where to save the collected test cases
     * @param f an entry in {@link #location}
     */
    public abstract void go(List<Object[]> $, final File f);
  }

  /* Auxiliary function for test suite inherited classes */
  enum TestDirection {
    In, Out
  }
}
