/* TODO Ori Roth <ori.rothh@gmail.com> please add a description
 *
 * @author Ori Roth <ori.rothh@gmail.com>
 *
 * @since Dec 14, 2016 */
package il.org.spartan.spartanizer.utils;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.utils.*;

public enum LogToTest {
  ;
  // TODO Ori Roth: replace "\\\\" with File.separator (bug in Java???)
  private static final String TESTS_FOLDER = "src.test.java.il.org.spartan.automatic".replaceAll("\\.", "\\\\");
  private static final Supplier<String> TEST_NAMER = () -> "Automatic_" + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());

  public static void main(final String[] args) {
    if (!new File(TESTS_FOLDER).exists()) {
      System.out.println("First create a package named il.org.spartan.automatic under src/test/java.");
      return;
    }
    final File d = new File("logs");
    if (!d.exists()) {
      System.out.println("First create a 'logs' directory and run some tests to create a log file.");
      return;
    }
    final File[] fs = d.listFiles();
    final Int fc = new Int();
    Stream.of(fs).filter(λ -> λ.isFile() && λ.getName().startsWith("log_spartan")).forEach(λ -> ++fc.inner);
    if (fc.inner == 0) {
      System.out.println("First run some tests to create a log file.");
      return;
    }
    System.out.println("Creating test cases...");
    final Collection<String> xs = new HashSet<>();
    final List<String> ts = an.empty.list();
    final Map<String, Integer> nu = new HashMap<>();
    for (final File f : fs)
      try (BufferedReader r = new BufferedReader(new FileReader(f))) {
        final List<String> es = an.empty.list();
        es.add("");
        for (String l = r.readLine(); l != null; l = r.readLine())
          if (l.equals(note.FILE_SEPARATOR.trim())) {
            analyze(xs, ts, nu, es);
            es.clear();
            es.add("");
          } else if (l.equals(note.FILE_SUB_SEPARATOR.trim()))
            es.add("");
          else
            es.set(es.size() - 1, the.lastOf(es) + "\n" + l);
      } catch (final IOException ¢) {
        note.io(¢, f + "");
        return;
      }
    System.out.println("Creating test file...");
    final String fileName = TEST_NAMER.get();
    try (Writer w = new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(TESTS_FOLDER + File.separator + fileName + ".java", true), "utf-8"))) {
      w.write(wrap(ts, fileName));
    } catch (final IOException ¢) {
      note.io(¢);
      return;
    }
    System.out.println("Done! Written " + ts.size() + " tests to " + fileName + ".java");
  }
  private static void analyze(final Collection<String> xs, final Collection<String> ts, final Map<String, Integer> nu, final List<String> ss) {
    final String errorLocationUnparsed = ss.get(1).trim().split("\n")[1],
        errorLocationFile = errorLocationUnparsed.replaceFirst(".*at ", "").replaceFirst("\\(.*", "");
    if (xs.contains(errorLocationFile))
      return;
    xs.add(errorLocationFile);
    final String[] s = errorLocationFile.split("\\.");
    String errorLocationFileClean = s[s.length - 1];
    if (!nu.containsKey(errorLocationFileClean))
      nu.put(errorLocationFileClean, Integer.valueOf(1));
    else {
      nu.put(errorLocationFileClean, Integer.valueOf(nu.get(s[s.length - 1]).intValue() + 1));
      errorLocationFileClean += nu.get(errorLocationFileClean).intValue() + 1;
    }
    buildTest(ts, errorLocationFileClean, errorLocationUnparsed.replaceFirst(".*:", "").replaceFirst("\\)", ""), the.firstOf(ss),
        ss.get(2).trim().equals(English.UNKNOWN) ? "some test file" : ss.get(2).trim(), ss.get(3), ss.get(4), errorLocationFile);
  }
  private static void buildTest(final Collection<String> ss, final String errorLocationFileClean, final String errorLocationLine,
      final String errorName, final String fileName, final String errorCode, final String rawCode, final String errorLocationFileUnclean) {
    ss.add(wrap(errorLocationFileClean, errorLocationLine, errorName, fileName, errorCode, JUnitTestMethodFacotry.unWrapedTestCase(rawCode),
        errorLocationFileUnclean));
  }
  private static String wrap(final String errorLocationFileClean, final String errorLocationLine, final String errorName, final String fileName,
      @SuppressWarnings("unused") final String errorCode, final String code, final String errorLocationFileUnclean) {
    return "/** Test created automatically due to " + errorName + " thrown while testing " + fileName + ".\nOriginated at " + errorLocationFileUnclean
        + "\n at line #" + errorLocationLine + ".\n\n*/\n@Test public void " + errorLocationFileClean + "Test() {\ntrimmingOf(" + code
        + ").doesNotCrash();\n}";
  }
  private static String wrap(final Iterable<String> ss, final String fileName) {
    final StringBuilder $ = new StringBuilder(
        "package il.org.spartan.automatic;\n\nimport static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;\n\n"
            + "import org.junit.*;\n\n/** @author Ori Roth\n* @since " + new SimpleDateFormat("yyyy_MM_dd").format(new Date()) + " */\n" //
            + "@SuppressWarnings(\"static-method\")\n" //
            + "@forget\n" //
            + "public class " + fileName + " {\n");
    ss.forEach(λ -> $.append(λ).append("\n"));
    return format.code($.append("}\n") + "");
  }
}
