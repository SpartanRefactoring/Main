package il.org.spartan.spartanizer.utils;

import static il.org.spartan.lisp.*;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.function.*;

import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.research.util.*;

public class LogToTest {
  // TODO Ori Roth: replace "\\\\" with File.separator (bug in Java???)
  private static String TESTS_FOLDER = "src.test.java.il.org.spartan.automatic".replaceAll("\\.", "\\\\");
  private static Supplier<String> TEST_NAMER = () -> "Automatic_" + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());

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
    int fc = 0;
    for (final File element : fs) // NANO
      if (element.isFile() && element.getName().startsWith("log_spartan"))
        ++fc;
    if (fc == 0) {
      System.out.println("First run some tests to create a log file.");
      return;
    }
    System.out.println("Creating test cases...");
    final Set<String> xs = new HashSet<>();
    final List<String> ts = new LinkedList<>();
    final Map<String, Integer> nu = new HashMap<>();
    for (final File element : fs)
      try (BufferedReader r = new BufferedReader(new FileReader(element))) {
        final List<String> es = new LinkedList<>();
        es.add("");
        for (String l = r.readLine(); l != null;) {
          if (l.equals(monitor.FILE_SEPARATOR.trim())) {
            analyze(xs, ts, nu, es);
            es.clear();
            es.add("");
          } else if (l.equals(monitor.FILE_SUB_SEPARATOR.trim()))
            es.add("");
          else
            es.set(es.size() - 1, last(es) + "\n" + l);
          l = r.readLine();
        }
      } catch (final IOException ¢) {
        ¢.printStackTrace();
        System.out.println("IO problem!");
        return;
      }
    System.out.println("Creating test file...");
    final String fileName = TEST_NAMER.get();
    try (Writer w = new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(TESTS_FOLDER + File.separator + fileName + ".java", true), "utf-8"))) {
      w.write(wrap(ts, fileName));
    } catch (final IOException ¢) {
      ¢.printStackTrace();
      System.out.println("IO problem!");
      return;
    }
    System.out.println("Done! Written " + ts.size() + " tests to " + fileName + ".java");
  }

  private static void analyze(final Set<String> xs, final List<String> ts, final Map<String, Integer> nu, final List<String> ss) {
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
    buildTest(ts, errorLocationFileClean, errorLocationUnparsed.replaceFirst(".*:", "").replaceFirst("\\)", ""), first(ss),
        ss.get(2).trim().equals(Linguistic.UNKNOWN) ? "some test file" : ss.get(2).trim(), ss.get(3), ss.get(4), errorLocationFile);
  }

  private static void buildTest(final List<String> ss, final String errorLocationFileClean, final String errorLocationLine, final String errorName,
      final String fileName, final String errorCode, final String rawCode, final String errorLocationFileUnclean) {
    ss.add(wrap(errorLocationFileClean, errorLocationLine, errorName, fileName, errorCode, normalize.unwarpedTestcase(rawCode),
        errorLocationFileUnclean));
  }

  private static String wrap(final String errorLocationFileClean, final String errorLocationLine, final String errorName, final String fileName,
      @SuppressWarnings("unused") final String errorCode, final String code, final String errorLocationFileUnclean) {
    return "/** Test created automatically due to " + errorName + " thrown while testing " + fileName + ".\nOriginated at " + errorLocationFileUnclean
        + "\n at line #" + errorLocationLine + ".\n\n*/\n@Test public void " + errorLocationFileClean + "Test() {" + "\ntrimmingOf(" + code
        + ").doesNotCrash();\n}";
  }

  private static String wrap(final List<String> ss, final String fileName) {
    final StringBuilder $ = new StringBuilder(
        "package il.org.spartan.automatic;\n\n" + "import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;\n\n"
            + "import org.junit.*;\n\n" + "/** @author Ori Roth\n" + "* @since " + new SimpleDateFormat("yyyy_MM_dd").format(new Date()) + " */\n" //
            + "@SuppressWarnings(\"static-method\")\n" //
            + "@Ignore\n" //
            + "public class " + fileName + " {\n");
    ss.forEach(¢ -> $.append(¢).append("\n"));
    return format.code($.append("}\n") + "");
  }
}
